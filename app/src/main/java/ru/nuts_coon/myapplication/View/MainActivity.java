package ru.nuts_coon.myapplication.View;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nuts_coon.myapplication.App;
import ru.nuts_coon.myapplication.Interfaces.Presenters.MainPresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Views.MainView;
import ru.nuts_coon.myapplication.Models.UserData;
import ru.nuts_coon.myapplication.R;


public class MainActivity extends AppCompatActivity implements MainView {

    @Inject
    MainPresenterInterface presenter;

    private static final int TAKE_PICTURE = 1;
    private static final int REQ_WIDTH = 250;
    private static final int REQ_HEIGHT = 250;
    private Uri photoPath;
    private InputMethodManager imm;

    @BindView(R.id.textInputLayoutEmail) TextInputLayout textInputLayoutEmail;
    @BindView(R.id.textInputLayoutPhone) TextInputLayout textInputLayoutPhone;
    @BindView(R.id.textInputLayoutPassword) TextInputLayout textInputLayoutPassword;

    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_phone) EditText etPhone;
    @BindView(R.id.et_password) EditText etPassword;

    @BindView(R.id.profile_image) ImageView ivProfile;


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);

        presenter.attachView(this, getApplicationContext());
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public boolean verifyStoragePermissions() {
        return checkPermissions();
    }

    @OnClick(R.id.btn_view)
    void OnViewClick(){
        presenter.onBtnViewClick();
    }

    @OnClick(R.id.profile_image)
    void onProfileImageClick(){
        presenter.onProfileImageClick();
    }

    @Override
    public UserData getUserData(){
        UserData userData = new UserData();
        userData.setEmail(etEmail.getText().toString());
        userData.setPhone(etPhone.getText().toString());
        userData.setPassword(etPassword.getText().toString());
        return userData;
    }

    @Override
    public String getAppName(){
        return getApplicationContext().getString(R.string.app_name);
    }

    @Override
    public void takePhoto(Uri path) {
        photoPath = path;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPath);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void startNextScreen(UserData userData){
        Intent intent = new Intent(MainActivity.this, SendActivity.class);
        intent.putExtra("userData", userData);

        startActivity(intent);
    }

    @Override
    public void setEmailError(boolean isEmailValid){
        setErrorOnEditText(textInputLayoutEmail, etEmail,
                isEmailValid, getString(R.string.error_invalid_email));
    }

    @Override
    public void setPhoneError(boolean isPhoneValid){
        setErrorOnEditText(textInputLayoutPhone, etPhone,
                isPhoneValid, getString(R.string.error_invalid_phone));
    }

    @Override
    public void setPasswordError(boolean isPasswordValid){
        setErrorOnEditText(textInputLayoutPassword, etPassword,
                isPasswordValid, getString(R.string.error_invalid_password));
    }

    @Override
    public void showPhotoError(){
        Toast.makeText(getApplicationContext(), getString(R.string.error_photo), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getPermissions(){
        ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void setProfileImage(Bitmap img){
        ivProfile.setScaleType(ImageView.ScaleType.FIT_CENTER);
        ivProfile.setBackground(null);
        ivProfile.setImageBitmap(img);
    }

    private void setErrorOnEditText(TextInputLayout textInput, EditText et, boolean isDataValid, String error){
        if(isDataValid){
            textInput.setError(null);
        }
        else{
            textInput.setError(error);
            setFocusOnEditText(et);
        }
    }

    private void setFocusOnEditText(EditText editText){
        editText.requestFocusFromTouch();
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void showNoStoragePermissionMsg() {
        Toast.makeText(this, getString(R.string.permissions_is_not_granted), Toast.LENGTH_LONG).show();
    }

    private boolean checkPermissions(){
        for(String s : PERMISSIONS_STORAGE){
            if(ActivityCompat.checkSelfPermission(this, s) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE) {
            presenter.photoWasTaken(photoPath, REQ_WIDTH, REQ_HEIGHT);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_EXTERNAL_STORAGE && grantResults.length == PERMISSIONS_STORAGE.length) {
            if(checkPermissions()){
                presenter.permissionsIsGranted();
            }else {
                showNoStoragePermissionMsg();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}