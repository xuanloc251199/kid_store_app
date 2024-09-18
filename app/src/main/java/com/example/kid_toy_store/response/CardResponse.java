package com.example.kid_toy_store.response;

import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.model.CardInfo;

public class CardResponse {
    public String message;
    public Card card;

    public CardResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
