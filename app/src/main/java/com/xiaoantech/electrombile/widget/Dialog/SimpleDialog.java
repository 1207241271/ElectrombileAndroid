package com.xiaoantech.electrombile.widget.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaoantech.electrombile.R;

/**
 * Created by yangxu on 2016/12/15.
 */

public class SimpleDialog extends Dialog{

    public SimpleDialog(Context context) {
        super(context);
    }

    public SimpleDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {

        private SimpleDialog simpleDialog;
        private Context context;
        private String title;
        private String message;
        private View contentView;

        public Builder(Context context) {
            this.context = context;
        }

        public SimpleDialog.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @param message
         * @return
         */
        public SimpleDialog.Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public SimpleDialog.Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public SimpleDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleDialog.Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public SimpleDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            simpleDialog = new SimpleDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_simple_layout, null);
            simpleDialog.addContentView(layout, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            // set the content message
            if (message != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(message);
            } else if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(
                        contentView, new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT,
                                ViewGroup.LayoutParams.FILL_PARENT));
            }
            simpleDialog.setContentView(layout);
            return simpleDialog;
        }

        public SimpleDialog getSimpleDialog(){
            return simpleDialog;
        }
    }

}
