package com.peakmain.ui.adapter;

import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

/**
 * author ：Peakmain
 * createTime：2020/9/4
 * mail:2726449200@qq.com
 * describe：基本的ViewPager适配器
 */
public abstract class BaseViewPagerAdapter extends PagerAdapter {
    static final int IGNORE_ITEM_VIEW_TYPE = AdapterView.ITEM_VIEW_TYPE_IGNORE;
    private final RecycleBin recycleBin;

    public BaseViewPagerAdapter() {
        this(new RecycleBin());
    }

    BaseViewPagerAdapter(RecycleBin recycleBin) {
        this.recycleBin = recycleBin;
        recycleBin.setViewTypeCount(getViewTypeCount());
    }

    @Override
    public void notifyDataSetChanged() {
        recycleBin.scrapActiveViews();
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public final Object instantiateItem(@Nullable ViewGroup container, int position) {
        int viewType = getItemViewType(position);
        View view = null;
        if (viewType != IGNORE_ITEM_VIEW_TYPE) {
            view = recycleBin.getScrapView(position, viewType);
        }
        view = getView(position, view, container);
        if (container != null)
            container.addView(view);
        return view;
    }

    @Override
    public final void destroyItem(@Nullable ViewGroup container, int position, @Nullable Object object) {
        if (object == null || container == null) return;
        View view = (View) object;
        container.removeView(view);
        int viewType = getItemViewType(position);
        if (viewType != IGNORE_ITEM_VIEW_TYPE) {
            recycleBin.addScrapView(view, position, viewType);
        }
    }

    public int getViewTypeCount() {
        return 1;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public abstract View getView(int position, View convertView, ViewGroup container);

    @Override
    public final boolean isViewFromObject(@Nullable View view, @Nullable Object object) {
        return view == object;
    }

    public static class RecycleBin {
        /**
         * Views that were on screen at the start of layout. This array is populated at the start of
         * layout, and at the end of layout all view in activeViews are moved to scrapViews.
         * Views in activeViews represent a contiguous range of Views, with position of the first
         * view store in mFirstActivePosition.
         */
        private final View[] activeViews = new View[0];
        private final int[] activeViewTypes = new int[0];

        /**
         * Unsorted views that can be used by the adapter as a convert view.
         */
        private SparseArray<View>[] scrapViews;

        private int viewTypeCount;

        private SparseArray<View> currentScrapViews;

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            //noinspection unchecked
            SparseArray<View>[] scrapViews = new SparseArray[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new SparseArray<>();
            }
            this.viewTypeCount = viewTypeCount;
            currentScrapViews = scrapViews[0];
            this.scrapViews = scrapViews;
        }

        protected boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        /**
         * @return A view from the ScrapViews collection. These are unordered.
         */
        View getScrapView(int position, int viewType) {
            if (viewTypeCount == 1) {
                return retrieveFromScrap(currentScrapViews, position);
            } else if (viewType >= 0 && viewType < scrapViews.length) {
                return retrieveFromScrap(scrapViews[viewType], position);
            }
            return null;
        }

        /**
         * Put a view into the ScrapViews list. These views are unordered.
         *
         * @param scrap The view to add
         */
        void addScrapView(View scrap, int position, int viewType) {
            if (viewTypeCount == 1) {
                currentScrapViews.put(position, scrap);
            } else {
                scrapViews[viewType].put(position, scrap);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                scrap.setAccessibilityDelegate(null);
            }
        }

        /**
         * Move all views remaining in activeViews to scrapViews.
         */
        void scrapActiveViews() {
            final View[] activeViews = this.activeViews;
            final int[] activeViewTypes = this.activeViewTypes;
            final boolean multipleScraps = viewTypeCount > 1;

            SparseArray<View> scrapViews = currentScrapViews;
            final int count = activeViews.length;
            for (int i = count - 1; i >= 0; i--) {
                final View victim = activeViews[i];
                if (victim != null) {
                    int whichScrap = activeViewTypes[i];

                    activeViews[i] = null;
                    activeViewTypes[i] = -1;

                    if (!shouldRecycleViewType(whichScrap)) {
                        continue;
                    }

                    if (multipleScraps) {
                        scrapViews = this.scrapViews[whichScrap];
                    }
                    scrapViews.put(i, victim);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        victim.setAccessibilityDelegate(null);
                    }
                }
            }

            pruneScrapViews();
        }

        /**
         * Makes sure that the size of scrapViews does not exceed the size of activeViews.
         * (This can happen if an adapter does not recycle its views).
         */
        private void pruneScrapViews() {
            final int maxViews = activeViews.length;
            final int viewTypeCount = this.viewTypeCount;
            final SparseArray<View>[] scrapViews = this.scrapViews;
            for (int i = 0; i < viewTypeCount; ++i) {
                final SparseArray<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                final int extras = size - maxViews;
                size--;
                for (int j = 0; j < extras; j++) {
                    scrapPile.remove(scrapPile.keyAt(size--));
                }
            }
        }

        static View retrieveFromScrap(SparseArray<View> scrapViews, int position) {
            int size = scrapViews.size();
            if (size > 0) {
                // See if we still have a view for this position.
                for (int i = 0; i < size; i++) {
                    int fromPosition = scrapViews.keyAt(i);
                    View view = scrapViews.get(fromPosition);
                    if (fromPosition == position) {
                        scrapViews.remove(fromPosition);
                        return view;
                    }
                }
                int index = size - 1;
                View r = scrapViews.valueAt(index);
                scrapViews.remove(scrapViews.keyAt(index));
                return r;
            } else {
                return null;
            }
        }
    }
}
