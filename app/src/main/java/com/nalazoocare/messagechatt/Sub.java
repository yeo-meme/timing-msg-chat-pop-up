package com.nalazoocare.messagechatt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import com.nalazoocare.messagechatt.databinding.SubBinding;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-07-07
 */
public class Sub extends LinearLayout {

    SubBinding b;

    public Sub(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Sub(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        b = DataBindingUtil.inflate(inflater, R.layout.sub, this ,false);
    }
}
