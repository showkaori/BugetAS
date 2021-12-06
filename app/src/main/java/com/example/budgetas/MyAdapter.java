package com.example.budgetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Expenditure> dataList;

    public MyAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataList(ArrayList<Expenditure> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.row_main,parent,false);//使用したいLIstViewのレイアウトセット

        //レイアウト（R.layout.row_main）の表示したいIdの箇所に何を当てはめるか
        ((TextView)convertView.findViewById(R.id.category)).setText(dataList.get(position).getCategory());
        ((TextView)convertView.findViewById(R.id.money)).setText(dataList.get(position).getStrMoney());
        ((TextView)convertView.findViewById(R.id.detail)).setText(dataList.get(position).getDetail());

        return convertView;
    }
}
