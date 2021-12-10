package com.example.budgetas;

import java.text.NumberFormat;

public class Kind {
    private int id;
    private String category;
    private int sum;

    public Kind(int id, String category, int sum){
        this.id = id;
        this.category = category;
        this.sum = sum;
    }

    public int getId(){
        return this.id;
    }
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
