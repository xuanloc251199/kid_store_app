package com.example.kid_toy_store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.utils.KeyUtils;
import com.example.kid_toy_store.view.activity.CheckOutActivity;
import com.example.kid_toy_store.view.activity.EditPaymentCardActivity;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<Card> cardList;
    private Context mContext;

    public CardAdapter(List<Card> cardList, Context mContext) {
        this.cardList = cardList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_payment_card, parent, false);
        return new CardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cardList.get(position);
        holder.tvCardNumber.setText("**** **** **** " + card.getCardNumber().substring(card.getCardNumber().length() - 4));  // Đã có dạng **** **** **** 1234 từ API
        holder.imgEditPaymentCard.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditPaymentCardActivity.class);
            intent.putExtra(KeyUtils.CARD_PAYMENT_KEY, card.getId());
            mContext.startActivity(intent);
        });
        holder.cvCard.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CheckOutActivity.class);
            intent.putExtra(KeyUtils.CARD_PAYMENT_KEY, card.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    // ViewHolder class
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCardNumber;
        public CardView cvCard;
        public ImageView imgEditPaymentCard;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCardNumber = itemView.findViewById(R.id.tvCardNumber);
            cvCard = itemView.findViewById(R.id.cvPaymentCard);
            imgEditPaymentCard = itemView.findViewById(R.id.imgEditPaymentMethod);
        }
    }
}

