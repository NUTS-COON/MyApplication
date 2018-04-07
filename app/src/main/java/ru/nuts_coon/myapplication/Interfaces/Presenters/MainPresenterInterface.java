package ru.nuts_coon.myapplication.Interfaces.Presenters;

import android.net.Uri;

import ru.nuts_coon.myapplication.Interfaces.Views.MainView;

public interface MainPresenterInterface extends BasePresenterInterface<MainView> {
    void onProfileImageClick();
    void permissionsIsGranted();

    void photoWasTaken(Uri photoPath, int reqWidth, int reqHeight);
}
