package yjjc.cl.com.androidstudy.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import yjjc.cl.com.androidstudy.R;

/**
 * 实现ScrollView 嵌套 多层ListView和GridView
 */
public class Main2Activity extends Activity {
    public static final int INT = 10;
    //    private ListView lv_test;
    private MyListView lv_test;
    private List<MyBean> beans = new ArrayList<>();
    private BeanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initData();
        initView();
    }

    private void initData() {
        MyBean bean = null;
        for (int i=0; i<100; i++) {
            bean = new MyBean();
            bean.setId(i);
            bean.setName("张" + i);
            if (i%10 == 0) {
                bean.setGrade("等级：" + (i/10));
            }
            if (i%2 == 0) {
                int ran = getRandomNum(10);
                List<MyObj> objs = new ArrayList<>();
                MyObj obj = null;
                for (int j=0; j< ran; j++){
                    obj = new MyObj();
                    obj.setId(j);
                    obj.setName("GrideView_"+j);
                    objs.add(obj);
                }
                bean.setObj1s(objs);
            }
            if (i%3 == 0){
                int ran = getRandomNum(20);
                List<MyObj> objs = new ArrayList<>();
                for (int j=0; j< ran; j++){
                    MyObj obj = new MyObj();
                    obj.setId(j);
                    obj.setName("ListView_"+j);
                    objs.add(obj);
                }
                bean.setObj2s(objs);
            }
            beans.add(bean);
        }
        adapter = new BeanAdapter(this, beans);
    }

    private int getRandomNum (int n) {
        return new Random().nextInt(n) + 1;
    }

    private void initView(){
//        lv_test = (ListView) findViewById(R.id.lv_test);
        lv_test = (MyListView) findViewById(R.id.lv_test);
        lv_test.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(lv_test);
    }

    private static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
