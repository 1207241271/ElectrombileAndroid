package com.xiaoantech.electrombile.ui.main.InfoFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoantech.electrombile.R;
import com.xiaoantech.electrombile.base.BaseFragment;
import com.xiaoantech.electrombile.databinding.FragmentInfoBinding;

/**
 * Created by yangxu on 2016/11/3.
 */

public class InfoFragment extends BaseFragment {
    private FragmentInfoBinding mBinding;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentInfoBinding.inflate(inflater);
        return inflater.inflate(R.layout.fragment_info,container,false);
    }

    @Override
    public void initView() {

    }
}
