package ru.nuts_coon.myapplication.Interfaces.Presenters;

import android.os.Bundle;

import ru.nuts_coon.myapplication.Interfaces.Views.SendView;

public interface SendPresenterInterface extends BasePresenterInterface<SendView> {
    void viewIsReady(Bundle bundle);
}
