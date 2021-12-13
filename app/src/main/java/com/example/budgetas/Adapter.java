package com.example.budgetas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
//データページカテゴリー別総出費リスト表示用
public class Adapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<Kind> categoryList;

    public Adapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDataList(ArrayList<Kind> categoryList) {
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.row_data,parent,false);//使用したいLIstViewのレイアウトセット

        //レイアウト（R.layout.row_main）の表示したいIdの箇所に何を当てはめるか
        ((TextView)convertView.findViewById(R.id.category)).setText(categoryList.get(position).getCategory());
        ((TextView)convertView.findViewById(R.id.money)).setText(categoryList.get(position).getStrMoney());

        return convertView;
    }
}
