package ru.prostor.myalbum.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import ru.prostor.myalbum.DetailAlbum;
import ru.prostor.myalbum.MainActivity;
import ru.prostor.myalbum.dagger.module.ItunesApiModule;

@Singleton
@Component(modules = ItunesApiModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void injectDetail(DetailAlbum activity);
}
