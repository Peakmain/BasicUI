package com.peakmain.basicui.activity.home;

import com.peakmain.basicui.R;
import com.peakmain.basicui.adapter.address.JdAddressAreaSelectAdapter;
import com.peakmain.basicui.adapter.address.JdAddressCitySelectAdapter;
import com.peakmain.basicui.adapter.address.JdAddressProviceSelectAdapter;
import com.peakmain.basicui.adapter.address.JdAddressStreetSelectAdapter;
import com.peakmain.basicui.base.BaseActivity;
import com.peakmain.basicui.bean.SearchCityBean;
import com.peakmain.basicui.utils.JsonUtil;
import com.peakmain.ui.constants.BasicUIConstants;
import com.peakmain.ui.widget.address.AddressSelectLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * author ：Peakmain
 * createTime：2020/3/16
 * mail:2726449200@qq.com
 * describe：
 */
public class JdAddressSelectActivity extends BaseActivity {
    private AddressSelectLayout mAddressSelect;
    private JdAddressProviceSelectAdapter mProviceSelectAdapter;
    private JdAddressCitySelectAdapter mCitySelectAdapter;
    private JdAddressAreaSelectAdapter mAreaSelectAdapter;
    private JdAddressStreetSelectAdapter mStreetSelectAdapter;
    private List<SearchCityBean> mProviceBean;
    private List<SearchCityBean> mCityBeans;
    private List<SearchCityBean> mAreaBean;
    private List<SearchCityBean> mStreetBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_jd_address_select;
    }

    @Override
    protected void initView() {
        mAddressSelect = findViewById(R.id.address_select);
    }

    @Override
    protected void initData() {
        String cityStr = JsonUtil.getAssetsJson("city.json");
        try {
            JSONObject jsonObject = new JSONObject(cityStr);
            mProviceBean = JsonUtil.parseArrList(jsonObject.getJSONArray("list").toString(), SearchCityBean.class);
            mCityBeans = JsonUtil.parseArrList(jsonObject.getJSONArray("list").toString(), SearchCityBean.class);
            mAreaBean = JsonUtil.parseArrList(jsonObject.getJSONArray("list").toString(), SearchCityBean.class);
            mStreetBean = JsonUtil.parseArrList(jsonObject.getJSONArray("list").toString(), SearchCityBean.class);

            mProviceSelectAdapter = new JdAddressProviceSelectAdapter(this, mProviceBean);
            mAddressSelect.setAdapter(mProviceSelectAdapter, BasicUIConstants.PROVICE_TYPE);
            mAddressSelect.setChangeDataListener(new AddressSelectLayout.OnChangeDataListener() {
                @Override
                public void onChangeData() {
                    switch (mAddressSelect.getCurrentType()) {
                        case BasicUIConstants.PROVICE_TYPE:
                            mProviceSelectAdapter = new JdAddressProviceSelectAdapter(JdAddressSelectActivity.this, mProviceBean);
                            mAddressSelect.setAdapter(mProviceSelectAdapter, mAddressSelect.getCurrentType());
                            mProviceSelectAdapter.notifyDataSetChanged();
                            break;
                        case BasicUIConstants.CITY_TYPE:
                            mCitySelectAdapter = new JdAddressCitySelectAdapter(JdAddressSelectActivity.this, mCityBeans);
                            mAddressSelect.setAdapter(mCitySelectAdapter, mAddressSelect.getCurrentType());
                            mCitySelectAdapter.notifyDataSetChanged();
                            break;
                        case BasicUIConstants.AREA_TYPE:
                            mAreaSelectAdapter = new JdAddressAreaSelectAdapter(JdAddressSelectActivity.this, mAreaBean);
                            mAddressSelect.setAdapter(mAreaSelectAdapter, mAddressSelect.getCurrentType());
                            mAreaSelectAdapter.notifyDataSetChanged();
                            break;
                        case BasicUIConstants.STREET_TYPE:
                            mStreetSelectAdapter = new JdAddressStreetSelectAdapter(JdAddressSelectActivity.this, mStreetBean);
                            mAddressSelect.setAdapter(mStreetSelectAdapter, mAddressSelect.getCurrentType());
                            mStreetSelectAdapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }


                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
