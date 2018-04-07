package ru.nuts_coon.myapplication.Presenters;

import android.content.Context;

import javax.inject.Inject;

import ru.nuts_coon.myapplication.App;
import ru.nuts_coon.myapplication.Helpers.ImageHelper;
import ru.nuts_coon.myapplication.Interfaces.Presenters.BasePresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Views.BaseView;

public abstract class BasePresenter<T extends BaseView> implements BasePresenterInterface<T> {

    T view;
    Context context;

    @Inject
    ImageHelper imageHelper;

    BasePresenter() {
        imageHelper = App.getHelpersComponent().getImageHelper();
    }

    @Override
    public void attachView(T view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void detachView() {
        view = null;
        context = null;
    }

    @Override
    public void onBtnViewClick() {
        btnClickEvent();
    }

    abstract void btnClickEvent();
}
