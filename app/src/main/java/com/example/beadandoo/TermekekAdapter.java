package com.example.beadandoo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class TermekekAdapter extends RecyclerView.Adapter<TermekekAdapter.ViewHolder> implements Filterable {
    private ArrayList<Termekek> mShoppingItemsData;
    private ArrayList<Termekek> mShoppingItemsDataAll;
    private Context mcontext;
    private int lastPosition=-1;

    TermekekAdapter(Context context, ArrayList<Termekek> itemsData){
    this.mShoppingItemsData=itemsData;
    this.mShoppingItemsDataAll=itemsData;
    this.mcontext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.aruk,parent,false));
    }

    @Override
    public void onBindViewHolder(TermekekAdapter.ViewHolder holder, int position) {
        Termekek currentItem =mShoppingItemsData.get(position);

        holder.bindTo(currentItem);

        if (holder.getAdapterPosition()>lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition=holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mShoppingItemsData.size();
    }

    @Override
    public Filter getFilter() {
        return shoppingFilter;
    }
    private Filter shoppingFilter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Termekek> filteredList = new ArrayList<>();
            FilterResults results =new FilterResults();
            if (charSequence==null || charSequence.length()==0){
                results.count = mShoppingItemsDataAll.size();
                results.values = mShoppingItemsDataAll;
            }else {
                String filterPattern =charSequence.toString().toLowerCase().trim();
                for (Termekek termek : mShoppingItemsDataAll){
                    if(termek.getNeve().toLowerCase().contains(filterPattern)){
                        filteredList.add(termek);
                    }
                }

                results.count=filteredList.size();
                results.values=filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mShoppingItemsData=(ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText;
        private TextView mInfotext;
        private TextView mArText;
        private ImageView TermekKep;
        private RatingBar csillagok;


        public ViewHolder(View itemView) {
            super(itemView);

            mTitleText=itemView.findViewById(R.id.itemTitle);
            mInfotext=itemView.findViewById(R.id.subTitle);
            mArText=itemView.findViewById(R.id.ar);
            TermekKep=itemView.findViewById(R.id.termekKep);
            csillagok=itemView.findViewById(R.id.csillagok);

            itemView.findViewById(R.id.kosarhozadás).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("Activity","Add cart button click");
                    ((ShopActivity)mcontext).updateAlertIcon();
                }
            });
        }

        public void bindTo(Termekek currentItem) {
            mTitleText.setText(currentItem.getNeve());
            mInfotext.setText(currentItem.getInfo());
            mArText.setText(currentItem.getAr());
            csillagok.setRating(currentItem.getCsillagokSzama());
            Glide.with(mcontext).load(currentItem.getImageResource()).into(TermekKep);
            itemView.findViewById(R.id.kosarhozadás).setOnClickListener(view -> ((ShopActivity)mcontext).updateAlertIcon());
        }
    };

};
