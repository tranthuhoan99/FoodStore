package com.tranthuhoan.foodstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food implements Parcelable {

    @SerializedName("FoodId")
    @Expose
    private String foodId;

    @SerializedName("FoodName")
    @Expose
    private String foodName;

    @SerializedName("FoodDes")
    @Expose
    private String foodDes;


    @SerializedName("FoodPrice")
    @Expose
    private String foodPrice;

    @SerializedName("FoodAvail")
    @Expose
    private String foodAvail;

    @SerializedName("FoodQuantity")
    @Expose
    private String foodQuantity;

    @SerializedName("FoodPhoto")
    @Expose
    private String foodPhoto;


    protected Food(Parcel in) {
        foodId = in.readString();
        foodName = in.readString();
        foodDes = in.readString();
        foodPrice = in.readString();
        foodAvail = in.readString();
        foodQuantity = in.readString();
        foodPhoto = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(foodId);
        parcel.writeString(foodName);
        parcel.writeString(foodDes);
        parcel.writeString(foodPrice);
        parcel.writeString(foodAvail);
        parcel.writeString(foodQuantity);
        parcel.writeString(foodPhoto);
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDes() {
        return foodDes;
    }

    public void setFoodDes(String foodDes) {
        this.foodDes = foodDes;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getFoodAvail() {
        return foodAvail;
    }

    public void setFoodAvail(String foodAvail) {
        this.foodAvail = foodAvail;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getFoodPhoto() {
        return foodPhoto;
    }

    public void setFoodPhoto(String foodPhoto) {
        this.foodPhoto = foodPhoto;
    }
}
