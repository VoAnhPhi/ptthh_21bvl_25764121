package lab4.exercises.ex04;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RemoteCalculatorServer {
    private static final int PORT = 5005;

    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Calculator server listening on port " + PORT);

            while (true) {
                try (Socket socket = server.accept()) {
                    System.out.println("Connected: " + socket.getRemoteSocketAddress());
                    serve(socket);
                } catch (IOException e) {
                    System.err.println("Client error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Cannot start server: " + e.getMessage());
        }
    }

    private static void serve(Socket socket) throws IOException {
        try (
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8)
            );
            PrintWriter out = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),
                true
            )
        ) {
            String request;

            while ((request = in.readLine()) != null) {
                if (request.equalsIgnoreCase("QUIT")) {
                    out.println("OK BYE");
                    break;
                }

                out.println(process(request));
            }
        }
    }

    static String process(String request) {
        String[] parts = request.trim().split("\\s+");

        if (parts.length != 4 || !parts[0].equals("CALC")) {
            return "ERR INVALID_FORMAT";
        }

        try {
            BigDecimal a = new BigDecimal(parts[2]);
            BigDecimal b = new BigDecimal(parts[3]);
            BigDecimal result;

            switch (parts[1]) {
                case "+" -> result = a.add(b);
                case "-" -> result = a.subtract(b);
                case "*" -> result = a.multiply(b);
                case "/" -> {
                    if (b.compareTo(BigDecimal.ZERO) == 0) {
                        return "ERR DIVIDE_BY_ZERO";
                    }
                    result = a.divide(b, MathContext.DECIMAL64);
                }
                default -> {
                    return "ERR UNSUPPORTED_OPERATOR";
                }
            }

            return "OK " + result.stripTrailingZeros().toPlainString();
        } catch (NumberFormatException e) {
            return "ERR INVALID_NUMBER";
        }
    }
}