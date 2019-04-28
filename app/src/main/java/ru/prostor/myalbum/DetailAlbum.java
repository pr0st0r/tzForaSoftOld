package ru.prostor.myalbum;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.prostor.myalbum.UI.list.TrackListAdapter;
import ru.prostor.myalbum.dagger.component.DaggerAppComponent;
import ru.prostor.myalbum.http.dto.ResponseItunesApi;
import ru.prostor.myalbum.http.entity.Album;
import ru.prostor.myalbum.http.entity.Track;
import ru.prostor.myalbum.http.itunesApi.ItunesApi;

import static ru.prostor.myalbum.http.itunesApi.Entity.SONG;

public class DetailAlbum extends AppCompatActivity {

    private ImageView mDetailCover;
    private TextView mTitleAlbum;
    private TextView mNameArtist;
    private TextView mGenre;
    private TextView mPrice;
    private TextView mDate;
    private ListView mListTrack;
    private TrackListAdapter mTrackListAdapter;
    private String mGenreText;

    @Inject
    ItunesApi itunesApi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_album);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Кнопка назад по стеку
        ImageButton back = findViewById(R.id.back);
        back.setVisibility(View.VISIBLE);
        back.setOnClickListener(v -> onBackPressed());

        DaggerAppComponent.create().injectDetail(this);

        mGenreText = getResources().getString(R.string.genre);

        mDetailCover = findViewById(R.id.detail_cover);
        mTitleAlbum = findViewById(R.id.title_album);
        mNameArtist = findViewById(R.id.name_artist);
        mGenre = findViewById(R.id.genre);
        mPrice = findViewById(R.id.price);
        mDate = findViewById(R.id.date);
        mListTrack = findViewById(R.id.track_list);

        mTrackListAdapter = new TrackListAdapter(getApplicationContext());
        mListTrack.setAdapter(mTrackListAdapter);

        Intent intent = getIntent();

        if(intent != null){
            Album album = (Album) intent.getSerializableExtra("album");
            showDetailAlbum(album);
        }

    }

    /**
     * Детальное описание альбома
     * @param album - альбом
     */
    private void showDetailAlbum(Album album){
        showCover(album.getCover());
        mTitleAlbum.setText(album.getCollectionName());
        mNameArtist.setText(album.getArtistName());
        mGenre.setText(String.format("%s : %s", mGenreText, album.getPrimaryGenreName()));
        mPrice.setText(price(album.getCollectionPrice(), album.getCurrency()));
        mDate.setText(album.getReleaseDate().split("T")[0]);
        losdTrackList(album.getCollectionId());
    }

    /**
     * Показываем детальную картинку
     * @param img - ссылка на картинку
     */
    private void showCover(String img){
        Picasso.with(getApplicationContext())
                .load(img)
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(mDetailCover);
    }

    /**
     * Собираем строку цены
     * @param price - сумма
     * @param currency - валюта
     * @return - строка цены
     */
    private StringBuilder price(Double price, String currency){
        StringBuilder strPrice = new StringBuilder();
        strPrice.append(price.intValue());

        switch (currency) {
            case "RUB":
                strPrice.append(" руб.");
                break;
            case "USD":
                strPrice.append("$");
        }

        return strPrice;
    }

    /**
     * Загружаем треки
     * @param id - айди трека
     */
    private void losdTrackList(final long id){
        itunesApi.getTracks(id, SONG.getType()).enqueue(new Callback<ResponseItunesApi<Track>>() {
            @Override
            public void onResponse(@NonNull Call<ResponseItunesApi<Track>> call,@NonNull Response<ResponseItunesApi<Track>> response) {
                if(response.isSuccessful()){
                    if (response.body() != null && response.body().getResultCount() > 0){
                        List<Track> list = new ArrayList<>(response.body().getResults());
                        list.remove(0);

                        mTrackListAdapter.setTracks(list);
                        mTrackListAdapter.notifyDataSetChanged();
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.no_tracks_found, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), R.string.error_search, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseItunesApi<Track>> call,@NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), R.string.connection_error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
