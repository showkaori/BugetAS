package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;


public class DataActivity extends AppCompatActivity {

    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        //メイン画面からの値の受け取り
        Intent intent = new Intent();
        String getMonth = getIntent().getExtras().getString("M");

        //データを取得して表示する
        create(getMonth);
    }


    public void create(String getMonth){
        System.out.println("前から受け取った値：" + getMonth);
        String yyyy = getMonth.substring(0, 4);
        String mm = getMonth.substring(4);
        String dispMonth = new StringBuilder().append(yyyy).append("年 ").append(mm).append("月").toString();
        //月の表示
        TextView Month = findViewById(R.id.month);
        Month.setText(dispMonth);

        //総出費の表示
        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        //SQLの実行
        Cursor cursor = db.query(
                "expenditure",
                new String[] { "sum(money)"},
                "month =" + getMonth,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();
        int totalNum = cursor.getInt(0);
        cursor.close();
        TextView total = findViewById(R.id.allSum);
        NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
        total.setText(nfCur.format(totalNum));

        //各カテゴリー別の出費
        ArrayList<Kind> sum = new ArrayList<>();
        if(helper == null){
            helper = new TestOpenHelper(getApplicationContext());
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }

        String[] categoryList = {"'食費'", "'外食'", "'日用品'", "'嗜好品'", "'服・美容'", "'交通費'", "'教養'", "'家族'", "'医療'", "'通信費'", "'光熱費'", "'住まい'", "'税金'", "'保険'", "'その他'"};
        //SQLの実行
        for(int i = 0; i < categoryList.length ; i++)
        {
            //データ抽出用SQL
            String category = categoryList[i];
            Cursor cursor2 = db.query(
                    "expenditure",
                    new String[] { "nullif(sum(money), 0)" },
                    "month = " + getMonth + " and" + " category = " + category,
                    null,
                    null,
                    null,
                    null
            );
            cursor2.moveToFirst();
            int id = i;
            String cate = categoryList[i];
            String strMoney = String.valueOf(cursor2.getInt(0));
            int cateSum = 0;
            if(strMoney != null){
                cateSum = cursor2.getInt(0);
            }
            Kind a = new Kind(id,cate,cateSum);
            sum.add(a);
            cursor2.close();
        }

        ListView listView = (ListView)findViewById(R.id.categoryList); //LIstViewインスタンス化
        Adapter Adapter = new Adapter(DataActivity.this);//自作のアダプター
        Adapter.setDataList(sum);    //表示したいリストをセット
        listView.setAdapter(Adapter);     //リストビューに表示

        //前の月へ
        TextView prev = (TextView)findViewById(R.id.previous);
        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int YYYY = Integer.parseInt(yyyy);
                int MM = Integer.parseInt(mm);
                if(MM == 1){
                    MM = 12;
                    YYYY = YYYY - 1;
                }else{
                    MM = MM - 1;
                }
                String yyyy1 = String.valueOf(YYYY);
                String mm1;
                if(MM < 10){
                    mm1 = "0" + MM;
                }else{
                    mm1 = String.valueOf(MM);
                }
                String month = yyyy1 + mm1;
                create(month);
            }
        });

        //次の月へ
        TextView next = (TextView)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int YYYY = Integer.parseInt(yyyy);
                int MM = Integer.parseInt(mm);
                if(MM == 12){
                    MM = 1;
                    YYYY = YYYY + 1;
                }else{
                    MM = MM + 1;
                }
                String yyyy1 = String.valueOf(YYYY);
                String mm1;
                if(MM < 10){
                    mm1 = "0" + MM;
                }else{
                    mm1 = String.valueOf(MM);
                }
                String month = yyyy1 + mm1;
                create(month);
            }
        });

    }
}