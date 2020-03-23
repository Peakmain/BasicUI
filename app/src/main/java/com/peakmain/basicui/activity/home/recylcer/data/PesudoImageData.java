package com.peakmain.basicui.activity.home.recylcer.data;

import com.peakmain.basicui.activity.home.recylcer.bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/23
 * mail:2726449200@qq.com
 * describe：
 */
public class PesudoImageData {
    private PesudoImageData() {
    }

    private static class Holder {
        public static PesudoImageData instance = new PesudoImageData();
    }

    public static PesudoImageData getInstance() {
        return Holder.instance;
    }

    public List<GroupBean> getData() {
        String[] times = {"2020年1月", "2020年2月", "2020年3月", "2020年4月"};
        List<GroupBean> groupBeans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            groupBeans.add(new GroupBean("http://dingyue.nosdn.127.net/iQ7THjahD4whhkkp=O0KohtFXf1gdn8w0NJQkoEbWOaGn1517149396307compressflag.jpg", times[i]));
            groupBeans.add(new GroupBean("https://04.imgmini.eastday.com/mobile/20170509/5ed27871c50a965d0f73c83352534e0e.jpeg", times[i]));
            groupBeans.add(new GroupBean("http://image3.xyzs.com/upload/86/ce/39722/20130831/137795629164825_0.jpg", times[i]));
            groupBeans.add(new GroupBean("http://g-ec4.images-amazon.com/images/G/28/aplus_rbs/C791820130315487.jpg", times[i]));
            groupBeans.add(new GroupBean("http://p7.qhimg.com/t01fb77727413c50484.jpg", times[i]));
            groupBeans.add(new GroupBean("https://i03piccdn.sogoucdn.com/816305ef14d12f61", times[i]));
            groupBeans.add(new GroupBean("http://blog.ellechina.com/775207/files/2011/04/201104172052245qHeT-1024x640.jpg", times[i]));
            groupBeans.add(new GroupBean("http://www.jnfkzx.com/uploads/allimg/130427/2-13042G01213N2.jpg", times[i]));
            groupBeans.add(new GroupBean("http://img5.hao123.com/data/1_59c413fa3d0e8c96cf9877b36c90bb0b_510", times[i]));
            groupBeans.add(new GroupBean("http://5b0988e595225.cdn.sohucs.com/images/20191001/d650ac75a9314471844558c34b6917a9.jpeg", times[i]));
            groupBeans.add(new GroupBean("http://img2.hnol.net/bbsimg/2007-3-5/8/20073585258510501.jpg", times[i]));
        }
        return groupBeans;
    }
}
