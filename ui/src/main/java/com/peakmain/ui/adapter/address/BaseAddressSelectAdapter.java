package com.peakmain.ui.adapter.address;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.peakmain.ui.R;
import com.peakmain.ui.adapter.bean.AddressSelectBean;
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;
import com.peakmain.ui.utils.SizeUtils;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public abstract class BaseAddressSelectAdapter<T extends AddressSelectBean> extends CommonRecyclerAdapter<T> {
    private String data;
    private AddressSelectObserver mObserver;

    public void notifyDataChange() {
        if (mObserver != null) {
            mObserver.notifyDataChange();
        }
    }

    public void registerDataSetObserver(AddressSelectObserver observer) {
        this.mObserver = observer;
    }

    //3.观察者模式及时更新
    public void unregisterDataSetObserver() {
        mObserver = null;
    }

    public BaseAddressSelectAdapter(Context context, List<T> data) {
        super(context, data, R.layout.item_address_select_recycler);
    }

    @Override
    public void convert(ViewHolder holder, final T item) {
        final TextView tvAddressName = holder.getView(R.id.tv_address_name);
        final String addressText = getAddressText(item);
        tvAddressName.setText(addressText);
        if (item.isSelect()) {
            tvAddressName.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
            Drawable drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.ic_address_select);
            tvAddressName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft
                    , null, null, null);
            tvAddressName.setCompoundDrawablePadding(SizeUtils.dp2px(mContext, 4));
        }
        holder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isSelect()){
                    //设置没有被选中
                    tvAddressName.setTextColor(ContextCompat.getColor(mContext, R.color.color_4A4A4A));
                    tvAddressName.setCompoundDrawablesWithIntrinsicBounds(null
                            , null, null, null);
                    item.setSelect(false);
                }else{
                    //设置为选中
                    tvAddressName.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                    Drawable drawableLeft = ContextCompat.getDrawable(mContext, R.drawable.ic_address_select);
                    tvAddressName.setCompoundDrawablesWithIntrinsicBounds(drawableLeft
                            , null, null, null);
                    tvAddressName.setCompoundDrawablePadding(SizeUtils.dp2px(mContext, 4));
                    setData(addressText);
                    item.setSelect(true);
                }
                notifyDataChange();

            }
        });
    }

    public String getSelectName() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public abstract String getAddressText(T item);

}
