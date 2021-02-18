package com.nalazoocare.messagechatt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.nalazoocare.messagechatt.databinding.ItemBinding;
import com.nalazoocare.messagechatt.databinding.ItemitmeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nalazoo.yeomeme@gmail.com on 2020-07-13
 */
public class ListAdapter extends BaseAdapter {

    public class Msg {
        private String msg;
        Msg(String msg) {
            this.msg = msg;
        }
    }

    private List<Msg> itemList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater = null;


    public ListAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
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

        ItemitmeBinding b;
        if (convertView == null) {
            b = DataBindingUtil.inflate(mLayoutInflater, R.layout.itemitme,parent,false);
            convertView = b.getRoot();
        } else {
            b = (ItemitmeBinding) convertView.getTag();
        }
        MessageHolder msgHolder = new MessageHolder(b);
        convertView.setTag(b);
        Log.d("매스", "매스 :" + itemList.get(position).msg );
        b.itemitem.setText(itemList.get(position).msg);

        return convertView;
    }

    public void addItem(String msg) {
        itemList.add(new Msg(msg));
    }

    public class MessageHolder {
        ItemitmeBinding binding;
        MessageHolder(ItemitmeBinding binding) {
            this.binding = binding;
        }
    }
}
