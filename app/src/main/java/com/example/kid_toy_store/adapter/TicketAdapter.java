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
import com.example.kid_toy_store.activity.DetailPlaygroundActivity;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.utils.ApiUtils;
import com.squareup.picasso.Picasso;

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
        String pricePG = CurrencyFormatter.formatCurrency(ticket.getPrice());

        holder.tvNamePG.setText(ticket.getName());
        holder.tvDatePG.setText(ticket.getDate());
        holder.tvAddressPG.setText(ticket.getPlace());
        holder.tvPricePG.setText(pricePG);
        Picasso.get().load(ApiUtils.IP_DEFAULT + ticket.getThumbnail()).into(holder.imgPG);
        holder.cvticket.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DetailPlaygroundActivity.class)));
    }

    @Override
    public int getItemCount() {
        return tickets == null ? 0 : tickets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvticket;
        ImageView imgPG;
        TextView tvNamePG, tvDatePG, tvAddressPG, tvPricePG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvticket = itemView.findViewById(R.id.cvPlayGround);
            imgPG = itemView.findViewById(R.id.imgPG);
            tvNamePG = itemView.findViewById(R.id.tvNamePG);
            tvDatePG = itemView.findViewById(R.id.tvDatePG);
            tvAddressPG = itemView.findViewById(R.id.tvAddressPG);
            tvPricePG = itemView.findViewById(R.id.tvPricePG);

        }
    }
}
