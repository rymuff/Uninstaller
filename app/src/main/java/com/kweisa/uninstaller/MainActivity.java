package com.kweisa.uninstaller;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;

import com.kweisa.uninstaller.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    AppDataAdapter appDataAdapter;
    ObservableArrayList<AppData> appDataList;
    Calendar calendar;
    private Stack<String> uninstallStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        appDataList = new ObservableArrayList<>();
        appDataAdapter = new AppDataAdapter();
        appDataAdapter.setAppDataList(appDataList);

        binding.recyclerView.setAdapter(appDataAdapter);
        binding.setAppDataList(appDataList);

        calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        update();
//        checkAll();
    }

    @Override
    protected void onPostResume() {
        update();
        if (!uninstallStack.isEmpty())
            uninstall(uninstallStack.pop());
        super.onPostResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            if (item.isChecked()) {
                uncheckAll();
                item.setChecked(false);
                item.setIcon(getDrawable(R.drawable.ic_check_box_outline));
            } else {
                checkAll();
                item.setChecked(true);
                item.setIcon(getDrawable(R.drawable.ic_check_box));
            }
            return true;
        } else if (id == R.id.action_uninstall) {
            for (AppData appData : appDataList) {
                if (appData.isChecked()) {
                    uninstallStack.push(appData.getPackageName());
                }
            }
            if (!uninstallStack.isEmpty())
                uninstall(uninstallStack.pop());
        }

        return super.onOptionsItemSelected(item);
    }

    private void uninstall(String packName) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE).setData(Uri.parse("package:" + packName));
        startActivity(intent);
    }

    private void checkAll() {
        for (AppData appData : appDataList) {
            if (!appData.isChecked()) {
                appData.setChecked(true);
            }
        }
        appDataAdapter.notifyDataSetChanged();
    }

    private void uncheckAll() {
        for (AppData appData : appDataList) {
            if (appData.isChecked()) {
                appData.setChecked(false);
            }
        }
        appDataAdapter.notifyDataSetChanged();
    }

    private void update() {
        appDataList.clear();

        Map<String, UsageStats> UsageStatsMap = getUsageStatsManager().queryAndAggregateUsageStats(calendar.getTimeInMillis(), System.currentTimeMillis());
        List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);

        for (PackageInfo packageInfo : packageInfoList) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !packageInfo.packageName.equals(getPackageName())) {
                String packageName = packageInfo.packageName;
                Drawable icon = applicationInfo.loadIcon(getPackageManager());
                String label = (String) applicationInfo.loadLabel(getPackageManager());

                long lastTimeUsed = 0;

                UsageStats usageStats = UsageStatsMap.get(packageName);
                if (usageStats != null) {
                    lastTimeUsed = usageStats.getLastTimeUsed();
                }

                appDataList.add(new AppData(icon, label, lastTimeUsed, packageName));
            }
        }

        appDataList.sort((o1, o2) -> Long.compare(o1.getLastTimeUsed(), o2.getLastTimeUsed()));
        appDataAdapter.notifyDataSetChanged();
    }

    UsageStatsManager getUsageStatsManager() {
        return (UsageStatsManager) getSystemService(USAGE_STATS_SERVICE);
    }
}
