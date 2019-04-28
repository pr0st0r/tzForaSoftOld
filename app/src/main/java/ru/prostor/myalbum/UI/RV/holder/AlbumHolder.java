package ru.prostor.myalbum.UI.RV.holder;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import com.squareup.picasso.Picasso;

import ru.prostor.myalbum.DetailAlbum;
import ru.prostor.myalbum.R;
import ru.prostor.myalbum.http.entity.Album;

public class AlbumHolder extends RecyclerView.ViewHolder{

    private ImageButton mCover;
    private Context mContext;
    private Album mAlbum;

    public AlbumHolder(@NonNull View itemView) {
        super(itemView);

        mCover = itemView.findViewById(R.id.album_cover);
        mContext = itemView.getContext();

        //Переходим в детальное описание альбома
        mCover.setOnClickListener(v -> {
            Bundle bundle = null;

            //Анимация перехода
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, mCover, mContext.getString(R.string.cover));
                bundle = options.toBundle();
            }

            Intent intent = new Intent(mContext, DetailAlbum.class);
            intent.putExtra("album", mAlbum);

            if(bundle != null){
                mContext.startActivity(intent, bundle);
            }else{
                mContext.startActivity(intent);
            }
        });
    }

    public void bind(Album album){
        this.mAlbum = album;
        showCover();
    }

    /**
     * Загружаем обложку альбома
     */
    private void showCover(){
        Picasso.with(mContext)
                .load(mAlbum.getCover())
                .placeholder(R.drawable.no_img)
                .error(R.drawable.no_img)
                .into(mCover);
    }
}
