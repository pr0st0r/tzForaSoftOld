package ru.prostor.myalbum;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.prostor.myalbum.UI.RV.adapter.AlbumAdapter;
import ru.prostor.myalbum.dagger.component.DaggerAppComponent;
import ru.prostor.myalbum.http.entity.Album;
import ru.prostor.myalbum.http.dto.ResponseItunesApi;
import ru.prostor.myalbum.http.itunesApi.ItunesApi;

import static ru.prostor.myalbum.http.itunesApi.Country.RU;
import static ru.prostor.myalbum.http.itunesApi.Entity.ALBUM;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private AlbumAdapter albumAdapter;
    private CountDownTimer mTimer;
    private SearchView mSearchAlbum;
    private AlertDialog alert;

    private boolean isShowDialog = true;

    private final String searchQuery = "searchQuery";
    private final String showDialog = "showDialog";

    @Inject
    ItunesApi itunesApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        setContentView(R.layout.activity_main);

        DaggerAppComponent.create().inject(this);

        if(isConnectInternet()){
            builderAlert(R.string.alert_title, R.string.alert_message);
            alert.setIcon(R.drawable.alert_succes);
        }else{
            builderAlert(R.string.alert_title_error, R.string.alert_message_error);
            alert.setIcon(R.drawable.alert_error);
        }

        if(savedInstanceState != null)
            isShowDialog = savedInstanceState.getBoolean(showDialog);

        // Настраиваем список
        RecyclerView albumList = findViewById(R.id.album_list);
        albumAdapter = new AlbumAdapter();
        albumList.setAdapter(albumAdapter);

        // Настраиваем поиск
        mSearchAlbum = findViewById(R.id.search_album);
        mSearchAlbum.setOnQueryTextListener(this);
        mSearchAlbum.setFocusable(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(isShowDialog)
            alert.show();
    }

    /**
     * Событие SearchView при клике
     * @param s - текст в SearchView
     * @return - boolean
     */
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    /**
     * Событие изменинея текста в SearchView
     * @param s - текст в SearchView
     * @return - boolean
     */
    @Override
    public boolean onQueryTextChange(String s) {

        if(mTimer != null){
            mTimer.cancel();
        }

        if(!s.isEmpty()){
            final String term = s;

            mTimer = new CountDownTimer(1000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }
                @Override
                public void onFinish() {
                    loadAlbumList(term);
                }
            };
            mTimer.start();
        }

        return true;
    }

    /**
     * Загрузка альбомов
     * @param term - чей альбом ищем
     */
    private void loadAlbumList(String term){
        itunesApi.getAlbums(term, ALBUM.getType(), RU.getType()).enqueue(new Callback<ResponseItunesApi<Album>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseItunesApi<Album>> call,@NonNull Response<ResponseItunesApi<Album>> response) {
                if(response.isSuccessful()){
                    if(response.body() != null && response.body().getResultCount() > 0){
                        albumAdapter.setAlbumList(response.body().getResults());
                        albumAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), R.string.error_search, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseItunesApi<Album>> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Проверяем подключение к интернету
     * @return - true если интернет есть
     * иначе false
     */
    private boolean isConnectInternet(){
        Context context = getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * Диалоговое окно
     * @param title - заголовок
     * @param message - сообщение
     */
    private void builderAlert(@StringRes int title,@StringRes int message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("OK",(dialog, id) -> {
                    dialog.cancel();
                    isShowDialog = false;
                });

        alert = builder.create();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(searchQuery, mSearchAlbum.getQuery());
        outState.putBoolean(showDialog, isShowDialog);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSearchAlbum.setQuery(savedInstanceState.getCharSequence(searchQuery), true);
    }
}
