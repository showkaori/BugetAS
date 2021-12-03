package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DatePickerDialog.OnDateSetListener;
import androidx.fragment.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private TestOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //データベースヘルパーのインスタンスを作成する（まだデータベースはできない）
        helper = new TestOpenHelper(this);

        Spinner spinnerCategory = findViewById(R.id.category);
        Spinner spinnerPayment = findViewById(R.id.payment);
        Button executeButton = (Button) findViewById(R.id.executeButton);
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

        // Alayout/custom_spinnerからデザイン取得、value/stringからpaymentSpinner取得
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

                //ページを閉じる
                finish();
            }
        });

    }
}
