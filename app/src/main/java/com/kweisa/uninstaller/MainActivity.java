package com.kweisa.uninstaller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private ListItemAdapter listItemAdapter;
    private Stack<String> uninstallStack;

    @Override
    protected void onResume() {
        refresh();
        if (!uninstallStack.isEmpty())
            uninstall(uninstallStack.pop());
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(R.string.delete_all_title)
                        .setMessage(R.string.delete_all_message)
                        .setPositiveButton(R.string.delete_all_positive, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                for (ListData listData : listItemAdapter.getList()) {
                                    uninstallStack.push(listData.getPackName());
                                }
                                if (!uninstallStack.isEmpty())
                                    uninstall(uninstallStack.pop());
                            }
                        })
                        .setNegativeButton(R.string.delete_all_negative, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setIcon(R.mipmap.ic_launcher).show();
            }
        });

        uninstallStack = new Stack<>();
        ListView listView = findViewById(R.id.listView);
        listItemAdapter = new ListItemAdapter(this);

        listView.setAdapter(listItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                uninstall(((ListData) listItemAdapter.getItem(position)).getPackName());
            }
        });
    }

    private void uninstall(String packName) {
        Intent intent = new Intent(Intent.ACTION_DELETE).setData(Uri.parse("package:" + packName));
        startActivity(intent);
    }

    private void refresh() {
        listItemAdapter.clear();
        List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo packageInfo : packageInfoList) {
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !packageInfo.packageName.equals(getPackageName())) {
                listItemAdapter.addItem(applicationInfo.loadIcon(getPackageManager()), applicationInfo.loadLabel(getPackageManager()), packageInfo.packageName);
            }
        }
        listItemAdapter.notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            refresh();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
