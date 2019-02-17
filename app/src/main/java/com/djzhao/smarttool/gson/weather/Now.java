package com.djzhao.smarttool.gson.weather;

import com.google.gson.annotations.SerializedName;

/**
 * Created by djzhao on 18/04/19.
 *
 * "now":{
 *    "tmp":"29",
 *    "cond":{
 *        "txt":"阵雨"
 *    }
 * }
 */

public class Now {

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("cond")
    public More more;

    public class More {

        @SerializedName("txt")
        public String info;

    }
}
