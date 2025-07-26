package com.example.demo.service;

import com.example.demo.Repository.*;
import com.example.demo.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepo;
    private final ProductRepository productRepo;
    private final DistributionCenterRepository dcRepo;
    private final InventoryItemRepository inventoryRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    // Corrected formatter to handle space instead of 'T' and microseconds + timezone
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSXXX");

    @Override
    public void run(String... args) throws Exception {
        loadDistributionCenters("src/main/resources/data/distribution_centers.csv");
        loadUsers("src/main/resources/data/users.csv");
        loadProducts("src/main/resources/data/products.csv");
        loadInventoryItems("src/main/resources/data/inventory_items.csv");
        loadOrders("src/main/resources/data/orders.csv");
        loadOrderItems("src/main/resources/data/order_items.csv");
    }

    private void loadDistributionCenters(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            DistributionCenter dc = new DistributionCenter(
                    Long.parseLong(parts[0]),
                    parts[1],
                    Double.parseDouble(parts[2]),
                    Double.parseDouble(parts[3])
            );
            dcRepo.save(dc);
        }
        br.close();
    }

    private void loadUsers(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            User u = new User(
                    Long.parseLong(p[0]), p[1], p[2], p[3], Integer.parseInt(p[4]), p[5],
                    p[6], p[7], p[8], p[9], p[10],
                    Double.parseDouble(p[11]), Double.parseDouble(p[12]),
                    p[13],
                    parseTime(p[14])
            );
            userRepo.save(u);
        }
        br.close();
    }

    private void loadProducts(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            DistributionCenter dc = dcRepo.findById(Long.parseLong(p[8])).orElse(null);
            Product product = new Product(
                    Long.parseLong(p[0]), Double.parseDouble(p[1]), p[2], p[3], p[4],
                    Double.parseDouble(p[5]), p[6], p[7], dc
            );
            productRepo.save(product);
        }
        br.close();
    }

    private void loadInventoryItems(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            Product product = productRepo.findById(Long.parseLong(p[1])).orElse(null);
            DistributionCenter dc = dcRepo.findById(Long.parseLong(p[11])).orElse(null);

            InventoryItem item = new InventoryItem(
                    Long.parseLong(p[0]), product,
                    parseTime(p[2]), parseTime(p[3]),
                    Double.parseDouble(p[4]), p[5], p[6], p[7],
                    Double.parseDouble(p[8]), p[9], p[10],
                    dc
            );
            inventoryRepo.save(item);
        }
        br.close();
    }

    private void loadOrders(String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",");
            User user = userRepo.findById(Long.parseLong(p[1])).orElse(null);
            Order order = new Order(
                    Long.parseLong(p[0]), user, p[2], p[3],
                    parseTime(p[4]), parseTime(p[5]), parseTime(p[6]), parseTime(p[7]),
                    Integer.parseInt(p[8])
            );
            orderRepo.save(order);
        }
        br.close();
    }

    private void loadOrderItems(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        lines.remove(0); // header

        for (String line : lines) {
            String[] p = line.split(",");

            if (p.length < 11) {
                System.out.println("Skipping malformed line: " + Arrays.toString(p));
                continue;
            }

            OrderItem orderItem = new OrderItem(
                    parseLong(p[0]),
                    orderRepo.findById(parseLong(p[1])).orElse(null),
                    userRepo.findById(parseLong(p[2])).orElse(null),
                    productRepo.findById(parseLong(p[3])).orElse(null),
                    inventoryRepo.findById(parseLong(p[4])).orElse(null),
                    p[5],
                    parseTime(p[6]),
                    parseTime(p[7]),
                    parseTime(p[8]),
                    parseTime(p[9]),
                    parseDouble(p[10])
            );

            orderItemRepo.save(orderItem);
        }
    }

    private OffsetDateTime parseTime(String raw) {
        try {
            raw = raw.trim().replace(" ", "T");
            return raw.isEmpty() ? null : OffsetDateTime.parse(raw, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception ex) {
            try {
                return OffsetDateTime.parse(raw, formatter);
            } catch (Exception e) {
                System.out.println("Failed to parse datetime: " + raw);
                return null;
            }
        }
    }

    private Long parseLong(String s) {
        try {
            return s == null || s.isEmpty() ? null : Long.parseLong(s.trim().replaceAll("\"", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String s) {
        try {
            return s == null || s.isEmpty() ? null : Double.parseDouble(s.trim().replaceAll("\"", ""));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
