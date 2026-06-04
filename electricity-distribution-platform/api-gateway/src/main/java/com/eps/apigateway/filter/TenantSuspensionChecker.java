package com.eps.apigateway.filter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class TenantSuspensionChecker {

  private final String redisHost;
  private final int redisPort;
  private final int timeoutMs;

  public TenantSuspensionChecker(
      @Value("${spring.data.redis.host:localhost}") String redisHost,
      @Value("${spring.data.redis.port:6379}") int redisPort,
      @Value("${tenant.suspension.redis.timeout-ms:150}") int timeoutMs) {
    this.redisHost = redisHost;
    this.redisPort = redisPort;
    this.timeoutMs = timeoutMs;
  }

  public Mono<Boolean> isSuspended(String tenantId) {
    if (tenantId == null || tenantId.isBlank()) {
      return Mono.just(false);
    }
    return Mono.fromCallable(() -> exists("tenant:suspended:" + tenantId))
        .subscribeOn(Schedulers.boundedElastic())
        .onErrorReturn(false);
  }

  private boolean exists(String key) throws IOException {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(redisHost, redisPort), timeoutMs);
      socket.setSoTimeout(timeoutMs);
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
          socket.getOutputStream(), StandardCharsets.UTF_8));
          BufferedReader reader = new BufferedReader(new InputStreamReader(
              socket.getInputStream(), StandardCharsets.UTF_8))) {
        writer.write("*2\r\n$6\r\nEXISTS\r\n$" + key.length() + "\r\n" + key + "\r\n");
        writer.flush();
        String response = reader.readLine();
        return ":1".equals(response);
      }
    }
  }
}
