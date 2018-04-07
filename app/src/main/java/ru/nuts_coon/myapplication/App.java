package ru.nuts_coon.myapplication;

import android.app.Application;

import ru.nuts_coon.myapplication.di.AppComponent;
import ru.nuts_coon.myapplication.di.DaggerAppComponent;
import ru.nuts_coon.myapplication.di.DaggerHelpersComponent;
import ru.nuts_coon.myapplication.di.HelpersComponent;

public class App extends Application {

    private static AppComponent component;
    private static HelpersComponent helpersComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }

    void initDagger(){
        component = DaggerAppComponent.create();
        helpersComponent = DaggerHelpersComponent.create();
    }

    public static AppComponent getAppComponent(){
        return component;
    }

    public static HelpersComponent getHelpersComponent(){
        return helpersComponent;
    }
}
