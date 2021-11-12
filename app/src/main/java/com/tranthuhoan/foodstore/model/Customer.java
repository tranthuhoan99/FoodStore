package com.tranthuhoan.foodstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Customer implements Parcelable {
    @SerializedName("CusId")
    @Expose
    private String cusId;
    @SerializedName("CusEmail")
    @Expose
    private String cusEmail;
    @SerializedName("CusPassword")
    @Expose
    private String cusPassword;
    @SerializedName("CusName")
    @Expose
    private String cusName;
    @SerializedName("CusPhone")
    @Expose
    private String cusPhone;
    @SerializedName("CusAddress")
    @Expose
    private String cusAddress;

    protected Customer(Parcel in) {
        cusId = in.readString();
        cusEmail = in.readString();
        cusPassword = in.readString();
        cusName = in.readString();
        cusPhone = in.readString();
        cusAddress = in.readString();
    }
    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cusId);
        dest.writeString(cusEmail);
        dest.writeString(cusPassword);
        dest.writeString(cusName);
        dest.writeString(cusPhone);
        dest.writeString(cusAddress);
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getCusPassword() {
        return cusPassword;
    }

    public void setCusPassword(String cusPassword) {
        this.cusPassword = cusPassword;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getCusAddress() {
        return cusAddress;
    }

    public void setCusAddress(String cusAddress) {
        this.cusAddress = cusAddress;
    }
}
