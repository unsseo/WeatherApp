package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.google.api.services.youtube.model.SearchResult; // 이 부분은 그대로 두고

import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.ViewHolder> {
    private List<YouTubeVideo> videoList; // YouTubeVideo로 변경

    public YouTubeAdapter(List<YouTubeVideo> videoList) {
        this.videoList = videoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        YouTubeVideo video = videoList.get(position); // YouTubeVideo로 변경
        holder.textView.setText(video.getSnippet().getTitle()); // 비디오 제목 표시
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}
