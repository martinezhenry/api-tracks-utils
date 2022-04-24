package com.hvs.api.tracks.utils.components.utils;

public class CommonUtils {


    public static void concatIf(StringBuilder data, String value, boolean validation) {
        if (validation) {
            data.append(value);
        }
    }


}
