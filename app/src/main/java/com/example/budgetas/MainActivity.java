package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    TextView day = (TextView) findViewById(R.id.dayDisp);
    TextView sum = (TextView) findViewById(R.id.sumDisp);

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
                if((mMonth+1) < 10){    //一桁の場合０を付ける
                    mon = "0" + (mMonth+1);
                }else{
                    mon = String.valueOf(mMonth + 1);
                }
                int mDay = dayOfMonth;  //日にちの取得
                String d = "";
                if((mDay) < 10){    //一桁の場合０を付ける
                    d = "0" + (mDay);
                }else{
                    d = String.valueOf(mDay);
                }
                //カレンダーで選択している日付の取得
                String selectedDate = new StringBuilder().append(mYear).append(mon).append(d).toString(); //yyyyMMddの形に

                day.setText(mDay);  //選択中の日付表示

            }
        });
    }

}