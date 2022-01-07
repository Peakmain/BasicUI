package com.peakmain.ui.utils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.ExifInterface
import android.os.AsyncTask
import android.os.Environment
import android.text.TextUtils
import com.peakmain.ui.image.entry.PictureFileInfo
import com.peakmain.ui.utils.FileTypeUtil.getFileInfosFromFileArray
import java.io.*
import java.text.DecimalFormat
import java.util.*

/**
 * author ：Peakmain
 * createTime：2020/3/28
 * mail:2726449200@qq.com
 * describe：文件操作
 */
object FileUtils {
    private const val JPEG_FILE_PREFIX = "IMG_"
    private const val JPEG_FILE_SUFFIX = ".jpg"
    private const val EXTERNAL_STORAGE_PERMISSION =
            "android.permission.WRITE_EXTERNAL_STORAGE"
    private const val UI_CACHE_FOLDER_NAME = "UICache"
    const val VIDEO_PATH = "/video/"

    private fun getESD() =
            Environment.getExternalStorageDirectory().absolutePath + File.separator + UI_CACHE_FOLDER_NAME


    fun getVideoBasePath(): String = getESD() + VIDEO_PATH

    var getImageFolderPath: String = getESD() + File.separator + "image" + File.separator

    private val mDownloadFolderPath: String =
            getESD() + File.separator + "download" + File.separator

    fun getDownloadFolderPath(): String {
        return mDownloadFolderPath
    }

    fun copyDir(srcDirPath: String?,
                destDirPath: String?): Boolean {
        return copyDir(getFileByPath(srcDirPath), getFileByPath(destDirPath))
    }

    @JvmStatic
    fun getFileByPath(filePath: String?): File? {
        return if (isSpace(filePath)) null else File(filePath)
    }

    private fun isSpace(s: String?): Boolean {
        if (s == null) return true
        var i = 0
        val len = s.length
        while (i < len) {
            if (!Character.isWhitespace(s[i])) {
                return false
            }
            ++i
        }
        return true
    }

    private fun copyDir(srcDir: File?,
                        destDir: File?): Boolean {
        return copyOrMoveDir(srcDir, destDir, false)
    }

    private fun copyOrMoveDir(srcDir: File?,
                              destDir: File?,
                              isMove: Boolean): Boolean {
        return copyOrMoveDir(srcDir, destDir, object : OnReplaceListener {
            override fun onReplace(): Boolean {
                return true
            }
        }, isMove)
    }

    private fun copyOrMoveDir(srcDir: File?,
                              destDir: File?,
                              listener: OnReplaceListener?,
                              isMove: Boolean): Boolean {
        if (srcDir == null || destDir == null) return false
        // destDir's path locate in srcDir's path then return false
        val srcPath = srcDir.path + File.separator
        val destPath = destDir.path + File.separator
        if (destPath.contains(srcPath)) return false
        if (!srcDir.exists() || !srcDir.isDirectory) return false
        if (destDir.exists()) {
            if (listener == null || listener.onReplace()) { // require delete the old directory
                if (!deleteAllInDir(destDir)) { // unsuccessfully delete then return false
                    return false
                }
            } else {
                return true
            }
        }
        if (!createOrExistsDir(destDir)) return false
        val files = srcDir.listFiles()
        for (file in files) {
            val oneDestFile = File(destPath + file.name)
            if (file.isFile) {
                if (!copyOrMoveFile(file, oneDestFile, listener, isMove)) return false
            } else if (file.isDirectory) {
                if (!copyOrMoveDir(file, oneDestFile, listener, isMove)) return false
            }
        }
        return !isMove || deleteDir(srcDir)
    }

    private fun copyOrMoveFile(srcFile: File,
                               destFile: File,
                               isMove: Boolean): Boolean {
        return copyOrMoveFile(srcFile, destFile, object : OnReplaceListener {
            override fun onReplace(): Boolean {
                return true
            }
        }, isMove)
    }

