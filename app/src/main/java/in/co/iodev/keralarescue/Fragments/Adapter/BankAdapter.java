package in.co.iodev.keralarescue.Fragments.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import in.co.iodev.keralarescue.Models.Bank;
import in.co.iodev.keralarescue.R;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyViewHolder> {
    private Context context;
    private List<Bank> bankList;
    private RecyclerViewClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView bankIcon;
        private TextView bankName;
        private RecyclerViewClickListener clickListener;

        public MyViewHolder(View view , RecyclerViewClickListener clickListener) {
            super(view);
            this.clickListener = clickListener;
            bankIcon = view.findViewById(R.id.thumbnail);
            bankName = view.findViewById(R.id.bank_name);
            bankIcon.setOnClickListener(this);
            bankName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view , getAdapterPosition());
        }
    }

    public BankAdapter(Context context , List<Bank> banks , RecyclerViewClickListener recyclerViewClickListener) {
        this.context = context;
        this.bankList = banks;
        this.clickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bank_card , parent , false);
        return new MyViewHolder(itemView , clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Bank bank = bankList.get(position);
        holder.bankName.setText(bank.getBankName());
        Glide.with(context).load(bank.getThumbnail()).into(holder.bankIcon);
    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }
}

