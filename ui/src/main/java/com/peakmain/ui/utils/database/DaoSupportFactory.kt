package com.peakmain.ui.utils.database

import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.peakmain.ui.utils.LogUtils
import java.io.File

class DaoSupportFactory private constructor() {
    private var mFile: File? = null
    private val mSqliteDatabase: SQLiteDatabase
    fun <T> getDao(clazz: Class<T?>?): IDaoSupport<T?> {
        val daoSupport: IDaoSupport<T?> = DaoSupport()
        daoSupport.init(mSqliteDatabase, clazz)
        return daoSupport
    }

    /**
     * 设置保存的文件目录
     *
     * @param file 默认是/BaseUI/database
     */
    fun setFileDir(file: File?) {
        mFile = file
    }

    /**
     * 设置保存的文件目录
     *
     * @param pathName 保存的目录，默认是/BaseUI/database
     */
    fun setFileDir(pathName: String?) {
        mFile = File(pathName)
    }

    companion object {
        @Volatile
        private var mFactory: DaoSupportFactory? = null
        @JvmStatic
        val instance: DaoSupportFactory?
            get() {
                if (mFactory == null) {
                    synchronized(DaoSupportFactory::class.java) {
                        if (mFactory == null) {
                            mFactory = DaoSupportFactory()
                        }
                    }
                }
                return mFactory
            }
    }

    init {
        if (mFile == null) mFile = File(Environment.getExternalStorageDirectory().absolutePath + File.separator
                + "BaseUI" + File.separator + "database")
        LogUtils.e(mFile!!.absolutePath)
        if (!mFile!!.exists()) {
            mFile!!.mkdirs()
        }
        val dbFile = File(mFile, "baseUI.db")
        mSqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFile, null)
    }
}