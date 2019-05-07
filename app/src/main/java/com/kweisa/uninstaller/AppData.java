package com.kweisa.uninstaller;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;

public class AppData {
    private Drawable icon;
    private String label;
    private long lastTimeUsed;
    private String packageName;
    private ObservableBoolean isChecked = new ObservableBoolean();

    public AppData(Drawable icon, String label, long lastTimeUsed, String packageName) {
        this.icon = icon;
        this.label = label;
        this.lastTimeUsed = lastTimeUsed;
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getLabel() {
        return label;
    }

    public long getLastTimeUsed() {
        return lastTimeUsed;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isChecked() {
        return isChecked.get();
    }

    public void setChecked(boolean checked) {
        isChecked.set(checked);
    }

    @NonNull
    @Override
    public String toString() {
        return "{String label = " + label + ", long lastTimeUsed = " + lastTimeUsed + ", String packageName = " + packageName + ", boolean isChecked = " + isChecked.get() + "}";
    }
}
