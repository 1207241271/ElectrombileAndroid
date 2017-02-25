package com.xiaoantech.electrombile.base;

/**
 * Created by yangxu on 2016/10/28.
 */
public interface BaseView<T> {

    void setPresenter(T presenter);
    void showToast(String errorMeg);
    void showWaitingDialog(String dialogString);
    void hideWaitingDialog();
}
