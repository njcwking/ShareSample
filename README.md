## ShareSample

### 介绍
该项目用于学习使用，学习利用Android系统原生的API方法实现分享文字、图片、文件等功能，其中`Share`类为分享的工具类，通过调用`share`方法实现分享功能。
博客地址：[Android调用系统原生分享](http://knowledge.njcwking.cn/2018/10/29/android-system-share/)

### 截图
![截图](./resources/screenshots/截图.jpg)

### 使用
#### 分享文字
```java
new ShareBuilder()
    .setText("文字内容")
    .setChooserTitle("选择框标题")
    .setShareType(ShareBuilder.SHARE_TEXT)
    .build()
    .share(mActivity);
```
#### 分享单个文件
```java
Uri fileUri = UriUtils.getUriFromFile(this,"/sdcard/DCIM/Camera/IMG_20181126_012932.jpg");
new ShareBuilder()
    .setChooserTitle("选择框标题")
    .setShareType(ShareBuilder.SHARE_FILE)
    .setShareFiles(Arrays.asList(fileUri))
    .build()
    .share(this);
```

#### 分享多个文件
```java
Uri fileUri = UriUtils.getUriFromFile(this,"/sdcard/DCIM/Camera/IMG_20181126_012933.jpg");
Uri fileUri2 = UriUtils.getUriFromFile(this,"/sdcard/DCIM/Camera/IMG_20181126_012934.jpg");
ArrayList<Uri> uris = new ArrayList<>();
uris.add(fileUri);
uris.add(fileUri2);
new ShareBuilder()
    .setChooserTitle("选择框标题")
    .setShareType(ShareBuilder.SHARE_MULTIPLE_FILES)
    .setShareFiles(uris)
    .build()
    .share(this);
```

### 注意事项
1. 运行项目时分享图片如遇崩溃请替换本地已有的图片路径；
2. 项目界面比较简陋，功能基本实现；
3. `Share`类自动通过后缀名识别mimeType,无需用户指定；