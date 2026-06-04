package com.eps.paymentservice.util;

import com.eps.paymentservice.model.Payment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PDFGenerator {

  private static final String PDF_DIRECTORY = "receipts";

  public PDFGenerator() {
    try {
      Files.createDirectories(Paths.get(PDF_DIRECTORY));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String generatePaymentReceipt(Payment payment) throws IOException {
    String fileName = String.format("RECEIPT_%s.pdf", payment.getTransactionId());
    Path filePath = Paths.get(PDF_DIRECTORY, fileName);
    Files.write(filePath, buildPdf(payment).getBytes(StandardCharsets.US_ASCII));
    return filePath.toString();
  }

  private String buildPdf(Payment payment) {
    List<String> lines = new ArrayList<>();
    lines.add("ELECTRICITY DISTRIBUTION COMPANY");
    lines.add("Payment Receipt");
    lines.add("Transaction ID: " + payment.getTransactionId());
    lines.add("Customer ID: " + payment.getCustomerId());
    lines.add("Bill ID: " + payment.getBillId());
    lines.add(String.format("Amount: %.2f", payment.getAmount()));
    lines.add("Payment Method: " + payment.getMethod());
    lines.add("Payment Status: " + payment.getStatus());
    lines.add("Payment Date: " + payment.getPaymentDate());
    lines.add("Thank you for your payment.");

    StringBuilder content = new StringBuilder();
    content.append("BT\n/F1 12 Tf\n72 760 Td\n");
    for (int i = 0; i < lines.size(); i++) {
      if (i > 0) {
        content.append("0 -22 Td\n");
      }
      content.append("(").append(escapePdfText(lines.get(i))).append(") Tj\n");
    }
    content.append("ET\n");

    String contentStream = content.toString();
    String[] objects = new String[] {
        "1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n",
        "2 0 obj\n<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n",
        "3 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] "
            + "/Resources << /Font << /F1 5 0 R >> >> /Contents 4 0 R >>\nendobj\n",
        "4 0 obj\n<< /Length " + contentStream.getBytes(StandardCharsets.US_ASCII).length
            + " >>\nstream\n" + contentStream + "endstream\nendobj\n",
        "5 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n"
    };

    StringBuilder pdf = new StringBuilder();
    List<Integer> offsets = new ArrayList<>();
    pdf.append("%PDF-1.4\n");
    offsets.add(0);
    for (String object : objects) {
      offsets.add(pdf.length());
      pdf.append(object);
    }

    int xrefOffset = pdf.length();
    pdf.append("xref\n0 ").append(objects.length + 1).append("\n");
    pdf.append("0000000000 65535 f \n");
    for (int i = 1; i < offsets.size(); i++) {
      pdf.append(String.format("%010d 00000 n \n", offsets.get(i)));
    }
    pdf.append("trailer\n<< /Size ").append(objects.length + 1).append(" /Root 1 0 R >>\n");
    pdf.append("startxref\n").append(xrefOffset).append("\n%%EOF\n");
    return pdf.toString();
  }

  private String escapePdfText(String value) {
    return value.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)");
  }
}
