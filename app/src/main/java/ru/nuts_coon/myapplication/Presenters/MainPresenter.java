package ru.nuts_coon.myapplication.Presenters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.Patterns;

import ru.nuts_coon.myapplication.Interfaces.Presenters.MainPresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Views.MainView;
import ru.nuts_coon.myapplication.Models.UserData;

public class MainPresenter extends BasePresenter<MainView> implements MainPresenterInterface {

    private Uri photoPath;
    private boolean photoWasAdded = false;

    @Override
    void btnClickEvent() {
        UserData userData = view.getUserData();
        if(checkUserData(userData)){
            userData.setPhotoPath(photoPath);
            view.startNextScreen(userData);
        }
    }

    @Override
    public void onProfileImageClick() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(view.verifyStoragePermissions()){
                takePhoto();
            }else {
                view.getPermissions();
            }
        }else {
            takePhoto();
        }
    }

    @Override
    public void permissionsIsGranted(){
        takePhoto();
    }

    @Override
    public void photoWasTaken(Uri photoPath, int reqWidth, int reqHeight) {
        Bitmap img = getImage(photoPath, reqWidth, reqHeight);
        view.setProfileImage(imageHelper.getRoundedShape(img, reqWidth, reqHeight));
        this.photoPath = photoPath;
        photoWasAdded = true;
    }

    private void takePhoto(){
        view.takePhoto(getPhotoPath(view.getAppName()));
    }

    private Uri getPhotoPath(String appName){
        return imageHelper.getPhotoPath(appName);
    }

    private Bitmap getImage(Uri photoPath, int reqWidth, int reqHeight){
        Bitmap img = null;
        try {
            img = imageHelper.getPicture(photoPath, reqWidth, reqHeight, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    private boolean checkUserData(UserData userData){
        return checkEmail(userData.getEmail())
                && checkPhone(userData.getPhone())
                && checkPassword(userData.getPassword())
                && checkPhoto();
    }

    private boolean checkEmail(String email){
        boolean isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        view.setEmailError(isValid);
        return isValid;
    }

    private boolean checkPhone(String phone){
        boolean isValid = (phone.length() == 12 && phone.matches("[+]\\d+"))
                || (phone.length() == 11 && phone.matches("\\d+"));
        view.setPhoneError(isValid);
        return isValid;
    }

    private boolean checkPassword(String password){
        boolean isValid = password.length() >= 6 &&
                //Not contains letters or numbers
                ((password.matches(".*\\d+.*") && password.matches(".*[a-zA-Zа-яА-Я]+.*")));
        view.setPasswordError(isValid);
        return isValid;
    }

    private boolean checkPhoto(){
        if(!photoWasAdded)
            view.showPhotoError();
        return photoWasAdded;
    }
}
