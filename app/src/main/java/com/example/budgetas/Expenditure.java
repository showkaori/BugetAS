package com.example.budgetas;

import java.text.NumberFormat;

public class Expenditure {
    private int Id;             //id
    private String Detail;      //タイトル
    private String Memo;        //メモ
    private String Category;    //カテゴリー
    private int Money;          //金額
    private String Payment;     //支払方法
    private int Day;            //支払日yyyyMMdd
    private String Month;       //支払月yyyyMM

    //コンストラクタ
    public Expenditure(){}
    public Expenditure(int id, String detail, String memo, String category, int money, String payment, int day, String month){
        this.Id = id;
        this.Detail = detail;
        this.Memo = memo;
        this.Category = category;
        this.Money = money;
        this.Payment = payment;
        this.Day = day;
        this.Month = month;
    }

    // ゲッター
    public int getId(){ return this.Id; }
    public String getDetail(){ return this.Detail; }
    public String getMemo(){ return this.Memo; }
    public String getCategory(){ return this.Category; }
    public int getMoney(){ return this.Money;}
    public String getPayment(){ return this.Payment; }
    public int getDay(){ return this.Day; }
    public String getMonth(){ return this.Month; }

    public String getStrMoney(){
        NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
        String strMoney = nfCur.format(this.Money);
    return strMoney;
    }
}
