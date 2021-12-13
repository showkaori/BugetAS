package com.example.budgetas;

import java.text.NumberFormat;

public class Kind {
    private int id;         //管理番号
    private String category;    //カテゴリー
    private int sum;        //その月のカテゴリ別総出費

    public Kind(int id, String category, int sum){
        this.id = id;
        this.category = category;
        this.sum = sum;
    }

    public int getId(){
        return this.id;
    }
    //シングルコーテーションの除去
    public String getCategory(){
        String result = this.category.replace("'", "");
        return result;
    }

    public String getStrMoney(){
        NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
        String strMoney = nfCur.format(this.sum);
        return strMoney;
    }
}
