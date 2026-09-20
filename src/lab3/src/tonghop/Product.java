package lab3.src.tonghop;

public class Product {

    private String code;
    private String name;
    private double unitPrice;
    private int quantity;

    public Product(
            String code,
            String name,
            double unitPrice,
            int quantity
    ) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(
                    "Mã sản phẩm không được rỗng"
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Tên sản phẩm không được rỗng"
            );
        }

        if (unitPrice <= 0) {
            throw new IllegalArgumentException(
                    "Đơn giá phải lớn hơn 0"
            );
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Số lượng không được âm"
            );
        }

        this.code = code;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double inventoryValue() {
        return unitPrice * quantity;
    }

    public String toCsv() {
        return code + ","
                + name + ","
                + unitPrice + ","
                + quantity;
    }

    @Override
    public String toString() {

        return code
                + " - "
                + name
                + " - Giá: "
                + String.format("%,.0f", unitPrice)
                + " VND"
                + " - SL: "
                + quantity
                + " - Giá trị tồn kho: "
                + String.format("%,.0f", inventoryValue())
                + " VND";
    }
}