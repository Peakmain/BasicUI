<p align="center">
  <img src="https://upload-images.jianshu.io/upload_images/9387746-3866ee64845e0831.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240"  />
</p>

#### BasicUI
****
BasicUI是一些常用的Android UI组件和一些实用工具类封装，提高Android的开发效率

**使用文档链接：https://github.com/Peakmain/BasicUI/wiki**
#### How to
- Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:
```
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
 ```
- Step 2. Add the dependency
```
implementation 'com.github.Peakmain:BasicUI:+'
```
- Step 3.some probleam

  如果你的gradle版本比3.5.3高，可能会出现以下几个问题：

  1、Entry name 'AndroidManifest.xml' collided

  **解决办法：在gradle.properties添加以下代码**
  ```
  android.useNewApkCreator=false
  ```
  2、如果安装失败，用adb install安装报错提示如下

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
![RecyclerView实现下拉刷新和加载更多2](https://github.com/Peakmain/BasicUI/assets/26482737/2719c91c-78be-4cfa-a850-d1ebfc944952)
![RecyclerView实现下拉刷新和加载1](https://github.com/Peakmain/BasicUI/assets/26482737/84aa3b54-ec90-48f8-85a8-0c6ed79af5b9)
![RecyclerView实现多状态布局](https://github.com/Peakmain/BasicUI/assets/26482737/62186a30-4c9e-456e-9378-4ed21a22a28c)
![LinearlayoutManager悬浮](https://github.com/Peakmain/BasicUI/assets/26482737/4ae1237c-99ad-43c0-80d8-236ce96a0cc5)
![LinearlayoutManager分组](https://github.com/Peakmain/BasicUI/assets/26482737/546a3ab4-3084-4e96-b941-8b6331505453)
![ItemTouchHelp实现删除和土拖拽](https://github.com/Peakmain/BasicUI/assets/26482737/5db10a1f-7fb0-4224-836b-e8f27fb8cad8)
![GridLayoutManager悬浮](https://github.com/Peakmain/BasicUI/assets/26482737/cf0b7384-24dd-44a8-8912-2a301ee3c505)
![GridLayoutManager分组](https://github.com/Peakmain/BasicUI/assets/26482737/2c9e8cb6-9b3d-46f3-b20e-54f12b0fda58)

![多条目菜单删选](https://github.com/Peakmain/BasicUI/assets/26482737/56032239-f6f6-470e-a7ab-2cce2ace5bca)
![仿今日头条的TableLayout](https://github.com/Peakmain/BasicUI/assets/26482737/604c593f-a036-4b91-a118-eb99986a2781)
![加载loading](https://github.com/Peakmain/BasicUI/assets/26482737/e648337b-9198-4d82-ba55-b823af0502bd)
![View创建Bitmap](https://github.com/Peakmain/BasicUI/assets/26482737/cca525f2-f737-4198-8c39-130834739540)
![TextView的封装](https://github.com/Peakmain/BasicUI/assets/26482737/d83394c8-b029-45a0-bd0b-ba178b74048e)
![popwindow+flowlayout的使用](https://github.com/Peakmain/BasicUI/assets/26482737/5c1a18a9-50dd-47bf-8548-527a6b23b33a)
![OkHttp的封装](https://github.com/Peakmain/BasicUI/assets/26482737/590a29d1-b105-480c-b547-2a725c81fe3a)
![navigtionBar的使用](https://github.com/Peakmain/BasicUI/assets/26482737/a544257a-d262-4333-985f-8fe7de85222e)
![glide的使用](https://github.com/Peakmain/BasicUI/assets/26482737/5f83daff-4d0f-4950-bc62-91cd7ace4fb2)
![dialog的使用](https://github.com/Peakmain/BasicUI/assets/26482737/b9682234-df0f-4c1d-be5a-fa64f1bca974)
![自定义键盘](https://github.com/Peakmain/BasicUI/assets/26482737/a4f7ca55-3968-47d1-940d-60caff7c5a99)
![自带删除的EditText](https://github.com/Peakmain/BasicUI/assets/26482737/5f61b138-3358-458b-b641-75a28fd884f1)
![选择器的使用](https://github.com/Peakmain/BasicUI/assets/26482737/a3c16cb7-4ac7-42bd-bc77-aca7a46df932)
![我的](https://github.com/Peakmain/BasicUI/assets/26482737/d8eb2e85-a3b6-47b5-8e84-a55be4e13532)
![文件选择](https://github.com/Peakmain/BasicUI/assets/26482737/bf7fc317-71f2-4301-ae60-9d19130277e7)
![文本高亮的使用](https://github.com/Peakmain/BasicUI/assets/26482737/a1e15fe8-655d-4528-8055-b4060a2930ac)
![图片压缩](https://github.com/Peakmain/BasicUI/assets/26482737/e772a8f8-7fbf-4cb2-8f38-e2bff792df2f)
![图片选择](https://github.com/Peakmain/BasicUI/assets/26482737/e200fade-4e43-4283-8b5d-c925cba65c17)
![首页](https://github.com/Peakmain/BasicUI/assets/26482737/c85b2679-8ef3-4269-a22c-79626669c758)


#### 关于我
- 简书： [https://www.jianshu.com/u/3ff32f5aea98](https://www.jianshu.com/u/3ff32f5aea98)
- 我的GitHub地址：[https://github.com/Peakmain](https://github.com/Peakmain)

#### Donations
如果您觉得我的开源库帮您节省了大量的开发时间，请扫描下方的二维码随意打赏，您的支持将激励我不断前进
![微信](https://user-images.githubusercontent.com/26482737/184805287-0561a7e2-da13-4ef4-b367-c5e8672c121d.jpg)
![支付宝](https://user-images.githubusercontent.com/26482737/184805306-f44511a7-7660-4fe1-9f07-305005576c2c.jpg)
````
