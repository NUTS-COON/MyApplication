package ru.nuts_coon.myapplication.Interfaces.Views;

import ru.nuts_coon.myapplication.Models.UserData;

public interface SendView extends BaseView {
    void sendEmail(UserData sendData);
    void inflateTextView(UserData sendData);
    int getImageWidth();
    int getImageHeight();
}
