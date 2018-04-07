package ru.nuts_coon.myapplication.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {

    private Uri photoPath;
    private String email;
    private String phone;
    private String password;

    public UserData(){}

    private UserData(Parcel in) {
        this.photoPath = Uri.parse(in.readString());
        this.email = in.readString();
        this.phone = in.readString();
        this.password = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Uri getPhotoPath(){
        return this.photoPath;
    }

    public void setPhotoPath(Uri photoPath) {
        this.photoPath = photoPath;
    }

    public String formatPhoneNumber(String phone){
        if (phone.length() > 10){
            int offset = 12 - phone.length();
            return formatPhone(phone, offset);
        }
        return phone;
    }

    private String formatPhone(String s, int offset){
        return s.substring(0, 2 - offset)
                + " (" + phone.substring(2 - offset,5 - offset)
                + ") " + phone.substring(5 - offset, 8 - offset)
                + " " + phone.substring(8 - offset, 10 - offset)
                + " " + phone.substring(10 - offset);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(photoPath.toString());
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(password);
    }
}
