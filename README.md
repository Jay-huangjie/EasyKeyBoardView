# KeyBoardView
安卓自定义数字键盘(含4位自动加空格的功能,仿支付宝键盘，自动顶布局)
## 效果图：

[效果动图下载](https://github.com/Jay-huangjie/KeyBoardView/blob/master/img/Screen%20Record_2017-03-19-23-02-31.mp4?=true "效果动图下载")  

 ## 实现方法
 ### 由于需要实现自动顶布局，所以采用的popwindow的方法实现的软键盘,重写Edittext,并且根据项目需求加入输入4位自动空格的方法
 ![演示图片](https://github.com/Jay-huangjie/KeyBoardView/blob/master/img/Screenshot_2017-03-20-00-15-41.png)
 
 ## 自定义属性
 `xml` 自定义键盘布局 <br>
 `spance` 是否开启4位空格 <br>
 `randomkeys` 是否开启随机数字键盘
 
 ## 自定义方法
 `setKeyBordFocuable(boolean focuable)` 设置是否禁止输入，注意，如果你使用原始的方法禁止输入字符
 ，点击会弹出自定义键盘，所以请使用此方法禁止
 
 ````
 此键盘可以与原生键盘完美切换，如果只需要纯数字可以在原有xml文件上稍做修改
 ````
 
 # 如果对您有用，欢迎Star
 
