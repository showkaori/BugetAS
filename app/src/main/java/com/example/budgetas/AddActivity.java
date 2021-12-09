package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddActivity extends AppCompatActivity {
    private TestOpenHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        System.out.println("AddPage onCreate");

        //日付データを持って移動してきたとき
        Intent intent = getIntent();
        String selectDay = intent.getStringExtra("day");
        if(selectDay != null){
            EditText day = (EditText) findViewById(R.id.day);
            day.setText(selectDay);
        }

        //データベースヘルパーのインスタンスを作成する（まだデータベースはできない）
        helper = new TestOpenHelper(this);

        Spinner spinnerCategory = findViewById(R.id.category);
        Spinner spinnerPayment = findViewById(R.id.payment);
        Button executeButton = (Button) findViewById(R.id.executeButton);
        Button cancelButton = (Button) findViewById(R.id.cancel);
        EditText day = (EditText) findViewById(R.id.day);
        EditText detail = (EditText) findViewById(R.id.detail);
        EditText memo = (EditText) findViewById(R.id.memo);
        EditText money = (EditText) findViewById(R.id.money);

        // layout/custom_spinnerからデザイン取得、value/stringからcategorySpinner取得
        ArrayAdapter<String> adapter1
                = new ArrayAdapter<>(this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.categorySpinner));

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinnerCategory.setAdapter(adapter1);

        // layout/custom_spinnerからデザイン取得、value/stringからpaymentSpinner取得
        ArrayAdapter<String> adapter2
                = new ArrayAdapter<>(this,
                R.layout.custom_spinner,
                getResources().getStringArray(R.array.paymentSpinner));

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner に adapter をセット
        spinnerPayment.setAdapter(adapter2);



        //追加ボタンクリック時
        executeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //入力された値の取得
                String Day = day.getText().toString();    //日付文字列を取得
                String numDay = Day.replaceAll("[^\\d]", ""); //数字のみを抽出yyyyMMddの形に
                int NumDay = Integer.parseInt(numDay);
                String Category = (String)spinnerCategory.getSelectedItem();
                String Detail = detail.getText().toString();
                String Memo = memo.getText().toString();
                int Money = Integer.parseInt(money.getText().toString());
                String Payment = (String)spinnerPayment.getSelectedItem();
                String Month = numDay.substring(0, 6);

                //データを保存する
                values.put("detail", Detail);
                values.put("memo", Memo);
                values.put("category", Category);
                values.put("money", Money);
                values.put("payment", Payment);
                values.put("day", NumDay);
                values.put("month", Month);
                db.insert("expenditure", null, values);

                //Mainに日付を戻す
                Intent intent = new Intent();
                intent.putExtra("Day",NumDay);
                setResult(RESULT_OK, intent);

                // このアクティビティを終了する
                finish();
                //確認用
                System.out.println("ライフサイクル onCreate()");
            }
        });

        //キャンセルボタンクリック時
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ページを閉じる
                finish();
            }
        });

    }

    // 以下は、ライフサイクルのテスト用

    @Override
    protected void onStart() {
        System.out.println("ライフサイクル onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        System.out.println("ライフサイクル onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        System.out.println("ライフサイクル onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        System.out.println("ライフサイクル onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        System.out.println("ライフサイクル onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        System.out.println("ライフサイクル onDestroy()");
        super.onDestroy();
    }
}

