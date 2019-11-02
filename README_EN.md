## Even if you don't listen to the music by Spotify, be sure to collect this library, please!

&nbsp;

### Origin

Jetpack-MusicPlayer is a music player component developed based on the Jetpack MVVM architecture. It exists because of the needs of the ["Jetpack-MVVM-Best-Practice"](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice) project.

After I searching all around GitHub and didn't find a suitable open source library that highly decoupled, remotely dependent, I decided to study the logic of the Multimedia playback controller with existing open source projects and write **a highly decoupled, Easy to configure, true third-party libraries that can be remotely dependent on the Maven repository**.

The personalized configuration of Jetpack-MusicPlayer is very simple. Based on Design Pattern Principles and Java Generic Features, users do not need to know the internal implementation details, Customization and extension of business entity classes can be done only by inheriting the base classes of Album, Music, and Artist.

In addition, Jetpack-MusicPlayer can be **extreme ran by only one line of code** while without setting up a custom configuration.

&nbsp;

|                          PureMusic                           |                      LiveData Dispatch                       |                        PlayMode Switch                       |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :----------------------------------------------------------: |
| ![333.gif](https://i.loli.net/2019/11/02/yMhiVuHngAwaCSf.gif) | ![3333.gif](https://i.loli.net/2019/11/02/YkJuRQmTDvniMoG.gif) | ![playmode.gif](![播放模式.gif](https://i.loli.net/2019/11/02/QxyAXYN7BtasGl8.gif)) |


&nbsp;

### Aims

The goal of Jetpack-MusicPlayer is to access the Multimedia playback controller by just one line of code.

In addition to one-line access and eliminating 99% of unnecessary, complex, and repetitive tasks, what extra you can get from this open source project include:

1. Neat code style and standard resource naming conventions.
2. **The best practice for writing third-party libraries by Jetpack MVVM architecture: Use LiveData with a singleton as the only trusted source to properly distribute the full application-wide playback state **to avoid unpredictable errors that occur in the context of software engineering**.
3. Excellent code layering and encapsulation ideas, one line of code can be accessed without any personalized configuration.
4. The main project is based on the cutting-edge, software engineering security JetPack MVVM architecture.
5. Full use of AndroidX and Material Design 2.
6. Best practices for ConstraintLayout.
7. Never use Dagger, never use geeks and write hard-coded code.

If you are thinking about [**how to choose the right architecture for your project**](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice), this project is worth your reference!

&nbsp;

### Simple Guide:

1. Add a dependency on the library in build.gradle.

```groovy
implementation 'com.kunminx.player:player:1.0.5'
```

2. Prepare a string of data according to the structure of the default album entity class `DefaultAlbum`. (The following is JSON as an example).

```java
// DefaultAlbum contains two subclasses, DefaultMusic and DefaultArtist:
// The fields of the three are detailed in BaseAlbumItem, BaseMusicItem, and BaseArtistItem.
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

3.Initialize the multimedia playback controll component in the Application class.

```java
DefaultPlayerManager.getInstance().init(this);
```

4.Once the data is available, the data can be initialized with a minimum of one line of code.

```java
DefaultAlbum album = gson.fromJson(...);

//One line of code completes the load of the data.
DefaultPlayerManager.getInstance().loadAlbum(album);
```

5.Sends a request to change the playback state in the View Controller, and receives a result response from a unique trusted source for unified distribution.

```java
// 1.Send a request anywhere in anywhere of the View Controller.

// 1.1.For example, here is requested to play the next one.
DefaultPlayerManager.getInstance().playNext();

// 2.Listen to the resulting response from a unique trusted source push in the view controller that is subscribed to the corresponding status notification.

// 2.1.For example, here is the push of the play button status.
DefaultPlayerManager.getInstance().pauseLiveData().observe(this, aBoolean -> {
    mPlayerViewModel.isPlaying.set(!aBoolean);
});

// 2.2.For example, here is a push for the current song details.
DefaultPlayerManager.getInstance().changeMusicLiveData().observe(this, changeMusic -> {
    mPlayerViewModel.title.set(changeMusic.getTitle());
    mPlayerViewModel.artist.set(changeMusic.getSummary());
    mPlayerViewModel.coverImg.set(changeMusic.getImg());
});

// 2.3.For example, here is a push that responds to the current song playback progress.
DefaultPlayerManager.getInstance().playingMusicLiveData().observe(this, playingMusic -> {
    mPlayerViewModel.maxSeekDuration.set(playingMusic.getDuration());
    mPlayerViewModel.currentSeekPosition.set(playingMusic.getPlayerPosition());
});
```

Note: If using JSON, configure an obfuscated whitelist for this entity class in ProGuard Rules:

```java
-keep class com.kunminx.player.bean.** {*;}
```

&nbsp;

### Personalized configuration:

The library prepares the base entity classes for Album, Music, and Artist respectively. When customizing the entity classes, it inherits, extends, and replaces these entity classes.

For personalization configuration, please refer to the cases I wrote in the classes `TestAlbum`, `PlayerManager` and `PlayerFragment`:

### Step1: Extend the entity class according to your needs

You need to expand on the basis of the entity class such as `BaseAlbumItem` according to your needs. The specific method is to write an entity class that must inherit from `BaseAlbumItem`; the two inner classes in the entity class must also be separated. Inherited from `BaseMusicItem` and `BaseArtistItem`.

Take the TestAlbum entity class as an example, expand the three fields `albumMid`:

```java
public class TestAlbum extends BaseAlbumItem<TestAlbum.TestMusic, TestAlbum.TestArtist> {

    private String albumMid;

    public String getAlbumMid() { return albumMid; }

    public void setAlbumMid(String albumMid) { this.albumMid = albumMid; }

    public static class TestMusic extends BaseMusicItem<TestArtist> {

        private String songMid;

        public String getSongMid() { return songMid; }

        public void setSongMid(String songMid) { this.songMid = songMid; }
    }

    public static class TestArtist extends BaseArtistItem {

        private String birthday;

        public String getBirthday() { return birthday; }

        public void setBirthday(String birthday) { this.birthday = birthday; }
    }
}
```

Note: If using JSON, configure an obfuscated whitelist for this entity class in ProGuard Rules.

&nbsp;

### Step2: Implement the interface and complete the custom configuration

When loading data and implementing a custom configuration, the generic box must indicate the entity class you wrote.

For details, please refer to the `PlayerManager` class. The existence of this singleton is to specify your own defined entity class, otherwise you can use `DefaultPlayerManager` directly.

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
        
    // In fact, you can directly copy this class into the project and modify the entity classes that are generic.
        
}
```

&nbsp;

# Thanks to

[material-components-android](https://github.com/material-components/material-components-android)

[AndroidX](https://developer.android.google.cn/jetpack/androidx)

[HDMediaPlayer](https://github.com/yinhaide/HDMediaPlayer)

[AndroidVideoCache](https://github.com/danikula/AndroidVideoCache)

&nbsp;

# My Pages

Email：[kunminx@gmail.com](mailto:kunminx@gmail.com)

Blog：[KunMinX's Personal Blog](https://www.kunminx.com/)

Medium：[KunMinX @ Medium](https://medium.com/@kunminx)

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
