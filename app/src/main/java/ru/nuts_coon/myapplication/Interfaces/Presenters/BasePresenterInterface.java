package ru.nuts_coon.myapplication.Interfaces.Presenters;

import android.content.Context;

import ru.nuts_coon.myapplication.Interfaces.Views.BaseView;

public interface BasePresenterInterface<V extends BaseView> {
    void attachView(V view, Context context);
    void detachView();
    void onBtnViewClick();
}
