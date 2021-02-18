package com.nalazoocare.messagechatt;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.nalazoocare.messagechatt.databinding.ItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-07-03
 */
public class Adapter extends BaseAdapter {

    public class Msg {
        private String msg;
        private int type;

        Msg(String msg, int type) {
            this.msg = msg;
            this.type = type;
        }
    }

    private List<Msg> itemList = new ArrayList<>();
    private TextView contentMsg;
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;
    private TextView text;


    public Adapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemBinding binding;

        if (convertView == null) {
            //dataBinding
            binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item, parent, false);
            convertView = binding.getRoot();
        } else {
            binding = (ItemBinding) convertView.getTag();
        }

        //dataBinding
        convertView.setTag(binding);


        //dataBinding
        if (itemList.get(position).type == 0) {
            binding.layout.setGravity(Gravity.LEFT);
        } else if (itemList.get(position).type == 1) {
            binding.layout.setGravity(Gravity.RIGHT);
        }
        binding.kim.setText(itemList.get(position).msg);
        Log.d("매스", "매스 :" + itemList);

        return convertView;
    }

    public void addItem(String msg, int type) {
        itemList.add(new Msg(msg, type));
    }

    public class MsgHolder {
        ItemBinding binding;

        MsgHolder(ItemBinding binding) {
            this.binding = binding;
        }
    }

}
