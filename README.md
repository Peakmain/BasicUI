# BasicUI

### BasicUI is A collection of common UI components.These include recycle clerview encapsulation, basic navigation bar, universal dialog, basic textView, basic LinearLayout, rounded corner image (see GitHub famous CircleImageView), PopupWindow encapsulation, EditText encapsulation, streaming layout

### How to
#### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

#### Step 2. Add the dependency
```
	dependencies {
	        implementation 'com.github.Peakmain:BasicUI:0.0.2'
	}
```
### Use
#### Use of RecyclerView
- Single Layout Adapter 
extends CommonRecyclerAdapter<T>.implementation method
```
public CommonRecyclerAdapter(Context context, List<T> data, int layoutId) {}
```

1、for setup text,you can 
```
holder.setText(int viewId,CharSequence text)
```
you can also:
```
TextView tv=holder.getView(view viewId)
tv.setText("")
```
2、In addition to setting text, you can also set textColor、setVisibility、setOnClickListener、setOnLongClickListener

3、For setting the picture url,you can 
```
  holder.setImageByUrl(R.id.iv_meizhi, new GlideImageLoader(item.getUrl()));
```
GlideImageLoaders is a class that I use Glide to load pictures myself. You can also use other libraries. 
All you need to do is replace GlideImageLoader with a class that you inherit from ViewHolder.HolderImageLoader.


for example:
```
public class PagePoupAdapter extends CommonRecyclerAdapter<String>{
    private int mSelectPosition=0;
    public PagePoupAdapter(Context context, List<String> data) {
        super(context, data, R.layout.item_popup_window);
    }

    @Override
    public void convert(ViewHolder holder, String item) {
        TextView tvName=holder.getView(R.id.tv_name);
        tvName.setText(item);
       tvName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
    }
}
```
- Use of multiple layouts
extends CommonRecyclerAdapter<T>.implementation method
```
 public CommonRecyclerAdapter(Context context, List<T> data, MultiTypeSupport<T> multiTypeSupport) {}
```

for example:
```
    public SearchCityAdapter(Context context, List<SearchCityBean> data) {
        super(context, data, new MultiTypeSupport<SearchCityBean>() {
            @Override
            public int getLayoutId(SearchCityBean item, int position) {
                return R.layout.default_city_recycler_item;
            }
        });
    }
    @Override
    public void convert(ViewHolder holder, SearchCityBean item) {
        int itemViewType = getItemViewType(holder.getAdapterPosition());
        if (itemViewType == R.layout.default_city_recycler_item) {
            holder.setText(R.id.tv_city_name, item.getName());
        }
    }
```

- The use of the Recyclerview basic suspension list.You can directly inherit BaseSuspenisonItemDecoration.Just implement getTopText method.
If you want to set the background color, the height of the text, the size of the text, the color of the text, and so on, you need to inherit baseitemdeclaration.builder <B extends Builder, T >


for example:
```
public class SectionItemDecoration extends BaseItemDecoration<SearchCityBean> {

    public SectionItemDecoration(Builder builder) {
        super(builder);
    }
    @Override
    public String getTopText(List<SearchCityBean> data, int position) {
        return data.get(position).getSection();
    }
    public static class Builder extends BaseItemDecoration.Builder<SectionItemDecoration.Builder,SearchCityBean>{

        public Builder(Context context, List<SearchCityBean> data) {
            super(context, data);
        }
        @Override
        public SectionItemDecoration create() {
            return new SectionItemDecoration(this);
        }
    }
}
```
The use is also particularly simple
```
   SectionItemDecoration decoration = new SectionItemDecoration.Builder(this, mAllCities)
                .setBgColor(ContextCompat.getColor(this,R.color.colorAccent))
                .setTextColor(ContextCompat.getColor(this,R.color.color_black))
                .setSectionHeight(SizeUtils.dp2px(this,30))
                .create();
```

#### Use Custom NavigationBar
```
    mDefaultNavigationBar = new DefaultNavigationBar
                  .Builder(this, (ViewGroup) findViewById(android.R.id.content))
                  //Whether to display the return button
                  .setDisplayHomeAsUpEnabled(true)
                  //Set left click event
                  .setLeftClickListener(v -> {
  
                  })
                  //Whether to display the title that comes with the toolbar by default
                  .setDisplayShowTitleEnabled(true)
                  //Hide right view
                  .hideRightView()
                  //Set the click event of the return button
                  .setNavigationOnClickListener(v -> finish())
                  //set left text color
                  .setLeftTextColor(ContextCompat.getColor(this,R.color.color_272A3D))
                  //set title
                  .setTitleText("")
                  //set toolbar background color
                  .setToolbarBackgroundColor(0)
                  .create();
```
#### Use Custom AlertDialog
```
        AlertDialog dialog = new AlertDialog.Builder(ImagePreviewActivity.this)
                .setContentView(R.layout.dialog_show_image_deal)
                .fromButtom(true)
                // Set click events for view
                .setOnClickListener(R.id.bt_logout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                })
                //set animation
                .setAnimation(R.style.dialog_from_bottom_anim)
                //Eject from bottom
                .fromButtom(true)
                //set width  MATCH_PARENT
                .setFullWidth()
                .show();
```

#### Use Cutom PopupWindow
```
       new CustomPopupWindow.PopupWindowBuilder(this)
                .setView(R.layout.popup_window_view)
                .enableBackgroundDark(true)
                .setAnimationStyle(R.style.PopupWindowAnimation)
                .setBgDarkAlpha(0.7f)
                .create();
```

#### Use Custom TextView
```
    <com.peakmain.ui.widget.ShapeTextView
        android:id="@+id/shape_text_view"
        android:layout_width="@dimen/space_100"
        android:layout_height="@dimen/space_100"
        android:layout_centerInParent="true"
        android:gravity="center"
        android:textColor="@color/color_white"
        android:textSize="28sp"
        tools:text="A"
        app:shapeTvRadius="@dimen/space_6"
        app:shapeTvBackgroundColor="#333333" />
```

#### Use Auto Delete EditText
```
    <com.peakmain.ui.widget.AutoDeleteEditText
                    android:id="@+id/edt_address"
                    android:layout_width="0dp"
                    android:layout_height="match_parent"
                    android:layout_weight="3"
                    android:paddingBottom="@dimen/spacing_5"
                    app:ad_HintColor="@color/color_9B9B9B"
                    app:ad_TextColor="@color/color_4A4A4A"
                    app:ad_TextSize="@dimen/font_16"
                    app:ad_hint="@string/input_details_address"
                    app:ad_paddingTop="@dimen/spacing_15"
                    app:ad_top="true" />
```

