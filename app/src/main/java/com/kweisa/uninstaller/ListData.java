package com.kweisa.uninstaller;

import android.graphics.drawable.Drawable;

public class ListData {
    private Drawable icon;
    private String appName;
    private String packName;

    public ListData(Drawable icon, String appName, String packName) {
        this.icon = icon;
        this.appName = appName;
        this.packName = packName;

    }

    public Drawable getIcon() {
        return icon;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackName() {
        return packName;
    }

    public boolean isIconNull() {
        return icon == null;
    }
}