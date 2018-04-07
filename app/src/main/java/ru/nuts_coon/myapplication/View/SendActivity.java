package ru.nuts_coon.myapplication.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.nuts_coon.myapplication.App;
import ru.nuts_coon.myapplication.Interfaces.Presenters.SendPresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Views.SendView;
import ru.nuts_coon.myapplication.Models.UserData;
import ru.nuts_coon.myapplication.R;


public class SendActivity extends AppCompatActivity implements SendView{

    @Inject
    SendPresenterInterface presenter;

    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.tv_phone) TextView tvPhone;
    @BindView(R.id.tv_password) TextView tvPassword;

    @BindView(R.id.send_iv) ImageView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_activity);
        ButterKnife.bind(this);
        App.getAppComponent().inject(this);
        presenter.attachView(this, getApplicationContext());

        setImageViewSize();
        receiveIntent();
    }

    private void setImageViewSize(){
        imageView.getLayoutParams().height = getImageHeight();
    }

    private void receiveIntent(){
        presenter.viewIsReady(getIntent().getExtras());
    }

    @Override
    public int getImageHeight(){
        return (9 * getResources().getDisplayMetrics().widthPixels) / 16;
    }

    @Override
    public int getImageWidth(){
        return imageView.getWidth();
    }

    @Override
    public void inflateTextView(UserData userData){
        tvEmail.setText(userData.getEmail());
        tvPhone.setText(userData.formatPhoneNumber(userData.getPhone()));
        tvPassword.setText(userData.getPassword());
    }

    @Override
    public void setProfileImage(Bitmap img){
        imageView.setImageBitmap(img);
    }

    @OnClick(R.id.btn_send)
    void onSendButtonClick(){
        presenter.onBtnViewClick();
    }

    @Override
    public void sendEmail(UserData userData){
        String email = userData.getEmail();
        String phone = userData.getPhone();
        Uri photoPath = userData.getPhotoPath();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
