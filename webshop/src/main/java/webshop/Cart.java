package webshop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart {

    private List<Item> items = new ArrayList<>();

    public List<Item> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void addItem(Item item) {
        if (items.stream().noneMatch(i -> i.getProduct().equals(item.getProduct()))) {
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
