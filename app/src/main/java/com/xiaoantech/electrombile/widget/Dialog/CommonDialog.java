package com.xiaoantech.electrombile.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;

/**
 * Created by yangxu on 2016/12/14.
 */

public class CommonDialog {
    private String TAG = "CommonDialog";
    private Context mContext;
    private int customLayoutId;
    private String dialogTitle;
    private String positiveText;
    private String negativeText;
    private DialogType dialogType;

    public enum DialogType{
        DIALOG_TYPE_NORMAL,
        DIALOG_TYPE_CERTAIN,
        DIALOG_TYPE_SIMPLE
    }


        private View dialogView;
    private OnDialogListener listener;

    public CommonDialog(Context context,int customLayoutId,String dialogTitle,String positiveText,String negativeText){
        this.mContext = context;
        this.customLayoutId = customLayoutId;
        this.dialogTitle = dialogTitle;
        this.positiveText = positiveText;
        this.negativeText = negativeText;

        this.dialogView = View.inflate(context,customLayoutId,null);
        this.dialogType = DialogType.DIALOG_TYPE_NORMAL;
    }

    public CommonDialog(Context context,int customLayoutId,String dialogTitle,String positiveText){
        this.mContext = context;
        this.customLayoutId = customLayoutId;
        this.dialogTitle = dialogTitle;
        this.positiveText = positiveText;

        this.dialogView = View.inflate(context,customLayoutId,null);
        this.dialogType = DialogType.DIALOG_TYPE_CERTAIN;
    }

    public CommonDialog(Context context,int customLayoutId,String dialogTitle){
        this.mContext = context;
        this.customLayoutId = customLayoutId;
        this.dialogTitle = dialogTitle;

        this.dialogView = View.inflate(context,customLayoutId,null);
        this.dialogType = DialogType.DIALOG_TYPE_SIMPLE;
    }




    public void showDialog(){
        if(mContext == null){
            Log.e(TAG,"context is null");
            return;
        }


        switch (dialogType){
            case DIALOG_TYPE_NORMAL:
                CustomDialog.Builder dialog = new CustomDialog.Builder(mContext);
                break;
            default:
        }
        CustomDialog.Builder dialog = new CustomDialog.Builder(mContext);
        dialog.setContentView(dialogView);

    }


    public CommonDialog setOnDialogListener(OnDialogListener listener){
        this.listener = listener;
        return this;
    }

    public interface OnDialogListener{
        public void dialogPositiveListener(View customView, DialogInterface dialogInterface, int which);
        public void dialogNegativeListener(View customView, DialogInterface dialogInterface, int which);
    }
}
