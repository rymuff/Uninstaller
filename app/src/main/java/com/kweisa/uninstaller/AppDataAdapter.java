package com.kweisa.uninstaller;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.kweisa.uninstaller.databinding.LayoutItemBinding;

public class AppDataAdapter extends RecyclerView.Adapter<AppDataAdapter.ViewHolder> {
    private ObservableArrayList<AppData> appDataList;

    void setAppDataList(ObservableArrayList<AppData> appDataList) {
        this.appDataList = appDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemBinding binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppData appData = appDataList.get(position);
        holder.bind(appData);

        holder.itemView.setOnClickListener(v -> holder.binding.checkBox.setChecked(!holder.binding.checkBox.isChecked()));
    }

    @Override
    public int getItemCount() {
        return appDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LayoutItemBinding binding;

        ViewHolder(LayoutItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(AppData appData) {
            binding.setAppData(appData);
        }
    }
}
