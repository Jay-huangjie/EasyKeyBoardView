## EasyKeyBoardView
EasyKeyBoardView旨在帮助开发者快速实现一个自定义键盘，无需实现键盘内部的输入逻辑，只需关注键盘的
布局和输入后的功能实现即可
### 功能介绍：
该库是基于安卓原生键盘API实现，只需提供布局文件即可快速实现一个自定义键盘，内部已封装好键盘的输入逻辑，并提供了两种不同的弹出方式：

* 键盘固定在底部
* 键盘从底部弹出(如果键盘遮挡了输入框,EasyKeyBoardView会自动将输入框顶上去)

### 效果图
图一:
![图一](http://p2p0lrpx1.bkt.clouddn.com/k1.gif-gif)
图二：
![图二](http://p2p0lrpx1.bkt.clouddn.com/k2.gif-gif)

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
	        implementation 'com.github.Jay-huangjie:EasyKeyBoardView:v1.1'
	}
```

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

其他xml属性:

`keyViewbg` 用于设置键盘的按压效果和按钮之间线的粗细颜色等

`xmlLayoutResId`设置该布局文件


java属性：

`setXmlLayoutResId`可用于在java中指定布局

`setKeyboardUI`可用来定义键盘上字体的UI，如颜色，大小

`setKeybgDrawable` 设置按压效果文件

`setOnKeyboardActionListener`实现键盘监听接口,**注意，此接口必须实现，且必须实现其中的onKey()方法,详情请见demo**

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

`space` 开启4位空格功能,使用场景是输入身份证或银行卡号的时候。

其他用法与`SystemKeyboard`一样，`SystemKeyBoardEditText`实际是`SystemKeyboard`的进一步封装，通过`EditText`与`PopupWindow`结合的方法来实现键盘的灵活弹出.

可以通过`getSystemKeyboard`方法来获取到`SystemKeyBoard`对象。

需要注意的是，如果你是在`RecyclerView`或`ListView`中使用，由于item的复用性,请设置`setActiveRelease(false)`来关闭主动回收功能,然后在合适的场景调用`recycle()`方法来回收。

### 更新日志
```
2018/2/9
重构项目,将项目发布到了JitPack仓库
```

### 后续优化
* 加入随机键盘功能
* 解决在RecyclerView中的回收问题
* 优化KeyBoardView的模式，期望增加唯一键盘模式(待设计ing)

### end
有任何问题可以在issuse中反馈，如果对你有帮助，希望给我颗小星星


