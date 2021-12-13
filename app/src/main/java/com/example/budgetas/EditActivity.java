package com.example.budgetas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

//編集ページ
public class EditActivity extends AppCompatActivity {

    private TestOpenHelper helper;
    private SQLiteDatabase db;
    private int dispDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        System.out.println("EditPage onCreate");

        //ListViewからデータを持って移動してきたとき
        Intent intent = getIntent();
        int getId = intent.getIntExtra("id",0);

        //データベースヘルパーのインスタンスを作成する（まだデータベースはできない）
        helper = new TestOpenHelper(this);

        Spinner spinnerCategory = findViewById(R.id.category);
        Spinner spinnerPayment = findViewById(R.id.payment);
        Button editButton = (Button) findViewById(R.id.editButton);
        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        Button cancelButton = (Button) findViewById(R.id.cancel);
        TextView id = (TextView) findViewById(R.id.t8Id);
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

        //メインページから持ってきたidのデータをD/Bから取得
        if(helper == null){
            helper = new TestOpenHelper(this);
        }
        if(db == null){
            db = helper.getReadableDatabase();
        }
        //SQLの実行
        Cursor cursor = db.query(
                "expenditure",
                new String[] {"detail","memo", "category", "money", "payment", "day"},
                "id =" + getId,
                null,
                null,
                null,
                null
        );
        cursor.moveToFirst();

        //出費データのオブジェクト作成
        String dispDetail = cursor.getString(0);
        String dispMemo = cursor.getString(1);
        String dispCategory = cursor.getString(2);
        int dispMoney = cursor.getInt(3);
        String dispPayment = cursor.getString(4);
        dispDay = cursor.getInt(5);

        cursor.close();

        //yyyyMMdd→yyyy/MM/dd
        String strDay = String.valueOf(dispDay);
        String yyyy = strDay.substring(0, 4);
        String mm   = strDay.substring(4, 6);
        String dd   = strDay.substring(6);
        String dispDay1 = new StringBuilder().append(yyyy).append("/").append(mm).append("/").append(dd).toString();
        System.out.println(dispDay1);
        //各場所に取得した値をセットしていく
        System.out.println(dispDay1);
        id.setText(String.valueOf(getId));
        day.setText(dispDay1);
        ArrayAdapter myAdap = (ArrayAdapter) spinnerCategory.getAdapter();
        int spinnerPosition = myAdap.getPosition(dispCategory);
        spinnerCategory.setSelection(spinnerPosition);
        detail.setText(dispDetail);
        memo.setText(dispMemo);
        money.setText(String.valueOf(dispMoney));
        ArrayAdapter myAdap2 = (ArrayAdapter) spinnerPayment.getAdapter();
        int spinnerPosition2 = myAdap2.getPosition(dispPayment);
        spinnerPayment.setSelection(spinnerPosition2);


        //変更ボタンクリック時
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = helper.getWritableDatabase();
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
                db.update("expenditure", values, "id = " + getId, null);

                //Mainに日付を戻す
                Intent intent = new Intent();
                intent.putExtra("Day",NumDay);
                setResult(RESULT_OK, intent);

                //ページを閉じる
                finish();
            }
        });

        //削除ボタンクリック時
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //削除していいか確認を表示したい
                //ダイアログフラグメントのオブジェクトを生成
                SampleDialogFragment dialogFragment = new SampleDialogFragment();
                // 渡す値をセット
                Bundle args = new Bundle();
                args.putInt("ID", getId);
                dialogFragment.setArguments(args);
                //ダイアログの表示
                dialogFragment.show(getSupportFragmentManager(),"SampleDialogFragment");

                //Mainに日付を戻す
                Intent intent = new Intent();
                intent.putExtra("Day",dispDay);
                setResult(RESULT_OK, intent);
            }

        });



        //キャンセルボタンクリック時
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mainに日付を戻す
                Intent intent = new Intent();
                intent.putExtra("Day",dispDay);
                setResult(RESULT_OK, intent);
                //ページを閉じる
                finish();
            }
        });

    }

    //ダイアログで値が返された時に戻ってくる（削除を選んだ時）
    public void onReturnValue(int number) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        db.delete("expenditure", "id =" + number, null);

        //Mainに日付を戻す
        Intent intent = new Intent();
        intent.putExtra("Day",dispDay);
        setResult(RESULT_OK, intent);
        //ページを閉じる
        finish();
    }

}