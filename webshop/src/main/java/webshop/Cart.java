package webshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public List<String> contentOfCartStringFormat() {
        List<String> contentOfCart = new ArrayList<>();
        for (Item item: items) {
            contentOfCart.add(String.format("%d %s %d", item.getProduct().getId(), item.getProduct().getName(), item.getAmount()));
        }
        return contentOfCart;
    }

    public void addItem(Item item) {
        if (items.stream().map(Item::getProduct).map(Product::getName)
                .noneMatch(s -> s.equals(item.getProduct().getName()))) {
            items.add(item);
        } else {
            items.stream()
                    .filter(i -> i.getProduct().getName().equals(item.getProduct().getName()))
                    .toList().get(0).modifyAmount(item.getAmount());
        }
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
