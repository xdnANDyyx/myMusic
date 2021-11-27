package com.example.myweatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipsViewHolder> {
  private Context mContent;
  private List<OtherTips> otherTipsBean;

    public TipsAdapter(Context mContent, List<OtherTips> otherTipsBean) {
        this.mContent = mContent;
        this.otherTipsBean = otherTipsBean;
    }

    @NonNull
    @Override
    public TipsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContent).inflate(R.layout.tips_item, parent, false);

        return new TipsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TipsViewHolder holder, int position) {
        OtherTips otherTipsw = otherTipsBean.get(position);
        holder.tvTitle.setText(otherTipsw.getTitle());
        holder.tvLevel.setText(otherTipsw.getLevel());
        holder.tvDesc.setText(otherTipsw.getDesc());

    }

    @Override
    public int getItemCount() {
        return (otherTipsBean == null)?0:otherTipsBean.size();
    }

    class TipsViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvDesc,tvLevel;
        public TipsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDesc = itemView.findViewById(R.id.tv_descd);
            tvLevel = itemView.findViewById(R.id.tv_level);
        }
    }
}
