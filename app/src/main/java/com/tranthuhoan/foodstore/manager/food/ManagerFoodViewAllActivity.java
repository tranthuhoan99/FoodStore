package com.tranthuhoan.foodstore.manager.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tranthuhoan.foodstore.DividerItemDecorator;
import com.tranthuhoan.foodstore.R;
import com.tranthuhoan.foodstore.adapters.FoodListAdapter;
import com.tranthuhoan.foodstore.model.Food;
import com.tranthuhoan.foodstore.retrofit.APIUtils;
import com.tranthuhoan.foodstore.retrofit.DataClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerFoodViewAllActivity extends AppCompatActivity {

    private ArrayList<Food> foodArr, foodArrSearch;
    RecyclerView rvItems;
    SwipeRefreshLayout srlMnFoodViewAll;
    private FoodListAdapter foodListAdapter;

    ImageButton ibFoodAdd;
    EditText edtFoodViewAllSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_food_view_all);

        //Search
        edtFoodViewAllSearch = findViewById(R.id.edt_stu_view_all_search);
        edtFoodViewAllSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textSearch = charSequence.toString();
                FilterData(textSearch);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //SwipeRefreshLayout
        srlMnFoodViewAll = findViewById(R.id.srl_ad_stu_view_all);
        srlMnFoodViewAll.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onRefresh() {
                readData();
                foodListAdapter.notifyDataSetChanged();
                srlMnFoodViewAll.setRefreshing(false);
            }
        });

        //Circle Button Add
        ibFoodAdd = findViewById(R.id.ib_stu_add);
        ibFoodAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManagerFoodViewAllActivity.this, ManagerFoodAddActivity.class));
            }
        });

        addControls();
        readData();
    }
    //Filter data
    @SuppressLint("NotifyDataSetChanged")
    public void FilterData(String textSearch) {
        textSearch = textSearch.toLowerCase(Locale.getDefault());
        Log.d("filter", textSearch);
        foodArr.clear();
        if(textSearch.length() == 0) {
            foodArr.addAll(foodArrSearch);
            Log.d("load data", "all");
        }
        else {
            Log.d("load data", "filtered");
            for (int i=0; i<foodArrSearch.size(); i++) {
                if(foodArrSearch.get(i).getFoodName().toLowerCase(Locale.getDefault()).contains(textSearch) ||
                        foodArrSearch.get(i).getFoodPrice().toLowerCase(Locale.getDefault()).contains(textSearch)) {
                    foodArr.add(foodArrSearch.get(i));
                }
            }
        }
        foodListAdapter.notifyDataSetChanged();
    }

    private void readData() {
        foodArr.clear();
        foodArrSearch.clear();
        DataClient dataClient = APIUtils.getData();
        retrofit2.Call<List<Food>> callback = dataClient.ManagerViewAllFoodData();
        callback.enqueue(new Callback<List<Food>>() {
            @Override
            public void onResponse(Call<List<Food>> call, Response<List<Food>> response) {
                foodArr = (ArrayList<Food>) response.body();

                if (foodArr.size() > 0) {
                    foodArrSearch.addAll(foodArr);
                    foodListAdapter = new FoodListAdapter(getApplicationContext(), foodArr);
                    //foodListAdapter.notifyDataSetChanged();
                    rvItems.setAdapter(foodListAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<Food>> call, Throwable t) {
                Log.d("Error load all stu", t.getMessage());
            }
        });
    }

    private void addControls() {
        foodArr = new ArrayList<>();
        foodArrSearch = new ArrayList<>();
        rvItems = findViewById(R.id.rv_ad_stu_view_all_items);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvItems.setLayoutManager(layoutManager);
        rvItems.setHasFixedSize(true);

        //divider for RecycleView(need Class DividerItemDecorator and divider.xml)
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(ManagerFoodViewAllActivity.this, R.drawable.divider));
        rvItems.addItemDecoration(dividerItemDecoration);

        //Fix: No adapter attached; skipping layout
        //Set adapter first after show
        foodListAdapter = new FoodListAdapter(getApplicationContext(), foodArr); // this
        rvItems.setAdapter(foodListAdapter);
    }
}