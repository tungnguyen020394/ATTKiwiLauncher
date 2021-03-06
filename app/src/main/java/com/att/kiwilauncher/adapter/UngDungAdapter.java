package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.TrangChu;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.xuly.ImageEdit;

import java.util.List;

/**
 * Created by mac on 5/23/17.
 */

public class UngDungAdapter extends RecyclerView.Adapter<UngDungAdapter.ViewHolder> {
    Context context;
    List<UngDung> apps;
    PackageManager manager;

    public UngDungAdapter(Context context, List<UngDung> apps) {
        this.context = context;
        this.apps    = apps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_app, parent, false);
        itemView.setOnClickListener(TrangChu.appClick);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
       // Glide.with(context).load(apps.get(position).getIcon()).into(holder.imgApp);
        holder.imgApp.setImageBitmap(ImageEdit.decodeBase64(apps.get(position).getIcon()));
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgApp;
        RelativeLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgApp = (ImageView) itemView.findViewById(R.id.image_app);
            layout = (RelativeLayout) itemView.findViewById(R.id.relay_app);
        }
    }
}
