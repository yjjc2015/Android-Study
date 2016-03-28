package yjjc.cl.com.androidstudy.test;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import yjjc.cl.com.androidstudy.R;

/**
 * Created by Administrator on 2016/3/28.
 */
public class ObjAdapter extends BaseAdapter {
    private List<MyObj> list;
    private LayoutInflater mLayoutInflater;

    public ObjAdapter (Context context, List<MyObj> list) {
        this.list = list;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ObjHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_obj, null);
            convertView.setBackgroundColor(Color.YELLOW);
            holder = new ObjHolder();
            holder.id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(holder);
        } else {
            holder = (ObjHolder) convertView.getTag();
        }
        MyObj obj = list.get(position);
        holder.id.setText(""+obj.getId());
        holder.name.setText(obj.getName());
        return convertView;
    }

    public static class ObjHolder {
        public TextView id;
        public TextView name;
    }
}
