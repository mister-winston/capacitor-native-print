package com.mrwinston.capacitor.print;

import android.util.Log;

public class NativePrint {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
