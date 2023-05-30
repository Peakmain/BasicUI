package com.peakmain.ui.utils.log;


import android.app.Activity;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author ：Peakmain
 * createTime：2021/3/14
 * mail:2726449200@qq.com
 * describe：
 */
public class LogManager {
    private LogConfig mConfig;
    private static volatile LogManager instance;
    private List<LogPrinter> printers = new ArrayList<>();
    private ViewPrinter mViewPrinter;

    public static LogManager getInstance() {
        if (instance == null) {
            synchronized (LogManager.class) {
                if (instance == null) {
                    instance = new LogManager(new LogConfig.DefalutConfig());
                }
            }
        }
        return instance;
    }

    private LogManager(LogConfig config) {
        this.mConfig = config;
    }

    private LogManager(LogConfig config, LogPrinter[] printers) {
        this.mConfig = config;
        this.printers.addAll(Arrays.asList(printers));
    }

    public static void init(@NonNull LogConfig config, LogPrinter... printers) {
        instance = new LogManager(config, printers);
    }

    public LogConfig getConfig() {
        return mConfig;
    }

    public List<LogPrinter> getPrinters() {
        return printers;
    }

    public void addPrinter(LogPrinter printer) {
        printers.add(printer);
    }

    public void removePrinter(LogPrinter printer) {
        if (printers != null) {
            printers.remove(printer);
        }
    }

    public boolean showConsolePrint() {
        return true;
    }

    public void showPrintView(Activity activity) {
        mViewPrinter = new ViewPrinter(activity);
        mViewPrinter.getViewProvider().showFloatingView();
        addPrinter(mViewPrinter);
    }
}
