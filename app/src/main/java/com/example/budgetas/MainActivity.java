package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;

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

                //その日にデータがある場合はリストビューに表示
                List<Expenditure> dataList = new ArrayList<>();
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

                if(cursor.getCount() != 0 ){
                    for(int i = 0; i < cursor.getCount(); i++){
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
                        cursor.moveToNext();
                    }
                    cursor.close();

                    //結果をListViewに表示させるところから
                }


            }

        });
    }

}