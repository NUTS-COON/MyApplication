package ru.nuts_coon.myapplication.di;

import javax.inject.Singleton;

import dagger.Component;
import ru.nuts_coon.myapplication.Helpers.ImageHelper;

@Component(modules = {HelpersModule.class})
@Singleton
public interface HelpersComponent {
    ImageHelper getImageHelper();
}
