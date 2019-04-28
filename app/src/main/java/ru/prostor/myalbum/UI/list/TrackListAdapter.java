package ru.prostor.myalbum.UI.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import ru.prostor.myalbum.R;
import ru.prostor.myalbum.http.entity.Track;

public class TrackListAdapter extends BaseAdapter {

    private List<Track> mTracks;
    private LayoutInflater mInflater;
    private final String mDuration;

    public TrackListAdapter(Context context) {
        mDuration = context.getResources().getString(R.string.duration);
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = mInflater.inflate(R.layout.track, parent, false);
        }

        TextView titleTrack = view.findViewById(R.id.title_track);
        TextView trackInfo = view.findViewById(R.id.track_info);

        Track track = mTracks.get(position);

        titleTrack.setText(track.getTrackName());
        trackInfo.setText(String.format("%s", convertInMinutAndSecond(track.getTrackTimeMillis())));

        return view;
    }

    public void setTracks(List<Track> tracks) {
        mTracks = tracks;
    }

    @Override
    public int getCount() {
        if(mTracks != null){
            return mTracks.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mTracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Конвектируем из миллисекунд в секунды и минуты
     * @param timeMillis - время трека в миллисекундах
     * @return - строка из минут и секунд трека
     */
    private String convertInMinutAndSecond(int timeMillis){
        int minut = timeMillis / 1000 / 60;
        int second = timeMillis / 1000 - minut * 60;

        String resultSecond = second < 10 ? "0" + second : Integer.toString(second);

        return mDuration + ": " + minut + ":" + resultSecond;
    }
}
