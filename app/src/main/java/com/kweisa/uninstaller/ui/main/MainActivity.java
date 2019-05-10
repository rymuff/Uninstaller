package com.kweisa.uninstaller.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.kweisa.uninstaller.MyReceiver;
import com.kweisa.uninstaller.R;
import com.kweisa.uninstaller.data.App;
import com.kweisa.uninstaller.data.AppItem;
import com.kweisa.uninstaller.databinding.ActivityMainBinding;
import com.kweisa.uninstaller.utilities.FormatUtils;
import com.kweisa.uninstaller.utilities.InjectorUtils;
import com.xwray.groupie.GroupAdapter;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private MyReceiver myReceiver = new MyReceiver();

    private GroupAdapter groupAdapter;
    private MainViewModel mainViewModel;

    private Stack<String> uninstallStack = new Stack<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        MainViewModelFactory mainViewModelFactory = InjectorUtils.INSTANCE.provideMainViewModelFactory();
        mainViewModel = new ViewModelProvider(this, mainViewModelFactory).get(MainViewModel.class);

        groupAdapter = new GroupAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(groupAdapter);

        groupAdapter.setOnItemClickListener((item, view) -> {
            AppItem appItem = (AppItem) item;
            appItem.getAppData().setChecked(!appItem.getAppData().isChecked());
            groupAdapter.notifyItemChanged(groupAdapter.getAdapterPosition(item));
        });

        initializeUi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(myReceiver, intentFilter);
    }

    // TODO: unregister receiver

    private void initializeUi() {
        mainViewModel.update(this);
        mainViewModel.getApps().observe(this, apps -> {
            groupAdapter.clear();
            groupAdapter.addAll(FormatUtils.INSTANCE.toAppItems(apps));
        });
    }

    @Override
    protected void onPostResume() {
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
        Log.d(TAG, "onOptionsItemSelected: " + id);
        if (id == R.id.action_select_all) {
            mainViewModel.setCheckedAll(true);
            return true;
        } else if (id == R.id.action_uninstall) {
            for (App app : mainViewModel.getCheckedAll()) {
                uninstallStack.push(app.getPackageName());
            }

            if (!uninstallStack.isEmpty())
                uninstall(uninstallStack.pop());
        }

        return super.onOptionsItemSelected(item);
    }

    private void uninstall(String packName) {
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, Uri.parse("package:" + packName));
        startActivity(intent);
    }
}
