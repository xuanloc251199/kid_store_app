package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tickets;

import java.util.List;

public class SearchResponse {
    private List<Products> products;
    private List<Tickets> tickets;

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public List<Tickets> getTickets() {
        return tickets;
    }

    public void setTickets(List<Tickets> tickets) {
        this.tickets = tickets;
    }
}

