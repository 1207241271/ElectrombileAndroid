package com.xiaoantech.electrombile.base;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by jk on 16-10-26.
 *
 * the base class of all fragment
 */

public abstract class BaseFragment extends Fragment {
    public Context mContext;
    public abstract void initView();
}
