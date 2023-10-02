/*
 * Declaring the package
 */
package com.example.weather_app;
/*
 * Importing all the required Android Libraries
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    /*
     * Declaring private class variables
     */
    private String name;
    private Context context;
    private ArrayList<WeatherModal> WeatherModalArrayList;
    /*
    * A constructor for the WeatherAdapter class
     */
    public WeatherAdapter(Context context, ArrayList<WeatherModal> weatherModalArrayList) {
        this.context = context;
        WeatherModalArrayList = weatherModalArrayList;
    }
    /*
    * This method is used to create a new instance of the ViewHolder by inflating the
    * weather_item layout
    */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item, parent,
                false);
        return new ViewHolder(view);
    }
    /*
    * Responsible for binding the data from the WeatherModal object to the views
    * inside the ViewHolder object at the specified position
    */
    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        WeatherModal modal = WeatherModalArrayList.get(position);
        holder.temp.setText(modal.getTemp() + "°C");
        Picasso.get().load("https:".concat(modal.getIcon())).into(holder.condition);
        holder.windSpeed.setText(modal.getWindSpeed() + "Mph");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        /*
        * Responsible for formatting the date and time string from the API response to be
        * displayed in a readable format in the app
        */
        try{
            Date date = input.parse(modal.getTime());
            holder.time.setText(output.format(date));
        }
        catch (ParseException e){
            e.printStackTrace();
        }
    }
    /*
    * Gets the length of the WeatherModalArrayList
    */
    @Override
    public int getItemCount() {
        return WeatherModalArrayList.size();
    }
    /*
    * Initialises these views in the constructor using the findViewById method
    * to get references to the corresponding layout elements
    */
    public class ViewHolder  extends RecyclerView.ViewHolder {
        private TextView windSpeed, temp, time;
        private ImageView condition;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            windSpeed = itemView.findViewById(R.id.idWindSpeed);
            temp = itemView.findViewById(R.id.idTemp);
            time = itemView.findViewById(R.id.idTime);
            condition = itemView.findViewById(R.id.idCondition);
            }
        }
}