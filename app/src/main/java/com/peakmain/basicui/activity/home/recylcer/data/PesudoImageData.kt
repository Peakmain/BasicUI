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
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Ue16c54cac6574a06a0c1afdad979b007W.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Uec00959acd9c4d0aa900d5fb8ea481931.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Uef43b2afdd2e4480aab896c8fad7e5f1c.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/U892b3e7fb9b84ce3ade783d3396fc371A.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/U76a18e0d315e407a8daf3d91de033e31i.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Ue16c54cac6574a06a0c1afdad979b007W.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Uec00959acd9c4d0aa900d5fb8ea481931.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/U9a21a2f4b83c4030b87c840bc07105e5A.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/U97dda0f4080140bb99911d798328f56dJ.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/Udc4f4aac155545818ea877ccb4d09aa0Y.jpg", times[i]))
                groupBeans.add(GroupBean("https://ae01.alicdn.com/kf/U8f29046315a345b488a91f19e0691d7bx.jpg", times[i]))
            }
            return groupBeans
        }

    companion object {
        @JvmStatic
        val instance: PesudoImageData
            get() = Holder.instance
    }
}