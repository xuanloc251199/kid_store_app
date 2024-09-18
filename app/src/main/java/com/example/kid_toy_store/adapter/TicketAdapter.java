package com.example.kid_toy_store.adapter;

import android.annotation.SuppressLint;
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
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.formatter.PromotionFormatter;
import com.example.kid_toy_store.formatter.QuantityFormatter;
import com.example.kid_toy_store.view.activity.DetailPlaygroundActivity;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    List<Tickets> tickets;
    Context mContext;

    public TicketAdapter(List<Tickets> tickets, Context mContext) {
        this.tickets = tickets;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playground, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TicketAdapter.ViewHolder holder, int position) {

        Tickets ticket = tickets.get(position);

        holder.tvNamePG.setText(ticket.getName());
        holder.tvDatePG.setText(ticket.getDate());
        holder.tvAddressPG.setText(ticket.getPlace());
        holder.tvPricePG.setText(CurrencyFormatter.formatCurrency(ticket.getFinalPrice()));
        if (ticket.getPromotion() != 0){
            holder.tvPromotion.setText(PromotionFormatter.formatPromotion(ticket.getPromotion()));
        } else holder.tvPromotion.setVisibility(View.GONE);
        if (ticket.getSold() == 0){
            holder.tvSold.setVisibility(View.GONE);
        } else {
            holder.tvSold.setText(QuantityFormatter.formatQuantity(ticket.getSold()));
        }
        Picasso.get().load(ApiUtils.IP_DEFAULT + ticket.getThumbnail()).into(holder.imgPG);
        holder.cvticket.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailPlaygroundActivity.class);
            intent.putExtra(KeyUtils.TICKET_KEY, ticket.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return tickets == null ? 0 : tickets.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTickets(List<Tickets> tickets) {
        this.tickets = tickets != null ? tickets : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvticket;
        ImageView imgPG;
        TextView tvNamePG, tvDatePG, tvAddressPG, tvPricePG, tvSold, tvPromotion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvticket = itemView.findViewById(R.id.cvPlayGround);
            imgPG = itemView.findViewById(R.id.imgPG);
            tvNamePG = itemView.findViewById(R.id.tvNamePG);
            tvDatePG = itemView.findViewById(R.id.tvDatePG);
            tvAddressPG = itemView.findViewById(R.id.tvAddressPG);
            tvPricePG = itemView.findViewById(R.id.tvPricePG);
            tvPromotion = itemView.findViewById(R.id.tvPromotionPG);
            tvSold = itemView.findViewById(R.id.tvSoldPG);

        }
    }
}
