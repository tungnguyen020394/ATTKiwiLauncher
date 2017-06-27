package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.model.ChuDe;

import java.util.List;

/**
 * Created by mac on 6/23/17.
 */

public class ChuDeDsAdapter extends RecyclerView.Adapter<ChuDeDsAdapter.ViewHolder> {
    Context context;
    List<ChuDe> cates;
    UngDungDsAdapter ungDungAdapter;
    List<UngDung> dsUngDung;
    float mRatio = 0.5f;

    int index = -1;

    public ChuDeDsAdapter(Context context, List<ChuDe> cates, UngDungDsAdapter ungDungAdapter, List<UngDung> dsUngDung) {
        this.context = context;
        this.cates = cates;
        this.ungDungAdapter = ungDungAdapter;
        this.dsUngDung = dsUngDung;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_cate_ds, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (cates.get(position).isCheckedCate()) {
            holder.txtApp.setTextSize(holder.size1);
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.txtApp.setTextSize(holder.size);
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colormiplus));
        }
        holder.txtApp.setText(cates.get(position).getNameCate());

    }

    @Override
    public int getItemCount() {
        return cates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtApp;
        RelativeLayout layoutCateDs;
        int size;
        int size1;

        public ViewHolder(View itemView) {
            super(itemView);
<<<<<<< HEAD
            final int pos =getAdapterPosition();
            final DatabaseHelper mDadabaseHelper;
            mDadabaseHelper = new DatabaseHelper(context);
            size = context.getResources().getDimensionPixelSize(R.dimen._5sdp);
            size1 = context.getResources().getDimensionPixelSize(R.dimen._7sdp);
=======
>>>>>>> origin/master
            txtApp = (TextView) itemView.findViewById(R.id.textcate_ds);
            layoutCateDs = (RelativeLayout) itemView.findViewById(R.id.layout_cate_ds);

//                    //Toast.makeText(this, listApps.size() + "s" + listApps.get(0).size(), Toast.LENGTH_SHORT).show();
//                    TrangChu.listAppBottom.clear();
//                    if (listApps.size()>0){
//                        TrangChu.listAppBottom.addAll(listApps.get(TrangChu.demdsApp));
//                    }else{
//                        TrangChu.listAppBottom.clear();
//                    }
//                    TrangChu.listapp.notifyDataSetChanged();
//
//                }
//            });
//
//            layoutCate1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    layoutCate.callOnClick();
//                }
//            });
        }
    }

    private void RemoveCheck() {
        for (ChuDe cate : cates) {
            if (cate.isCheckedCate()) {
                cate.setCheckedCate(false);
                break;
            }
        }
    }
}

