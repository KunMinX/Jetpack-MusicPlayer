&nbsp;

## 由来

Jetpack-MusicPlayer 是一款基于 Jetpack MVVM 架构音乐播放控制组件，它是因 [“Jetpack-MVVM-Best-Practice”](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice) 这项目需求而存在。

最初寻遍 GitHub 也未找到合适开源库，于是另起炉灶，自己编写 **高度解耦、轻松配置、可通过 Maven 仓库远程依赖** 的真正第三方库。

Jetpack-MusicPlayer 使用十分简单，依托于 设计模式原则 及 Java 泛型特性，使用者无需知道内部实现细节，**仅通过继承** Album、Music、Artist **基类 即可完成 “业务实体类” 定制和扩展**。

此外，在不设置自定义配置情况下，Jetpack-MusicPlayer 最少只需 **一行代码即可运行起来**。

&nbsp;

|                          PureMusic                           |                                   Dispatcher                                   |                       PlayMode Switch                        |
| :----------------------------------------------------------: |:------------------------------------------------------------------------------:| :----------------------------------------------------------: |
| ![](https://upload-images.jianshu.io/upload_images/57036-eeaa9ea7399d90d5.gif) | ![](https://upload-images.jianshu.io/upload_images/57036-a9b1831b428993b0.gif) | ![](https://upload-images.jianshu.io/upload_images/57036-466fe782f7170a44.gif) |


&nbsp;

## 目标

Jetpack-MusicPlayer 目标是：**一行代码即可接入 音乐播放控制组件**。

除一键接入省去 99% 繁杂重复工作外，你还可从该项目获得内容包括：

- 整洁代码风格 和 标准资源命名规范。
- 遵循 "响应式编程" 开发模式的 Dispatcher 使用， 
- 优秀代码分层和封装思想，在不做任何个性化配置情况下，一行代码即可接入。
- 主体工程基于前沿软件工程安全 JetPack MVVM 架构。 
- AndroidX 和 Material Design 2 全面使用。
- ConstraintLayout 约束布局最佳实践。
- 绝不使用 Dagger，绝不使用奇技淫巧、编写艰深晦涩代码。

&nbsp;

如你正在思考 [**如何为项目挑选合适的架构**](https://juejin.cn/post/7106042518457810952)，这项目值得你参考！

&nbsp;

&nbsp;

### 简单使用：

1.在 build.gradle 中添加依赖。

```groovy
implementation 'com.kunminx.player:player:4.1.0'
```

提示：本库托管于 Maven Central，请自行在根目录 build.gradle 添加 `mavenCentral()`。

2.依据默认专辑实体类 `DefaultAlbum` 结构准备一串数据。此处以 JSON 为例：

```java
// DefaultAlbum 包含 DefaultMusic 和 DefaultArtist 两个子类：
// 三者的字段可详见 BaseAlbumItem、BaseMusicItem 和 BaseArtistItem。
```

```java
{
  "albumId": "JAY_008",
  "title": "依然范特西",
  "summary": "Still Fantasy",
  "artist": {
    "name": "Jay"
  },
  "coverImg": "https://...57036-570ed96eb055ef17.png",
  "musics": [
    {
      "musicId": "JAY_008_001",
      "title": "本草纲目",
      "artist": {
        "name": "Jay"
      },
      "coverImg": "https://.../57036-570ed96eb055ef17.png",
      "url": "bencaogangmu.mp3"
    },
    ...
  ]
}
```

3.在 Application 中初始化 多媒体播放控制组件。

```java
DefaultPlayerManager.getInstance().init(this);
```

4.在得到数据后，最少只需一行代码 即可完成数据的装载。

```java
//一行代码完成数据的初始化。
DefaultPlayerManager.getInstance().loadAlbum(album);
```

&nbsp;

**温馨提示：**

1.实际开发中，项目数据或与本库数据 “结构上存在差异”，故通常做法是，从后端拿到和解析项目 JSON 数据，并对该数据进行遍历。在遍历过程中，实例化并装载 “本库实体类对象” 到列表中，以获本库所能使用的列表数据。

2.注意：如后端直接使用本库实体类读写 JSON 数据，请在 ProGuard Rules 中为该实体类配置混淆白名单：

```java
-keep class com.kunminx.player.bean.** {*;}
```

&nbsp;

## 更多

"播放控制" 及 "个性化配置" 等，详见 [Wiki](https://github.com/KunMinX/Jetpack-MusicPlayer/wiki/%E4%BD%BF%E7%94%A8%E8%AF%B4%E6%98%8E)

&nbsp;

# Thanks to

[material-components-android](https://github.com/material-components/material-components-android)

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[ExoPlayer](https://github.com/google/ExoPlayer)

[AndroidVideoCache](https://github.com/danikula/AndroidVideoCache)

&nbsp;

# My Pages

Email：[kunminx@gmail.com](mailto:kunminx@gmail.com)

Juejin：[KunMinX 在掘金](https://juejin.im/user/58ab0de9ac502e006975d757/posts)

&nbsp;

# License

```
Copyright 2018-present KunMinX

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```