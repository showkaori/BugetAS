package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    private CalendarView calendarView;
    private TestOpenHelper helper;
    private SQLiteDatabase db;
    ArrayList<Expenditure> dataList = new ArrayList<>();
    private String day;
    // 画面間でやりとりする任意のコード
    private final int FORM_REQUESTCODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("MainPage onCreate");

        initCalendar();

        //今日の日付の取得
        //デフォルトのタイムゾーンおよびロケールを使用してカレンダを取得
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String mon;
        if((month+1) < 10){
            mon = "0" + (month+1);
        }else{
            mon = String.valueOf(month+1);
        }
        int today = c.get(Calendar.DAY_OF_MONTH);
        String d;
        if ((today) < 10) {    //一桁の場合０を付ける
            d = "0" + (today);
        } else {
            d = String.valueOf(today);
        }

        TextView d1 = findViewById(R.id.d1);
        d1.setText(today + "日"); //クリックしている日にちの表示
        int Disp = Integer.parseInt(new StringBuilder().append(year).append(mon).append(d).toString()); //yyyyMMddの形に

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
                "day =" + Disp,
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
                //合計金額をid=s1に表示させる
                s1.setText(nfCur.format(sum));
                cursor.moveToNext();
            }
            cursor.close();

            //新たにクリックした日の結果をListViewに表示させる
            MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
            myAdapter.setDataList(dataList);    //表示したいリストをセット
            listView.setAdapter(myAdapter);     //リストビューに表示

        }else{
            //その日にデータがないとき
            //ListViewのリセット(ひとつ前の動作で表示されているListViewの削除）
            MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
            dataList.clear ();
            myAdapter.setDataList(dataList);    //表示したいリストをセット
            listView.setAdapter(myAdapter);     //リストビューに表示

            s1.setText(R.string.s1);//合計金額に空白表示
        }

        Button addButton = findViewById(R.id.addButton);

        //追加ボタンクリック時
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day = new StringBuilder().append(year).append("/").append(mon).append("/").append(d).toString();
                System.out.println(day);
                //追加ページに画面遷移（戻る時に値を受け取る）
                // インテントの準備
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                // 追加画面に渡す値
                intent.putExtra("day",day);
                // サブ画面を表示する
                startActivityForResult(intent,FORM_REQUESTCODE);
                /*
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                System.out.println("追加したい日" + day);
                intent.putExtra("day", day);
                startActivity(intent);

                 */
            }
        });

        //リスト項目をクリック時に呼び出される
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //リスト項目クリック時の処理

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ここに処理を記述します。
                //今回は、id情報を持って画面遷移 インテントにセット
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                int intId = (int)id;
                intent.putExtra("id", intId);
                System.out.println(String.valueOf(intId));
                // サブ画面を表示する
                startActivityForResult(intent,FORM_REQUESTCODE);
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
                String selectedDay = new StringBuilder().append(mYear).append("/").append(mon).append("/").append(d).toString(); //画面遷移時に使用yyyy/MM/dd
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
                        //合計金額をid=s1に表示させる
                        s1.setText(nfCur.format(sum));
                        cursor.moveToNext();
                    }
                    cursor.close();

                    //新たにクリックした日の結果をListViewに表示させる
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                }else{
                    //その日にデータがないとき
                    //ListViewのリセット(ひとつ前の動作で表示されているListViewの削除）
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    dataList.clear ();
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                    s1.setText(R.string.s1);//合計金額に空白表示
                }

                //リスト項目をクリック時に呼び出される
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    //リスト項目クリック時の処理

                    /* 1.AdapterView<?> parent
                     * ->タップされたListView全体

                     * 2.View view
                     * ->タップされた１行分の画面部品

                     * 3.int position
                     * ->タップされた行番号　
                     * 0から始まる

                     * 4.long id
                     * ->DBのデータをもとにListViewを生成した際の主キーの値
                     * DBを使わない場合は上のpositionと同じ値*/

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ここに処理を記述します。
                        //今回は、id情報を持って画面遷移 インテントにセット
                        Intent intent = new Intent(MainActivity.this, EditActivity.class);
                        int intId = (int)id;
                        intent.putExtra("id", intId);
                        System.out.println(String.valueOf(intId));
                        startActivityForResult(intent,FORM_REQUESTCODE);
                    }

                });

                Button addButton = findViewById(R.id.addButton);
                //日付をセレクトしている状態で追加ボタン押下時
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        day = selectedDay;
                        System.out.println(day);
                        //追加ページに画面遷移
                        Intent intent = new Intent(MainActivity.this, AddActivity.class);
                        System.out.println("追加したい日" + day);
                        intent.putExtra("day", day);
                        startActivityForResult(intent,FORM_REQUESTCODE);
                    }
                });
            }

        });
    }

    public void onResume() {
        super.onResume();
        System.out.println("MainPage onResume");
    }

    //追加画面から日付を受け取りListViewを更新する
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 追加画面からの戻り
        if (requestCode == FORM_REQUESTCODE) {
            if (resultCode == RESULT_OK) {
                // サブ画面から戻り値を取得int型日付
                System.out.println("他画面からの戻り　onActivityResult");
                int numDay = data.getIntExtra("Day", 0);

                //日にちの表示
                TextView d1 = findViewById(R.id.d1);
                String mDay = String.valueOf(numDay);
                int DispDay = Integer.parseInt(mDay.substring(6));
                d1.setText(DispDay + "日");

                //他の日のデータに追加ではなくその日のデータのみ表示したいのでいったんクリア
                dataList.clear();

                //その日にデータがある場合はリストビューに表示
                if (helper == null) {
                    helper = new TestOpenHelper(getApplicationContext());
                }
                if (db == null) {
                    db = helper.getReadableDatabase();
                }
                //SQLの実行
                Cursor cursor = db.query(
                        "expenditure",
                        new String[]{"id", "detail", "memo", "category", "money", "payment", "day", "month"},
                        "day =" + numDay,
                        null,
                        null,
                        null,
                        null
                );
                cursor.moveToFirst();

                ListView listView = (ListView) findViewById(R.id.mainList); //LIstViewインスタンス化
                TextView s1 = (TextView) findViewById(R.id.s1);
                int sum = 0;
                NumberFormat nfCur = NumberFormat.getCurrencyInstance();  //通貨形式
                if (cursor.getCount() != 0) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        //出費データのオブジェクト作成
                        int id = cursor.getInt(0);
                        String detail = cursor.getString(1);
                        String memo = cursor.getString(2);
                        String category = cursor.getString(3);
                        int money = cursor.getInt(4);
                        String payment = cursor.getString(5);
                        int day = cursor.getInt(6);
                        String month1 = cursor.getString(7);
                        Expenditure exp = new Expenditure(id, detail, memo, category, money, payment, day, month1);
                        dataList.add(exp);

                        //出費合計
                        sum += money;
                        s1.setText(nfCur.format(sum));
                        cursor.moveToNext();
                    }
                    cursor.close();

                    //結果をListViewに表示させる
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                    //合計金額をid=s1に表示させる
                } else {
                    //その日にデータがないとき
                    //ListViewのリセット(ひとつ前の動作で表示されているListViewの削除）
                    MyAdapter myAdapter = new MyAdapter(MainActivity.this);//自作のアダプター
                    dataList.clear();
                    myAdapter.setDataList(dataList);    //表示したいリストをセット
                    listView.setAdapter(myAdapter);     //リストビューに表示

                    s1.setText(R.string.s1);//合計金額に空白表示
                }

                //カレンダークリック時の動作セット
                initCalendar();

            }


        }


    }

}