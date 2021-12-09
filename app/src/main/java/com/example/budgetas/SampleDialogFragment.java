package com.example.budgetas;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class SampleDialogFragment extends DialogFragment {

    private int number;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //値を受け取る
        number = getArguments().getInt("ID", 0);
        //ビルダーでタイトルやメッセージを設定できる
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //ダイアログのタイトル文を設定
        builder.setTitle(R.string.dialog_title);
        //ダイアログのメッセージ文を設定
        builder.setMessage("データ番号：" + number + " を削除してもよろしいですか？");

        //Positive ボタンに表示内容 クリックしたときの処理
        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //OKボタンが押されたときのメソッドを呼び出し
                        System.out.println("削除するでーたIDは" + number);
                        EditActivity callingActivity = (EditActivity) getActivity();
                        callingActivity.onReturnValue(number);
                        dismiss();

                    }
                });
        //Negative ボタンに表示内容と
        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        //設定したダイアログを作成
        AlertDialog dialog = builder.create();
        //ダイアログをリターン
        return dialog;
    };

}
