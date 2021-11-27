package com.example.mymusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.LocalMusicViewHolder> {
    Context context;
    List<LocalMusicBean> mData;
    OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner{
        public void OnItemClick(View view, int position);
    }

    public LocalMusicAdapter(Context context, List<LocalMusicBean> mData) {
        this.context = context;
        this.mData = mData;
    }

    @NonNull
    @Override
    public LocalMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lm,parent,false);
        LocalMusicViewHolder holder = new LocalMusicViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocalMusicViewHolder holder, final int position) {
        LocalMusicBean localMusicBean = mData.get(position);
        holder.id.setText(localMusicBean.getId());
        holder.song.setText(localMusicBean.getSong());
        holder.singer.setText(localMusicBean.getSinger());
        holder.album.setText(localMusicBean.getAlbum());
        holder.time.setText(localMusicBean.getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListner.OnItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mData == null)?0:mData.size();
    }

    class LocalMusicViewHolder extends RecyclerView.ViewHolder{
        TextView id,song,singer,album,time;


        public LocalMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.num);
            song = itemView.findViewById(R.id.song_name);
            singer = itemView.findViewById(R.id.singer);
            album = itemView.findViewById(R.id.album);
            time = itemView.findViewById(R.id.time);
        }
    }
}
