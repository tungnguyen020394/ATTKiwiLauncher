package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.R;

import java.util.List;

/**
 * Created by mac on 5/23/17.
 */

public class ChuDeAdapter extends RecyclerView.Adapter<ChuDeAdapter.ViewHolder> {
    Context context;
    List<ChuDe> cates;

    int index = -1;

    public ChuDeAdapter(Context context, List<ChuDe> cates) {
        this.context = context;
        this.cates    = cates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cates.get(position).isCheckedCate()) {
            holder.layoutCate.setBackground(context.getResources().getDrawable(R.drawable.border_cate));
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorcatenew));
            holder.imgApp.setColorFilter(context.getResources().getColor(R.color.colorcatenew));
        } else {
            holder.layoutCate.setBackgroundColor(context.getResources().getColor(R.color.none));
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorcatenone));
            holder.imgApp.setColorFilter(context.getResources().getColor(R.color.colorcatenone));
        }
        holder.imgApp.setImageResource(cates.get(position).getDrawCate());
        holder.txtApp.setText(cates.get(position).getNameCate());
    }

    @Override
    public int getItemCount() {
        return cates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgApp;
        TextView txtApp;
        LinearLayout layoutCate;

        public ViewHolder(View itemView) {
            super(itemView);
            imgApp = (ImageView) itemView.findViewById(R.id.image_category);
            txtApp = (TextView)  itemView.findViewById(R.id.txt_catego);
            layoutCate = (LinearLayout) itemView.findViewById(R.id.layout_category);
            layoutCate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveCheck();
                    ChuDe cate = cates.get(getAdapterPosition());
                    cate.setCheckedCate(true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void RemoveCheck() {
        for (ChuDe cate : cates ) {
            if (cate.isCheckedCate()) {
                cate.setCheckedCate(false);
                break;
            }
        }
    }
}
