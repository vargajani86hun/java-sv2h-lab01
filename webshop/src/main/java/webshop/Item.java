package webshop;

import java.util.Objects;

public class Item {
    private Product product;
    private int amount;
    private int sumPrice;

    public Item(Product product, int amount) {
        this.product = product;
        this.amount = amount;
        setPrice();
    }

    private void setPrice() {
        sumPrice = product.getPrice() * amount;
    }

    public void modifyAmount(int value) {
        amount += value;
        setPrice();
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public int getSumPrice() {
        return sumPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return Objects.equals(product, item.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}
