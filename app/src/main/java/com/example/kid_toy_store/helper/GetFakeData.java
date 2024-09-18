package com.example.kid_toy_store.helper;

import com.example.kid_toy_store.model.Categories;
import com.example.kid_toy_store.model.Notifications;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tickets;

import java.util.ArrayList;

public class GetFakeData {

    public static  <T> ArrayList<T> getFakeData(Class<T> clazz, int quantity) {
        ArrayList<T> list = new ArrayList<>();

        try {
            if (clazz == Categories.class) {
//                for (int i = 0; i < quantity; i++) {
//                    Category category = new Category("Category", R.drawable.ic_category);
//                    list.add(clazz.cast(category));
//                }
            } else if (clazz == Products.class) {
//                for (int i = 0; i < quantity; i++) {
//                    int pricePrd = 100000 + i * 5000;
//                    int soldPrd = 2000 + i * 1000;
//                    Product product = new Product("Product Name " + i, "Product Name " + i, pricePrd, R.drawable.product, soldPrd, i);
//                    list.add(clazz.cast(product));
//                }
            } else if (clazz == Tickets.class) {
//                for (int i = 0; i < quantity; i++) {
//                    int pricePG = 100000 + i * 5000;
//                    Event playground = new Event("Playground Name" + i, "22 Sep, 2024", "Việt Nam", pricePG, R.drawable.playground);
//                    list.add(clazz.cast(playground));
//                }
            } else if (clazz == Notifications.class) {
//                for (int i = 0; i < quantity; i++) {
//                    Notifications notifications = new Notifications("New notify" + i);
//                    list.add(clazz.cast(notifications));
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

}
