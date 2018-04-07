package ru.nuts_coon.myapplication.Interfaces.Views;

import android.net.Uri;

import ru.nuts_coon.myapplication.Models.UserData;

public interface MainView extends BaseView {
    UserData getUserData();
    String getAppName();
    void takePhoto(Uri patch);
    void setEmailError(boolean isEmailValid);
    void setPhoneError(boolean isPhoneValid);
    void setPasswordError(boolean isPasswordValid);
    void showPhotoError();
    void startNextScreen(UserData userData);

    boolean verifyStoragePermissions();
    void getPermissions();
}
