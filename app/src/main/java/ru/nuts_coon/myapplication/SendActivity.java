package ru.nuts_coon.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;


public class SendActivity extends Activity {

    int imageHeight;

    AppCompatDelegate mDelegate;

    TextView tvEmail, tvPhone, tvPassword;
    ImageView imageView;
    Button btnSend;

    String email, phone, password;
    Uri photoPath;

    AppModel appModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity);
        appModel = new AppModel(getApplicationContext());
        setupActionBar();
        setupObjects();
        setImageViewSize();
        receiveIntent();
        inflateTextView();
        setImage();
        setupClickListener();
    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getResources().getString(R.string.app_name));
    }

    private ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    private void setupObjects(){
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvPassword = findViewById(R.id.tv_password);

        imageView = findViewById(R.id.send_iv);
        btnSend = findViewById(R.id.btn_send);
    }

    private void setImageViewSize(){
        imageHeight = (9 * getResources().getDisplayMetrics().widthPixels) / 16;
        imageView.getLayoutParams().height = imageHeight;
    }

    private void receiveIntent(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            email = bundle.getString("email");
            phone = appModel.formatPhoneNumber(bundle.getString("phone"));
            password = bundle.getString("password");
            photoPath = Uri.parse(bundle.getString("photoPath"));
        }
    }

    private void inflateTextView(){
        tvEmail.setText(email);
        tvPhone.setText(phone);
        tvPassword.setText(password);
    }

    private void setImage(){
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = imageHeight;
        Bitmap bm = null;

        try {
            bm = appModel.getPicture(photoPath, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bm);
    }

    private void setupClickListener(){
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });
    }

    private void sendEmail(){
        String emailText =
                getString(R.string.email) + ": " + email + "\n\r" +
                        getString(R.string.phone) + ": " + phone + "\n\r";

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + ": данные анкеты" );
        intent.putExtra(Intent.EXTRA_TEXT, emailText);
        intent.putExtra(Intent.EXTRA_STREAM, photoPath);

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return false;
    }
}
