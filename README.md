Appluck Android 插件集成说明
=========



使用要求
--------
minSdk 21(android5)以上

## 1.下载AppluckSDK
 [AppLuckSDK.aar][alup]

## 2. 引入SDK
1. 在 Android Studio 里的 Project Navigator， 选择 Project，你将在 Android Studio project 里的 app 模块下看到 libs 文件夹 。
2. 你需要将 AppLuckSDK.aar 文件添加到 libs 文件夹中。
3. 在你的 Android Studio 项目的 app 模块，选择 build.gradle 文件，确保将以下添加到 dependencies block 中：
```groovy
dependencies {
  implementation fileTree(dir: 'libs', include: ['*.jar', '*.aar'])
  implementation files('libs/tenjin.aar')
}
```

## 3. 配置依赖
可通过以下两种方式引入，任选其一

* 通过gradle引入

  ```groovy
  implementation 'androidx.appcompat:appcompat:1.3.0'
  implementation 'com.google.android.material:material:1.4.0'
  //常用工具
  implementation 'org.apache.commons:commons-lang3:3.3.2'
  //http请求
  implementation 'org.jsoup:jsoup:1.11.2'
  //json处理
  implementation 'com.alibaba:fastjson:1.1.72.android'
  //图片加载缓存
  implementation 'com.github.bumptech.glide:glide:4.14.2'
  //gaid
  implementation 'com.google.android.gms:play-services-ads-identifier:18.0.1'
  ```

 * 手动引入

   * 将libs.zip解压，内容全部放入 Assets/Plugins/Android

启用Jetifier和AndroidX,在gradleTemplate.properties中添加以下内容:
  ```
  android.useAndroidX=true
  android.enableJetifier=true 
  ```

## 4. 开始集成

### 4.1 初始化SDK

4.1.1 填充初始化成功回调.

  ```c#
AppLuckEvents.onInitSuccessEvent += () =>{
  //Appluck SDK 初始化成功
  //可以开始设置广告位入口
}
  ```

4.1.2 初始化

  ```c#
//placementId - 广告位ID 插件会自动对该位置做预加载，如产品中有多个广告位建议传入最重要即预期曝光最多的广告位ID。生产环境的placementId请与运营人员联系获取。
AppLuck.instance.init(placementId);
  ```

### 4.2 设置广告位入口

Appluck支持两种方式的广告位入口

- 使用封装好的方法加载入口(建议使用)
  - 只需传入入口的宽高及位置即可展示，Appluck将会对素材做系统推荐并根据点击率实时优化
- 自行设置入口(适合对入口有特殊要求，或在某些场景希望直接打开互动广告的需求)
  - 希望打开互动广告时，调用我们提供的方法打开Appluck的活动页面。

#### 4.2.1 使用Appluck封装的入口

1. 填充placement加载成功回调

  ```c#
//loadedPlacementId - 加载成功的placementId
AppLuckEvents.onPlacementLoadSuccessEvent += (loadedPlacementId) =>{
  //placement 加载成功，showPlacement素材
  if (loadedPlacementId == placementId)
  {
      //将placement入口素材显示在指定坐标
      AppLuck.instance.showInteractiveEntrance(loadedPlacementId, Screen.height - 800, Screen.width - 600);
  }
}
  ```

2. 加载placement素材

  ```c#
//placementId - 广告位id
//creative type - 素材类型，当前仅支持 icon
//width - 入口位置的素材宽度
//height - 入口位置的素材高度
AppLuck.instance.loadPlacement(placementId, "icon", 200, 200);
  ```



#### 4.2.2 自行设置入口

- 直接打开互动广告的场景请直接调用

```c#
AppLuck.instance.openInteractiveAds(请传入placementId);
```

- 自行设置入口，等待Appluck预加载完成再展示

```c#
//游戏初始化时默认隐藏入口素材游戏对象placement
placement.gameObject.SetActive(false);

//placement绑定点击事件
placement.onClick.AddListener(() =>
{
    //唤起webview并加载活动，请传入placementId
    AppLuck.instance.openInteractiveAds(请传入placementId);
});

//在SDK初始化成功的回调中显示placement
AppLuckEvents.onInitSuccessEvent += () =>{
    placement.gameObject.SetActive(true);
}
```

### 4.3 其他事件
```c#
//用户互动回调 - 此事件非全量开发，若有需求请提前与Appluck对接人员沟通
//interaction 
//	INTERACTIVE_PLAY 活动参与
//	INTERACTIVE_CLICK 广告点击
AppLuckEvents.onUserInteractionEvent += (placementId, interaction) =>{
	toast(placementId + "  " + interaction);
};
```

[alup]: https://github.com/jxsong1989/appluck_intergration_guide_sdk_android/releases/tag/v1.0.1
