package com.djzhao.smarttool.gson.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by djzhao on 18/04/19.
 *
 *"basic":{
 *   "city":"苏州",
 *   "id":"CN101190401",
 *   "update":{
 *      "loc":"2016-08-08 21:58"
 *   }
 *}
 */

public class Basic {

    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update {

        @SerializedName("loc")
        public String updateTime;

    }

}
