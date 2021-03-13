package com.peakmain.basicui.activity.home.recylcer.data

import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
class PesudoImageData private constructor() {
    private object Holder {
        var instance = PesudoImageData()
    }

    val data: List<GroupBean>
        get() {
            val times = arrayOf("2020年1月", "2020年2月", "2020年3月", "2020年4月")
            val groupBeans: MutableList<GroupBean> = ArrayList()
            for (i in 0..3) {
                groupBeans.add(GroupBean("http://dingyue.nosdn.127.net/iQ7THjahD4whhkkp=O0KohtFXf1gdn8w0NJQkoEbWOaGn1517149396307compressflag.jpg", times[i]))
                groupBeans.add(GroupBean("https://04.imgmini.eastday.com/mobile/20170509/5ed27871c50a965d0f73c83352534e0e.jpeg", times[i]))
                groupBeans.add(GroupBean("http://image3.xyzs.com/upload/86/ce/39722/20130831/137795629164825_0.jpg", times[i]))
                groupBeans.add(GroupBean("http://g-ec4.images-amazon.com/images/G/28/aplus_rbs/C791820130315487.jpg", times[i]))
                groupBeans.add(GroupBean("http://p7.qhimg.com/t01fb77727413c50484.jpg", times[i]))
                groupBeans.add(GroupBean("https://i03piccdn.sogoucdn.com/816305ef14d12f61", times[i]))
                groupBeans.add(GroupBean("http://blog.ellechina.com/775207/files/2011/04/201104172052245qHeT-1024x640.jpg", times[i]))
                groupBeans.add(GroupBean("http://www.jnfkzx.com/uploads/allimg/130427/2-13042G01213N2.jpg", times[i]))
                groupBeans.add(GroupBean("http://img5.hao123.com/data/1_59c413fa3d0e8c96cf9877b36c90bb0b_510", times[i]))
                groupBeans.add(GroupBean("http://5b0988e595225.cdn.sohucs.com/images/20191001/d650ac75a9314471844558c34b6917a9.jpeg", times[i]))
                groupBeans.add(GroupBean("http://img2.hnol.net/bbsimg/2007-3-5/8/20073585258510501.jpg", times[i]))
            }
            return groupBeans
        }

    companion object {
        @JvmStatic
        val instance: PesudoImageData
            get() = Holder.instance
    }
}