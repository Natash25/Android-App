package com.example.android.sunshine.utilities;

import android.content.Context;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sunshine.R;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>{
    private String[] mWeatherData;
    private final ForecastAdapterOnClickHandler mClickHandler;
    // (3) Create a final private ForecastAdapterOnClickHandler called mClickHandler

    // (1) Add an interface called ForecastAdapterOnClickHandler
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForToday);

        // (4) When the load is finished, show either the data or an error message if there is no data
        //void onLoadFinished(Loader<String> loader, String[] data);
    }
    // (2) Within that interface, define a void method that access a String as a parameter

    // (4) Add a ForecastAdapterOnClickHandler as a parameter to the constructor and store it in mClickHandler
    // (5) Implement OnClickListener in the ForecastAdapterViewHolder class
    public ForecastAdapter(ForecastAdapterOnClickHandler adapterOnClickHandler) {
        mClickHandler = adapterOnClickHandler;
    }


    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        ForecastAdapterViewHolder adapterViewHolder = new ForecastAdapterViewHolder(view);
        return adapterViewHolder;
    }

    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder holder, int position) {
        String weatherDataToday = mWeatherData[position];
        holder.mWeatherTextView.setText(weatherDataToday);
    }

    @Override
    public int getItemCount() {
        if (mWeatherData == null)
            return 0;
        else {
            return mWeatherData.length;
        }

    }

    public void setWeatherData(String[] weatherData) {
        mWeatherData = weatherData;
        notifyDataSetChanged();
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mWeatherTextView;

        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
            mWeatherTextView = (TextView) itemView.findViewById(R.id.tv_weather_data);
            // (7) Call setOnClickListener on the view passed into the constructor (use 'this' as the OnClickListener)
            itemView.setOnClickListener(this);
        }

        // (6) Override onClick, passing the clicked day's data to mClickHandler via its onClick method
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            String weatherToday = mWeatherData[adapterPosition];
            mClickHandler.onClick(weatherToday);
        }

    }
}
