package ru.prostor.myalbum.dagger.module;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.prostor.myalbum.http.itunesApi.ItunesApi;

@Module
public class ItunesApiModule {

    @Provides
    Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://itunes.apple.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    ItunesApi getItunesApi(Retrofit retrofit){
        return retrofit.create(ItunesApi.class);
    }
}
