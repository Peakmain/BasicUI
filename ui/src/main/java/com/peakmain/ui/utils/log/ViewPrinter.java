package com.peakmain.ui.utils.log;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.peakmain.ui.R;
import com.peakmain.ui.recyclerview.adapter.CommonRecyclerAdapter;
import com.peakmain.ui.recyclerview.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：将log显示在界面上
 */
public class ViewPrinter implements LogPrinter {
    private RecyclerView mRecyclerView;
    private LogAdapter mAdapter;
    private List<LogRecord> mLogRecords = new ArrayList<>();
    private ViewPrinterProvider mViewProvider;

    public ViewPrinter(Activity activity) {
        FrameLayout rootView = activity.findViewById(android.R.id.content);
        mRecyclerView = new RecyclerView(activity);
        mAdapter = new LogAdapter(activity, mLogRecords);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        //添加到frameLayout
        mViewProvider=new ViewPrinterProvider(rootView,mRecyclerView);
    }

    public ViewPrinterProvider getViewProvider() {
        return mViewProvider;
    }

    @Override
    public void print(@NonNull LogConfig config, int level, String tag, @NonNull String printString) {
        mLogRecords.add(new LogRecord(System.currentTimeMillis(), level, tag, printString));
        mAdapter.setData(mLogRecords);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
    }

    static class LogAdapter extends CommonRecyclerAdapter<LogRecord> {
        public LogAdapter(Context context, List<LogRecord> data) {
            super(context, data, R.layout.log_item);
        }

        /**
         * 跟进log级别获取不同的高了颜色
         *
         * @param level log 级别
         * @return 高亮的颜色
         */
        private int getHighlightColor(int level) {
            int highlight;
            switch (level) {
                case PLog.V:
                    highlight = 0xffbbbbbb;
                    break;
                case PLog.D:
                    highlight = 0xffffffff;
                    break;
                case PLog.I:
                    highlight = 0xff6a8759;
                    break;
                case PLog.W:
                    highlight = 0xffbbb529;
                    break;
                case PLog.E:
                    highlight = 0xffff6b68;
                    break;
                default:
                    highlight = 0xffffff00;
                    break;
            }
            return highlight;
        }

        @Override
        public void convert(ViewHolder holder, LogRecord item) {
            int color = getHighlightColor(item.level);
            holder.setTextColor(R.id.tv_log_tag, color);
            holder.setTextColor(R.id.tv_log_message, color);
            holder.setText(R.id.tv_log_message, item.message);
            holder.setText(R.id.tv_log_tag, item.flattenedLog());
        }
    }
}
