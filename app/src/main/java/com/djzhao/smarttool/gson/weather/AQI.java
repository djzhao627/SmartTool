package com.djzhao.smarttool.gson.weather;

/**
 * Created by djzhao on 18/04/19.
 *
 * "aqi":{
 *   "city":{
 *       "aqi":"44",
 *       "pm25":"13"
 *   }
 * }
 *
 */

public class AQI {

    public AQICity city;

    public class AQICity {

        public String aqi;

        public String pm25;

    }

}
