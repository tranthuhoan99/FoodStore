package com.tranthuhoan.foodstore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tranthuhoan.foodstore.model.Food;


import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> {
    //Form for adapter
    Context context;
    ArrayList<Food> foodArr;

    public FoodListAdapter(Context context, ArrayList<Food> foodArr) {
        this.context = context;
        this.foodArr = foodArr;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_food, parent, false);
        return new FoodViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodArr.get(position);

        String foodName = food.getFoodName();
        String foodPrice = food.getFoodPrice();


        if (!food.getFoodPhoto().equals("")) {
            Picasso.get()
                    .load(food.getFoodPhoto())
                    .placeholder(R.drawable.salad)
                    .error(R.drawable.salad)
                    .into(holder.ivFoodPhoto);
        } else {
            holder.ivFoodPhoto.setImageResource(R.drawable.salad);
        }


        holder.tvFoodName.setText(foodName);
        holder.tvFoodPrice.setText(foodPrice);

        //Click for RecycleView
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "Food " + food.getFoodName(), Toast.LENGTH_SHORT).show();
                } else {
//                    Intent intent = new Intent(view.getContext(), ManagerFoodViewInfoActivity.class);
//
//                    Bundle bundle = new Bundle();
//
//                    bundle.putParcelableArrayList("STUDENT_DATA_ARRAY", foodArr);
//                    bundle.putInt("STUDENT_DATA_POSITION", position);
//                    intent.putExtra("STUDENT_DATA_FROM_STUDENT_ADAPTER_TO_AD_STU_VIEW_PROFILE", bundle);
//                    view.getContext().startActivity(intent);
//                    ((Activity) view.getContext()).finish();

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodArr == null ? 0 : foodArr.size();
    }


    //Data ViewHolder class
    public static class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivFoodPhoto;
        TextView tvFoodName, tvFoodPrice;

        ItemClickListener itemClickListener;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tv_food_name);
            tvFoodPrice = itemView.findViewById(R.id.tv_food_price);
            ivFoodPhoto = itemView.findViewById(R.id.iv_food_avt);

            //Turn On Click for RecycleView
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        //onClick for RecycleView
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), false);
        }

        //onLongClick for RecycleView
        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
            //return false; // if not use
        }
    }
}
