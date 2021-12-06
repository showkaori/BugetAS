package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;
    ArrayList<Expenditure> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCalendar();

        Button addButton = findViewById(R.id.addButton);
        //追加ボタン押下時
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //追加ページに画面遷移
                Intent intent = new Intent(getApplication(), AddActivity.class);
                startActivity(intent);
            }
        });
    }

    //日付選択時のイベント
    private void initCalendar(){
        calendarView = (CalendarView)findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                int mYear = year;   //年の取得
                int mMonth = month; //（月ー1）の取得
                String mon = "";
                if ((mMonth + 1) < 10) {    //一桁の場合０を付ける
                    mon = "0" + (mMonth + 1);
                } else {
                    mon = String.valueOf(mMonth + 1);
                }
                int mDay = dayOfMonth;  //日にちの取得
                String d = "";
                if ((mDay) < 10) {    //一桁の場合０を付ける
                    d = "0" + (mDay);
                } else {
                    d = String.valueOf(mDay);
                }

                //カレンダーで選択している日付の取得
                String selectedDate = new StringBuilder().append(mYear).append(mon).append(d).toString(); //yyyyMMddの形に
                System.out.println(selectedDate);

                TextView d1 = findViewById(R.id.d1);
                d1.setText(String.valueOf(mDay) + "日"); //クリックしている日にちの表示

                //他の日のデータに追加ではなくその日のデータのみ表示したいのでいったんクリア
                dataList.clear ();

                //その日にデータがある場合はリストビューに表示
                if(helper == null){
                    helper = new TestOpenHelper(getApplicationContext());
                }
                if(db == null){
                    db = helper.getReadableDatabase();
                }
                //SQLの実行
                Cursor cursor = db.query(
                        "expenditure",
                        new String[] { "id", "detail","memo", "category", "money", "payment", "day", "month"},
                        "day =" + selectedDate,
                        null,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();

                ListView listView = (ListView)findViewById(R.id.mainList); //LIstViewインスタンス化
                TextView s1 = (TextView) findViewById(R.id.s1);
                int sum = 0;
                NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
                if(cursor.getCount() != 0 ){
                    for(int i = 0; i < cursor.getCount(); i++){
                        //出費データのオブジェクト作成
                        int id = cursor.getInt(0);
                        String detail = cursor.getString(1);
                        String memo = cursor.getString(2);
                        String category = cursor.getString(3);
                        int money = cursor.getInt(4);
                        String payment = cursor.getString(5);
                        int day = cursor.getInt(6);
                        String month1 = cursor.getString(7);
                        Expenditure exp = new Expenditure(id, detail, memo, category, money, payment, day,month1);
                        dataList.add(exp);

                        //出費合計
                        sum += money;
                        s1.setText(nfCur.format(sum));
                        cursor.moveToNext();
                    }
                    cursor.close();

                    //新たにクリックした日の結果をListViewに表示させる
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                    //合計金額をid=s1に表示させる
                }else{
                    //その日にデータがないとき
                    //ListViewのリセット(ひとつ前の動作で表示されているListViewの削除）
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    dataList.clear ();
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                    s1.setText(R.string.s1);//合計金額に0円表示
                }
            }

        });
    }

}