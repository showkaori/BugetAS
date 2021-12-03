package com.example.budgetas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestOpenHelper extends SQLiteOpenHelper {

    // データーベースのバージョン
    private static final int DATABASE_VERSION = 3;

    // データーベース情報を変数に格納
    private static final String DATABASE_NAME = "BudgetAS.db";
    private static final String TABLE_NAME = "expenditure";
    private static final String _ID = "id";
    private static final String COLUMN_NAME1= "detail";
    private static final String COLUMN_NAME2 = "memo";
    private static final String COLUMN_NAME3 = "category";
    private static final String COLUMN_NAME4 = "money";
    private static final String COLUMN_NAME5 = "payment";
    private static final String COLUMN_NAME6 = "day";
    private static final String COLUMN_NAME7 = "month";

    //テーブル作成SQL文
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    _ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME1 + " TEXT," +
                    COLUMN_NAME2 + " TEXT," +
                    COLUMN_NAME3 + " TEXT," +
                    COLUMN_NAME4 + " INTEGER," +
                    COLUMN_NAME5 + " TEXT," +
                    COLUMN_NAME6 + " INTEGER," +
                    COLUMN_NAME7 + " TEXT)";

    //テーブル削除SQL文
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    //コンストラクタ―
    TestOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //テーブル作成
        db.execSQL(
                SQL_CREATE_ENTRIES
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  //テーブル削除
        db.execSQL(
                SQL_DELETE_ENTRIES
        );
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //データの新規追加
    public void saveData
    (SQLiteDatabase db, String detail, String memo, String category, int money, String payment, int day, String month){
        ContentValues values = new ContentValues();
        values.put("detail", detail);
        values.put("memo", memo);
        values.put("category", category);
        values.put("money", money);
        values.put("payment", payment);
        values.put("day", day);
        values.put("month", month);

        db.insert("expenditure", null, values);
    }
}