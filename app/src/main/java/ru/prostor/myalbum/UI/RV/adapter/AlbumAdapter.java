package ru.prostor.myalbum.UI.RV.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import ru.prostor.myalbum.R;
import ru.prostor.myalbum.UI.RV.holder.AlbumHolder;
import ru.prostor.myalbum.http.entity.Album;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder>{

    private List<Album> mAlbumList;

    @NonNull
    @Override
    public AlbumHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album, viewGroup, false);
        return new AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumHolder albumHolder, int i) {
        albumHolder.bind(mAlbumList.get(i));
    }

    @Override
    public int getItemCount() {
        return mAlbumList != null ? mAlbumList.size() : 0;
    }

    public void setAlbumList(List<Album> albumList) {
        mAlbumList = albumList;
        sortedAlphabeticallyList();
    }

    /**
     * Сортировка по алфавиту
     */
    private void sortedAlphabeticallyList(){
        Collections.sort(mAlbumList, (albumNameFirst, albumNameSecond) -> albumNameFirst.getCollectionName().compareTo(albumNameSecond.getCollectionName()));
    }
}
