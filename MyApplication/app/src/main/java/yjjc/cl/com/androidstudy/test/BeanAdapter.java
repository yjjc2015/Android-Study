package yjjc.cl.com.androidstudy.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import yjjc.cl.com.androidstudy.R;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class BeanAdapter extends BaseAdapter {
    private List<MyBean> list;
    private LayoutInflater mInflater;
    private Context context;
    public BeanAdapter(Context context, List<MyBean> list) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
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
        BeanHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_test_bean, null);
            holder = new BeanHolder();
            holder.id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.grade = (TextView) convertView.findViewById(R.id.tv_grade);
            holder.gridView = (MyGridView) convertView.findViewById(R.id.gridView);
            holder.listView = (MyListView) convertView.findViewById(R.id.listView);
            convertView.setTag(holder);
        }else {
            holder = (BeanHolder) convertView.getTag();
        }
        MyBean bean = list.get(position);
        holder.id.setText(bean.getId()+"");
        holder.name.setText(bean.getName());
        if (bean.getGrade() != null) {
            holder.grade.setText(bean.getGrade());
            convertView.findViewById(R.id.ll_grade).setVisibility(View.VISIBLE);
        }else{
            convertView.findViewById(R.id.ll_grade).setVisibility(View.GONE);
        }
        if (bean.getObj1s() != null) {
            ObjAdapter adapter1 = new ObjAdapter(context, bean.getObj1s());
            holder.gridView.setAdapter(adapter1);
            holder.gridView.setVisibility(View.VISIBLE);
        }else{
            holder.gridView.setVisibility(View.GONE);
        }
        if (bean.getObj2s() != null){
            ObjAdapter adapter2 = new ObjAdapter(context, bean.getObj2s());
            holder.listView.setAdapter(adapter2);
            holder.listView.setVisibility(View.VISIBLE);
        }else{
            holder.listView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public static class BeanHolder {
        TextView id;
        TextView name;
        TextView grade;
        MyGridView gridView;
        MyListView listView;
    }
}
