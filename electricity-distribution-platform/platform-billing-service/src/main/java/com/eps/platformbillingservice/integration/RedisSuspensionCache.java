package com.eps.platformbillingservice.integration;

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

@Component
public class RedisSuspensionCache {

  private final String host;
  private final int port;

  public RedisSuspensionCache(
      @Value("${spring.data.redis.host:localhost}") String host,
      @Value("${spring.data.redis.port:6379}") int port) {
    this.host = host;
    this.port = port;
  }

  public void suspend(String tenantCode) {
    execute("*3\r\n$3\r\nSET\r\n$" + key(tenantCode).length() + "\r\n" + key(tenantCode)
        + "\r\n$1\r\n1\r\n");
  }

  public void reinstate(String tenantCode) {
    execute("*2\r\n$3\r\nDEL\r\n$" + key(tenantCode).length() + "\r\n" + key(tenantCode)
        + "\r\n");
  }

  private String key(String tenantCode) {
    return "tenant:suspended:" + tenantCode;
  }

  private void execute(String command) {
    try (Socket socket = new Socket()) {
      socket.connect(new InetSocketAddress(host, port), 200);
      socket.setSoTimeout(200);
      try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
          socket.getOutputStream(), StandardCharsets.UTF_8));
          BufferedReader reader = new BufferedReader(new InputStreamReader(
              socket.getInputStream(), StandardCharsets.UTF_8))) {
        writer.write(command);
        writer.flush();
        reader.readLine();
      }
    } catch (IOException ignored) {
      // Redis may be absent in local unit runs; database state remains authoritative.
    }
  }
}