    private fun copyOrMoveFile(srcFile: File?,
                               destFile: File?,
                               listener: OnReplaceListener?,
                               isMove: Boolean): Boolean {
        if (srcFile == null || destFile == null) return false
        // srcFile equals destFile then return false
        if (srcFile == destFile) return false
        // srcFile doesn't exist or isn't a file then return false
        if (!srcFile.exists() || !srcFile.isFile) return false
        if (destFile.exists()) {
            if (listener == null || listener.onReplace()) { // require delete the old file
                if (!destFile.delete()) { // unsuccessfully delete then return false
                    return false
                }
            } else {
                return true
            }
        }
        return if (!createOrExistsDir(destFile.parentFile)) false else try {
            (writeFileFromIS(destFile, FileInputStream(srcFile))
                    && !(isMove && !deleteFile(srcFile)))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    fun writeFileFromIS(file: File?,
                        `is`: InputStream): Boolean {
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(FileOutputStream(file))
            val data = ByteArray(8192)
            var len: Int
            while (`is`.read(data, 0, 8192).also { len = it } != -1) {
                os.write(data, 0, len)
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun createOrExistsDir(dirPath: String?): Boolean {
        return createOrExistsDir(getFileByPath(dirPath))
    }

    fun createOrExistsDir(file: File?): Boolean {
        return file != null && if (file.exists()) file.isDirectory else file.mkdirs()
    }

    fun deleteAllInDir(dir: File?): Boolean {
        return deleteFilesInDirWithFilter(dir, FileFilter { true })
    }

    fun deleteFilesInDirWithFilter(dir: File?, filter: FileFilter): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (filter.accept(file)) {
                    if (file.isFile) {
                        if (!file.delete()) return false
                    } else if (file.isDirectory) {
                        if (!deleteDir(file)) return false
                    }
                }
            }
        }
        return true
    }

    fun deleteDir(dir: File?): Boolean {
        if (dir == null) return false
        // dir doesn't exist then return true
        if (!dir.exists()) return true
        // dir isn't a directory then return false
        if (!dir.isDirectory) return false
        val files = dir.listFiles()
        if (files != null && files.size != 0) {
            for (file in files) {
                if (file.isFile) {
                    if (!file.delete()) return false
                } else if (file.isDirectory) {
                    if (!deleteDir(file)) return false
                }
            }
        }
        return dir.delete()
    }

    fun deleteFile(file: File?): Boolean {
        return file != null && (!file.exists() || file.isFile && file.delete())
    }

    /**
     * 判断目录是否存在 不存在在判断是否创建成功过
     *
     * @param file 文件
     */
    fun mkdirs(file: File?): Boolean {
        //如果存在是目录就返回true是文件则返回false 不存在则返回是否创建成功
        return if (file != null && file.exists()) file.isDirectory else file!!.mkdirs()
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(filePath: String?): Boolean {
        return isFileExists(getFileByPath(filePath))
    }

    /**
     * 判断文件是否存在
     *
     * @param file 文件
     * @return `true`: 存在<br></br>`false`: 不存在
     */
    fun isFileExists(file: File?): Boolean {
        return file != null && file.exists()
    }

    /**
     * 获取指定文件大小
     *
     * @param file file
     * @return 文件的大小
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getFileSize(file: File): Long {
        var size: Long = 0
        if (file.exists()) {
            var fis: FileInputStream? = null
            fis = FileInputStream(file)
            size = fis.available().toLong()
        }
        return size
    }

    /**
     * 转换文件大小
     *
     * @param fileSize 文件的大小
     * @return 文件大小的字符串
     */
    fun FormetFileSize(fileSize: Long): String {
        val df = DecimalFormat("#.0")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileSize == 0L) {
            return wrongSize
        }
        fileSizeString = if (fileSize < 1024) {
            df.format(fileSize.toDouble()) + "B"
        } else if (fileSize < 1048576) {
            df.format(fileSize.toDouble() / 1024) + "KB"
        } else if (fileSize < 1073741824) {
            df.format(fileSize.toDouble() / 1048576) + "MB"
        } else {
            df.format(fileSize.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }

    /**
     * 判断bitmap对象是否为空
     *
     * @param src 源图片
     * @return `true`: 是<br></br>`false`: 否
     */
    private fun isEmptyBitmap(src: Bitmap?): Boolean {
        return src == null || src.width == 0 || src.height == 0
    }

    /**
     * 保存Bitmap至文件夹
     *
     * @param bitmap bitmap
     * @param file file
     * @return 保存至文件夹
     */
    fun saveBitmap(bitmap: Bitmap, file: File?): Boolean {
        if (isEmptyBitmap(bitmap)) {
            return false
        }
        var os: OutputStream? = null
        var ret = false
        try {
            os = BufferedOutputStream(FileOutputStream(file))
            ret = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            if (!bitmap.isRecycled) {
                bitmap.recycle()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            closeIO(os)
        }
        return ret
    }

    /**
     * inputStream转outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    fun input2OutputStream(`is`: InputStream?): ByteArrayOutputStream? {
        return if (`is` == null) null else try {
            val os = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var len: Int
            while (`is`.read(b, 0, 1024).also { len = it } != -1) {
                os.write(b, 0, len)
            }
            os
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            closeIO(`is`)
        }
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    fun readPictureDegree(path: String?): Int {
        var degree = 0
        try {
            val exifInterface = ExifInterface(path)
            val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> degree = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> degree = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> degree = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return degree
    }

    @Throws(IOException::class)
    fun toByteArray(input: InputStream): ByteArray {
        val output = ByteArrayOutputStream()
        write(input, output)
        output.close()
        return output.toByteArray()
    }

    @Throws(IOException::class)
    fun write(inputStream: InputStream, outputStream: OutputStream) {
        var len: Int
        val buffer = ByteArray(4096)
        while (inputStream.read(buffer).also { len = it } != -1) outputStream.write(buffer, 0, len)
    }

    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    @JvmStatic
    fun closeIO(vararg closeables: Closeable?) {
        if (closeables == null) return
        try {
            for (closeable in closeables) {
                closeable?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    interface OnReplaceListener {
        fun onReplace(): Boolean
    }

    @Throws(IOException::class)
    @JvmStatic
    fun createTmpFile(context: Context): File {
        var dir: File? = null
        if (TextUtils.equals(
                        Environment.getExternalStorageState(),
                        Environment.MEDIA_MOUNTED
                )
        ) {
            // 获取手机的图片路径
            dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            // 如果不存在
            if (!dir.exists()) {
                dir.createNewFile()
            }
            if (dir.exists()) {
                dir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM + "/Camera")
                if (!dir.exists()) {
                    dir.createNewFile()
                }
            }
            if (!dir.exists()) {
                dir = getCacheDirectory(context)
            }
        } else {
            dir = getCacheDirectory(context)
        }
        return File(
                dir,
                JPEG_FILE_PREFIX + System.currentTimeMillis() + JPEG_FILE_SUFFIX
        )
    }

    fun getCacheDirectory(context: Context): File {
        return getCacheDirectory(context, true)
    }

    private fun getExternalCacheDir(context: Context): File? {
        val dataDir = File(
                File(
                        Environment.getExternalStorageDirectory(),
                        "Android"
                ), "data"
        )
        val appCacheDir =
                File(File(dataDir, context.packageName), "cache")
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null
            }
            try {
                File(appCacheDir, ".nomedia").createNewFile()
            } catch (e: IOException) {
            }
        }
        return appCacheDir
    }

    private fun hasExternalStoragePermission(context: Context): Boolean {
        val perm =
                context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION)
        return perm == PackageManager.PERMISSION_GRANTED
    }

    fun getCacheDirectory(
            context: Context,
            preferExternal: Boolean
    ): File {
        var appCacheDir: File? = null
        val externalStorageState: String = try {
            Environment.getExternalStorageState()
        } catch (e: NullPointerException) { // (sh)it happens (Issue #660)
            ""
        } catch (e: IncompatibleClassChangeError) { // (sh)it happens too (Issue #989)
            ""
        }
        if (preferExternal && Environment.MEDIA_MOUNTED == externalStorageState && hasExternalStoragePermission(
                        context
                )
        ) {
            appCacheDir = getExternalCacheDir(context)
        }
        if (appCacheDir == null) {
            appCacheDir = context.cacheDir
        }
        if (appCacheDir == null) {
            val cacheDirPath = "/data/data/" + context.packageName + "/cache/"
            appCacheDir = File(cacheDirPath)
        }
        return appCacheDir
    }

    fun loadFileList(path: String, callback: LoadFileTask.FileCallback?) {
        getAllFileList(getFileByPath(path), callback)
    }

    private fun getAllFileList(path: File?, callback: LoadFileTask.FileCallback?) {
        LoadFileTask().getAllFileList(path, callback)
    }

    /***
     * 获取文件类型
     */
    fun getFileType(paramString: String): String? {
        var str: String? = ""
        if (TextUtils.isEmpty(paramString)) {
            return str
        }
        val i = paramString.lastIndexOf('.')
        if (i <= -1) {
            return str
        }
        str = paramString.substring(i + 1)
        return str
    }

}

class LoadFileTask :
        AsyncTask<File?, Void?, List<PictureFileInfo>> {
    private var mLoadFileTask: LoadFileTask? = null
    private var mCallback: FileCallback? = null

    /**
     * 根据文件路径查找所有文件
     */
    fun getAllFileList(
            path: File?,
            callback: FileCallback?
    ) {
        mLoadFileTask = LoadFileTask(callback)
        mLoadFileTask!!.executeOnExecutor(THREAD_POOL_EXECUTOR, path)
    }

    override fun onPreExecute() {
        if (mCallback != null) {
            mCallback!!.showLoading("")
        }
        super.onPreExecute()
    }

    /**
     * 执行文件列表查找
     * @param files
     * @return
     */
    protected override fun doInBackground(vararg files: File?): List<PictureFileInfo> {
        return try {
            val fileInfoList: List<PictureFileInfo>
            val mFiles =
                    files[0]?.listFiles(FileTypeUtil.ALL_FOLDER_AND_FILES_FILTER)
            fileInfoList = getFileInfosFromFileArray(mFiles)
            if (fileInfoList == null) {
                ArrayList()
            } else if (isCancelled) {
                ArrayList()
            } else {
                Collections.sort(fileInfoList, FileTypeUtil.FileNameComparator())
                if (mCallback != null) {
                    mCallback!!.hideLoading()
                }
                fileInfoList
            }
        } catch (e: Exception) {
            if (mCallback != null) {
                mCallback!!.hideLoading()
            }
            ArrayList()
        }
    }

    /**
     * 查找完毕
     * @param fileInfos
     */
    override fun onPostExecute(fileInfos: List<PictureFileInfo>) {
        super.onPostExecute(fileInfos)
        mLoadFileTask = null
        if (mCallback != null) {
            mCallback!!.onGetFileList(fileInfos)
        }
    }

    override fun onCancelled() {
        mLoadFileTask = null
        if (mCallback != null) {
            mCallback!!.hideLoading()
        }
        super.onCancelled()
    }

    interface FileCallback {
        fun onGetFileList(mFileInfoList: List<PictureFileInfo>?)

        /**
         * 显示loading
         */
        fun showLoading(message: String?)

        /**
         * 隐藏loading
         */
        fun hideLoading()
    }

    constructor(callback: FileCallback?) {
        mCallback = callback
    }

    constructor()
}