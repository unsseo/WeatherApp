package com.example.weatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List<WeatherData> weatherList;

    // ✅ 인터페이스 정의
    public interface OnItemClickListener {
        void onItemClick(WeatherData data);
    }

    private OnItemClickListener listener;

    // ✅ setter 추가
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText, tempLow, tempHigh;
        ImageView weatherIcon;

        public ViewHolder(View view) {
            super(view);
            dayText = view.findViewById(R.id.day_text);
            weatherIcon = view.findViewById(R.id.weather_icon);
            tempLow = view.findViewById(R.id.temp_low);
            tempHigh = view.findViewById(R.id.temp_high);
        }
    }

    public WeatherAdapter(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeatherData data = weatherList.get(position);
        holder.dayText.setText(data.getDay());
        holder.tempLow.setText(data.getTempLow());
        holder.tempHigh.setText(data.getTempHigh());
        holder.weatherIcon.setImageResource(data.getIconResId());

        // ✅ 클릭 이벤트 연결
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(data);
            }
        });
    }

    @Override
    public int getItemCount() {
        return weatherList.size();
    }
}
