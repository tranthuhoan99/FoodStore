package com.tranthuhoan.foodstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Manager implements Parcelable {
    @SerializedName("MnId")
    @Expose
    private String mnId;
    @SerializedName("MnEmail")
    @Expose
    private String mnEmail;
    @SerializedName("MnPassword")
    @Expose
    private String mnPassword;
    @SerializedName("MnName")
    @Expose
    private String mnName;
    @SerializedName("MnPhone")
    @Expose
    private String mnPhone;
    @SerializedName("MnAvatar")
    @Expose
    private String mnAvatar;
    
    protected Manager(Parcel in) {
        mnId = in.readString();
        mnEmail = in.readString();
        mnPassword = in.readString();
        mnName = in.readString();
        mnPhone = in.readString();
        mnAvatar = in.readString();
    }
    public static final Creator<Manager> CREATOR = new Creator<Manager>() {
        @Override
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mnId);
        dest.writeString(mnEmail);
        dest.writeString(mnPassword);
        dest.writeString(mnName);
        dest.writeString(mnPhone);
        dest.writeString(mnAvatar);
    }

    public String getMnId() {
        return mnId;
    }

    public void setMnId(String mnId) {
        this.mnId = mnId;
    }

    public String getMnEmail() {
        return mnEmail;
    }

    public void setMnEmail(String mnEmail) {
        this.mnEmail = mnEmail;
    }

    public String getMnPassword() {
        return mnPassword;
    }

    public void setMnPassword(String mnPassword) {
        this.mnPassword = mnPassword;
    }

    public String getMnName() {
        return mnName;
    }

    public void setMnName(String mnName) {
        this.mnName = mnName;
    }

    public String getMnPhone() {
        return mnPhone;
    }

    public void setMnPhone(String mnPhone) {
        this.mnPhone = mnPhone;
    }

    public String getMnAvatar() {
        return mnAvatar;
    }

    public void setMnAvatar(String mnAvatar) {
        this.mnAvatar = mnAvatar;
    }
}
