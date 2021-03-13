package com.peakmain.basicui.activity.utils

import android.support.v7.widget.RecyclerView
import android.view.View
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.basicui.bean.Person
import com.peakmain.ui.navigationbar.DefaultNavigationBar
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.database.DaoSupportFactory.Companion.instance
import com.peakmain.ui.utils.database.IDaoSupport
import java.util.*

class DataBaseActivity : BaseActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mDao: IDaoSupport<Person>? = null
    private var mAdapter: BaseRecyclerStringAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_data_base
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
        DefaultNavigationBar.Builder(this, findViewById(R.id.view_root))
                .hideLeftText()
                .hideRightView()
                .setTitleText("数据库")
                .setToolbarBackgroundColor(R.color.colorAccent)
                .create()
    }

    override fun initData() {
        //todo 记得打开权限
        mDao =instance!!.getDao(Person::class.java)
        val list = mDao!!.querySupport()!!.queryAll()
        if (list == null || list.size == 0) setData(mDao!!)
        val data: MutableList<String> = ArrayList()
        for (i in list.indices.reversed()) {
            val person = list[i]
            data.add("名字是:" + person!!.name + ",年龄是:" + person.age)
        }
        mAdapter = BaseRecyclerStringAdapter(this, data)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun setData(dao: IDaoSupport<Person>) {
        val persons: MutableList<Person> = ArrayList()
        for (i in 0..9) {
            persons.add(Person("测试", 100 + i))
        }
        //批量插入
        dao.insert(persons)
    }

    fun insertData(view: View?) {
        mDao!!.insert(Person("新的数据啊", 119110))
        val list = mDao!!.querySupport()!!.queryAll()
        val data: MutableList<String> = ArrayList()
        for (i in list.indices.reversed()) {
            val person = list[i]
            data.add("名字是:" + person!!.name + ",年龄是:" + person.age)
        }
        mAdapter!!.setData(data)
    }

    fun queryData(view: View?) {
        val list: List<Person> = mDao!!.querySupport()!!.selection("name like ?").selectionArgs(*arrayOf("新%")).query() as List<Person>
        for (person in list) {
            LogUtils.e(person.name + "," + person.age)
        }
    }

    fun deleteData(view: View?) {
        val delete = mDao!!.delete("age = ?", *arrayOf("109"))
        LogUtils.e("删除的：$delete")
        val data: MutableList<String> = ArrayList()
        val list = mDao!!.querySupport()!!.queryAll()
        for (i in list.indices.reversed()) {
            val person = list[i]
            data.add("名字是:" + person!!.name + ",年龄是:" + person.age)
        }
        mAdapter!!.setData(data)
    }

    fun updateData(view: View?) {
        mDao!!.update(Person("peakmain", 18), "age =?", *arrayOf("108"))
        val list = mDao!!.querySupport()!!.queryAll()
        val data: MutableList<String> = ArrayList()
        for (i in list.indices.reversed()) {
            val person = list[i]
            data.add("名字是:" + person!!.name + ",年龄是:" + person.age)
        }
        mAdapter!!.setData(data)
    }

    companion object {
        //权限回调ØØ
        private const val RC_WRITE_READ = 0x0100
    }
}