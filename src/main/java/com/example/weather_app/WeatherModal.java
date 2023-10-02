/*
 * Declaring the package
 */
package com.example.weather_app;
/*
 * Importing all the required Android Libraries
 */
public class WeatherModal{
    /*
     * Declaring private class variables
     */
    private String time;
    private String temp;
    private String icon;
    private String windSpeed;
    /*
    * A constructor for the WeatherModal class
    */
    public WeatherModal(String time, String temp, String icon, String windSpeed) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this. windSpeed = windSpeed;
    }
    /*
    * Getter and setter methods for the time, temp, icon, and windSpeed
    */
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}
