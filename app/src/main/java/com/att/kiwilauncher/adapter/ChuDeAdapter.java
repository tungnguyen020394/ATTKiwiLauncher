package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.att.kiwilauncher.R;
import com.att.kiwilauncher.TrangChu;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
import com.att.kiwilauncher.model.TheLoaiUngDung;
import com.att.kiwilauncher.xuly.DuLieu;
import com.att.kiwilauncher.xuly.ImageEdit;
import com.att.kiwilauncher.xuly.RequestToServer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static com.att.kiwilauncher.TrangChu.listApps;

/**
 * Created by mac on 5/23/17.
 */

public class ChuDeAdapter extends RecyclerView.Adapter<ChuDeAdapter.ViewHolder> {
    Context context;
    List<ChuDe> cates;
    List<TheLoaiUngDung> mListTheLoaiUngDung;
    List<UngDung> mListUngDung;
String s="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";
    public ChuDeAdapter(Context context, List<ChuDe> cates, List<TheLoaiUngDung> mListTheLoaiUngDung, List<UngDung> mListUngDung) {
        this.context = context;
        this.cates = cates;
        this.mListTheLoaiUngDung = mListTheLoaiUngDung;
        this.mListUngDung = mListUngDung;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.item_category, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (cates.get(position).isCheckedCate()) {
            holder.layoutCate.setBackground(context.getResources().getDrawable(R.drawable.bordercate));
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorcatenew));
            holder.imgApp.setColorFilter(context.getResources().getColor(R.color.colorcatenew));
        } else {
            holder.layoutCate.setBackgroundColor(context.getResources().getColor(R.color.none));
            holder.txtApp.setTextColor(context.getResources().getColor(R.color.colorcatenone));
            holder.imgApp.setColorFilter(context.getResources().getColor(R.color.colorcatenone));
        }
       // holder.imgApp.setImageBitmap(ImageEdit.decodeBase64(s));
        holder.imgApp.setImageBitmap(ImageEdit.decodeBase64(cates.get(position).getIconLink()));
      //  Toast.makeText(context,cates.get(position).getIconLink()+"xxx"+cates.get(position).getIconLink().length()+" size",Toast.LENGTH_SHORT).show();
        //  Glide.with(context).load(DuLieu.URL_IMAGE+"/"+cates.get(position).getIconLink()).into(holder.imgApp);
        holder.txtApp.setText(cates.get(position).getNameCate());
        holder.layoutCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveCheck();
                ChuDe cate = cates.get(position);
                cate.setCheckedCate(true);
                notifyDataSetChanged();
                TrangChu.demdsApp = 0;
                listApps.clear();
                //List<UngDung> checkedList = new ArrayList<>();
                // checkedList = mDadabaseHelper.getListUngDung(cate);
                List<UngDung> checkedList = DuLieu.getListUngDungByTheLoaiId(cate.getIndexCate() + "", mListTheLoaiUngDung, mListUngDung);
                List<UngDung> tmpList = new ArrayList<>();
                //   listApps.add(mDatabaseHelper.getListUngDung(cates.get(0)));
                for (int j = 1; j <= checkedList.size(); j++) {
                    UngDung ungDung = new UngDung();
                    ungDung.setNameApp(checkedList.get(j - 1).getNameApp());
                    ungDung.setIcon(checkedList.get(j - 1).getIcon());
                    ungDung.setId(checkedList.get(j - 1).getId());
                    tmpList.add(ungDung);
                    if (j % 7 == 0 || j == checkedList.size()) {
                        listApps.add(tmpList);
                        tmpList = new ArrayList<>();
                        tmpList.clear();
                    }
                }
                //Toast.makeText(this, listApps.size() + "s" + listApps.get(0).size(), Toast.LENGTH_SHORT).show();
                TrangChu.listAppBottom.clear();
                if (listApps.size() > 0) {
                    TrangChu.listAppBottom.addAll(listApps.get(TrangChu.demdsApp));
                } else {
                    TrangChu.listAppBottom.clear();
                }
                TrangChu.listapp.notifyDataSetChanged();

            }
        });

        holder.layoutCate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.layoutCate.callOnClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgApp;
        TextView txtApp;
        RelativeLayout layoutCate, layoutCate1;

        public ViewHolder(View itemView) {
            super(itemView);
            final DatabaseHelper mDadabaseHelper;
            mDadabaseHelper = new DatabaseHelper(context);
            imgApp = (ImageView) itemView.findViewById(R.id.image_category);
            txtApp = (TextView) itemView.findViewById(R.id.txt_catego);
            layoutCate1 = (RelativeLayout) itemView.findViewById(R.id.layout_category1);
            layoutCate = (RelativeLayout) itemView.findViewById(R.id.layout_category);
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
