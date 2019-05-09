package com.kweisa.uninstaller;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.Date;

public class BindingUtils {
    @BindingAdapter("item")
    public static void bindItem(RecyclerView recyclerView, ObservableArrayList<AppData> appDataObservableArrayList) {
        AppDataAdapter appDataAdapter = (AppDataAdapter) recyclerView.getAdapter();
        if (appDataAdapter != null) {
            appDataAdapter.setAppDataList(appDataObservableArrayList);
        }
    }

    public static String format(long lastTimeUsed) {
        if (lastTimeUsed == 0)
            return " never";
        return " " + DateFormat.getDateTimeInstance().format(new Date(lastTimeUsed));
    }
}
