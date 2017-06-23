package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;

import java.util.List;

/**
 * Created by mac on 6/23/17.
 */

public class ChuDeDsAdapter extends RecyclerView.Adapter<ChuDeDsAdapter.ViewHolder> {
    Context context;
    List<ChuDe> cates;
    float mRatio = 0.5f;

    int index = -1;

    public ChuDeDsAdapter(Context context, List<ChuDe> cates) {
        this.context = context;
        this.cates    = cates;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_cate_ds, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cates.get(position).isCheckedCate()) {
            holder.txtApp.setTextSize(15);
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorWhite));
        } else {
            holder.txtApp.setTextSize(10);
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

        public ViewHolder(View itemView) {
            super(itemView);
            final DatabaseHelper mDadabaseHelper;
            mDadabaseHelper=new DatabaseHelper(context);
            txtApp = (TextView) itemView.findViewById(R.id.textcate_ds);
            layoutCateDs = (RelativeLayout) itemView.findViewById(R.id.layout_cate_ds);
            layoutCateDs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveCheck();
                    ChuDe cate = cates.get(getAdapterPosition());
                    cate.setCheckedCate(true);
                    notifyDataSetChanged();
                }
            });
//            imgApp = (ImageView) itemView.findViewById(R.id.image_category);
//            txtApp = (TextView)  itemView.findViewById(R.id.txt_catego);
//            layoutCate1 = (RelativeLayout) itemView.findViewById(R.id.layout_category1);
//            layoutCate = (RelativeLayout) itemView.findViewById(R.id.layout_category);
//            layoutCate.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    RemoveCheck();
//                    ChuDe cate = cates.get(getAdapterPosition());
//                    cate.setCheckedCate(true);
//                    notifyDataSetChanged();
//                    TrangChu.demdsApp=0;
//                    listApps.clear();
//                    List<UngDung> checkedList = new ArrayList<>();
//                    checkedList = mDadabaseHelper.getListUngDung(cate);
//                    List<UngDung> tmpList = new ArrayList<>();
//                    //   listApps.add(mDatabaseHelper.getListUngDung(cates.get(0)));
//                    for (int j = 1; j <= checkedList.size(); j++) {
//                        UngDung ungDung = new UngDung();
//                        ungDung.setNameApp(checkedList.get(j - 1).getNameApp());
//                        ungDung.setIcon(checkedList.get(j - 1).getIcon());
//                        ungDung.setId(checkedList.get(j - 1).getId());
//                        tmpList.add(ungDung);
//                        if (j % 7 == 0 || j == checkedList.size()) {
//                            listApps.add(tmpList);
//                            tmpList = new ArrayList<>();
//                            tmpList.clear();
//                        }
//                    }
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
        for (ChuDe cate : cates ) {
            if (cate.isCheckedCate()) {
                cate.setCheckedCate(false);
                break;
            }
        }
    }
}

