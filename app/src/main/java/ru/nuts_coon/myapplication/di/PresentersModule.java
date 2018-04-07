package ru.nuts_coon.myapplication.di;

import dagger.Module;
import dagger.Provides;
import ru.nuts_coon.myapplication.Interfaces.Presenters.MainPresenterInterface;
import ru.nuts_coon.myapplication.Interfaces.Presenters.SendPresenterInterface;
import ru.nuts_coon.myapplication.Presenters.MainPresenter;
import ru.nuts_coon.myapplication.Presenters.SendPresenter;

@Module
public class PresentersModule {
    @Provides
    MainPresenterInterface mainPresenter(){
        return new MainPresenter();
    }

    @Provides
    SendPresenterInterface sendPresenter(){
        return new SendPresenter();
    }
}
