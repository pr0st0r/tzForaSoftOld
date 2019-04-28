package ru.prostor.myalbum.http.itunesApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.prostor.myalbum.http.entity.Album;
import ru.prostor.myalbum.http.dto.ResponseItunesApi;
import ru.prostor.myalbum.http.entity.Track;

public interface ItunesApi {
    /**
     * Поиск альбомов по исполнителю
     * @param term - имя исполнителя
     * @param entity - сущность
     * @return ResponseItunesApi
     */
    @GET("/search")
    Call<ResponseItunesApi<Album>> getAlbums(@Query("term") String term, @Query("entity") String entity, @Query("country") String country);

    /**
     * Поиск треков
     * @param id - альбома
     * @param entity - сущность
     * @return ResponseItunesApi
     */
    @GET("/lookup")
    Call<ResponseItunesApi<Track>> getTracks(@Query("id") long id, @Query("entity") String entity);
}
