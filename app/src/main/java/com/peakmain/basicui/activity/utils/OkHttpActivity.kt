package com.peakmain.basicui.activity.utils

import android.os.Environment
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.peakmain.basicui.R
import com.peakmain.basicui.adapter.BaseRecyclerStringAdapter
import com.peakmain.basicui.base.BaseActivity
import com.peakmain.ui.recyclerview.listener.OnItemClickListener
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.ToastUtils
import com.peakmain.ui.utils.network.HttpUtils.Companion.PARAMS_KEY_BACKSPLASH_VALUE
import com.peakmain.ui.utils.network.HttpUtils.Companion.PARAMS_KEY_EQUAL_VALUE
import com.peakmain.ui.utils.network.HttpUtils.Companion.PARAMS_KEY_NOKEY_VALUE
import com.peakmain.ui.utils.network.HttpUtils.Companion.with
import com.peakmain.ui.utils.network.callback.DownloadCallback
import com.peakmain.ui.utils.network.callback.EngineCallBack
import java.io.File
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/9/13
 * mail:2726449200@qq.com
 * describe：okhttp工具类
 */
class OkHttpActivity : BaseActivity() {
    private lateinit var file: File
    private lateinit var mBean: MutableList<String>
    private var mAdapter: BaseRecyclerStringAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var mProgressBar: ProgressBar? = null
    private lateinit var mTvResult: TextView
    override fun getLayoutId(): Int {
        return R.layout.basic_linear_recycler_view
    }

    override fun initView() {
        mRecyclerView = findViewById(R.id.recycler_view)
      mNavigationBuilder!!
                .setTitleText("okhttp网络引擎切换工具类")
                .create()
        mProgressBar = findViewById(R.id.progressbar)
        mTvResult = findViewById(R.id.tv_result)
        mTvResult.visibility = View.VISIBLE
    }

    override fun initData() {
        mBean = ArrayList()
        mBean.add("get方法请求——params_key_equal_value")
        mBean.add("get方法请求——params_key_equal_value")
        mBean.add("get方法请求——params_key_nokey_value")
        mBean.add("post方法请求")
        mBean.add("单线程下载")
        mBean.add("多线程下载")
        mAdapter = BaseRecyclerStringAdapter(this, mBean)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))
        mRecyclerView!!.adapter = mAdapter
        mAdapter!!.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                when (position) {
                    0 -> with(this@OkHttpActivity)
                            .url("http://i.jandan.net/")
                            .addParams("oxwlxojflwblxbsapi", "jandan.get_pic_comments")
                            .addParams("page", "1")
                            .paramsType(PARAMS_KEY_EQUAL_VALUE)
                            .execute(object : EngineCallBack {
                                override fun onError(e: Exception?) {
                                    LogUtils.e(e!!.message)
                                }

                                override fun onSuccess(result: String?) {
                                    mTvResult.text = result
                                }
                            })
                    1->{
                        with(this@OkHttpActivity)
                                .url("http://gank.io/api/search/query/listview/")
                                .addParams("category","Android")
                                .addParams("count","10")
                                .addParams("page","1")
                                .paramsType(PARAMS_KEY_BACKSPLASH_VALUE)
                                .execute(object :EngineCallBack{
                                    override fun onError(e: Exception?) {
                                        LogUtils.e(e!!.message)
                                    }

                                    override fun onSuccess(result: String?) {
                                        mTvResult.text = result
                                    }

                                })
                    }
                    2->{
                        with(this@OkHttpActivity)
                                .url("http://gank.io/api/history/content/")
                                .addParams("count",1)
                                .addParams("page",1)
                                .paramsType(PARAMS_KEY_NOKEY_VALUE)
                                .execute(object :EngineCallBack{
                                    override fun onError(e: Exception?) {
                                        LogUtils.e(e!!.message)
                                    }

                                    override fun onSuccess(result: String?) {
                                        mTvResult.text = result
                                    }

                                })
                    }
                    3 -> with(this@OkHttpActivity)
                            .url("https://www.wanandroid.com/user/login")
                            .addParams("username", "peakmain123")
                            .addParams("password", 123456)
                            .post()
                            .execute(object : EngineCallBack {
                                override fun onError(e: Exception?) {
                                    LogUtils.e(e!!.message)
                                }

                                override fun onSuccess(result: String?) {
                                    mTvResult.text = result
                                }
                            })
                    4-> {
                        val file = File(Environment.getExternalStorageDirectory(), "test.apk")
                        if (file.exists()) {
                            file.delete()
                        }
                        with(this@OkHttpActivity)
                                .url("http://imtt.dd.qq.com/16891/apk/87B3504EE9CE9DC51E9F295976F29724.apk")
                                .downloadSingle()
                                .file(file)
                                .exectureDownload(object : DownloadCallback {
                                    override fun onFailure(e: Exception?) {
                                        LogUtils.e(e!!.message)
                                    }

                                    override fun onSucceed(file: File?) {
                                        ToastUtils.showShort("file下载完成")
                                        LogUtils.e("文件保存的位置:" + file!!.absolutePath)
                                        mProgressBar!!.visibility = View.GONE
                                        mProgressBar!!.progress = 0
                                    }

                                    override fun onProgress(progress: Int) {
                                        LogUtils.e("单线程下载apk的进度:$progress")
                                        mProgressBar!!.progress = progress
                                        mProgressBar!!.visibility = View.VISIBLE
                                    }
                                })
                    }
                    5-> {
                        file = File(Environment.getExternalStorageDirectory(), "test.apk")
                        if (file.exists()) {
                            file.delete()
                        }
                        with(this@OkHttpActivity)
                                .url("http://imtt.dd.qq.com/16891/apk/87B3504EE9CE9DC51E9F295976F29724.apk")
                                .downloadMutil()
                                .file(file)
                                .exectureDownload(object : DownloadCallback {
                                    override fun onFailure(e: Exception?) {
                                        LogUtils.e(e!!.message)
                                    }

                                    override fun onSucceed(file: File?) {
                                        LogUtils.e(file!!.absolutePath + "," + file.name)
                                        Toast.makeText(this@OkHttpActivity, "下载完成", Toast.LENGTH_LONG).show()
                                        mProgressBar!!.visibility = View.GONE
                                        mProgressBar!!.progress = 0
                                    }

                                    override fun onProgress(progress: Int) {
                                        LogUtils.e("$progress%")
                                        mProgressBar!!.visibility = View.VISIBLE
                                        mProgressBar!!.progress = progress
                                    }
                                })
                    }
                }
            }
        })
    }
}