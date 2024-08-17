package com.etrash.mytrashapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etrash.mytrashapp.databinding.ItemRedeemBinding;

import java.util.ArrayList;

public class trAdaptor extends RecyclerView.Adapter<trAdaptor.viewHolder> {

    Context context;
    ArrayList<trModel>list;

    public trAdaptor(Context context, ArrayList<trModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_redeem, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final trModel model = list.get(position);
        String status = model.getStatus();
        String coin = model.getCoin();
        String method = model.getPayment_Method();

        int currentCoin = Integer.parseInt(coin);

        double earn = currentCoin*0.02;

        holder.binding.paymentTitle.setText(model.getPayment_Method());
        holder.binding.paymentdetails.setText(model.getPaymentdetails());
        holder.binding.paymentDate.setText(model.getDate());

        holder.binding.textView15.setText("("+"Rs. "+earn+""+")");

        if(status.equals("false")){
            holder.binding.btnStatus.setText("Pending");
        }else {
            holder.binding.btnStatus.setText("Success");
            holder.binding.btnStatus.setBackgroundResource(R.drawable.btn_status);
        }

        if(method.equals("Paytm")){
            holder.binding.paymentMethodlogo.setImageResource(R.drawable.paytm);
        }
        if(method.equals("Amazon Coupons")){
            holder.binding.paymentMethodlogo.setImageResource(R.drawable.amazon);
        }
        if(method.equals("Google Play")){
            holder.binding.paymentMethodlogo.setImageResource(R.drawable.gplay);
        }
        if(method.equals("Paypal Coupons")){
            holder.binding.paymentMethodlogo.setImageResource(R.drawable.paypal);
        }

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class  viewHolder extends  RecyclerView.ViewHolder{

        ItemRedeemBinding binding;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            binding = ItemRedeemBinding.bind(itemView);
        }
    }
}
