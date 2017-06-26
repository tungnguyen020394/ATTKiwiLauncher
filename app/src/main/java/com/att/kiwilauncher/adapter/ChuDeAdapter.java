package com.att.kiwilauncher.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.att.kiwilauncher.R;
import com.att.kiwilauncher.TrangChu;
import com.att.kiwilauncher.UngDung;
import com.att.kiwilauncher.database.DatabaseHelper;
import com.att.kiwilauncher.model.ChuDe;
<<<<<<< HEAD
import com.att.kiwilauncher.model.TheLoaiUngDung;
import com.att.kiwilauncher.xuly.DuLieu;

=======
>>>>>>> master
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cates.get(position).isCheckedCate()) {
            holder.layoutCate.setBackground(context.getResources().getDrawable(R.drawable.bordercate));
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
        RelativeLayout layoutCate;

        public ViewHolder(View itemView) {
            super(itemView);
            final DatabaseHelper mDadabaseHelper;
            mDadabaseHelper = new DatabaseHelper(context);
            imgApp = (ImageView) itemView.findViewById(R.id.image_category);
            txtApp = (TextView) itemView.findViewById(R.id.txt_catego);
            layoutCate = (RelativeLayout) itemView.findViewById(R.id.layout_category);
            layoutCate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RemoveCheck();
                    ChuDe cate = cates.get(getAdapterPosition());
                    cate.setCheckedCate(true);
                    notifyDataSetChanged();
                    TrangChu.demdsApp = 0;
                    listApps.clear();
                    //  checkedList = mDadabaseHelper.getListUngDung(cate);
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
