package webshop;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return items;
    }

    public List<Item> contentOfCart() {
        return items;
    }

    public List<String> contentOfCartStringFormat() {
        List<String> contentOfCart = new ArrayList<>();
        for (Item item: items) {
            contentOfCart.add(String.format("%d %s %d", item.getProduct().getId(), item.getProduct().getName(), item.getAmount()));
        }
        return contentOfCart;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public int getTotalPrice() {
        return items.stream().mapToInt(Item::getSumPrice).sum();
    }

    public void emptyCart() {
        items.clear();
    }
}
