package yjjc.cl.com.androidstudy.test;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class MyBean {
    private int id;
    private String name;
    private String grade;
    private List<MyObj> obj1s;
    private List<MyObj> obj2s;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<MyObj> getObj1s() {
        return obj1s;
    }

    public void setObj1s(List<MyObj> obj1s) {
        this.obj1s = obj1s;
    }

    public List<MyObj> getObj2s() {
        return obj2s;
    }

    public void setObj2s(List<MyObj> obj2s) {
        this.obj2s = obj2s;
    }
}
