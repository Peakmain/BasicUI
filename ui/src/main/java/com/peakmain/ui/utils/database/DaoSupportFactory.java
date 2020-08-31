package com.peakmain.ui.utils.database;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.peakmain.ui.utils.LogUtils;

import java.io.File;

public class DaoSupportFactory {
    private static volatile DaoSupportFactory mFactory;
    private File mFile;

    public static DaoSupportFactory getInstance() {
        if (mFactory == null) {
            synchronized (DaoSupportFactory.class) {
                if (mFactory == null) {
                    mFactory = new DaoSupportFactory();
                }
            }
        }
        return mFactory;
    }

    private SQLiteDatabase mSqliteDatabase;

    private DaoSupportFactory() {
        if (mFile == null)
            mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
                    + "BaseUI" + File.separator + "database");
        LogUtils.e(mFile.getAbsolutePath());
        if (!mFile.exists()) {
            mFile.mkdirs();
        }
        File dbFile = new File(mFile, "baseUI.db");
        mSqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
    }

    public <T> IDaoSupport<T> getDao(Class<T> clazz) {
        IDaoSupport<T> daoSupport = new DaoSupport();
        daoSupport.init(mSqliteDatabase, clazz);
        return daoSupport;
    }

    /**
     * 设置保存的文件目录
     *
     * @param file 默认是/BaseUI/database
     */
    public void setFileDir(File file) {
        this.mFile = file;
    }

    /**
     * 设置保存的文件目录
     *
     * @param pathName 保存的目录，默认是/BaseUI/database
     */
    public void setFileDir(String pathName) {
        this.mFile = new File(pathName);
    }
}
