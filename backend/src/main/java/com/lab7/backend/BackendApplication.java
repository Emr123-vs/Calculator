package com.lab7.backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BackendApplication {
    private static final Calculator calculator = new Calculator();
    private static final Pattern NUM_A = Pattern.compile("\"a\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern NUM_B = Pattern.compile("\"b\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
    private static final Pattern OP = Pattern.compile("\"op\"\\s*:\\s*\"([+\\-*/])\"");

    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8000"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/health", ex -> respond(ex, 200, "{\"status\":\"ok\"}"));
        server.createContext("/calculate", BackendApplication::handleCalculate);
        server.start();
        System.out.println("Calculator backend API listening on port " + port);
    }

    private static void handleCalculate(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 204, "");
            return;
        }
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            respond(exchange, 405, "{\"error\":\"Method Not Allowed\"}");
            return;
        }
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Matcher ma = NUM_A.matcher(body);
        Matcher mb = NUM_B.matcher(body);
        Matcher mo = OP.matcher(body);
        if (!ma.find() || !mb.find() || !mo.find()) {
            respond(exchange, 400, "{\"error\":\"Invalid request body\"}");
            return;
        }
        double a = Double.parseDouble(ma.group(1));
        double b = Double.parseDouble(mb.group(1));
        String op = mo.group(1);
        try {
            double result;
            switch (op) {
                case "+": result = calculator.add(a, b); break;
                case "-": result = calculator.subtract(a, b); break;
                case "*": result = calculator.multiply(a, b); break;
                case "/": result = calculator.divide(a, b); break;
                default: respond(exchange, 400, "{\"error\":\"Unknown operator\"}"); return;
            }
            respond(exchange, 200, "{\"result\":" + result + "}");
        } catch (ArithmeticException e) {
            respond(exchange, 400, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }
}