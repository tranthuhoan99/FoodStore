package com.tranthuhoan.foodstore.manager.food;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.model.Food;

import java.util.ArrayList;

public class ManagerFoodViewDetailsActivity extends AppCompatActivity {

    private ImageView ivAdFoodItemViewDetailsImage, ivAdFoodItemViewDetailsExit;
    private TextView tvAdFoodItemViewDetailsName, tvAdFoodItemViewDetailsPrice, tvAdFoodItemViewDetailsDes, tvAdFoodItemViewDetailsQuantity, tvAdFoodItemViewDetailsAvail;
    private Button btnAdFoodItemViewDetailsUpdate, btnAdFoodItemViewDetailsExit;

    ArrayList<Food> foodItemArr;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_food_view_details);

        //Connect layout
        initUI();

        //Receive Data from View All
        receiveDataFromFoodItemAdapter();

        //Set on View
        initView();

        //Button Exit
        btnAdFoodItemViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //ImageView Exit
        ivAdFoodItemViewDetailsExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });

        //Button update
        btnAdFoodItemViewDetailsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManagerFoodViewDetailsActivity.this, ManagerFoodUpdateActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("FOOD_DATA_ARRAY",foodItemArr);
                bundle.putInt("FOOD_DATA_POSITION", position);
                intent.putExtra("FOOD_DATA_FROM_AD_FOOD_VIEW_DETAILS_TO_UPDATE", bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        tvAdFoodItemViewDetailsName.setText(foodItemArr.get(position).getFoodName());
        tvAdFoodItemViewDetailsPrice.setText(foodItemArr.get(position).getFoodPrice());
        tvAdFoodItemViewDetailsDes.setText(foodItemArr.get(position).getFoodDes());
        tvAdFoodItemViewDetailsQuantity.setText(foodItemArr.get(position).getFoodQuantity());
        tvAdFoodItemViewDetailsAvail.setText(foodItemArr.get(position).getFoodAvail());

        if (!foodItemArr.get(position).getFoodPhoto().equals("")) {
            Picasso.get()
                    .load(foodItemArr.get(position).getFoodPhoto())
                    .placeholder(R.drawable.diet)
                    .error(R.drawable.diet)
                    .into(ivAdFoodItemViewDetailsImage);
        } else {
            ivAdFoodItemViewDetailsImage.setImageResource(R.drawable.diet);
        }
    }

    private void receiveDataFromFoodItemAdapter() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("FOOD_DATA_FROM_ROOM_ADAPTER_TO_AD_FOOD_VIEW_DETAILS");
        if (bundle != null) {
            foodItemArr = bundle.getParcelableArrayList("FOOD_DATA_ARRAY");
            position = bundle.getInt("FOOD_DATA_POSITION");
        }
    }

    private void initUI() {
        ivAdFoodItemViewDetailsImage = findViewById(R.id.iv_ad_food_item_view_details_avt);
        ivAdFoodItemViewDetailsExit = findViewById(R.id.iv_ad_food_item_view_details_exit);
        tvAdFoodItemViewDetailsName = findViewById(R.id.tv_ad_food_item_view_details_name);
        tvAdFoodItemViewDetailsPrice = findViewById(R.id.tv_ad_food_item_view_details_price);
        tvAdFoodItemViewDetailsDes = findViewById(R.id.tv_ad_food_item_view_details_des);
        tvAdFoodItemViewDetailsQuantity = findViewById(R.id.tv_ad_food_item_view_details_quantity);
        tvAdFoodItemViewDetailsAvail = findViewById(R.id.tv_ad_food_item_view_details_avail);
        btnAdFoodItemViewDetailsExit = findViewById(R.id.btn_ad_food_item_view_details_exit);
        btnAdFoodItemViewDetailsUpdate = findViewById(R.id.btn_ad_food_item_view_details_update);
    }

    private void backToMenu() {
        Intent intent = new Intent(ManagerFoodViewDetailsActivity.this, ManagerFoodViewAllActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        backToMenu();
    }
}