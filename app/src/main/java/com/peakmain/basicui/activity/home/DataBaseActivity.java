package com.peakmain.basicui.activity.home;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.bean.Person;
import com.peakmain.basicui.utils.ToastUtils;
import com.peakmain.ui.utils.database.DaoSupportFactory;
import com.peakmain.ui.utils.database.IDaoSupport;

import java.util.ArrayList;
import java.util.List;


public class DataBaseActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    //权限回调ØØ
    private static final int RC_WRITE_READ = 0x0100;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_data_base;
    }

    @Override
    protected void initView() {
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    @Override
    protected void initData() {
        //todo 记得打开权限
        IDaoSupport<Person> dao = DaoSupportFactory.getInstance().getDao(Person.class);
        setData(dao);
        List<Person> list = dao.querySupport().queryAll();
        List<String> data = new ArrayList<>();
        for (Person person : list) {
            data.add("名字是:" + person.getName() + ",年龄是:" + person.getAge());
        }
        mRecyclerView.setAdapter(new BaseRecyclerStringAdapter(this,data));
    }

    private void setData(IDaoSupport<Person> dao) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            persons.add(new Person("测试", 100 + i));
        }
        dao.insert(persons);
    }


}
