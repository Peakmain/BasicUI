<p align="center">
  <img src="https://upload-images.jianshu.io/upload_images/9387746-3866ee64845e0831.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"  />
</p>

#### BasicUI
****
BasicUI是一些常用的Android UI组件和一些实用工具类封装，提高Android的开发效率
#### How to

- Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
- Step 2. Add the dependency
```
implementation 'com.github.Peakmain:BasicUI:1.1.9'
```
- Step 3.some probleam
如果你的gradle版本比3.5.3高，可能会出现以下几个问题：
  - Entry name 'AndroidManifest.xml' collided
  
  **解决办法：在gradle.properties添加以下代码**
  ```
  android.useNewApkCreator=false
  ```
  - 如果安装失败，用adb install安装报错提示如下
  failed to install app-debug.apk: Failure [INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION: Failed to parse /data/app/vmdl1335956833.tmp/base.apk: Corrupt XML binary file]
 
 **解决办法:在添加依赖的build.gradle中添加以下代码**
 ```
 android{
     packagingOptions {
        exclude 'AndroidManifest.xml'
    }
 }
 ```
 
##### Screenshot
![首页.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/首页.jpg)&emsp;![dialog的使用.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/dialog的使用.jpg)&emsp;![navigtionBar的使用.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/navigtionBar的使用.jpg)&emsp;![popwindow+flowlayout的使用.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/popwindow+flowlayout的使用.jpg)&emsp;
![自带删除的EditText.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/自带删除的EditText.jpg)&emsp;![TextView的封装.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/TextView的封装.jpg)&emsp;![仿今日头条的TableLayout.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/仿今日头条的TableLayout.gif)&emsp;![加载loading.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/加载loading.gif)&emsp;
![多条目菜单删选.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/多条目菜单删选.gif)&emsp;![自定义键盘.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/自定义键盘.jpg)&emsp;![LinearlayoutManager分组.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/LinearlayoutManager分组.jpg)&emsp;![LinearlayoutManager悬浮.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/LinearlayoutManager悬浮.gif)&emsp;
![GridLayoutManager分组.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/GridLayoutManager分组.jpg)&emsp;![GridLayoutManager悬浮.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/GridLayoutManager悬浮.gif)&emsp;![ItemTouchHelp实现删除和土拖拽.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/ItemTouchHelp实现删除和土拖拽.gif)&emsp;![RecyclerView实现下拉刷新和加载1.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/RecyclerView实现下拉刷新和加载1.gif)&emsp;
![RecyclerView实现下拉刷新和加载更多2.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/RecyclerView实现下拉刷新和加载更多2.gif)&emsp;![RecyclerView实现多状态布局.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/RecyclerView实现多状态布局.gif)&emsp;![图片压缩.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/图片压缩.gif)&emsp;![图片选择.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/图片选择.gif)&emsp;
![文件选择.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/文件选择.gif)&emsp;![选择器的使用.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/选择器的使用.gif)&emsp;![glide的使用.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/glide的使用.gif)&emsp;![View创建Bitmap.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/View创建Bitmap.jpg)&emsp;
![OkHttp的封装.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/OkHttp的封装.jpg)&emsp;![文本高亮的使用.gif](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/文本高亮的使用.gif)&emsp;![我的.jpg](https://github.com/Peakmain/BasicUI/blob/androidx/img-preview/我的.jpg)&emsp;

 #### BasicUI Demo APP 安装包下载
 ****
 - 蒲公英下载地址
 
 ![演示demo](https://www.pgyer.com/app/qrcode/BasicUI)
 
 - Github下载地址：
 
 ![image.png](https://upload-images.jianshu.io/upload_images/9387746-0a833896a68b7bc6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 关于我
- 简书： [https://www.jianshu.com/u/3ff32f5aea98](https://www.jianshu.com/u/3ff32f5aea98)
- 我的GitHub地址：[https://github.com/Peakmain](https://github.com/Peakmain)
