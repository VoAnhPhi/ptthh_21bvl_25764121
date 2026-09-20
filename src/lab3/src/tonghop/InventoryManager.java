package lab3.src.tonghop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {

    private static final Path INVENTORY_FILE =
            Path.of(
                    "src",
                    "lab3",
                    "data",
                    "inventory.csv"
            );

    private static final Path REPORT_FILE =
            Path.of(
                    "src",
                    "lab3",
                    "data",
                    "inventory-report.txt"
            );


    public static void main(String[] args) {

        BufferedReader console =
                new BufferedReader(
                        new InputStreamReader(
                                System.in,
                                StandardCharsets.UTF_8
                        )
                );

        try {

            Files.createDirectories(
                    INVENTORY_FILE.getParent()
            );

            // 1. Nhập sản phẩm
            List<Product> products =
                    inputProducts(console);

            // 2. Lưu xuống CSV
            saveToCsv(products);

            // 3. Đọc lại từ CSV
            List<Product> loadedProducts =
                    readFromCsv();

            // 4. Hiển thị
            displayProducts(loadedProducts);

            // 5. Tính tổng giá trị tồn kho
            double total =
                    calculateTotalInventory(
                            loadedProducts
                    );

            System.out.println(
                    "\nTổng giá trị tồn kho: "
                            + String.format(
                                    "%,.0f",
                                    total
                            )
                            + " VND"
            );

            // 6. Tìm sản phẩm có giá trị tồn kho cao nhất
            Product maxProduct =
                    findHighestInventoryValue(
                            loadedProducts
                    );

            if (maxProduct != null) {

                System.out.println(
                        "\nSản phẩm có giá trị tồn kho cao nhất:"
                );

                System.out.println(
                        maxProduct
                );
            }

            // 7. Ghi báo cáo
            writeReport(
                    loadedProducts,
                    total,
                    maxProduct
            );

        } catch (IOException e) {

            System.out.println(
                    "Lỗi chương trình: "
                            + e.getMessage()
            );
        }
    }


    // =========================
    // NHẬP SẢN PHẨM
    // =========================

    public static List<Product> inputProducts(
            BufferedReader reader
    ) throws IOException {

        List<Product> products =
                new ArrayList<>();

        int numberOfProducts;

        while (true) {

            try {

                System.out.print(
                        "Nhập số lượng sản phẩm: "
                );

                numberOfProducts =
                        Integer.parseInt(
                                reader.readLine()
                        );

                if (numberOfProducts <= 0) {

                    System.out.println(
                            "Số lượng phải lớn hơn 0."
                    );

                    continue;
                }

                break;

            } catch (NumberFormatException e) {

                System.out.println(
                        "Vui lòng nhập số nguyên."
                );
            }
        }


        for (int i = 0;
             i < numberOfProducts;
             i++) {

            System.out.println(
                    "\n--- Sản phẩm "
                            + (i + 1)
                            + " ---"
            );

            while (true) {

                try {

                    System.out.print(
                            "Mã sản phẩm: "
                    );

                    String code =
                            reader.readLine();


                    System.out.print(
                            "Tên sản phẩm: "
                    );

                    String name =
                            reader.readLine();


                    System.out.print(
                            "Đơn giá: "
                    );

                    double unitPrice =
                            Double.parseDouble(
                                    reader.readLine()
                            );


                    System.out.print(
                            "Số lượng: "
                    );

                    int quantity =
                            Integer.parseInt(
                                    reader.readLine()
                            );


                    Product product =
                            new Product(
                                    code,
                                    name,
                                    unitPrice,
                                    quantity
                            );

                    products.add(product);

                    break;

                } catch (NumberFormatException e) {

                    System.out.println(
                            "Đơn giá hoặc số lượng không hợp lệ."
                    );

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Lỗi: "
                                    + e.getMessage()
                    );
                }
            }
        }

        return products;
    }


    // =========================
    // GHI CSV
    // =========================

    public static void saveToCsv(
            List<Product> products
    ) {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             INVENTORY_FILE,
                             StandardCharsets.UTF_8
                     )) {

            writer.write(
                    "ma,ten,donGia,soLuong"
            );

            writer.newLine();


            for (Product product : products) {

                writer.write(
                        product.toCsv()
                );

                writer.newLine();
            }


            System.out.println(
                    "\nĐã lưu dữ liệu vào: "
                            + INVENTORY_FILE
            );


        } catch (IOException e) {

            System.out.println(
                    "Không ghi được file "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage()
            );
        }
    }


    // =========================
    // ĐỌC CSV
    // =========================

    public static List<Product> readFromCsv() {

        List<Product> products =
                new ArrayList<>();


        if (!Files.exists(INVENTORY_FILE)) {

            System.out.println(
                    "Không tìm thấy file: "
                            + INVENTORY_FILE
            );

            return products;
        }


        try (BufferedReader reader =
                     Files.newBufferedReader(
                             INVENTORY_FILE,
                             StandardCharsets.UTF_8
                     )) {

            // Bỏ dòng tiêu đề
            reader.readLine();

            String line;

            int lineNumber = 1;


            while ((line = reader.readLine())
                    != null) {

                lineNumber++;


                if (line.isBlank()) {
                    continue;
                }


                String[] parts =
                        line.split(",", -1);


                if (parts.length != 4) {

                    System.out.println(
                            "Dòng "
                                    + lineNumber
                                    + " thiếu cột."
                    );

                    continue;
                }


                try {

                    String code =
                            parts[0].trim();

                    String name =
                            parts[1].trim();

                    double unitPrice =
                            Double.parseDouble(
                                    parts[2].trim()
                            );

                    int quantity =
                            Integer.parseInt(
                                    parts[3].trim()
                            );


                    Product product =
                            new Product(
                                    code,
                                    name,
                                    unitPrice,
                                    quantity
                            );

                    products.add(product);


                } catch (NumberFormatException e) {

                    System.out.println(
                            "Dòng "
                                    + lineNumber
                                    + " có dữ liệu số không hợp lệ."
                    );

                } catch (IllegalArgumentException e) {

                    System.out.println(
                            "Dòng "
                                    + lineNumber
                                    + " không hợp lệ: "
                                    + e.getMessage()
                    );
                }
            }


        } catch (IOException e) {

            System.out.println(
                    "Không đọc được file "
                            + INVENTORY_FILE
                            + ": "
                            + e.getMessage()
            );
        }


        return products;
    }


    // =========================
    // HIỂN THỊ
    // =========================

    public static void displayProducts(
            List<Product> products
    ) {

        System.out.println(
                "\n===== DANH SÁCH SẢN PHẨM ====="
        );


        if (products.isEmpty()) {

            System.out.println(
                    "Danh sách trống."
            );

            return;
        }


        for (Product product : products) {

            System.out.println(
                    product
            );
        }
    }


    // =========================
    // TÍNH TỔNG
    // =========================

    public static double calculateTotalInventory(
            List<Product> products
    ) {

        double total = 0;


        for (Product product : products) {

            total +=
                    product.inventoryValue();
        }


        return total;
    }


    // =========================
    // TÌM MAX
    // =========================

    public static Product findHighestInventoryValue(
            List<Product> products
    ) {

        if (products.isEmpty()) {
            return null;
        }


        Product maxProduct =
                products.get(0);


        for (Product product : products) {

            if (product.inventoryValue()
                    > maxProduct.inventoryValue()) {

                maxProduct = product;
            }
        }


        return maxProduct;
    }


    // =========================
    // GHI REPORT
    // =========================

    public static void writeReport(
            List<Product> products,
            double total,
            Product maxProduct
    ) {

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             REPORT_FILE,
                             StandardCharsets.UTF_8
                     )) {

            writer.write(
                    "BÁO CÁO TỒN KHO"
            );

            writer.newLine();


            writer.write(
                    "Số sản phẩm: "
                            + products.size()
            );

            writer.newLine();


            writer.write(
                    "Tổng giá trị tồn kho: "
                            + String.format(
                                    "%,.0f",
                                    total
                            )
                            + " VND"
            );

            writer.newLine();


            if (maxProduct != null) {

                writer.write(
                        "Sản phẩm có giá trị tồn kho cao nhất: "
                                + maxProduct.getCode()
                                + " - "
                                + maxProduct.getName()
                                + " - "
                                + String.format(
                                        "%,.0f",
                                        maxProduct.inventoryValue()
                                )
                                + " VND"
                );

                writer.newLine();
            }


            System.out.println(
                    "\nĐã ghi báo cáo vào: "
                            + REPORT_FILE
            );


        } catch (IOException e) {

            System.out.println(
                    "Không ghi được báo cáo: "
                            + e.getMessage()
            );
        }
    }
}