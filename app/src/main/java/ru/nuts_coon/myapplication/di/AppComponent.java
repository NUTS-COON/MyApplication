package ru.nuts_coon.myapplication.di;

import dagger.Component;
import ru.nuts_coon.myapplication.View.MainActivity;
import ru.nuts_coon.myapplication.View.SendActivity;

@Component(modules = {PresentersModule.class, HelpersModule.class})
public interface AppComponent {
    void inject(MainActivity mainActivity);
    void inject(SendActivity sendActivity);
}
