## 真香警告：即使不用云音乐听曲儿，也请务必收藏好该库！

### [Here is the English guide](https://github.com/KunMinX/Linkage-RecyclerView/blob/master/README_EN.md)

&nbsp;

### 由来

Jetpack-MusicPlayer 是一款基于 Jetpack MVVM 架构开发的 音乐播放控制组件，它是因 [“Jetpack-MVVM-Best-Practice”](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice) 这个项目的需求而存在。

在最初寻遍了 GitHub 也没有找到合适的开源库（高度解耦、可远程依赖）之后，我决心研究参考现有开源项目关于 多媒体播放控制 的逻辑，并自己动手编写一个 **高度解耦、轻松配置、可通过 Maven 仓库远程依赖** 的真正的第三方库。

Jetpack-MusicPlayer 的使用十分简单，依托于 设计模式原则 及 JAVA 泛型特性，使用者无需知道内部的实现细节，**仅通过继承** Album、Music、Artist **基类 即可完成 业务实体类 的定制和扩展**。

此外，在不设置自定义配置的情况下，Jetpack-MusicPlayer 最少只需 **一行代码即可运行起来**。

&nbsp;

|                          PureMusic                           |                      LiveData Dispatch                       |                        PlayMode Switch                       |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![1231111323.gif](https://upload-images.jianshu.io/upload_images/57036-0a5cdc68f003211a.gif) | ![333.gif](https://upload-images.jianshu.io/upload_images/57036-9a541148ce5bed2e.gif) | ![playmode.gif](https://upload-images.jianshu.io/upload_images/57036-d16c6ff34b08181c.gif) |


&nbsp;

### 目标

Jetpack-MusicPlayer 的目标是：**一行代码即可接入 音乐播放控制组件**。

除了一键接入而省去 99% 不必要的、复杂的、重复的工作外，你还可以从这个开源项目获得的内容包括：

1. 整洁的代码风格 和 标准的资源命名规范。
2. **Jetpack MVVM 架构在 编写第三方库 中的最佳实践**：通过 LiveData 配合 作为唯一可信源的单例 来完成 全应用范围内 播放状态的正确分发，以避免 **在软件工程背景下** 滋生的各种 **不可预期的错误**。
3. 优秀的代码分层和封装思想，在不做任何个性化配置的情况下，一行代码即可接入。
4. 主体工程基于前沿的、软件工程安全的 JetPack MVVM 架构。
5. AndroidX 和 Material Design 2 的全面使用。
6. ConstraintLayout 约束布局的最佳实践。
7. 绝不使用 Dagger，绝不使用奇技淫巧、编写艰深晦涩的代码。

&nbsp;

如果你正在思考 [**如何为项目挑选合适的架构**](https://juejin.im/post/5dafc49b6fb9a04e17209922) 的话，这个项目值得你参考！

&nbsp;

### 简单使用：

1.在 build.gradle 中添加对该库的依赖。

```groovy
implementation 'com.kunminx.player:player:1.0.4'
```

2.依据默认的专辑实体类 `DefaultAlbum` 的结构准备一串数据（以下以  JSON 为例）。

```java
// DefaultAlbum 包含 DefaultMusic 和 DefaultArtist 两个子类：
// 三者的字段可详见 BaseAlbumItem、BaseMusicItem 和 BaseArtistItem。
```

```json
{
  "albumId": "001",
  "title": "Cute",
  "summary": "BenSound",
  "artist": {
    "name": "Linda"
  },
  "coverImg": "https://images.io/055ef18.png",
  "musics": [
    {
      "musicId": "001",
      "title": "Tomorrow",
      "artist": {
        "name": "Mike"
      },
      "coverImg": "https://images.io/055ef19.png",
      "url": "https://bensound.com/sunny.mp3"
    },
    {
      "musicId": "002",
      "title": "Sunny",
      "artist": {
        "name": "Jackson"
      },
      "coverImg": "https://images.io/055ef20.png",
      "url": "https://bensound.com/cute.mp3"
    }
  ]
}

```

3.在 Application 中初始化 多媒体播放控制组件。

```java
DefaultPlayerManager.getInstance().init(this);
```

4.在得到数据后，最少只需一行代码 即可完成数据的初始化。

```java
DefaultAlbum album = gson.fromJson(...);

//一行代码完成数据的初始化。
DefaultPlayerManager.getInstance().initAlbum(album);
```

5.在视图控制器中 发送改变播放状态的请求，并接收来自 唯一可信源 统一分发的结果响应。

```java
// 1.在 任一视图控制器 的 任一处 发送请求

// 1.1.例如 此处请求了 播放下一首
PlayerManager.getInstance().playNext();

// 2.在 订阅了对应状态通知 的 视图控制器 中，收听来自 唯一可信源 推送的结果响应。

// 2.1.例如 此处响应了 播放按钮状态 的推送
PlayerManager.getInstance().pauseLiveData().observe(this, aBoolean -> {
    mPlayerViewModel.isPlaying.set(!aBoolean);
});

// 2.2.例如 此处响应了 当前歌曲详细信息 的推送
PlayerManager.getInstance().changeMusicLiveData().observe(this, changeMusic -> {
    mPlayerViewModel.title.set(changeMusic.getTitle());
    mPlayerViewModel.artist.set(changeMusic.getSummary());
    mPlayerViewModel.coverImg.set(changeMusic.getImg());
});

// 2.3.例如 此处响应了 当前歌曲播放进度 的推送
PlayerManager.getInstance().playingMusicLiveData().observe(this, playingMusic -> {
    mPlayerViewModel.maxSeekDuration.set(playingMusic.getDuration());
    mPlayerViewModel.currentSeekPosition.set(playingMusic.getPlayerPosition());
});
```

注意：如使用 JSON，请在 ProGuard Rules 中为该实体类目录配置混淆白名单：

```java
-keep class com.kunminx.player.bean.** {*;}
```

&nbsp;

### 个性化配置：

该库为 Album、Music、Artist 分别准备了基础实体类，定制实体类时，即是去继承、拓展 并取代 这些实体类。

关于个性化配置，具体可以参考我在 `TestAlbum`、 `PlayerManager` 和 `PlayerFragment` 等类中编写的案例：

### Step1：根据需求扩展实体类

你需要根据需求，在 `BaseAlbumItem` 等实体类的基础上进行扩展，具体的办法是，编写一个实体类，该实体类须继承于 `BaseAlbumItem`；该实体类中的两个内部类也须分别继承于 `BaseMusicItem` 和  `BaseArtistItem`。

以 TestAlbum 实体类为例，扩充 `albumMid` 三个字段：

```java
public class TestAlbum extends BaseAlbumItem<TestAlbum.TestMusic, TestAlbum.TestArtist> {

    private String albumMid;

    public String getAlbumMid() {
        return albumMid;
    }

    public void setAlbumMid(String albumMid) {
        this.albumMid = albumMid;
    }

    public static class TestMusic extends BaseMusicItem<TestArtist> {

        private String songMid;

        public String getSongMid() {
            return songMid;
        }

        public void setSongMid(String songMid) {
            this.songMid = songMid;
        }
    }

    public static class TestArtist extends BaseArtistItem {

        private String birthday;

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }
    }
}
```

注意：如使用 JSON，请在 ProGuard Rules 中为该实体类配置混淆白名单。

&nbsp;

### Step2：实现接口，完成自定义配置

在装载数据和实现自定义配置时，泛型框中须指明你编写的实体类。

具体可参考 `PlayerManager` 类，该单例的存在就是为了指定你自己定义的实体类，否则你大可直接使用 `DefaultPlayerManager`。

```java
public class PlayerManager implements IPlayController<TestAlbum, TestAlbum.TestMusic> {

    private static PlayerManager sManager = new PlayerManager();

    private PlayerController<TestAlbum, TestAlbum.TestMusic> mController;

    private Context mContext;

    private PlayerManager() {
        mController = new PlayerController<>();
    }

    public static PlayerManager getInstance() {
        return sManager;
    }

    @Override
    public void init(Context context) {
        mController.init(context);
        mContext = context.getApplicationContext();
    }

    @Override
    public void initAlbum(TestAlbum musicAlbum) {
        mController.initAlbum(mContext, musicAlbum);
    }

    ...

    //事实上，你大可直接 Copy 本类到项目中、只需修改作为泛型的那几个实体类即可。

}
```

&nbsp;

# Thanks to

[material-components-android](https://github.com/material-components/material-components-android)

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

&nbsp;

# My Pages

Email：[kunminx@gmail.com](mailto:kunminx@gmail.com)

Home：[KunMinX 的个人博客](https://www.kunminx.com/)

Juejin：[KunMinX 在掘金](https://juejin.im/user/58ab0de9ac502e006975d757/posts)

[《重学安卓》 专栏](https://xiaozhuanlan.com/kunminx?rel=kunminx)

付费读者加微信进群：myatejx

[![重学安卓小专栏](https://i.loli.net/2019/06/17/5d067596c2dbf49609.png)](https://xiaozhuanlan.com/kunminx?rel=kunminx)

&nbsp;

# License

```
Copyright 2018-2019 KunMinX

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