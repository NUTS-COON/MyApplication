package ru.nuts_coon.myapplication.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.nuts_coon.myapplication.Helpers.ImageHelper;

@Module
public class HelpersModule {
    @Singleton
    @Provides
    ImageHelper imageHelper(){
        return new ImageHelper();
    }
}
