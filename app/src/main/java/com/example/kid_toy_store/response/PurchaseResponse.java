package com.example.kid_toy_store.response;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PurchaseResponse {

    @SerializedName("purchases")
    private List<Purchase> purchases;

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public static class Purchase {
        @SerializedName("order_id")
        private int orderId;
        @SerializedName("order_date")
        private String orderDate;
        @SerializedName("items")
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public static class Item {
            @SerializedName("product_name")
            private String productName;
            @SerializedName("product_thumbnail")
            private String productThumbnail;
            @SerializedName("ticket_name")
            private String ticketName;
            @SerializedName("ticket_date")
            private String ticketDate;
            @SerializedName("ticket_place")
            private String ticketPlace;
            @SerializedName("ticket_thumbnail")
            private String ticketThumbnail;
            @SerializedName("quantity")
            private int quantity;
            @SerializedName("price")
            private String price;

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductThumbnail() {
                return productThumbnail;
            }

            public void setProductThumbnail(String productThumbnail) {
                this.productThumbnail = productThumbnail;
            }

            public String getTicketName() {
                return ticketName;
            }

            public void setTicketName(String ticketName) {
                this.ticketName = ticketName;
            }

            public String getTicketDate() {
                return ticketDate;
            }

            public void setTicketDate(String ticketDate) {
                this.ticketDate = ticketDate;
            }

            public String getTicketPlace() {
                return ticketPlace;
            }

            public void setTicketPlace(String ticketPlace) {
                this.ticketPlace = ticketPlace;
            }

            public String getTicketThumbnail() {
                return ticketThumbnail;
            }

            public void setTicketThumbnail(String ticketThumbnail) {
                this.ticketThumbnail = ticketThumbnail;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }
        }
    }
}
