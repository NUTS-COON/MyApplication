package ru.nuts_coon.myapplication.Presenters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import ru.nuts_coon.myapplication.Interfaces.Presenters.SendPresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Views.SendView;
import ru.nuts_coon.myapplication.Models.UserData;

public class SendPresenter extends BasePresenter<SendView> implements SendPresenterInterface {

    private UserData userData;

    @Override
    public void detachView() {
        view = null;
        context = null;
    }

    @Override
    public void viewIsReady(Bundle bundle){
        initScreen(bundle);
    }

    @Override
    void btnClickEvent() {
        view.sendEmail(userData);
    }

    private void initScreen(Bundle bundle){
        userData = new UserData();

        if (bundle != null){
            userData = (UserData) bundle.get("userData");
        }
        view.inflateTextView(userData);

        Bitmap img = getPicture(userData.getPhotoPath(),
                view.getImageWidth(), view.getImageHeight());
        view.setProfileImage(img);
    }

    private Bitmap getPicture(Uri path, int reqWidth, int reqHeight){
        Bitmap img = null;
        try {
            img = imageHelper.getPicture(path, reqWidth, reqHeight, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }
}
