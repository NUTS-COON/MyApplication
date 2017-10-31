package ru.nuts_coon.myapplication;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.lang.ref.WeakReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    AppModel appModel;

    private static int TAKE_PICTURE = 1;

    private TextInputLayout textInputLayoutEmail, textInputLayoutPhone, textInputLayoutPassword;
    private EditText etEmail, etPhone, etPassword;
    private CircleImageView imageProfile;
    private Button btnView;

    private Uri photoPatch;
    private boolean photoWasAdded = false;

    InputMethodManager imm;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        appModel = new AppModel(getApplicationContext());
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setupObjects();
        setupListener();
        setupClickListener();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            verifyStoragePermissions(this);
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    private void setupObjects(){
        textInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        etEmail = findViewById(R.id.et_email);

        textInputLayoutPhone = findViewById(R.id.textInputLayoutPhone);
        etPhone = findViewById(R.id.et_phone);

        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        etPassword = findViewById(R.id.et_password);

        imageProfile = findViewById(R.id.profile_image);
        btnView = findViewById(R.id.btn_view);
    }

    private void setupListener(){
        etEmail.setOnEditorActionListener(ActionListener.newInstance(this));
        etPhone.setOnEditorActionListener(ActionListener.newInstance(this));
        etPassword.setOnEditorActionListener(ActionListener.newInstance(this));
    }

    private void setupClickListener(){
        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEmailValid() && checkPhoneValid()
                   && checkPasswordValid() && checkPhotoValid()){
                    startPreviewScreen();
                }
            }
        });
    }

    private void takePhoto() {
        photoPatch = appModel.getPhotoPath();

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoPatch);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private void startPreviewScreen(){
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String password = etPassword.getText().toString();

        Intent intent = new Intent(MainActivity.this, SendActivity.class);
        intent.putExtra("email", email);
        intent.putExtra("phone", phone);
        intent.putExtra("password", password);
        intent.putExtra("photoPath", photoPatch.toString());

        startActivity(intent);
    }

    private void showEmailError(){
        textInputLayoutEmail.setError(getString(R.string.error_invalid_email));
    }

    private void showPhoneError(){
        textInputLayoutPhone.setError(getString(R.string.error_invalid_phone));
    }

    private void showPasswordError(){
        textInputLayoutPassword.setError(getString(R.string.error_invalid_password));
    }

    private void showPhotoError(){
        Toast.makeText(getApplicationContext(), "You must add a photo", Toast.LENGTH_SHORT).show();
    }

    private void hideError(int id){
        switch (id){
            case R.id.et_email:
                textInputLayoutEmail.setError(null);
                break;
            case R.id.et_phone:
                textInputLayoutPhone.setError(null);
                break;
            case R.id.et_password:
                textInputLayoutPassword.setError(null);
        }
    }

    private boolean checkEmailValid(){
        if (Patterns.EMAIL_ADDRESS.matcher(etEmail.getText()).matches()){
            hideError(etEmail.getId());
            return true;
        }else {
            showEmailError();
            etEmail.requestFocusFromTouch();
            imm.showSoftInput(etEmail, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }
    }

    private boolean checkPhoneValid(){
        String s = etPhone.getText().toString();
        if (s.length() < 12 || !s.matches("[+]\\d+")){
            showPhoneError();
            etPhone.requestFocusFromTouch();
            imm.showSoftInput(etPhone, InputMethodManager.SHOW_IMPLICIT);
            return false;
        }else {
            hideError(etPhone.getId());
            return true;
        }
    }

    private boolean checkPasswordValid(){
        String s = etPassword.getText().toString();
        if (s.length() < 6 ||
                //Not contains letters or numbers
                !(s.matches("\\w*\\d+\\w*") && s.matches("\\w*[a-zA-Zа-яА-Я]+\\w*"))) {
            showPasswordError();
            etPassword.requestFocusFromTouch();
            imm.showSoftInput(etPassword, InputMethodManager.SHOW_IMPLICIT);
            return false;
        } else {
            hideError(etPassword.getId());
            return true;
        }
    }

    private boolean checkPhotoValid(){
        if (photoWasAdded){
            return true;
        }else {
            showPhotoError();
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == TAKE_PICTURE) {
            Bitmap img = null;
            try {
                img = appModel.getPicture(photoPatch, 100, 94);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageProfile.setImageBitmap(img);
            photoWasAdded = true;
        }
    }

    private static final class ActionListener implements TextView.OnEditorActionListener{
        private final WeakReference<MainActivity> mainActivityWeakReference;

        static ActionListener newInstance(MainActivity mainActivity){
            return new ActionListener(new WeakReference<>(mainActivity));
        }

        private ActionListener(WeakReference<MainActivity> weakReference){
            this.mainActivityWeakReference = weakReference;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            MainActivity mainActivity = mainActivityWeakReference.get();
            if (mainActivity != null){

                int id = v.getId();

                switch (id){
                    case R.id.et_email:
                        mainActivity.checkEmailValid();
                        break;
                    case R.id.et_phone:
                        mainActivity.checkPhoneValid();
                        break;
                    case R.id.et_password:
                        mainActivity.checkPasswordValid();
                        break;
                }
            }
            return false;
        }
    }
}