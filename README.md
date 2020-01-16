![tag](https://jitpack.io/v/Jay-huangjie/EasyKeyBoardView.svg?style=flat-square)
## EasyKeyBoardView
EasyKeyBoardView旨在帮助开发者快速实现一个自定义键盘，无需实现键盘内部的输入逻辑，只需关注键盘的
布局和输入后的功能实现即可
### 功能介绍：
该库是基于安卓原生键盘API实现，只需提供布局文件即可快速实现一个自定义键盘，内部已封装好键盘的输入逻辑，并提供了随机数字键盘，每输入4位则空格等功能。

本库提供了两种不同的弹出方式：

* 键盘固定在底部
* 键盘从底部弹出(如果键盘遮挡了输入框,EasyKeyBoardView会自动将输入框顶上去)

### 效果图
图一:
![图一](https://upload-images.jianshu.io/upload_images/3468978-383182c3fd48d1fd.gif?imageMogr2/auto-orient/strip/2/2/720)

图二：
![图二](https://upload-images.jianshu.io/upload_images/3468978-4c550ce01711d0de.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/720)

### 使用
第一步：
在你的root build.gradle中添加
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

在 app build.gradle中添加
```
	dependencies {
	        implementation 'com.github.Jay-huangjie:EasyKeyBoardView:tag'
	}
```
最新版本见最上面的版本号标识,注意要加v字符哦，例如：`implementation 'com.github.Jay-huangjie:EasyKeyBoardView:v1.6'`

第二步：
#### 实现固定在底部的键盘
在xml中定义：
```java
 <com.jay.easykeyboard.SystemKeyboard
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        keyboard:xmlLayoutResId="@xml/keyboard_numbers" />
```
其中:

`keyboard_numbers`是该键盘的布局文件，由使用者自己编写,名称可自取

关于xml里codes的定义问题，可以参阅[ASCI码对照表](http://ascii.911cha.com/)来取值，找到对应的图形编码，对应的10进制就是我们想要的codes了

xml属性:

|属性|作用|
|----|----|
|keyDrawable|用于设置键盘的按压效果和按钮之间线的粗细颜色等
|xmlLayoutResId| 设置键盘的布局文件，必须设置
|isRandom|是否数字随机

java属性：

`setXmlLayoutResId`可用于在java中指定布局

`setKeyboardUI`可用来定义键盘上字体的UI，如颜色，大小

`setKeyDrawable` 设置按压效果文件

`setOnKeyboardActionListener` 键盘输入监听

`setRandomKeys` 设置键盘数字随机，如果已随机数字，设置为false即可另数字恢复正常

如果需要实现焦点监听，需要实现`setFocusChangeListence`接口，注意是项目方法不是原生Api方法噢。

#### 实现弹出的键盘
在xml中定义：
```java
 <com.jay.easykeyboard.SystemKeyBoardEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        keyboard:xmlLayoutResId="@xml/keyboard_numbers"
        />
```
xml属性：

|属性|作用|
|----|----|
|keyDrawable|用于设置键盘的按压效果和按钮之间线的粗细颜色等
|xmlLayoutResId| 设置键盘的布局文件，必须设置
|isRandom|是否数字随机
|space|开启4位空格功能,使用场景是输入身份证或银行卡号的时候。
|outSideCancel|点击外部可关闭键盘

其他用法与`SystemKeyboard`一样，`SystemKeyBoardEditText`实际是`SystemKeyboard`的进一步封装，通过`EditText`与`PopupWindow`结合的方法来实现键盘的灵活弹出.

可以通过`getSystemKeyboard`方法来获取到`SystemKeyBoard`对象。调用`getKeyboardWindow`可以获取包裹键盘View的popwindow对象，随后再调用`dismiss()`方法即可将键盘关闭

启用`removeCopyAndPaste`方法可以屏蔽EditText的长按复制粘贴功能

#### 其他
如果在项目中需要进行原生与自定义键盘的切换或者输入框的切换，可以使用`setEditText`方法，进行EditText的输入目标切换

混淆：
`-keep public class com.jay.easykeyboard.bean.**{ *;}`

### 更新日志
```
2018/2/9
重构项目,将项目发布到了JitPack仓库

2018/12/17
项目v1.2重构，解决输入框焦点选取问题，加入点击外部关闭自定义键盘功能，项目架构调整

2018/12/18
加入随机键盘功能

2019/11/28 v1.6
规范命名
优化：当键盘弹出时点击回退按钮关闭键盘,而不是Activity
```

### 后续优化
* ~~加入随机键盘功能~~
* ~~解决在RecyclerView中的回收问题~~

如果有别的未覆盖到的功能希望能在issuse中反馈，个人能想到的场景有限，希望各位大佬集思广益

### end
有任何问题可以在issuse中反馈，如果对你有帮助，希望给我颗小星星


