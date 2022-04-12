package webshop;

public class Item {
    private Product product;
    private int amount;

    public Item(Product product, int amount) {
        this.product = product;
        this.amount = amount;
    }

    public void modifyAmount(int value) {
        amount += value;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }
}
