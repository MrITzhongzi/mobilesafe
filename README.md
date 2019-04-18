#手机卫士Day01#

## 项目介绍 ##

> 演示功能有：

- 启动页面
- 主页
- 手机防盗（注意：演示时模拟器要提前设置有联系人）；
- 通讯卫士：黑名单的管理：电话拦截、短信拦截的演示；
- 软件管理：列出系统的所有软件，启动软件、卸载软件、系统的卸载失败（需要root权限这个后面也会介绍）
- 进程管理：列出系统中正在运行的程序；演示杀死软件
- 窗口小部件：添加桌面；
- 流量统计：模拟器并不支持，在真机上才能演示，只做个UI效果；
- 手机杀毒：检查手机安装的软件，发现那个是病毒，提醒用户就杀掉；
- 系统优化：清除系统的垃圾，刚开始运行，没用多余数据；
- 高级工具：归属地查询；常用号码查询；短信备份；

## svn工具使用 ##

> 为什么要安装svn服务器?

	方便学生从老师的电脑随时checkout代码,也方便学生更有效得管理自己的代码

- 安装VisualSVN Server
- VisualSVN Server的使用

	- 创建仓库
	- 创建用户,针对不同用户设置不同权限
	- checkout代码,commit代码
	- 从已有的仓库中引入项目

## 代码组织结构 ##

- 按照业务模块划分

		办公软件

    	--  开会            com.itheima.meeting
     	--  发放工资	       com.itheima.money
     	--  出差    	       com.itheima.travel

		网盘 

    	-- 上传		com.sina.vdisk.upload
    	-- 下载		com.sina.vdisk.download
     	-- 文件分享  com.sina.vdisk.share

- 按照组件划分

 		界面		     com.itheima.mobilesafe.activity
 		自定义UI	     com.itheima.mobilesafe.ui
 		业务逻辑代码   com.itheima.mobilesafe.engine
		数据库持久化 	  com.itheima.mobilesafe.db
 		              com.itheima.mobilesafe.db.dao
 		广播接收者      com.itheima.mobilesafe.receiver
 		长期在后台运行  com.itheima.mobilesafe.service
 		公用的api工具类 com.itheima.mobilesafe.utils

## 创建新项目 ##

> minSdkVersion、targetSdkVersion、maxSdkVersion、target API level四个数值到底有什么区别?

> minSdkVersion, maxSdkVersion是项目支持的最低sdk版本和最高sdk版本. 在安装apk前,系统会对这个两值进行判断, 决定当前系统是否可以安装,一般maxSdkVersion不会设置

> target API level是项目编译时的sdk版本

> targetSdkVersion会告诉系统,此版本已经经过充分测试,那么程序运行在该版本的系统是,就不会做过多额外的兼容性判断.运行效率会高一些

## Splash页面 ##

- Splash页面作用
	
	1. 展示品牌logo
	2. 程序初始化
	3. 检查版本更新
	4. 校验程序合法性,比如某些app会判断用户是否联网, 没有联网就无法进入页面
	
- Splash布局文件

		 <TextView
	        android:id="@+id/tv_version"
	        android:textColor="#000000"
	        android:textSize="20sp"
	        android:shadowColor="#ff0000"
	        android:shadowDx="1"
	        android:shadowDy="1"
	        android:shadowRadius="1"
	        android:layout_centerInParent="true"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:text="版本  1.0" />
	
- 获取版本信息

		versionName和versionCode的区别和用处

		//获取版本信息
		private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			String versionName = info.versionName;
			int versionCode = info.versionCode;
			Log.d(TAG, "versionName=" + versionName + "; versionCode=" + versionCode);
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		    return "";
		}

- 版本校验

> 服务器端json数据

	{
    "version_name": "2.0",
    "version_code": 2,
    "description": "最新版手机卫士,快来下载体验吧!",
    "download_url": "http://10.0.2.2:8080/mobilesafe2.0.apk"
	}

	注意: 保存文本为 "UTF-8 无BOM" 格式

> 读取服务器数据流

	URL url = new URL(getString(R.string.server_url));
	HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
	conn.setRequestMethod("GET");// 请求方法
	conn.setConnectTimeout(5000);// 请求超时

	int code = conn.getResponseCode();

	if (code == 200) {
		InputStream in = conn.getInputStream();
		String result = StreamTools.readFromStream(in);
						
		JSONObject json = new JSONObject(result);
		String versionName = json.optString("version_name",
				null);
		int versionCode = json.getInt("version_code");
		String description = json.optString("description");
		String downloadUrl = json.getString("download_url");

		Log.d(TAG, "description:" + description);
		}


	/**
	 * @param is 输入流
	 * @return String 返回的字符串
	 * @throws IOException 
	 */
	public static String readFromStream(InputStream is) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while((len = is.read(buffer))!=-1){
			baos.write(buffer, 0, len);
		}
		is.close();
		String result = baos.toString();
		baos.close();
		return result;
	}

- 更新弹窗
- 页面延时2秒后再跳转

		long end = System.currentTimeMillis();
		long elapse = end - start;
		if (elapse < 2000) {
			try {
				Thread.sleep(2000 - elapse);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		handler.sendMessage(msg);

- 添加AlphaAnimation动画效果

		//开启渐变动画
		AlphaAnimation anim = new AlphaAnimation(0.3f, 1f);
		anim.setDuration(2000);
		rlRoot.startAnimation(anim);

- 打一个2.0的apk包, 替换下载链接

- 下载apk

	- 判断SDcard是否挂载代码：

			if(Environment.getExternalStorageState().equal(Environment.MEDIA_MOUNTED))

	- 使用xutils框架进行下载

			// 下载apk
			HttpUtils hu = new HttpUtils();
			hu.download(downloadUrl, localPath, new RequestCallBack<File>() {
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					//下载进度回调
				}
				@Override
				public void onSuccess(ResponseInfo<File> responseInfo) {
					//下载成功	
				}
				@Override
				public void onFailure(HttpException error, String msg) {
					//下载失败
				}
			});


- 安装apk

> 查看PackageInstaller源码, 查看AndroidManifest.xml文件中Activity的配置, 从而决定在跳转系统安装界面的Activity时应该传哪些参数.

	// 安装apk
	Intent intent = new Intent();
	intent.setAction(Intent.ACTION_VIEW);
	intent.addCategory(Intent.CATEGORY_DEFAULT);
	intent.setDataAndType(
			Uri.fromFile(t),
			"application/vnd.android.package-archive");
	startActivity(intent);

> 安装失败

	 在Android手机里不允许有两个应用程序有相同的包名；

 	假设A应用的包名：com.itheima.mobilesafeA;
	 A应用已经在系统中存在了。

 	这个时候再去安装一个应用B ，他的包名也叫 con.itheima.mobilesafeA
 	系统就会去检查这两应用的签名是否相同。如果相同，B会把A给覆盖安装掉；
	 如果不相同 B安装失败；

 	要想自动安装成功，必须保证应用程序不同版本的签名完成一样。

- 签名

> 默认签名

	直接在eclipse里运行项目是, 会采用默认签名debug.keystore. 查找方式: Window->Preference->Android->Build, 可以看到默认签名文件的路径, 默认是: C:\Users\tt\.android\debug.keystore

	默认签名的特点: 
	1. 不同电脑,默认签名文件都不一样
	2. 有效期比较短, 默认是1年有效期
	3. 有默认密码: android, 别名:androiddebugkey

> 正式签名

	正式签名特点:
	1. 发布应用市场时, 统一使用一个签名文件
	2. 有效期比较长, 一般25年以上
	3. 正式签名文件比较重要,需要开发者妥善保存签名文件和密码

> 使用正式签名文件,分别打包1.0和2.0, 安装运行1.0版本,测试升级是否成功

> 签名文件丢失后, 肿么办?

	1. 让用户卸载旧版本, 重新在应用市场上下载最新版本, 会导致用户流失
	2. 更换包名, 重新发布, 会出现两个手机卫士, 运行新版手机卫士, 卸载旧版本
	3. 作为一名有经验的开发人员, 最好不要犯这种低级错误!

- 细节处理

	- 进度条样式的版本兼容问题
			
			Application主题设置为android:theme="@style/AppTheme"

		    <style name="AppTheme" parent="AppBaseTheme">
		         <item name="android:windowNoTitle">true</item>//隐藏标题
		    </style>

			分别在两种版本模拟器上运行看效果
			
	- 点击物理返回键的bug 

			// builder.setCancelable(false);//流氓手段,让用户点击返回键没有作用, 不建议采纳
			// 点击物理返回键,取消弹窗时的监听
			builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
			});
	- getApplicationContext和Activity.this的区别

			Context是Activity的父类
			父类有的方法, 子类一定有, 子类有的方法,父类不一定有

			当show一个Dialog时, 必须传Activity对象, 否则会出异常
			android.view.WindowManager$BadTokenException: Unable to add window -- token null is not for an application
			因为Dialog必须依赖Activity为载体才能展示出来, 所以必须将Activity对象传递进去

			以后在使用Context的时候, 尽量传递Activity对象, 这样比较安全

	- 用户取消安装apk, 卡死在Splash页面

			在跳转系统安装页面时,startActivityForResult(intent, 0), 在onActivityResult中跳转主页面


- 主页面GridView搭建

		<!--标题-->
	 	 <TextView
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:text="功能列表"
        android:background="#8866ff00"
        android:textSize="22sp"
        android:gravity="center"
        />

   		 <GridView
        android:id="@+id/gv_home"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:numColumns="3"
        android:verticalSpacing="15dp">
   		 </GridView>
	
- 自定义获取焦点的TextView,走马灯效果

		// 让系统认为,当前控件一直处于获取焦点的状态
		@Override
		public boolean isFocused() {
			return true;
		}

		  <com.itheima.mobilesafeteach.ui.FocusedTextView
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:singleLine="true"
	        android:ellipsize="marquee"
	        android:textSize="16sp"
	        android:textColor="#000000"
	        android:text="我是您的手机安全卫士, 我会时刻保护您手机的安全! 啊哈哈哈哈" />

## 手机卫士Day02 ##
- 主页面GridView搭建

		<!--标题-->
	 	 <TextView
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:text="功能列表"
        android:background="#8866ff00"
        android:textSize="22sp"
        android:gravity="center"
        />

   		 <GridView
        android:id="@+id/gv_home"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:numColumns="3"
        android:verticalSpacing="15dp">
   		 </GridView>
	
- 自定义获取焦点的TextView,走马灯效果

		// 让系统认为,当前控件一直处于获取焦点的状态
		@Override
		public boolean isFocused() {
			return true;
		}

		  <com.itheima.mobilesafeteach.ui.FocusedTextView
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:singleLine="true"
	        android:ellipsize="marquee"
	        android:textSize="16sp"
	        android:textColor="#000000"
	        android:text="我是您的手机安全卫士, 我会时刻保护您手机的安全! 啊哈哈哈哈" />

- 自定义组合控件SettingItemView

	1. 布局文件中完成item样式
	2. 创建自定义SettingItemView,继承RelativeLayout, 在构造方法中完成布局加载
	3. 设置item点击事件,Checkbox切换,文字变化
	4. 在SP中记录item状态, 在SplashActivity中判断item状态,决定是否升级

- SettingItemView自定义属性

	1. 删除代码中对文本的动态设置, 改为在布局文件中设置
	2. 在布局文件中增加新的命名空间
	
		    xmlns:itheima="http://schemas.android.com/apk/res/com.itheima.mobilesafeteach"

	3. 参照系统源码attrs.xml, 找到定义TextView属性的位置,拷贝相关代码
	4. 创建attrs.xml, 定义相关属性

			 <!-- 自定义属性 -->
		    <declare-styleable name="SettingItemView">
		        <attr name="title" format="string" />
		        <attr name="desc_on" format="string" />
		        <attr name="desc_off" format="string" />
		    </declare-styleable>

	5. 读取自定义属性的值, 更新TextView的内容

		    int count = attrs.getAttributeCount();
		    for (int i = 0; i < count; i++) {
			Log.d("Test", "name=" + attrs.getAttributeName(i) + "; value="
					+ attrs.getAttributeValue(i));
		    }

			String title = attrs.getAttributeValue(NAMESPACE, "title");

- 自定义组合控件小结

		 1.声明一个View对象 继承相对布局，或者线性布局 或者其他的ViewGroup
 		 2.在自定义的View对象里面重写它的构造方法。在构造方法里面就把布局都初始化完毕
 		 3.根据业务需求 添加一些api方法，扩展自定义的组合控件
 		 4.布局文件里面 可以自定义一些属性

- 防盗模块自定义对话框, 低版本样式适配

	1.检测密码是否已经设置, 弹出设置密码框或输入密码框

	2.自定义对话框布局文件

		  <TextView
	        android:layout_width="match_parent"
	        android:layout_height="40dp"
	        android:background="#66ff6600"
	        android:gravity="center"
	        android:orientation="vertical"
	        android:text="设置密码"
	        android:textSize="20sp" >
   		 </TextView>

    	<EditText
	        android:id="@+id/et_password"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:inputType="textPassword" />

	3. 按钮响应事件处理

		对比密码是否相同, 相同的话保存在sp中,进入手机防盗页,否则给错误提示

	4. 2.3版本对话框有黑色背景, 需要进行适配

			首先, 将布局文件的整体背景设置为白色
			 android:background="#ffffff"

			其次, 为了去掉残余边框, 需要将布局的边距设置为0
			alertDialog = builder.create();
			alertDialog.setView(view, 0, 0, 0, 0);
			alertDialog.show();
		
- md5介绍

		为了安全保存密码, 使用到了md5算法, md5是一种不可逆的加密算法

		public static void main(String[] args) {
		try {
			String password = "123456";
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] result = digest.digest(password.getBytes());

			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int i = b & 0xff;// 将字节转为整数
				String hexString = Integer.toHexString(i);// 将整数转为16进制

				if (hexString.length() == 1) {
					hexString = "0" + hexString;// 如果长度等于1, 加0补位
				}

				sb.append(hexString);
			}

			System.out.println(sb.toString());//打印得到的md5

			} catch (NoSuchAlgorithmException e) {
				// 如果算法不存在的话,就会进入该方法中
				e.printStackTrace();
			}
		}

> 登录网站: http://www.cmd5.com/ 验证md5准确性

> 演示md5如何暴力破解

> 为避免暴力破解, 可以对算法加盐

		什么是加盐? 

		比如以前我们只是把password进行md5加密, 现在可以给password加点盐,这个盐可以是一个固定的字符串,比如用户名username, 然后我们计算一下md5(username+password), 保存在服务器的数据库中, 即使这个md5泄露, 被人破解后也不是原始的密码, 一定程度上增加了安全性


## Day03 ##
- 设置向导
> 演示搜狗输入法设置向导
> 完成第一个向导页面Setup1Activity的布局文件

	- style样式介绍
	- 用到的系统图片

		  android:drawableLeft="@android:drawable/star_big_on"//五角星
		  android:src="@android:drawable/presence_online" //小点选中
		  android:src="@android:drawable/presence_invisible" //小点不选中
	
- selector介绍

		1. 查看系统style.xml中有关Button样式的描述, 寻找Button的背景xml
			 <style name="Widget.Holo.Light.Button" parent="Widget.Button">

		2. 查看谷歌官方文档, 了解selector的详细设置方法
			App Resources>Resource Types>Drawable>State List
			拷贝Example的代码,在项目中运行.使用美图秀秀作图 50*50

		3. 使用准备好的图片创建新的selector, 设置给引导页面和Dialog

- 9patch图 

	> *.9.png

	android手机上,可以按需求自动拉伸的图片

	> 制作9Patch图: sdk/tools/draw9patch.bat

	- 上边线控制水平拉伸
	- 左边线控制竖直拉伸
	- 右边线和下边线控制内容区域

- 完成4个设置引导页

		1. Button 样式统一style
		2. 上一页和下一页逻辑处理

- 完成手机防盗页布局

> "重新进入设置向导" 按钮样式调整, 使用TextView添加selector,  
> android:clickable="true", 处理该按钮的点击事件

- Shape介绍

		1. 查看官方文档有关Shape的介绍
			App Resources>Resource Types>Drawable>Shape Drawable
			拷贝Example的代码,在项目中运行

		2. 演示shape下的几个属性
			
			<?xml version="1.0" encoding="utf-8"?>
			<shape xmlns:android="http://schemas.android.com/apk/res/android"
		    android:shape="rectangle" >
		
		    <!-- 圆角弧度 -->
		    <corners android:radius="5dp" />
		    <!-- 渐变 <gradient android:startColor="#ff0000" android:endColor="#00ff00" /> -->
		    
		    <!-- 填充色 -->
		    <solid android:color="#fff" />
		
		    <!-- 边框(虚线) <stroke android:width="1dp" android:color="#000000" android:dashWidth="8dp" android:dashGap="3dp"/> -->
			</shape>

- Activity切换动画

> 下一页动画

	trans_in.xml

	<?xml version="1.0" encoding="utf-8"?>
	<translate
	    android:fromXDelta="100%p" android:toXDelta="0"
	    android:duration="500"
	    xmlns:android="http://schemas.android.com/apk/res/android">
	
	</translate>

	trans_out.xml

	<?xml version="1.0" encoding="utf-8"?>
	<translate
	    android:fromXDelta="0" android:toXDelta="-100%p"
	    android:duration="500"
	    xmlns:android="http://schemas.android.com/apk/res/android">
	
	</translate>

> 上一页动画

	trans_pre_in.xml

	<?xml version="1.0" encoding="utf-8"?>
	<translate
	    android:fromXDelta="-100%p" android:toXDelta="0"
	    android:duration="500"
	    xmlns:android="http://schemas.android.com/apk/res/android">
	
	</translate>

	trans_pre_out.xml

	<?xml version="1.0" encoding="utf-8"?>
	<translate
	    android:fromXDelta="0" android:toXDelta="100%p"
	    android:duration="500"
	    xmlns:android="http://schemas.android.com/apk/res/android">
	
	</translate>

> Activity切换的动画效果
> 
	overridePendingTransition(R.anim.trans_in, R.anim.trans_out);//Activity切换的动画效果

- 手势识别器

		detector = new GestureDetector(this,
				new GestureDetector.SimpleOnGestureListener() {
					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {

						if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
							Toast.makeText(BaseSetupActivity.this, "不能这样划哦!",
									Toast.LENGTH_SHORT).show();
							return true;
						}
						
						if (Math.abs(velocityX) < 100) {
							Toast.makeText(BaseSetupActivity.this, "速度太慢啦!",
									Toast.LENGTH_SHORT).show();
							return true;
						}
						
						if (e2.getRawX() - e1.getRawX() > 200) {
							Log.d("Test", "显示上一页");
							showPrevious();
							return true;
						}

						if (e1.getRawX() - e2.getRawX() > 200) {
							Log.d("Test", "显示下一页");
							showNext();
							return true;
						}

						return super.onFling(e1, e2, velocityX, velocityY);
					}
				});

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			detector.onTouchEvent(event);
			return super.onTouchEvent(event);
		}

- 代码重构, 抽取父类

> BaseSetupActivity
	
		// 展示下一页
		public abstract void showNext();
	
		// 展示上一页
		public abstract void showPrevious();
	
		// 下一页按钮点击
		public void next(View view) {
			showNext();
		}
	
		// 上一页按钮点击
		public void previous(View view) {
			showPrevious();
		}


- 手机防盗流程梳理

- sim卡绑定页面实现(Setup2Activity)

		TelephonyManager mTelePhonyManager;
		mTelePhonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
	
		String simSerialNumber = mTelePhonyManager.getSimSerialNumber();// 获取sim卡序列号
	
		需要权限: <uses-permission android:name="android.permission.READ_PHONE_STATE"/>
	
		将序列号保存在sp中,根据sp是否有值来更新选择框状态

- 监听开机启动,检测sim卡变化

		 <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>

		 <receiver android:name=".receiver.BootCompleteReceiver" >
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>

		如果发现当前sim卡和sp中保存的不一致,需要向安全号码发送报警短信

- 读取联系人Demo

		/**
		 * 读取联系人
		 */
		private ArrayList<HashMap<String, String>> readContacts() {
			ArrayList<HashMap<String, String>> contacts = new ArrayList<HashMap<String, String>>();
	
			ContentResolver resolver = getContentResolver();
			Uri uriRaw = Uri.parse("content://com.android.contacts/raw_contacts");// raw_contacts表的uri
			Uri uriData = Uri.parse("content://com.android.contacts/data");// data表的uri
	
			Cursor cursor = resolver.query(uriRaw, new String[] { "contact_id" },
					null, null, null);
	
			if (cursor != null) {
				while (cursor.moveToNext()) {
					String id = cursor.getString(0);
					Cursor dataCursor = resolver.query(uriData, new String[] {
							"data1", "mimetype" }, "raw_contact_id=?",
							new String[] { id }, null);
					if (dataCursor != null) {
						HashMap<String, String> map = new HashMap<String, String>();
						while (dataCursor.moveToNext()) {
							String data = dataCursor.getString(0);
							String mimeType = dataCursor.getString(1);
	
							if ("vnd.android.cursor.item/phone_v2".equals(mimeType)) {
								map.put("phone", data);// 设置手机号码
							} else if ("vnd.android.cursor.item/name"
									.equals(mimeType)) {
								map.put("name", data);// 设置名称
							}
						}
	
						contacts.add(map);
					}
				}
			}
	
			return contacts;
		}

		SimpleAdapter adapter = new SimpleAdapter(this, contacts,
				R.layout.list_contact_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone });
		lvList.setAdapter(adapter);

		需要配置权限 
		<uses-permission android:name="android.permission.READ_CONTACTS" />

- 将联系人模块导入到项目中, 点击"选择联系人",跳转到联系人列表页

		通过startActivityForResult方式跳转,可以获取联系人页面的回传数据

		SelectContactActivity:

		Intent intent = new Intent();
		intent.putExtra("phone", phone);	
		setResult(Activity.RESULT_OK, intent);
		finish();
		-------------------
		Setup3Activity:

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			System.out.println("onActivityResult:" + resultCode);
			if (resultCode == Activity.RESULT_OK) {
				String phone = data.getStringExtra("phone");
	
				phone = phone.replace("-", "");//去掉"-"
				phone = phone.replace(" ", "");//去掉空格
	
				etPhoneNumber.setText(phone);
			}
	
			super.onActivityResult(requestCode, resultCode, data);
		}

		 <EditText
	        android:id="@+id/et_phone_number"
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:inputType="phone"//设定键盘类型为电话号码
	        android:hint="请输入或选择安全号码"
         >

		//如果安全号码不为空,更新EditText
		String phone = mSp.getString("safe_phone", null);
		if (!TextUtils.isEmpty(phone)) {
			etPhoneNumber.setText(phone);
		}

		//跳转下一个页面
		String phone = etPhoneNumber.getText().toString().trim();// 过滤掉两侧空格后,获取号码信息

		if (TextUtils.isEmpty(phone)) {
			Toast.makeText(this, "必须设定安全号码!", Toast.LENGTH_SHORT).show();
			return;
		}

		mSp.edit().putString("safe_phone", phone).commit();// 保存电话号码

- 防盗保护页面状态更新(LostFindActivity)

		//判断防盗保护是否开启,更新图标状态
		boolean protecting = sp.getBoolean("protecting", false);
		if (protecting) {
			ivProtect.setImageResource(R.drawable.lock);
		} else {
			ivProtect.setImageResource(R.drawable.unlock);
		}
		
		tvSafePhone.setText(sp.getString("safe_phone", ""));//更新安全号码


- 手机定位

	- 网络定位

			根据IP显示具体的位置, 原理是建立一个库那个IP地址对应那个地方；早期警方破案就采用此特点；

			纯真IP数据库软件介绍

		    有局限性：针对固定的IP地址。
		    如果手机网或者ip地址是动态分布IP，这个偏差就很大。这种情况是无法满足需求的。

	- 基站定位

			工作原理：手机能打电话，是需要基站的。手机定位也是用基站的。
			手机附近能收到3个基站的信号，就可以定位了。
			基站定位有可能很准确，比如基站多的地方；
			如果基站少的话就会相差很大。
			精确度：几十米到几公里不等；

	- GPS定位

			至少需要3颗卫星；
			特点是：需要搜索卫星， 头顶必须是空旷的；
			影响条件：云层、建筑、大树。

			卫星：美国人、欧洲人的卫星。
			北斗：中国的，但没有民用，只是在大巴，战机等使用。

			A-GPS: 通过GPS和网络共同定位,弥补GPS的不足, 精确度可达到15米以内, 一般手机都采用此种定位方式

- 定位Demo演示

		lm = (LocationManager) getSystemService(LOCATION_SERVICE);//获取系统位置服务
		List<String> allProviders = lm.getAllProviders();//获取所有位置提供者

		listener = new MyLocationListener();//位置监听器
		lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0,
				listener);//更新位置, 参2和参3设置为0,表示只要位置有变化就立即更新

		class MyLocationListener implements LocationListener {

		//位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("onLocationChanged");
			String longitude = "经度:" + location.getLongitude();
			String latitude = "纬度:" + location.getLatitude();
			String accuracy = "精确度:" + location.getAccuracy();
			String altitude = "海拔:" + location.getAltitude();

			tvLocation.setText(longitude + "\n" + latitude + "\n" + accuracy
					+ "\n" + altitude);
		}

		//位置提供者的状态发生变化
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("onStatusChanged");
		}

		//位置提供者可用
		@Override
		public void onProviderEnabled(String provider) {
			System.out.println("onProviderEnabled");
		}

		//位置提供者不可用
		@Override
		public void onProviderDisabled(String provider) {
			System.out.println("onProviderDisabled");
		}

		}
	
		@Override
		protected void onDestroy() {
			super.onDestroy();
			lm.removeUpdates(listener);//为了节省性能,当页面销毁时,删除位置更新的服务
			listener = null;
		}
	
		需要权限:
		<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>//获取准确GPS坐标的权限
	    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION"/>//允许模拟器模拟位置坐标的权限
	    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>//获取粗略坐标的权限(网络定位时使用)

- 火星坐标

		获取到坐标后在谷歌地图上查询,发现坐标有所偏移, 不准确.这是因为中国的地图服务,为了国家安全, 坐标数据都经过了政府加偏处理, 加偏处理后的坐标被称为火星坐标.
	
		技术牛人通过对美国地图和中国地图的比对,生成了一个查询数据库, 专门用与标准坐标和火星坐标的转换.导入数据库文件axisoffset.dat和工具类ModifyOffset.java,创建一个java工程进行演示

		public static void main(String[] args) {
			try {
				ModifyOffset offset = ModifyOffset.getInstance(Demo.class
						.getResourceAsStream("axisoffset.dat"));//加载数据库文件
				PointDouble s2c = offset.s2c(new PointDouble(116.2821962,
						40.0408444));//标准坐标转为火星坐标
				System.out.println(s2c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

- 开启服务,动态存储最新的坐标

		LocationService

		public void onCreate() {
			lm = (LocationManager) getSystemService(LOCATION_SERVICE);// 获取系统位置服务
	
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);// 准确度良好
			criteria.setCostAllowed(true);// 是否允许花费(比如网络定位)
			String bestProvider = lm.getBestProvider(criteria, true);// 获取当前最好的位置提供者
	
			System.out.println("位置提供者:" + bestProvider);
	
			listener = new MyLocationListener();// 位置监听器
			lm.requestLocationUpdates(bestProvider, 0, 0, listener);// 更新位置,
																	// 参2和参3		设置为0,表示只要位置有变化就立即更新
		};

		// 位置发生变化
		@Override
		public void onLocationChanged(Location location) {
			String longitude = "j:" + location.getLongitude() + "\n";
			String latitude = "w:" + location.getLatitude() + "\n";
			String accuracy = "a:" + location.getAccuracy() + "\n";


			// 保存经纬度信息
			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
			sp.edit()
					.putString("last_location", longitude + latitude + accuracy)
					.commit();
			stopSelf();// 停止位置服务
		}

		----------------------------------

		SmsReceiver

		if ("#*location*#".equals(body)) {
			System.out.println("获取手机地理位置");
			context.startService(new Intent(context, LocationService.class));// 开启位置服务

			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			String location = sp.getString("last_location", null);

			String reply = location;
			if (TextUtils.isEmpty(reply)) {
				reply = "getting location...";
			}

			SmsManager.getDefault().sendTextMessage(address, null, reply,
					null, null);

			abortBroadcast();// 中断广播的传递
		}

	> 注意添加权限!

	> 项目演示
	> 
	> 开启两个模拟器,发送短信#*location*#,查看是否可以收到经纬度的短信.第一次发送时,sp中没有保存,返回的是"getting location...", 为了保证模拟器能更新sp,需要在控制台发送模拟的经纬度信息. LocationService启动后获取经纬度,一旦获取成功,马上停止服务,这样可以节省耗电量. 演示服务开启和结束的场景.

- 超级管理员

> Administration官方文档介绍: http://developer.android.com/guide/topics/admin/device-admin.html
> 
> 网站推荐: http://www.androiddevtools.cn/ 查看中文文档

> 应用: 锁屏, 清除系统数据

> ApiDemo中的案例演示

	配置超级管理员步骤:
	1. 自定义Receiver,继承DeviceAdminReceiver
	2. 配置manifest

		 <receiver
            android:name=".AdminReceiver"
            android:description="@string/sample_device_admin_description"
            android:label="@string/sample_device_admin"
            android:permission="android.permission.BIND_DEVICE_ADMIN" >
            <meta-data
                android:name="android.app.device_admin"
                android:resource="@xml/device_admin_sample" />

            <intent-filter>
                <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
            </intent-filter>
        </receiver>

	3. 添加配置文件@xml/device_admin_sample
	4. 获取DevicePolicyManager
	
		mDPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE)

	5. 一键锁屏
	
		mDPM.lockNow();//锁屏
		mDPM.resetPassword("123", 0);//设置锁屏密码
		注意: 必须先打开设置->安全->设备管理器的权限,否则运行崩溃

	6. 通过代码打开超级管理员权限
	
		public void openAdmin(View view) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			ComponentName component = new ComponentName(this, AdminReceiver.class); 
	        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component);
	        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
	               "打开超级管理员权限,可以一键锁屏,删除数据等");
			startActivity(intent);
		}

	7. 验证是否已经激活设备管理员

		ComponentName component = new ComponentName(this, AdminReceiver.class);
		if (mDPM.isAdminActive(component)) {
		}

	8. 桌面应用,一键锁屏, 应用市场搜索
	9. 如何卸载应用

		public void uninstall(View view) {
			ComponentName component = new ComponentName(this, AdminReceiver.class); 
			mDPM.removeActiveAdmin(component);//删除超级管理权限
			
			//跳转到卸载页面
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setData(Uri.parse("package:" + getPackageName()));
			startActivity(intent);
		}

	10. 清除数据

		//mDPM.wipeData(0);//恢复出厂设置
		//mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);//清除sdcard内容
 高级工具

		AToolsActivity

		布局文件:
		 <TextView
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:background="@drawable/button"
	        android:drawableLeft="@android:drawable/ic_menu_camera"
	        android:drawablePadding="5dp"
	        android:gravity="center_vertical"
	        android:onClick="numberAddressQuery"
	        android:padding="5dp"
	        android:clickable="true"
	        android:text="电话归属地查询" >
	    </TextView>
		
- 号码归属地查询

		NumberAddressActivity

	- 原理分析
		- 网络查询(百度搜索手机归属地查询)
		- 数据库查询(数据库可以从网上下载,也可从网络购买)

	- sqlite导入本地数据库
		- 原始数据库, 有很多地名重复,可以进一步优化

				 将地名和卡类型的数据单独导入一张表中, 再将手机号前缀导入另外一张表,通过外键查询,数据量大大减小
				 select area,city,cardtype from info group by area,city,cardtype

		- 小米数据库

				1. 根据号码前7位查询外键
					select outkey from data1 where id="1861094"
				2. 根据外键查询位置信息
					select area,location from data2 where id=91
				3. 组合查询,直接根据号码前7位查询位置信息
					select area,location from data2 where id=(select outkey from data1 where id="1861094")

- 拷贝数据库
> SQLiteDatabase不支持直接从assets读取文件,所以要提前拷贝数据库

		NumberAddressDao

		public static final String PATH = "data/data/com.itheima.mobilesafeteach/files/address.db";

		SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null,
				SQLiteDatabase.OPEN_READONLY);

		---------------------------------

		SplashActivity

		更新版本前,先拷贝数据库address.db
		/**
		 * 拷贝数据库
		 */
		private void copyDB(String dbName) {
			File file = new File(getFilesDir(), dbName);// 目的文件
	
			if (file.exists()) {
				System.out.println("数据库" + dbName + "已存在,无须拷贝!");
				return;
			}
	
			FileOutputStream out = null;
			InputStream in = null;
			try {
				out = new FileOutputStream(file);
				in = getAssets().open(dbName);// 源文件
	
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = in.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
	
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

- 查询数据库

		Cursor cursor = db
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?)",
							new String[] { number.substring(0, 7) });
			if (cursor != null) {
				if (cursor.moveToNext()) {
					address = cursor.getString(0);
					System.out.println("address:" + address);
				}

				cursor.close();
			}

- 号码合法性判断

> 正则表达式
> 
> 手机号: "^1[345678]\\d{9}$"; 数字: "^\\d+$"


 	- 特殊号码判断

		switch (number.length()) {
			case 3:
				// 匪警电话 ,110,120等
				address = "报警电话";
				break;
			case 4:
				// 模拟器电话,5554,5556
				address = "模拟器";
				break;
			case 5:
				// 客服电话,95555
				address = "客服电话";
				break;
			case 7:
			case 8:
				// 本地电话
				address = "本地电话";
				break;

	- 座机判断

		if (number.startsWith("0") && number.length() > 10) {// 座机号码
			Cursor cursor = db.rawQuery(
					"select location from data2 where area=?",
					new String[] { number.substring(1, 4) });

			if (cursor.moveToNext()) {// 先查询前4位
				address = cursor.getString(0);
			}

			cursor.close();

			if (TextUtils.isEmpty(address)) {// 如果前4位没有数据,就查询前3位
				cursor = db.rawQuery(
						"select location from data2 where area=?",
						new String[] { number.substring(1, 3) });
				if (cursor.moveToNext()) {
					address = cursor.getString(0);
				}

				cursor.close();
			}
		}

> 注意: db.close();//关闭数据库

- 监听文字变化,动态查询

		etNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				System.out.println("onTextChanged");
				if (s.length() >= 3) {
					String address = NumberAddressDao.getAddress(s.toString());
					if (!TextUtils.isEmpty(address)) {
						tvResult.setText(address);
					} else {
						tvResult.setText("无结果");
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				System.out.println("beforeTextChanged");
			}

			@Override
			public void afterTextChanged(Editable s) {
				System.out.println("afterTextChanged");
			}
		});

- 抖动效果

	1. 引入ApiDemo,查找抖动效果的代码
	2. 拷贝相关代码到自己的项目中,运行
	3. 代码解读,插补器介绍

			结合Interpolator的子类,如线性插补器和循环插补器的源码来分析,更容易理解

			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			shake.setInterpolator(new Interpolator() {

				@Override
				public float getInterpolation(float x) {
					float y = x;//线程插补器
					return y;
				}
			});

- 振动效果

		private void vibrate() {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			// vibrator.vibrate(2000);//震动2秒
			long[] pattern = new long[] { 1000, 2000, 1000, 3000 };// 先等待1秒,再震动2秒,再等待1秒,再震动3秒...
			vibrator.vibrate(pattern, -1);// 参2等于-1时,表示不循环,大于等于0时,表示从以上数组的哪个位置开始循环
		}

		注意权限:  <uses-permission android:name="android.permission.VIBRATE"/>
		
		
		
		## Day06 ##
- 来电监听

		创建后台服务 AddressService
 		
		public void onCreate() {
			listener = new MyPhoneListener();
			tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
			tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		};

		@Override
		public void onDestroy() {
			super.onDestroy();
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}

		class MyPhoneListener extends PhoneStateListener {

			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:
					String address = NumberAddressDao.getAddress(incomingNumber);
					Toast.makeText(AddressService.this, address, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
				}
				super.onCallStateChanged(state, incomingNumber);
			}
		}

		设置页面新增勾选框,点击后启动或停止service

- 判断服务是否在后台运行,更新checkbox

		public static boolean isServiceRunning(String serviceName, Context ctx) {
			ActivityManager am = (ActivityManager) ctx
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningServiceInfo> runningServices = am.getRunningServices(100);//获取所有后台运行的服务
	
			for (RunningServiceInfo runningServiceInfo : runningServices) {
				String className = runningServiceInfo.service.getClassName();
				if (className.equals(serviceName)) {
					return true;
				}
			}
	
			return false;
		}

- 去电监听

	- 静态注册广播

			 <receiver android:name=".receiver.OutCallReceiver" >
	            <intent-filter>
	                <action android:name="android.intent.action.NEW_OUTGOING_CALL" />
	            </intent-filter>
	        </receiver>

			注意添加权限:  <uses-permission android:name="android.permission.PROCESS_OUTGOING_CALLS"/>
		
			问题: 当开关关闭时,仍然能显示去电地址信息

	- 动态注册广播

		当启动后台服务时,注册广播,服务停止后,注销广播,这样的话,来电和去电的位置显示都可以由一个开关来控制

- 自定义Toast
	
	- Toast原理分析
		
			查找transient_notification文件,查看布局样式, 在values/themes中搜索toastFrameBackground, 查看背景图片toast_frame.9.png

			分析Toast源码, 创建自定义Toast

			private void showToast(String address) {
				view = new TextView(this);
				view.setText(address);
				view.setTextColor(Color.RED);
		
				final WindowManager.LayoutParams params = new WindowManager.LayoutParams();
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_TOAST;
				params.setTitle("Toast");
				wm.addView(view, params);
			}

			监听电话状态, 如果电话处于空闲状态,就从WindowManager中删除View
			case TelephonyManager.CALL_STATE_IDLE:
				if (wm != null && view != null) {
					wm.removeView(view);
				}
			break;

	- 金山手机卫士

			演示金山手机卫士归属地样式, 模仿其样式进行开发. 解压金山手机卫士apk,获取相关资源文件. 注意: 相关图片在drawable目录下, 而非drawable-hdpi

	- 自定义Toast样式

			1. 布局文件
				
				电话图标: @android:drawable/ic_menu_call

			2. 自定义SettingClickView, 类似SettingItemView

				去掉自定义属性,保留setDesc和setTitle两个方法

			3. 初始化SettingClickView, 设置点击事件,弹出单选Dialog
					
				// 选择归属地样式的弹窗
				AlertDialog.Builder builder = new AlertDialog.Builder(
						SettingActivity.this);
				
				int style = sp.getInt("address_style", 0);
				builder.setSingleChoiceItems(items, style,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								sp.edit().putInt("address_style", which)
										.commit();
								scvStyle.setDesc(items[which]);
								dialog.dismiss();
							}
						});

				builder.setNegativeButton("取消", null);
				builder.show();
						
			4. 选择相应样式,保存在sp中
			
			5. 从sp中读取样式,在AddressService中更改背景图片

				SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
				int style = sp.getInt("address_style", 0);
		
				int[] bgs = new int[] { R.drawable.call_locate_white,
						R.drawable.call_locate_orange, R.drawable.call_locate_blue,
						R.drawable.call_locate_gray, R.drawable.call_locate_green };
		
				view.setBackgroundResource(bgs[style]);

- 修改归属地显示位置

		定义DragViewActivity

		1. 布局文件:
				
			<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    xmlns:tools="http://schemas.android.com/tools"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical" >
			
			    <TextView
			        android:id="@+id/tv_top"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_alignParentTop="true"
			        android:layout_centerHorizontal="true"
			        android:background="@drawable/call_locate_blue"
			        android:gravity="center"
			        android:text="按住提示框拖动到任意位置,按手机返回键立刻生效"
			        android:textColor="#000"
			        android:textSize="20sp" />
			
			    <TextView
			        android:id="@+id/tv_bottom"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:layout_alignParentBottom="true"
			        android:layout_centerHorizontal="true"
			        android:background="@drawable/call_locate_blue"
			        android:gravity="center"
			        android:text="按住提示框拖动到任意位置,按手机返回键立刻生效"
			        android:textColor="#000"
			        android:textSize="20sp"
			        android:visibility="invisible" />
			
			    <ImageView
			        android:id="@+id/iv_drag"
			        android:layout_width="wrap_content"
			        android:layout_height="wrap_content"
			        android:layout_marginTop="90dp"
			        android:src="@drawable/drag" />
			
			</RelativeLayout>

		2. 拖拽事件监听

			ivDrag.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//获取起始点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					int dx = endX - startX;
					int dy = endY - startY;

					System.out.println("位置偏移:(" + dx + "," + dy + ")");

					//根据手指的移动偏移量,计算出图片相应的位置
					int left = ivDrag.getLeft() + dx;
					int top = ivDrag.getTop() + dy;
					int right = ivDrag.getRight() + dx;
					int bottom = ivDrag.getBottom() + dy;

					//判断图片是否移出屏幕
					if (left < 0 || right > windowWidth || top < 0
							|| bottom > windowHeight - 20) {
						break;
					}

					//判断图片位于屏幕上半部分还是下半部分
					if (top > windowHeight / 2) {
						tvBottom.setVisibility(View.INVISIBLE);
						tvTop.setVisibility(View.VISIBLE);
					} else {
						tvBottom.setVisibility(View.VISIBLE);
						tvTop.setVisibility(View.INVISIBLE);
					}

					//重新设定图片的位置
					ivDrag.layout(left, top, right, bottom);

					//重新获取起始点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					//记录拖拽结束后的坐标点
					Editor edit = sp.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;

				default:
					break;
				}

				return true;
			}
			});

			-----------------
			获取屏幕宽高

			WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
			final int windowWidth = wm.getDefaultDisplay().getWidth();
			final int windowHeight = wm.getDefaultDisplay().getHeight();

		3. 初始化图片位置

			LayoutParams params = (LayoutParams) ivDrag.getLayoutParams();
			params.leftMargin = lastX;
			params.topMargin = lastY;
			ivDrag.setLayoutParams(params);
	
			if (lastY > windowHeight / 2) {
				tvBottom.setVisibility(View.INVISIBLE);
				tvTop.setVisibility(View.VISIBLE);
			} else {
				tvBottom.setVisibility(View.VISIBLE);
				tvTop.setVisibility(View.INVISIBLE);
			}

			注意:此处不能使用该方法: ivDrag.layout(lastX, lastY, lastX + ivDrag.getWidth(), lastY + ivDrag.getHeight());
			因为当前还没有测量好, 所以不能直接调用layout. 顺序是measure,layout,ondraw

- 使用WindowManager设置归属地位置

		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);

		params.gravity = Gravity.TOP + Gravity.LEFT; //注意要将重心设置在左上方,默认位于屏幕中央
		params.x = lastX;
		params.y = lastY;

- 半透明效果处理

		1. 清单文件中增加样式, 将Activity设置为全透明
		 <activity
            android:name=".activity.DragViewActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" />

		2. 将根布局的背景设置为半透明颜色
- 双击事件

		/**
		 * 双击
		 * @param view
		 */
		public void onClick(View view) {
			if (firstClickTime > 0) {
				if (System.currentTimeMillis() - firstClickTime < 500) {
					System.out.println("双击");
					firstClickTime = 0;
					return;
				}
			}
	
			firstClickTime = System.currentTimeMillis();
		}

- 多击事件

		设置->关于手机->"Android 版本",多次点击后会跳转页面
		查看系统源码Settings, 搜索"Android 版本"字符串,查找相关代码,拷贝到自己的项目中
	
		long[] mHits = new long[3];//数组长度为点击次数

		/**
		 * 多次点击
		 * 
		 * @param view
		 */
		public void onClick(View view) {
			//		src 源数组
			//		srcPos 开始拷贝的位置
			//		dst 目标数组
			//		dstPos 目标数组的起始拷贝位置
			//		length 拷贝的数组长度
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);//拷贝数组
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
				System.out.println("是男人!!!");
				mHits = new long[3];
			}
		}

- 双击居中

		//图片设置为屏幕居中
		ivDrag.layout(windowWidth / 2 - ivDrag.getWidth() / 2,
				ivDrag.getTop(),
				windowWidth / 2 + ivDrag.getWidth() / 2,
				ivDrag.getBottom());
		
		//在sp中记录位置
		Editor edit = sp.edit();
		edit.putInt("lastX", ivDrag.getLeft());
		edit.putInt("lastY", ivDrag.getTop());
		edit.commit();

		注意: 为了能响应点击事件,需要在onTouch中返回false,将事件传递给onClick
	
- 窗体触摸移动

		1. 为了获取触摸事件,首先需要去掉WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		
		2. 其次设置params.type = WindowManager.LayoutParams.TYPE_Phone;
		
		3. 增加权限  <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		
		4. 移动逻辑处理

				case MotionEvent.ACTION_MOVE:
					int dx = (int) (event.getRawX() - startX);
					int dy = (int) (event.getRawY() - startY);

					params.x += dx;
					params.y += dy;

					//控制图片不要超出屏幕边界
					if (params.x < 0) {
						params.x = 0;
					}

					//控制图片不要超出屏幕边界
					if (params.y < 0) {
						params.y = 0;
					}

					//控制图片不要超出屏幕边界
					if (params.x > wm.getDefaultDisplay().getWidth()
							- view.getWidth()) {
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();
					}

					//控制图片不要超出屏幕边界
					if (params.y > wm.getDefaultDisplay().getHeight()
							- view.getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();
					}

					System.out.println("当前位置:" + params.x + ";" + params.y);

					wm.updateViewLayout(view, params);//更新图片的显示位置

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
					
					
- 双击居中

		//图片设置为屏幕居中
		ivDrag.layout(windowWidth / 2 - ivDrag.getWidth() / 2,
				ivDrag.getTop(),
				windowWidth / 2 + ivDrag.getWidth() / 2,
				ivDrag.getBottom());
		
		//在sp中记录位置
		Editor edit = sp.edit();
		edit.putInt("lastX", ivDrag.getLeft());
		edit.putInt("lastY", ivDrag.getTop());
		edit.commit();

		注意: 为了能响应点击事件,需要在onTouch中返回false,将事件传递给onClick
	
- 窗体触摸移动

		1. 为了获取触摸事件,首先需要去掉WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
		
		2. 其次设置params.type = WindowManager.LayoutParams.TYPE_Phone;
		
		3. 增加权限  <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
		
		4. 移动逻辑处理

				case MotionEvent.ACTION_MOVE:
					int dx = (int) (event.getRawX() - startX);
					int dy = (int) (event.getRawY() - startY);

					params.x += dx;
					params.y += dy;

					//控制图片不要超出屏幕边界
					if (params.x < 0) {
						params.x = 0;
					}

					//控制图片不要超出屏幕边界
					if (params.y < 0) {
						params.y = 0;
					}

					//控制图片不要超出屏幕边界
					if (params.x > wm.getDefaultDisplay().getWidth()
							- view.getWidth()) {
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();
					}

					//控制图片不要超出屏幕边界
					if (params.y > wm.getDefaultDisplay().getHeight()
							- view.getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();
					}

					System.out.println("当前位置:" + params.x + ";" + params.y);

					wm.updateViewLayout(view, params);//更新图片的显示位置

					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;

- 火箭发射系统

	1. 写主页面, 控制service的启动和停止

			public void startRocket(View view) {
				startService(new Intent(this, RocketService.class));
				finish();
			}
		
			public void endRocket(View view) {
				stopService(new Intent(this, RocketService.class));
				finish();
			}
	2. 写RocketService, 专用于火箭显示和移动
		
			public class RocketService extends Service {
		
			private WindowManager wm;
			private ImageView ivRocket;
			private View view;
		
			private int startX;
			private int startY;
			private WindowManager.LayoutParams params;
		
			@Override
			public IBinder onBind(Intent intent) {
				return null;
			}
		
			@Override
			public void onCreate() {
				super.onCreate();
				wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		
				view = View.inflate(this, R.layout.view_rocket, null);//初始化界面
		
				ivRocket = (ImageView) view.findViewById(R.id.iv_rocket);//获取火箭对象
				ivRocket.setBackgroundResource(R.drawable.rocket);
		
				AnimationDrawable anim = (AnimationDrawable) ivRocket.getBackground();
				anim.start();//启动火箭的帧动画
		
				params = new WindowManager.LayoutParams();
				params.gravity = Gravity.TOP + Gravity.LEFT;
		
				params.height = WindowManager.LayoutParams.WRAP_CONTENT;
				params.width = WindowManager.LayoutParams.WRAP_CONTENT;
				params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
				params.format = PixelFormat.TRANSLUCENT;
				params.type = WindowManager.LayoutParams.TYPE_PHONE;
		
				wm.addView(view, params);
		
				ivRocket.setOnTouchListener(new OnTouchListener() {
		
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// System.out.println("被触摸了");
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							startX = (int) event.getRawX();
							startY = (int) event.getRawY();
							break;
						case MotionEvent.ACTION_MOVE:
							int dx = (int) (event.getRawX() - startX);
							int dy = (int) (event.getRawY() - startY);
		
							params.x += dx;
							params.y += dy;
		
							wm.updateViewLayout(view, params);// 更新图片的显示位置
		
							startX = (int) event.getRawX();
							startY = (int) event.getRawY();
							break;
						case MotionEvent.ACTION_UP:
							// 发射火箭
							if (params.x > 100 && params.x < 300 && params.y > 320) {
								sendRocket();
		
								//启动BackgroundActivity,展示烟雾
								Intent intent = new Intent(RocketService.this,
										BackgroundActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(intent);
							}
							break;
		
						default:
							break;
						}
		
						return true;
					}
				});
			}
		
			private Handler mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					int y = msg.arg1;
					params.y = y;
					wm.updateViewLayout(view, params);// 更新图片的显示位置
				};
			};
		
			/**
			 * 发射火箭
			 */
			protected void sendRocket() {
				Toast.makeText(RocketService.this, "发射火箭", Toast.LENGTH_SHORT).show();
				
				//将火箭位置调整为屏幕居中
				params.x = wm.getDefaultDisplay().getWidth() / 2 - ivRocket.getWidth()
						/ 2;
				wm.updateViewLayout(view, params);
		
				//执行循环, 每隔一段时间发送当前y坐标, 更新界面
				new Thread() {
					@Override
					public void run() {
						int pos = 380;
						for (int i = 0; i < 11; i++) {
							try {
								Thread.sleep(50);//调整改时间,可以控制火箭速度
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
		
							int y = pos - 38 * i;
							Message msg = Message.obtain();
							msg.arg1 = y;
							mHandler.sendMessage(msg);
						}
					}
				}.start();
			}
		
			@Override
			public void onDestroy() {
				super.onDestroy();
				wm.removeView(view);
				view = null;
			}
			}

		
	3. 写BackgroundActivity, 用于烟雾的展示

			public class BackgroundActivity extends Activity {

			private ImageView ivSmokeBottom;
			private ImageView ivSmokeTop;
		
			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.activity_background);
		
				ivSmokeBottom = (ImageView) findViewById(R.id.iv_smoke_m);
				ivSmokeTop = (ImageView) findViewById(R.id.iv_smoke_t);
		
				// 初始化烟雾
				ivSmokeTop.setVisibility(View.VISIBLE);
				ivSmokeBottom.setVisibility(View.VISIBLE);
		
				//初始化渐变动画
				AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
				animAlpha.setFillAfter(true);
				animAlpha.setDuration(1000);
		
				ivSmokeTop.startAnimation(animAlpha);
				ivSmokeBottom.startAnimation(animAlpha);
		
				//1秒之后,销毁activity
				new Handler().postDelayed(new Runnable() {
		
					@Override
					public void run() {
						finish();
					}
				}, 1000);
			}
			}

			 <activity
            android:name=".BackgroundActivity"
            android:theme="@android:style/Theme.Translucent.NoTitleBar" >
       		 </activity>
   
## day08 ##
- 来电短信黑名单拦截

	- 演示金山卫士相关功能
	- 创建BlackNumberActivity
	- 布局文件

			<RelativeLayout
	        android:layout_width="match_parent"
	        android:layout_height="50dp"
	        android:background="#8866ff00" >
	
	        <TextView
	            android:id="@+id/textView1"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_centerVertical="true"
	            android:layout_marginLeft="5dp"
	            android:text="黑名单管理"
	            android:textColor="#000"
	            android:textSize="22sp" />
	
	        <Button
	            android:id="@+id/button1"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content"
	            android:layout_alignParentRight="true"
	            android:layout_centerVertical="true"
	            android:layout_marginRight="5dp"
	            android:text="添加" />
	    </RelativeLayout>
	- 数据库创建

		public class BlackNumberOpenHelper extends SQLiteOpenHelper {

				public BlackNumberOpenHelper(Context ctx) {
					super(ctx, "blacknumber.db", null, 1);//必须实现该构造方法
				}
			
				/**
				 * 第一次创建数据库
				 */
				@Override
				public void onCreate(SQLiteDatabase db) {
					// 创建表, 三个字段,_id, number(电话号码),mode(拦截模式:电话,短信,电话+短信)
					db.execSQL("create table blacknumber (_id integer primary key autoincrement, number varchar(20), mode integer)");
				}
			
				/**
				 * 数据库升级
				 */
				@Override
				public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
				}
			}
	- 单元测试

		- 创建具备单元测试的Android项目, 拷贝清单文件的相关代码

		  File->New->Project->Android Test Project
				  
				<instrumentation
			        android:name="android.test.InstrumentationTestRunner"
			        android:targetPackage="com.itheima.mobilesafeteach" />

				 <application>
					<uses-library android:name="android.test.runner" /> 
				</application>

	- 增删改查(crud)逻辑实现

			/**
			 * 黑名单数据库封装
			 * @author Kevin
			 *
			 */
			public class BlackNumberDao {
			
				private static BlackNumberDao sInstance;
				private BlackNumberOpenHelper mHelper;
			
				private BlackNumberDao(Context ctx) {
					mHelper = new BlackNumberOpenHelper(ctx);
				};
			
				/**
				 * 获取单例对象
				 * @param ctx
				 * @return
				 */
				public static BlackNumberDao getInstance(Context ctx) {
					if (sInstance == null) {
						synchronized (BlackNumberDao.class) {
							if (sInstance == null) {
								sInstance = new BlackNumberDao(ctx);
							}
						}
					}
			
					return sInstance;
				}
			
				/**
				 * 增加黑名单
				 * @param number
				 * @param mode
				 */
				public void add(String number, int mode) {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put("number", number);
					values.put("mode", mode);
					db.insert("blacknumber", null, values);
					db.close();
				}
			
				/**
				 * 删除黑名单
				 * @param number
				 */
				public void delete(String number) {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					db.delete("blacknumber", "number=?", new String[] { number });
					db.close();
				}
			
				/**
				 * 更新黑名单
				 * @param number
				 * @param mode
				 */
				public void update(String number, int mode) {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put("mode", mode);
					db.update("blacknumber", values, "number=?", new String[] { number });
					db.close();
				}
			
				/**
				 * 查找黑名单
				 * @param number
				 * @return
				 */
				public boolean find(String number) {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					Cursor cursor = db.query("blacknumber",
							new String[] { "number", "mode" }, "number=?",
							new String[] { number }, null, null, null);
			
					boolean result = false;
					if (cursor.moveToFirst()) {
						result = true;
					}
			
				    cursor.close();
					db.close();
					return result;
				}
			
				/**
				 * 查找号码拦截模式
				 * @param number
				 * @return
				 */
				public int findMode(String number) {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					Cursor cursor = db.query("blacknumber", new String[] { "mode" },
							"number=?", new String[] { number }, null, null, null);
			
					int mode = -1;
					if (cursor.moveToFirst()) {
						mode = cursor.getInt(0);
					}
			
					cursor.close();
					db.close();
					return mode;
				}
			
				/**
				 * 查找黑名单列表
				 * @return
				 */
				public ArrayList<BlackNumberInfo> findAll() {
					SQLiteDatabase db = mHelper.getWritableDatabase();
					Cursor cursor = db
							.query("blacknumber", new String[] { "number", "mode" }, null,
									null, null, null, null);
			
					ArrayList<BlackNumberInfo> list = new ArrayList<BlackNumberDao.BlackNumberInfo>();
					while (cursor.moveToNext()) {
						String number = cursor.getString(0);
						int mode = cursor.getInt(1);
						BlackNumberInfo info = new BlackNumberInfo();
						info.number = number;
						info.mode = mode;
						list.add(info);
					}
					cursor.close();
					db.close();
					return list;
				}
			
				/**
				 * 黑名单对象
				 * @author Kevin
				 *
				 */
				public class BlackNumberInfo {
					public String number;
					public int mode;
					
					@Override
					public String toString() {
						return "BlackNumberInfo [number=" + number + ", mode=" + mode + "]";
					}
				}
			}
	- 增删改查单元测试

			public class TestBlackNumberDao extends AndroidTestCase {
			
				/**
				 * 测试数据创建
				 */
				public void testCreateDb() {
					BlackNumberOpenHelper helper = new BlackNumberOpenHelper(getContext());
					helper.getWritableDatabase();
				}
			
				/**
				 * 测试增加黑名单
				 */
				public void testAdd() {
					//添加100个号码,拦截模式随机
					Random random = new Random();
					for (int i = 0; i < 100; i++) {
						int mode = random.nextInt(3) + 1;
						if (i < 10) {
							BlackNumberDao.getInstance(getContext()).add("1381234560" + i,
									mode);
						} else {
							BlackNumberDao.getInstance(getContext()).add("138123456" + i,
									mode);
						}
					}
				}
			
				/**
				 * 测试删除黑名单
				 */
				public void testDelete() {
					BlackNumberDao.getInstance(getContext()).delete("13812345601");
				}
			
				/**
				 * 测试更新黑名单
				 */
				public void testUpdate() {
					BlackNumberDao.getInstance(getContext()).update("13812345600", 2);
				}
			
				/**
				 * 测试查找黑名单
				 */
				public void testFind() {
					boolean find = BlackNumberDao.getInstance(getContext()).find(
							"13812345600");
					assertEquals(true, find);
				}
			
				/**
				 * 测试查找黑名单拦截模式
				 */
				public void testFindMode() {
					int mode = BlackNumberDao.getInstance(getContext()).findMode(
							"13812345600");
					System.out.println("拦截模式:" + mode);
				}
			}

		- 使用命令行查看数据库文件

				1. 运行adb shell进入linux环境
				2. 切换至data/data/包名/databases
				3. 运行sqlite3 *.db,进入数据库
				4. 编写sql语句,进行相关操作.记得加分号(;)结束
				5. .quit退出sqlite,切换到adb shell
	- 介绍convertView的重用机制
	- 介绍ViewHolder的使用方法
	
			//使用static修饰内部类,系统只加载一份字节码文件,节省内存
			static class ViewHolder {
				public TextView tvNumber;
				public TextView tvMode;
			}
			}
		- 使用convertView和ViewHolder进行优化之后,重新使用traceview计算getView的执行时间,进行对比

		- 最终优化结果
		
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					View view = null;
					ViewHolder holder = null;
					if (convertView == null) {
						view = View.inflate(BlackNumberActivity.this,
								R.layout.list_black_number_item, null);
						System.out.println("listview创建");
		
						// viewHolder类似一个容器,可以保存findViewById获得的view对象
						holder = new ViewHolder();
						holder.tvNumber = (TextView) view.findViewById(R.id.tv_number);
						holder.tvMode = (TextView) view.findViewById(R.id.tv_mode);
						// 将viewHolder设置给view对象,保存起来
						view.setTag(holder);
					} else {
						view = convertView;
						holder = (ViewHolder) view.getTag();// 从view对象中得到之前设置好的viewHolder
						System.out.println("listview重用了");
					}
		
					BlackNumberInfo info = mBlackNumberList.get(position);
					holder.tvNumber.setText(info.number);
		
					switch (info.mode) {
					case 1:
						holder.tvMode.setText("拦截电话");
						break;
					case 2:
						holder.tvMode.setText("拦截短信");
						break;
					case 3:
						holder.tvMode.setText("拦截电话+短信");
						break;
					}
		
					return view;
				}

				static class ViewHolder {
					public TextView tvNumber;
					public TextView tvMode;
				}

	- 启动子线程在数据库读取数据

			当数据量比较大时,读取数据比较耗时,为了避免ANR,最好将该逻辑放在子线程中进行, 为了模拟数据量大时访问比较慢的情况,可以让线程休眠1-2秒后再加载数据
	- 加载中的进度条展示
	- 数据分批加载

			分批加载优势:避免一次性加载过多内容, 节省时间和流量
			sql语句: 
			select * from blacknumber limit 20 offset 0, 表示起始位置是0,加载条数为20, 等同于limit 0,20

			/**
			 * 分页查找黑名单列表
			 * 
			 * @return
			 */
			public ArrayList<BlackNumberInfo> findPart(int startIndex) {
				SQLiteDatabase db = mHelper.getWritableDatabase();
		
				Cursor cursor = db.rawQuery(
						"select number,mode from blacknumber order by _id desc limit 20 offset ?",
						new String[] { String.valueOf(startIndex) });
		
				ArrayList<BlackNumberInfo> list = new ArrayList<BlackNumberDao.BlackNumberInfo>();
				while (cursor.moveToNext()) {
					String number = cursor.getString(0);
					int mode = cursor.getInt(1);
					BlackNumberInfo info = new BlackNumberInfo();
					info.number = number;
					info.mode = mode;
					list.add(info);
				}
		
				cursor.close();
				db.close();
				return list;
			}

			/**
			 * 获取黑名单数量
			 * 
			 * @return
			 */
			public int getTotalCount() {
				SQLiteDatabase db = mHelper.getWritableDatabase();
				Cursor cursor = db.rawQuery("select count(*) from blacknumber", null);
		
				int count = 0;
				if (cursor.moveToNext()) {
					count = cursor.getInt(0);
				}
				cursor.close();
				db.close();
				return count;
			}

			--------------------------------------
			
			//监听listview的滑动事件
			lvList.setOnScrollListener(new OnScrollListener() {

			// 滑动状态发生变化
			// 1.静止->滚动 2.滚动->静止 3.惯性滑动
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					//获取当前listview显示的最后一个item的位置
					int lastVisiblePosition = lvList.getLastVisiblePosition();

					//判断是否应该加载下一页
					if (lastVisiblePosition >= mBlackNumberList.size() - 1
							&& !isLoading) {
					int totalCount = BlackNumberDao.getInstance(
							BlackNumberActivity.this).getTotalCount();

					//判断是否已经到达最后一页
					if (mStartIndex >= totalCount) {
						Toast.makeText(BlackNumberActivity.this, "没有更多数据了",
								Toast.LENGTH_SHORT).show();
						return;
					}

					Toast.makeText(BlackNumberActivity.this, "加载更多数据...",
							Toast.LENGTH_SHORT).show();
					System.out.println("加载更多数据...");
					initData();
					}
				}
			}

			-----------------------------------

			//加载数据
			private void initData() {
				pbLoading.setVisibility(View.VISIBLE);//显示进度条
				isLoading = true;
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		
						// 第一页数据
						if (mBlackNumberList == null) {
							mBlackNumberList = BlackNumberDao.getInstance(
									BlackNumberActivity.this).findPart(mStartIndex);
						} else {
							mBlackNumberList.addAll(BlackNumberDao.getInstance(
									BlackNumberActivity.this).findPart(mStartIndex));
						}
		
						mHandler.sendEmptyMessage(0);
					}
				}.start();
			}

			-----------------------------------

			private int mStartIndex;//下一页的起始位置
			private boolean isLoading;// 表示是否正在加载

			private Handler mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					pbLoading.setVisibility(View.GONE);//隐藏进度条
					// 第一页数据
					if (mAdapter == null) {
						mAdapter = new BlackNumberAdapter();
						lvList.setAdapter(mAdapter);
					} else {
						mAdapter.notifyDataSetChanged();//刷新adapter
					}
		
					mStartIndex = mBlackNumberList.size();
					isLoading = false;
				};
			};
			
	- 添加黑名单

			/**
			 * 添加黑名单
			 * 
			 * @param view
			 */
			public void addBlackNumber(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				View view = View.inflate(this, R.layout.dialog_add_black_number, null);
		
				final AlertDialog dialog = builder.create();
				dialog.setView(view, 0, 0, 0, 0);
		
				final EditText etBlackNumber = (EditText) view
						.findViewById(R.id.et_black_number);
				final RadioGroup rgMode = (RadioGroup) view.findViewById(R.id.rg_mode);
		
				Button btnOK = (Button) view.findViewById(R.id.btn_ok);
				Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		
				btnOK.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						String number = etBlackNumber.getText().toString().trim();
						if (!TextUtils.isEmpty(number)) {
							int checkedRadioButtonId = rgMode.getCheckedRadioButtonId();
							int mode = 1;
							// 根据当前选中的RadioButtonId来判断是哪种拦截模式
							switch (checkedRadioButtonId) {
							case R.id.rb_call:
								mode = 1;
								break;
							case R.id.rb_sms:
								mode = 2;
								break;
							case R.id.rb_all:
								mode = 3;
								break;
		
							default:
								break;
							}
		
							// 保存数据库
							BlackNumberDao.getInstance(getApplicationContext()).add(
									number, mode);
		
							// 向列表第一个位置增加黑名单对象,并刷新listview
							//注意: 分页查询时需要逆序排列,保证后添加的最新数据展示在最前面
							BlackNumberInfo info = new BlackNumberInfo();
							info.number = number;
							info.mode = mode;
							mBlackNumberList.add(0, info);
		
							mAdapter.notifyDataSetChanged();
		
							dialog.dismiss();
						} else {
							Toast.makeText(getApplicationContext(), "输入内容不能为空!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
		
				btnCancel.setOnClickListener(new OnClickListener() {
		
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		
				dialog.show();
			}

	- 删除黑名单
		    holder.ivDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//从数据库中删除
					BlackNumberDao.getInstance(getApplicationContext()).delete(
							info.number);
					//从内存列表中删除并刷新listview
					mBlackNumberList.remove(info);
					mAdapter.notifyDataSetChanged();
				}
			});

	- 创建黑名单拦截服务
		- 拦截短信逻辑实现

				逻辑类似手机防盗页面拦截特殊短信指令的代码, 只不过该广播是动态注册,不是静态注册. 动态注册的好处是可以随服务的开启或关闭来决定是否监听广播,而且在同等优先级的前提下,动态注册的广播比静态注册的更先接收到广播(可以通过打印日志进行验证)

	- 设置页面增加黑名单拦截开关

			通过此开关来开启和关闭服务, 逻辑类似来电归属地显示的开关

	- 短信拦截优化
		- 通过关键词智能拦截(介绍)
			- 金山卫士智能拦截简介
			- 金山卫士关键词数据库
		
					查看第四天资料,金山卫士apk解压文件,assets目录下找firewall_sys_rules.db, 该数据制定了短信和来电的拦截规则

					短信拦截规则: 根据关键词对短信内容进行过滤.
					比如fapiao

					//对短信内容进行关键词过滤
					String messageBody = msg.getMessageBody();
					if (messageBody != null && messageBody.contains("fapiao")) {
						abortBroadcast();
					}
					
		- 分词(介绍)

				单纯依靠关键词进行过滤有时会出现一些问题, 比如:
				laogong, nikan,wode toufapiaobupiaoliang....
				
				所以有时候会对每一句话进行分词处理,比如可以将上述语句先拆分成不同的词语:laogong,nikan,wode,toufa,piaobupiaoliang, 然后在这些词汇中对关键词再进行过滤

				lucene分词检索框架
						
		- 短信拦截的兼容性处理
			
				4.4以上系统手机对短信权限进一步限制,导致无法拦截短信,可以通过监听短信数据库的变化,及时删除最新入库的垃圾短信来实现短信拦截的目的. 为了避免误删旧的短信,需要和短信广播结合起来使用
				
	- 来电拦截
			1. 挂断电话的API早期版本endCall()是可以使用的，现在不可以用了；但本身挂断电话这个功能是存在的
			
			2. 很多服务都是获取远程服务的代理对象IBinder,再调用里面的方法的.例如:
				    IBinder b = ServiceManager.getService(ALARM_SERVICE);
                    IAlarmManager service = IAlarmManager.Stub.asInterface(b);
                    return new AlarmManager(service);
			3. 于是我们跟踪TelephoneyManager,查看它的对象到底是如何创建的.我们跟踪到了这样一个方法: 
					private ITelephony getITelephony() {
       				return ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
    				}
			该方法返回一个ITelephony对象, 查看ITelephony对象的方法,发现有endCall方法
			4. 于是我们将获取ITelephony的代码拷贝到自己的项目中,发现无法导包,因为根本有没有ServiceManager这个类,但我们知道它肯定存在,因为TelephonyManager就引用了该类,只不过android系统隐藏了这个类,
			5. 为了调用隐藏类的方法,我们想到了反射

		- 通过反射获取endCall方法

				/**
				 * 挂断电话 
				 * 注意加权限: <uses-permission
				 * android:name="android.permission.CALL_PHONE"/>
				 */
				public void endCall() {
					try {
						// 获取ServiceManager
						Class clazz = BlackNumberService.class.getClassLoader().loadClass(
								"android.os.ServiceManager");
						Method method = clazz.getDeclaredMethod("getService", String.class);// 获取方法getService
						IBinder binder = (IBinder) method.invoke(null,
								Context.TELEPHONY_SERVICE);// 方法时静态的,不需要传递对象进去
			
						ITelephony telephony = ITelephony.Stub.asInterface(binder);// 获取ITelephony对象,前提是要先配置好aidl文件
						telephony.endCall();//挂断电话
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				注意加权限: <uses-permission
				  android:name="android.permission.CALL_PHONE"/>
				  
## day09 ##
- 清除来电记录

				代码挂断电话后,被挂断的号码仍然会进入通话记录中, 我们需要将这种记录删除.

				查看数据库contacts2中的表calls

				/**
				 * 删除通话记录
				 */
				private void deleteCallLog(String number) {
					getContentResolver().delete(Uri.parse("content://call_log/calls"),
							"number=?", new String[] {number});
				}

				注意加权限: <uses-permission android:name="android.permission.READ_CALL_LOG"/>
   				 <uses-permission android:name="android.permission.WRITE_CALL_LOG"/>

		- 通过内容观察者,解决通话记录删除失败的问题

				系统在往通话记录的数据库中插入数据时是异步逻辑,所以当数据库还没来得及添加电话日志时,我们就执行了删除日志的操作,从而导致删除失败,为了避免这个问题,可以监听数据库变化,当数据库发生变化后,我们才执行删除操作,从而解决这个问题

				/**
				 * 内容观察者
				 * @author Kevin
				 *
				 */
				class MyContentObserver extends ContentObserver {
			
					private String incomingNumber;
			
					public MyContentObserver(Handler handler, String incomingNumber) {
						super(handler);
						this.incomingNumber = incomingNumber;
					}
			
					/**
					 * 当数据库发生变化时,回调此方法
					 */
					@Override
					public void onChange(boolean selfChange) {
						System.out.println("call log changed...");
						
						//删除日志
						deleteCallLog(incomingNumber);
						
						//删除完日志后,注销内容观察者
						getContentResolver().unregisterContentObserver(mObserver);
					}
				}

				------------------------------

				//监听到来电时,注册内容观察者
				mObserver = new MyContentObserver(new Handler(),
							incomingNumber);
					
				//注册内容观察者
				getContentResolver().registerContentObserver(
							Uri.parse("content://call_log/calls"), true,
							mObserver);
				
				------------------------------

				注意:
				补充Android2.3模拟器上需要多加权限
				 <uses-permission android:name="android.permission.WRITE_CONTACTS"/>
- 短信备份

	- 查看短信数据库

			data/data/com.android.provider.telephony/databases/mmssms.db
			address 短信收件人发件人地址
			date 短信接收的时间
			type 1 发进来短信 2 发出去短信
			read 1 已读短信 0 未读短信
			body 短信内容

	- 读取短信数据库内容

			查看系统源码,找到uri地址:packages\provider\platform_packages_providers_telephonyprovider-master

			Uri uri = Uri.parse("content://sms/");// 所有短信
			Cursor cursor = ctx.getContentResolver().query(uri,
				new String[] { "address", "date", "type", "body" }, null, null,
				null);

			遍历cursor,获取短信信息

			注意权限: <uses-permission android:name="android.permission.READ_SMS"/>
	        <uses-permission android:name="android.permission.WRITE_SMS"/>

	- 将短信内容序列化为xml文件

			sms.xml
			<?xml version="1.0" encoding="utf-8"?>
			<smss>
				<sms>
				    <address>5556</address>
				    <date>10499949433</date>
				    <type>1</type>
					<body>wos shi haoren</body>
				</sms>
				
				<sms>
				    <address>13512345678</address>
				    <date>1049994889433</date>
				    <type>2</type>
					<body>hell world hei ma</body>
				</sms>
			</smss>

			------------------------------

			XmlSerializer serializer = Xml.newSerializer();// 初始化xml序列化工具
			serializer.setOutput(new FileOutputStream(output), "utf-8");//设置输出流
			/*
			 * startDocument(String encoding, Boolean standalone)encoding代表编码方式
			 * standalone 用来表示该文件是否关联其它外部的约束文件。 若值是 ”yes” 表示没有关联外部规则文件，若值是 ”no”
			 * 则表示有关联外部规则文件。默认值是 “yes”。
			 * <?xml version="1.0" encoding="utf-8" standalone=true ?>
			 * 
			 * 参数2传null时,不会生成standalone=true/false的语句
			 */
			serializer.startDocument("utf-8", null);// 生成xml顶栏描述语句<?xml
													// version="1.0"
													// encoding="utf-8"?>
			serializer.startTag(null, "smss");//起始标签
			serializer.text(body);// 设置内容
			serializer.endTag(null, "smss");//结束标签
			serializer.endDocument();//结束xml文档

			------------------------------

			AToolsActivity.java
			
			/**
			 * 短信备份
			 */
			public void smsBackup(View view) {
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					try {
						SmsUtils.smsBackup(this,
								new File(Environment.getExternalStorageDirectory(),
										"sms.xml"));
						Toast.makeText(this, "备份成功!", Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						e.printStackTrace();
						Toast.makeText(this, "备份失败!", Toast.LENGTH_SHORT).show();
					}
		
				} else {
					Toast.makeText(this, "没有检测到sdcard!", Toast.LENGTH_SHORT).show();
				}
			}

	- 异步备份短信,并显示进度条

			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("正在备份短信...");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置显示风格,此风格将展示一个进度条
			mProgressDialog.show();

			将ProgressDialog的引用传递给工具类,在工具类中更新进度
			SmsUtils.smsBackup(AToolsActivity.this, new File(
								Environment.getExternalStorageDirectory(),
								"sms.xml"), mProgressDialog);

			--------------------------------

			//短信工具类中更新进度条的逻辑
			progressDialog.setMax(cursor.getCount());// 设置进度条最大值

			Thread.sleep(500);//为了方便看效果,故意延时1秒钟
			progress++;
			progressDialog.setProgress(progress);//更新进度

			--------------------------------

			模拟需求变动的情况
			1. A负责短信备份界面, B负责短信工具类
			2. 将ProgressDialog改动为ProgressBar, 需要A通知B改动
			3. 又将ProgressBar改回ProgressDialog, 需要A通知B改动
			4. 既有ProgressBar,又要求有ProgressDialog, 需要A通知B改动

			问题: B除了负责底层业务逻辑之外,额外还需要帮A处理界面逻辑,如何实现A和B的解耦?

	- 使用回调接口通知进度,优化代码,实现解耦

			/**
			 * 短信备份回调接口
			 * @author Kevin
			 *
			 */
			public interface SmsBackupCallback {
				/**
				 * 备份之前获取短信总数
				 * @param total
				 */
				public void preSmsBackup(int total);
				/**
				 * 备份过程中实时获取备份进度
				 * @param progress
				 */
				public void onSmsBackup(int progress);
			}

			----------------------------

			SmsUtils.smsBackup(AToolsActivity.this, new File(
								Environment.getExternalStorageDirectory(),
								"sms.xml"), new SmsBackupCallback() {

							@Override
							public void preSmsBackup(int total) {
								mProgressDialog.setMax(total);
							}

							@Override
							public void onSmsBackup(int progress) {
								mProgressDialog.setProgress(progress);
							}
						});

- 短信还原(介绍)

- 应用管理器(AppManagerActivity)

	- 介绍金山卫士的应用管理器
	- 参考金山卫士,编写布局文件
	- 计算内置存储空间和sdcard剩余空间

			/**
			 * 获取剩余空间
			 * 
			 * @param path
			 * @return
			 */
			private String getAvailSpace(String path) {
				StatFs stat = new StatFs(path);
		
				// Integer.MAX_VALUE;
				// int最大只能表示到2G, 在一些高端手机上不足够接收大于2G的容量,所以可以用long来接收, 相乘的结果仍是long类型
				long blocks = stat.getAvailableBlocks();// 获取可用的存储块个数
				long blockSize = stat.getBlockSize();// 获取每一块的大小
		
				return Formatter.formatFileSize(this, blocks * blockSize);// 将字节转化为带有容量单位的字符串
			}

			//获取内存的地址
			Environment.getDataDirectory().getAbsolutePath()
			//获取sdcard的地址
			Environment.getExternalStorageDirectory().getAbsolutePath()

	- 获取已安装的应用列表

			/**
			 * 应用信息封装
			 * 
			 * @author Kevin
			 * 
			 */
			public class AppInfo {
			
				public String name;// 名称
				public String packageName;// 包名
				public Drawable icon;// 图标
			
				public boolean isUserApp;// 是否是用户程序
				public boolean isRom;// 是否安装在内置存储器中
			
				@Override
				public String toString() {
					return "AppInfo [name=" + name + ", packageName=" + packageName + "]";
				}
			}

			-------------------------------
		
			AppInfoProvider.java

			/**
			 * 获取已安装的应用信息
			 * @param ctx
			 */
			public static ArrayList<AppInfo> getAppInfos(Context ctx) {
		
				ArrayList<AppInfo> infoList = new ArrayList<AppInfo>();
		
				PackageManager pm = ctx.getPackageManager();
				List<PackageInfo> packages = pm.getInstalledPackages(0);// 获取已经安装的所有包
		
				for (PackageInfo packageInfo : packages) {
					AppInfo info = new AppInfo();
		
					String packageName = packageInfo.packageName;// 获取包名
					Drawable icon = packageInfo.applicationInfo.loadIcon(pm);// 获取图标
					String name = packageInfo.applicationInfo.loadLabel(pm).toString();// 获取名称
		
					info.packageName = packageName;
					info.icon = icon;
					info.name = name;
		
					infoList.add(info);
				}
		
				return infoList;
			}

			进行单元测试,打印应用列表

	- Android的应用程序安装位置
	
 			pc电脑默认安装在C:\Program Files
  			Android 的应用安装在哪里呢，如果是用户程序，安装在data/app/目录下,
			系统自带应用安装在system/app/目录下
  
  			安装Android软件 做两件事 
    		 A：把APK拷贝到data/app/目录下
    		 B：把安装包信息写到data/system/目录下两个文件packages.list 和 packages.xml

			安装包信息在data/system/
			Packages.list 里面的0 表示系统应用 1 表示用户应用
			Packages.xml是存放应用的一些权限信息的；

	- 判断是系统应用还是用户应用

		1. 解释标识左移几位的效果public static final int FLAG_SYSTEM = 1<<0; 
		2. 不同标识可以加起来一起使用, 加起来的结果和特定标识进行与运算,通过计算结果可以知道具不具备该特定标识的相关功能, 这种方式叫做状态机
		3. 玩游戏距离: 喝药水,加功能(加血,加攻击力,加防御,加魔法值),可以通过状态机来表示该药水具备哪些特性 
		4. 实际开发的机顶盒举例:{"cctv1":true,"cctv2":false,"cctv3":true}->{"flag":101}, 可以节省流量

				if ((flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					info.isUserApp = false;// 系统应用
				} else {
					info.isUserApp = true;//用户应用
				}
	
				if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
					info.isRom = false;// 安装位置是sdcard
				} else {
					info.isRom = true;//安装位置是内置存储器
				}

	- ListView列表展现

		- 仿照金山卫士编写item布局
		- 异步加载应用列表数据,并显示进度条

				加载应用列表有时会比较耗时,最好放在子线程执行. 加载期间显示加载中的布局
			
				 <FrameLayout
		        android:layout_width="match_parent"
		        android:layout_height="match_parent" >
		
		        <ListView
		            android:id="@+id/lv_list"
		            android:layout_width="match_parent"
		            android:layout_height="match_parent" >
		        </ListView>
		
		        <LinearLayout
		            android:id="@+id/ll_loading"
		            android:visibility="gone"
		            android:layout_width="match_parent"
		            android:layout_height="wrap_content"
		            android:layout_gravity="center"
		            android:gravity="center"
		            android:paddingBottom="50dp"
		            android:orientation="vertical" >
		
		            <ProgressBar
		                android:id="@+id/progressBar1"
		                android:layout_width="wrap_content"
		                android:layout_height="wrap_content" />
		
		            <TextView
		                android:id="@+id/textView1"
		                android:layout_width="wrap_content"
		                android:layout_height="wrap_content"
		                android:text="正在加载应用列表..." />
		        </LinearLayout>

				<TextView
		            android:id="@+id/tv_head"
		            android:layout_width="match_parent"
		            android:layout_height="wrap_content"
		            android:background="#FF888888"
		            android:textColor="#fff"
		            android:text="用户程序(5)"
		             />
		    </FrameLayout>

		- 清单文件中注册安装位置

			 	manifest根标签具有这样的属性,用来指定apk的安装位置, 缺省值是手机内存 android:installLocation="auto", 可以修改为auto, 表示优先使用手机内存, 如果内存不够,再使用sdcard. 不建议强制安装在sdcard,因为某些手机没有sdcard,会导致安装失败.设置完成后,就可以在系统应用管理中,移动apk的安装位置了.

	- 复杂ListView的展现方式

			核心思想: 重写BaseAdapter自带的getItemViewType方法来返回item类型,重写getViewTypeCount方法返回类型个数

			//应用信息适配器
			class AppInfoAdapter extends BaseAdapter {
		
				/**
				 * 返回总数量,包括两个标题栏
				 */
				@Override
				public int getCount() {
					return 1 + mUserAppList.size() + 1 + mSystemAppList.size();
				}
		
				/**
				 * 返回当前的对象
				 */
				@Override
				public AppInfo getItem(int position) {
					if (position == 0 || position == 1 + mUserAppList.size()) {//判断是否是标题栏
						return null;
					}
					
					AppInfo appInfo;
					if (position < mUserAppList.size() + 1) {//判断是否是用户应用
						appInfo = mUserAppList.get(position - 1);
					} else {//系统应用
						appInfo = mSystemAppList
								.get(position - mUserAppList.size() - 2);
					}
					return appInfo;
				}
		
				@Override
				public long getItemId(int position) {
					return position;
				}
		
				/**
				 * 返回当前view的类型
				 */
				@Override
				public int getItemViewType(int position) {
					if (position == 0 || position == 1 + mUserAppList.size()) {
						return 1;// 标题栏
					} else {
						return 0;// 应用信息
					}
				}
		
				/**
				 * 返回当前item的类型个数
				 */
				@Override
				public int getViewTypeCount() {
					return 2;
				}
		
				@Override
				public View getView(int position, View convertView, ViewGroup parent) {
					int type = getItemViewType(position);//获取item的类型
		
					if (convertView == null) {//初始化convertView
						switch (type) {
						case 1://标题
							convertView = new TextView(AppManagerActivity.this);
							((TextView) convertView).setTextColor(Color.WHITE);
							((TextView) convertView).setBackgroundColor(Color.GRAY);
							break;
						case 0://应用信息
							ViewHolder holder;
							convertView = View.inflate(AppManagerActivity.this,
									R.layout.list_appinfo_item, null);
		
							holder = new ViewHolder();
							holder.ivIcon = (ImageView) convertView
									.findViewById(R.id.iv_icon);
							holder.tvName = (TextView) convertView
									.findViewById(R.id.tv_name);
							holder.tvLocation = (TextView) convertView
									.findViewById(R.id.tv_location);
		
							convertView.setTag(holder);
							break;
		
						default:
							break;
						}
					}
		
					//根据类型来更新view的显示内容
					switch (type) {
					case 1:
						if (position == 0) {
							((TextView) convertView).setText("用户程序("
									+ mUserAppList.size() + ")");
						} else {
							((TextView) convertView).setText("系统程序("
									+ mUserAppList.size() + ")");
						}
						break;
					case 0:
						ViewHolder holder = (ViewHolder) convertView.getTag();
						AppInfo appInfo = getItem(position);
						holder.ivIcon.setImageDrawable(appInfo.icon);
						holder.tvName.setText(appInfo.name);
		
						if (appInfo.isRom) {
							holder.tvLocation.setText("手机内存");
						} else {
							holder.tvLocation.setText("外置存储卡");
						}
		
						break;
					default:
						break;
					}
		
					return convertView;
				}
			}

- PopupWindow使用

		专门写一个Demo,用于PopupWindow的演示

		/**
		 * 显示弹窗
		 * 
		 * @param view
		 */
		public void showPopupWindow(View view) {
			TextView contentView = new TextView(this);
			contentView.setText("我是弹窗哦!");
			contentView.setTextColor(Color.RED);
	
			PopupWindow popup = new PopupWindow(contentView, 100, 100, true);//设置尺寸及获取焦点
			popup.setBackgroundDrawable(new ColorDrawable(Color.BLUE));//设置背景颜色
			// popup.showAtLocation(rlRoot, Gravity.LEFT + Gravity.TOP, 0,
			// 0);//显示在屏幕的位置
			popup.showAsDropDown(btnPop, 0, 0);// 显示在某个控件的正下方
		}

- 将PopupWindow应用到项目当中

		//listview监听
		lvList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentAppInfo = mAdapter.getItem(position);
				System.out.println(mCurrentAppInfo.name + "被点击!");
				showPopupWindow(view);
			}
		});

		-----------------------------------

		/**
		 * 显示弹窗
		 */
		private void showPopupWindow(View view) {
			View contentView = View.inflate(this, R.layout.popup_appinfo, null);
	
			TextView tvUninstall = (TextView) contentView
					.findViewById(R.id.tv_uninstall);
			TextView tvLaunch = (TextView) contentView.findViewById(R.id.tv_launch);
			TextView tvShare = (TextView) contentView.findViewById(R.id.tv_share);
	
			//设置监听事件
			tvUninstall.setOnClickListener(this);
			tvLaunch.setOnClickListener(this);
			tvShare.setOnClickListener(this);
	
			//初始化PopupWindow
			PopupWindow popup = new PopupWindow(contentView,
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			popup.setBackgroundDrawable(new ColorDrawable());// 必须设置背景,否则无法返回
			popup.showAsDropDown(view, 50, -view.getHeight());// 显示弹窗
	
			//渐变动画
			AlphaAnimation alpha = new AlphaAnimation(0, 1);
			alpha.setDuration(500);
			alpha.setFillAfter(true);
	
			//缩放动画
			ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1,
					Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
					0.5f);
			scale.setDuration(500);
			scale.setFillAfter(true);
	
			//动画集合
			AnimationSet set = new AnimationSet(false);
			set.addAnimation(alpha);
			set.addAnimation(scale);
	
			//运行动画
			contentView.startAnimation(set);
		}

		-----------------------------------

		@Override
		public void onClick(View v) {
			if (mCurrentAppInfo == null) {
				return;
			}
			switch (v.getId()) {
			case R.id.tv_uninstall:
				System.out.println("卸载" + mCurrentAppInfo.name);
				break;
			case R.id.tv_launch:
				System.out.println("启动" + mCurrentAppInfo.name);
				break;
			case R.id.tv_share:
				System.out.println("分享" + mCurrentAppInfo.name);
				break;
	
			default:
				break;
			}
		}

- 卸载,启动和分享的逻辑

		/**
		 * 卸载
		 */
		private void uninstall() {
			if (mCurrentAppInfo.isUserApp) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_DELETE);
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				intent.setData(Uri.parse("package:" + mCurrentAppInfo.packageName));
				startActivityForResult(intent, 0);
			} else {
				Toast.makeText(this, "无法卸载系统程序!", Toast.LENGTH_SHORT).show();
			}
		}
	
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// 卸载成功后重新加载应用列表
			// 此处不必判断resultCode是否是RESULT_OK, 因为4.1+系统即使卸载成功也始终返回RESULT_CANCEL
			loadAppInfos();
			super.onActivityResult(requestCode, resultCode, data);
		}

		------------------------------------

		/**
		 * 启动App
		 */
		private void launchApp() {
			try {
				PackageManager pm = this.getPackageManager();
				Intent intent = pm
						.getLaunchIntentForPackage(mCurrentAppInfo.packageName);// 获取应用入口的Intent
				startActivity(intent);// 启动应用
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "无法启动该应用!", Toast.LENGTH_SHORT).show();
			}
		}

		------------------------------------

		/**
		 * 分享 此方法会呼起系统中所有支持文本分享的app列表
		 */
		private void shareApp() {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.addCategory(Intent.CATEGORY_DEFAULT);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_TEXT,
					"分享给你一个很好的应用哦! 下载地址: https://play.google.com/apps/details?id="
							+ mCurrentAppInfo.packageName);
			startActivity(intent);
		}

- ListView分类栏常驻效果

		//原理: 写一个TextView常驻在ListView顶栏, 样式和item中分类栏的样式完全一样. 监听ListView的滑动事件,动态修改TextView的内容

		//设置listview的滑动监听
		lvList.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				System.out.println("onScroll:" + firstVisibleItem);
				if (mUserAppList != null && mSystemAppList != null) {
					if (firstVisibleItem <= mUserAppList.size()) {
						tvListHead.setText("用户应用(" + mUserAppList.size() + ")");
					} else {
						tvListHead.setText("系统应用(" + mSystemAppList.size()
								+ ")");
					}
				}
			}
		});
		
## Android	线性布局权重

**权重（layout_weight）**：即为当前线性布局指定方向(水平、竖直)上剩余空间的一个分配规则。

注：以下皆以水平方向上剩余空间为案例分析，当前测试手机分辨率为480*320，屏幕像素密度为mdpi，即1dp = 1px；

####案例一布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <!--内部控件水平排列-->
	   <TextView
	        android:layout_width="0dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="0dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>

当前布局显示效果

![](http://i.imgur.com/Rrlc1pN.jpg)

由上图可见当前黑色部分占用屏幕的四分之三，绿色部分占屏幕的四分之一

黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：0dp 
	第二个子控件未分配权重前所占宽度：0dp 
	当前屏幕剩余空间总数：320dp-0dp-0dp = 320dp，将当前320dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：0dp+（（320dp-0dp-0dp）*3）/4 = 240dp
	第二个子控件分配权重后宽度：0dp+（320dp-0dp-0dp）/4 = 80dp


####案例二布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="60dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="60dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
	
当前布局显示效果

![](http://i.imgur.com/6PwNgOH.jpg)


黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：60dp 
	第二个子控件未分配权重前所占宽度：60dp 
	当前屏幕剩余空间总数：320dp-60dp-60dp = 200dp，将当前200dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：60dp+（（320dp-60dp-60dp）*3）/4 = 210dp
	第二个子控件分配权重后宽度：60dp+（320dp-60dp-60dp）/4 = 110dp

####案例三布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="260dp"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="260dp"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
当前布局显示效果

![](http://i.imgur.com/Ldq0yf1.jpg)


黑色区域宽度，绿色区域宽度计算方式：

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：260dp 
	第二个子控件未分配权重前所占宽度：260dp 
	当前屏幕剩余空间总数：320dp-260dp-260dp = -200dp，将当前-200dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：260dp+（（320dp-260dp-260dp）*3）/4 = 110dp
	第二个子控件分配权重后宽度：260dp+（320dp-260dp-260dp）/4 = 210dp


####案例四布局：

	<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:tools="http://schemas.android.com/tools"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:orientation="horizontal"
	    tools:context=".MainActivity" >
	   <TextView
	        android:layout_width="fill_parent"
	        android:layout_height="120dp"
	        android:layout_weight="3"
	        android:background="@android:color/black"/>
	    <TextView
	    	android:layout_width="fill_parent"
		    android:layout_height="120dp"
		    android:layout_weight="1"
		    android:background="@android:color/holo_green_dark"/>
	</LinearLayout>
当前布局显示效果

![](http://i.imgur.com/FuTljJV.jpg)

	当前屏幕横屏宽度：320dp
	第一个子控件未分配权重前所占宽度：fill_parent 即为充满横屏(320dp)
	第二个子控件未分配权重前所占宽度：fill_parent 即为充满横屏(320dp)
	当前屏幕剩余空间总数：320dp-320dp-320dp = -320dp，将当前-320dp按权重分配给两个子控件，子控件一分配到四分之三，子控件二分配到四分之一
	
	第一个子控件分配权重后宽度：320dp+（（320dp-320dp-320dp）*3）/4 = 80dp
	第二个子控件分配权重后宽度：320dp+（320dp-320dp-320dp）/4 = 240dp

由以上四个案例总结：
	
	例:如水平方向上的线性布局LinearLayout控件L中，包含两个水平占用空间的控件A,B。
	   L控件：L控件宽度layout_width = width_l
	   A控件：a控件宽度layout_width = width_a    a控件权重layout_weight = weight_a
	   B控件：b控件宽度layout_width = width_b    b控件权重layout_weight = weight_b

	L中子控件最终占用宽度 = 原有宽度(width_a)+剩余空间分配量
	
	A所占宽度 = width_a + (width_l-width_a-width_b)*weight_a/(weight_a+weight_b)
	B所占宽度 = width_b + (width_l-width_a-width_b)*weight_b/(weight_a+weight_b)
	
	由以上案例可以得出推断：

	情况一：当L中内部子控件(A,B)的宽度之和大于L的总宽度时，即(width_l-width_a-width_b)<0时，weight_a/(weight_a+weight_b)比例的值越大，当前控件所占空间越小。

	情况二：当L中内部子控件(A,B)的宽度之和小于L的总宽度时，即(width_l-width_a-width_b)>0时，weight_a/(weight_a+weight_b)比例的值越大，当前控件所占空间越大。
	 
## day11 ##
	- 系统进程显示和隐藏

		- 创建进程管理设置页面:ProcessManagerSettingActivity
		- 编写设置页面布局文件
		- 监听Checkbox的勾选事件,更新本地SharePreference

				// 根据本地记录,更新checkbox状态
				boolean showSystem = mPrefs.getBoolean("show_system_process", true);
				if (showSystem) {
					cbShowSystem.setChecked(true);
					cbShowSystem.setText("显示系统进程");
				} else {
					cbShowSystem.setChecked(false);
					cbShowSystem.setText("不显示系统进程");
				}
		
				// 设置状态勾选监听
				cbShowSystem.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cbShowSystem.setText("显示系统进程");
							mPrefs.edit().putBoolean("show_system_process", true).commit();
						} else {
							cbShowSystem.setText("不显示系统进程");
							mPrefs.edit().putBoolean("show_system_process", false)
									.commit();
						}
					}
				});

		- 根据sp记录的是否显示系统进程,更新listview的显示个数

				@Override
				public int getCount() {
					// 通过判断是否显示系统进程,更新list的数量
					boolean showSystem = mPrefs.getBoolean("show_system_process", true);
					if (showSystem) {
						return 1 + mUserProcessList.size() + 1 + mSystemProcessList.size();
					} else {
						return 1 + mUserProcessList.size();
					}
				}

		- 保证勾选框改变后,listview可以立即刷新

				public void setting(View view) {
					startActivityForResult(new Intent(this,
							ProcessManagerSettingActivity.class), 0);
				}
			
				@Override
				protected void onActivityResult(int requestCode, int resultCode, Intent data) {
					// 当从设置页面回跳回来之后,刷新listview
					mAdapter.notifyDataSetChanged();
				}

	- 锁屏清理

		- 演示金山进程管理效果
		- 后台启动服务,监听广播

				//判断锁屏清理的广播是否正在运行
				boolean serviceRunning = ServiceStatusUtils.isServiceRunning(
								"com.itheima.mobilesafeteach.service.AutoKillService", this);
		
				if (serviceRunning) {
					cbLockClear.setChecked(true);
					cbLockClear.setText("当前状态:锁屏清理已经开启");
				} else {
					cbLockClear.setChecked(false);
					cbLockClear.setText("当前状态:锁屏清理已经关闭");
				}
		
				cbLockClear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Intent intent = new Intent(ProcessManagerSettingActivity.this,
								AutoKillService.class);
						if (isChecked) {
							// 启动锁屏清理的服务
							startService(intent);
							cbLockClear.setText("当前状态:锁屏清理已经开启");
						} else {
							// 关闭锁屏清理的服务
							stopService(intent);
							cbLockClear.setText("当前状态:锁屏清理已经关闭");
						}
					}
				});

				-------------------------------------

				/**
				 * 锁屏清理进程的服务
				 * 
				 * @author Kevin
				 * 
				 */
				public class AutoKillService extends Service {
				
					private InnerScreenOffReceiver mReceiver;
				
					@Override
					public IBinder onBind(Intent intent) {
						return null;
					}
				
					@Override
					public void onCreate() {
						super.onCreate();
						//监听屏幕关闭的广播, 注意,该广播只能在代码中注册,不能在清单文件中注册
						mReceiver = new InnerScreenOffReceiver();
						IntentFilter filter = new IntentFilter();
						filter.addAction(Intent.ACTION_SCREEN_OFF);
						registerReceiver(mReceiver, filter);
					}
				
					@Override
					public void onDestroy() {
						super.onDestroy();
				
						unregisterReceiver(mReceiver);
						mReceiver = null;
					}
				
					/**
					 * 锁屏关闭的广播接收者
					 * 
					 * @author Kevin
					 * 
					 */
					class InnerScreenOffReceiver extends BroadcastReceiver {
				
						@Override
						public void onReceive(Context context, Intent intent) {
							System.out.println("屏幕关闭...");
							// 杀死后台所有运行的进程
							ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
							List<RunningAppProcessInfo> runningAppProcesses = am
									.getRunningAppProcesses();
				
							for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
								// 跳过手机卫士的服务
								if (runningAppProcessInfo.processName.equals(ctx.getPackageName())) {
									return;
								}

								am.killBackgroundProcesses(runningAppProcessInfo.processName);
							}
						}
				
					}
				}

	- 定时器清理(介绍)

			// 在AutoKillService的onCreate中启动定时器,定时清理任务
			mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
	
				@Override
				public void run() {
					System.out.println("5秒运行一次!");
				}
	
			}, 0, 5000);

			@Override
			protected void onDestroy() {
				super.onDestroy();
				mTimer.cancel();
				mTimer = null;
			}

- 桌面Widget(窗口小部件)

	- widget介绍(Android, 瑞星,早期word)
	- widget谷歌文档查看(API Guide->App Components->App Widget)
	- widget开发流程

			1. 在com.itheima.mobilesafe.receiver目录下创建MyWidget并且继承AppWidgetProvider
			2. 在功能清单文件注册，参照文档

				<receiver android:name=".receiver.MyWidget" >
		            <intent-filter>
		                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
		            </intent-filter>
		
		            <meta-data
		                android:name="android.appwidget.provider"
		                android:resource="@xml/appwidget_info" />
		        </receiver>

			3. 在res/xml/创建文件example_appwidget_info.xml拷贝文档内容
				
				<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"    
			    android:minWidth="294dp"    
			    android:minHeight="72dp"//能被调整的最小宽高，若大于minWidth minHeight 则忽略    
			    android:updatePeriodMillis="86400000"//更新周期,毫秒,最短默认半小时    
			    android:previewImage="@drawable/preview"//选择部件时 展示的图像,3.0以上使用,默认是ic_launcher    
			    android:initialLayout="@layout/example_appwidget"//布局文件
			    android:configure="com.example.android.ExampleAppWidgetConfigure"//添加widget之前,先跳转到配置的activity进行相关参数配置,这个我们暂时用不到       
			    android:resizeMode="horizontal|vertical"//widget可以被拉伸的方向。horizontal表示可以水平拉伸，vertical表示可以竖直拉伸
				android:widgetCategory="home_screen|keyguard"//分别在屏幕主页和锁屏状态也能显示(4.2+系统才支持)
   				android:initialKeyguardLayout="@layout/example_keyguard"//锁屏状态显示的样式(4.2+系统才支持)
				>
				</appwidget-provider>

			4. 精简example_appwidget_info.xml文件,最终结果:

				<appwidget-provider xmlns:android="http://schemas.android.com/apk/res/android"
				    android:minWidth="294dp"    
    				android:minHeight="72dp"
				    android:updatePeriodMillis="1800000"
				    android:initialLayout="@layout/appwidget"
				   >
				</appwidget-provider>

			5. widget布局文件:appwidget.xml

				<?xml version="1.0" encoding="utf-8"?>
				<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
				    android:layout_width="match_parent"
				    android:layout_height="match_parent"
				    android:orientation="vertical" >
				
				    <TextView
				        android:id="@+id/textView1"
				        android:layout_width="wrap_content"
				        android:layout_height="wrap_content"
				        android:background="#f00"
				        android:text="我是widget,哈哈哈"
				        android:textSize="30sp" />
				</LinearLayout>

	- 简单演示,高低版本对比
	- 仿照金山widget效果, apktool反编译,抄金山布局文件(业内抄袭成风)

			1. 反编译金山apk

				使用apktool,可以查看xml文件内容
				apktool d xxx.apk
			2. 在金山清单文件中查找 APPWIDGET_UPDATE, 找到widget注册的代码
			3. 拷贝金山widget的布局文件process_widget_provider.xml到自己的项目中
			4. 从金山项目中拷贝相关资源文件,解决报错 
			5. 运行,查看效果	
	
	- widget生命周期

			/**
			 * 窗口小部件widget
			 * 
			 * @author Kevin
			 * 
			 */
			public class MyWidget extends AppWidgetProvider {
			
				/**
				 * widget的每次变化都会调用onReceive
				 */
				@Override
				public void onReceive(Context context, Intent intent) {
					super.onReceive(context, intent);
					System.out.println("MyWidget: onReceive");
				}
			
				/**
				 * 当widget第一次被添加时,调用onEnable
				 */
				@Override
				public void onEnabled(Context context) {
					super.onEnabled(context);
					System.out.println("MyWidget: onEnabled");
				}
			
				/**
				 * 当widget完全从桌面移除时,调用onDisabled
				 */
				@Override
				public void onDisabled(Context context) {
					super.onDisabled(context);
					System.out.println("MyWidget: onDisabled");
				}
			
				/**
				 * 新增widget时,或者widget更新时,调用onUpdate
				 * 更新时间取决于xml中配置的时间,最短为半小时
				 */
				@Override
				public void onUpdate(Context context, AppWidgetManager appWidgetManager,
						int[] appWidgetIds) {
					super.onUpdate(context, appWidgetManager, appWidgetIds);
					System.out.println("MyWidget: onUpdate");
				}
			
				/**
				 * 删除widget时,调onDeleted
				 */
				@Override
				public void onDeleted(Context context, int[] appWidgetIds) {
					super.onDeleted(context, appWidgetIds);
					System.out.println("MyWidget: onDeleted");
				}
			
				/**
				 * 当widget大小发生变化时,调用此方法
				 */
				@Override
				public void onAppWidgetOptionsChanged(Context context,
						AppWidgetManager appWidgetManager, int appWidgetId,
						Bundle newOptions) {
					System.out.println("MyWidget: onAppWidgetOptionsChanged");
				}
			
			}
			

	- 定时更新widget

			问题: 我们需要通过widget实时显示当前进程数和可用内存,但widget最短也得半个小时才会更新一次, 如何才能间隔比较短的时间来及时更新?
	
			查看金山日志:
	
			当桌面有金山widget时, 金山会在后台启动service:ProcessService,并定时输出如下日志:
			03-29 08:43:03.070: D/MoSecurity.ProcessService(275): updateWidget
	
			该日志在锁屏状态下也一直输出.

			解决办法: 后台启动service,UpdateWidgetService, 并在service中启动定时器来控制widget的更新

	- 更新widget方法

			/**
			 * 定时更新widget的service
			 * 
			 * @author Kevin
			 * 
			 */
			public class UpdateWidgetService extends Service {
			
				private Timer mTimer;
				private AppWidgetManager mAWM;
			
				@Override
				public IBinder onBind(Intent intent) {
					return null;
				}
			
				@Override
				public void onCreate() {
					super.onCreate();
			
					mAWM = AppWidgetManager.getInstance(this);
			
					// 启动定时器,每个5秒一更新
					mTimer = new Timer();
					mTimer.schedule(new TimerTask() {
			
						@Override
						public void run() {
							System.out.println("更新widget啦!");
							updateWidget();
						}
					}, 0, 5000);
				}
			
				/**
				 * 更新widget
				 */
				private void updateWidget() {
					// 初始化远程的view对象
					RemoteViews views = new RemoteViews(getPackageName(),
							R.layout.process_widget);
			
					views.setTextViewText(R.id.tv_running_processes, "正在运行的软件:"
							+ ProcessInfoProvider.getRunningProcessNum(this));
					views.setTextViewText(
							R.id.tv_memory_left,
							"可用内存:"
									+ Formatter.formatFileSize(this,
											ProcessInfoProvider.getAvailMemory(this)));
			
					// 初始化组件
					ComponentName provider = new ComponentName(this, MyWidget.class);
			
					// 更新widget
					mAWM.updateAppWidget(provider, views);
				}
			
				@Override
				public void onDestroy() {
					super.onDestroy();
					mTimer.cancel();
					mTimer = null;
				}
			}

			-----------------------------

			启动和销毁service的时机

			分析widget的声明周期,在onEnabled和onUpdate中启动服务, 在onDisabled中结束服务

	- 注意: APK安装在sd卡上，widget在窗口小部件列表里无法显示。   android:installLocation="preferExternal", 修改过来后，需要卸载，再去安装widget才生效；

	- 点击事件处理

			// 初始化延迟意图,pending是等待的意思
			Intent intent = new Intent(this, HomeActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			// 当点击widget布局时,跳转到主页面
			views.setOnClickPendingIntent(R.id.ll_root, pendingIntent);
	
			//当一键清理被点击是,发送广播,清理内存
			Intent btnIntent = new Intent();
			btnIntent.setAction("com.itheima.mobilesafeteach.KILL_ALL");
			PendingIntent btnPendingIntent = PendingIntent.getBroadcast(this, 0,
					btnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.btn_clear, btnPendingIntent);

			---------------------------  

			/**
			 * 杀死后台进程的广播接受者
			 * 清单文件中配置action="com.itheima.mobilesafeteach.KILL_ALL"
			 * 
			 * @author Kevin
			 * 
			 */
			public class KillAllReceiver extends BroadcastReceiver {
			
				@Override
				public void onReceive(Context context, Intent intent) {
					System.out.println("kill all...");
					// 杀死后台所有运行的进程
					ProcessInfoProvider.killAll(context);
				}
			}

			---------------------------

			 <receiver android:name=".receiver.KillAllReceiver" >
	            <intent-filter>
	                <action android:name="com.itheima.mobilesafeteach.KILL_ALL" />
	            </intent-filter>
        	 </receiver>

	- 做一个有情怀的程序员, 拒绝耗电!

			当锁屏关闭时,停止widget定时器的更新

			UpdateWidgetService:

			// 注册屏幕开启和关闭的广播接受者
			mReceiver = new InnerScreenReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_SCREEN_ON);
			registerReceiver(mReceiver, filter);

			/**
			 * 屏幕关闭和开启的广播接收者
			 * 
			 * @author Kevin
			 * 
			 */
			class InnerScreenReceiver extends BroadcastReceiver {
		
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (Intent.ACTION_SCREEN_OFF.equals(action)) {// 屏幕关闭
						if (mTimer != null) {
							// 停止定时器
							mTimer.cancel();
							mTimer = null;
						}
					} else {// 屏幕开启
						startTimer();
					}
				}
			}
- 程序锁
	- 高级工具中添加程序锁入口
	- 新建程序锁页面 AppLockActivity
	- 程序锁页面布局文件实现

			activity_app_lock.xml

			<?xml version="1.0" encoding="utf-8"?>
			<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical" >
			
			    <LinearLayout
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:gravity="center"
			        android:orientation="horizontal" >
			
			        <TextView
			            android:id="@+id/tv_unlock"
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:background="@drawable/tab_left_pressed"
			            android:gravity="center"
			            android:text="未加锁"
			            android:textColor="#fff" />
			
			        <TextView
			            android:id="@+id/tv_locked"
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:background="@drawable/tab_right_default"
			            android:gravity="center"
			            android:text="已加锁"
			            android:textColor="#fff" />
			    </LinearLayout>
			
			    <LinearLayout
			        android:id="@+id/ll_unlock"
			        android:layout_width="match_parent"
			        android:layout_height="match_parent"
			        android:orientation="vertical" >
			
			        <TextView
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:text="未加锁软件:x个"
			            android:textColor="#000" />
			
			        <ListView
			            android:id="@+id/lv_unlock"
			            android:layout_width="match_parent"
			            android:layout_height="match_parent" />
			    </LinearLayout>
			
			    <LinearLayout
			        android:id="@+id/ll_locked"
			        android:layout_width="match_parent"
			        android:layout_height="match_parent"
			        android:orientation="vertical"
			        android:visibility="gone" >
			
			        <TextView
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:text="已加锁软件:x个"
			            android:textColor="#000" />
			
			        <ListView
			            android:id="@+id/lv_locked"
			            android:layout_width="match_parent"
			            android:layout_height="match_parent" />
			    </LinearLayout>
			
			</LinearLayout>

	- 点击标签切换页面

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_unlock:// 展示未加锁页面,隐藏已加锁页面
					llLocked.setVisibility(View.GONE);
					llUnlock.setVisibility(View.VISIBLE);
					tvUnlock.setBackgroundResource(R.drawable.tab_left_pressed);
					tvLocked.setBackgroundResource(R.drawable.tab_right_default);
					break;
				case R.id.tv_locked:// 展示已加锁页面,隐藏未加锁页面
					llUnlock.setVisibility(View.GONE);
					llLocked.setVisibility(View.VISIBLE);
					tvUnlock.setBackgroundResource(R.drawable.tab_left_default);
					tvLocked.setBackgroundResource(R.drawable.tab_right_pressed);
					break;
				default:
					break;
				}
			}

	- 应用列表信息展现(展现全部应用列表数据)
	
	- 使用数据库保存已加锁的软件

			AppLockOpenHelper.java
	
			// 创建表, 两个字段,_id, packagename(应用包名)
			db.execSQL("create table applock (_id integer primary key autoincrement, packagename varchar(50))");
	
			----------------------------------

			AppLockDao.java(逻辑和黑名单列表类似)

			/**
			 * 增加程序锁应用
			 */
			public void add(String packageName) {
				SQLiteDatabase db = mHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("packagename", packageName);
				db.insert("applock", null, values);
				db.close();
			}
		
			/**
			 * 删除程序锁应用
			 * 
			 * @param number
			 */
			public void delete(String packageName) {
				SQLiteDatabase db = mHelper.getWritableDatabase();
				db.delete("applock", "packagename=?", new String[] { packageName });
				db.close();
			}
		
			/**
			 * 查找程序锁应用
			 * 
			 * @param number
			 * @return
			 */
			public boolean find(String packageName) {
				SQLiteDatabase db = mHelper.getWritableDatabase();
				Cursor cursor = db.query("applock", null, "packagename=?",
						new String[] { packageName }, null, null, null);
		
				boolean result = false;
				if (cursor.moveToFirst()) {
					result = true;
				}
		
				cursor.close();
				db.close();
				return result;
			}
		
			/**
			 * 查找已加锁列表
			 * 
			 * @return
			 */
			public ArrayList<String> findAll() {
				SQLiteDatabase db = mHelper.getWritableDatabase();
				Cursor cursor = db.query("applock", new String[] { "packagename" },
						null, null, null, null, null);
		
				ArrayList<String> list = new ArrayList<String>();
				while (cursor.moveToNext()) {
					String packageName = cursor.getString(0);
					list.add(packageName);
				}
		
				cursor.close();
				db.close();
				return list;
			}

	- 监听list item点击事件,向数据库添加一些数据
			
			lvUnLock.setOnItemClickListener(new OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					AppInfo info = mUnlockList.get(position);
					mDao.add(info.packageName);
				}
			});

	- 已加锁和未加锁数据设置

			private ArrayList<AppInfo> mLockedList;// 已加锁列表集合
			private ArrayList<AppInfo> mUnlockList;// 未加锁列表集合

			private Handler mHandler = new Handler() {

				public void handleMessage(android.os.Message msg) {
					// 设置未加锁数据
					mUnlockAdapter = new AppLockAdapter(false);
					lvUnLock.setAdapter(mUnlockAdapter);
		
					// 设置已加锁数据
					mLockedAdapter = new AppLockAdapter(true);
					lvLocked.setAdapter(mLockedAdapter);
				};
			};

			/**
			 * 初始化应用列表数据
			 */
			private void initData() {
				new Thread() {
					@Override
					public void run() {
						mList = AppInfoProvider.getAppInfos(AppLockActivity.this);
		
						mLockedList = new ArrayList<AppInfo>();
						mUnlockList = new ArrayList<AppInfo>();
		
						for (AppInfo info : mList) {
							boolean isLocked = mDao.find(info.packageName);
							if (isLocked) {
								mLockedList.add(info);
							} else {
								mUnlockList.add(info);
							}
						}
		
						mHandler.sendEmptyMessage(0);
					}
				}.start();
			}

	- 界面效果完善

			点击锁子图标后, 实现加锁和去加锁的逻辑, 界面跟着更新

			class AppLockAdapter extends BaseAdapter {

				private boolean isLocked;//true表示已加锁数据
		
				public AppLockAdapter(boolean isLocked) {
					this.isLocked = isLocked;
				}
		
				@Override
				public int getCount() {
					if (isLocked) {
						return mLockedList.size();
					} else {
						return mUnlockList.size();
					}
				}
		
				@Override
				public AppInfo getItem(int position) {
					if (isLocked) {
						return mLockedList.get(position);
					} else {
						return mUnlockList.get(position);
					}
				}
		
				@Override
				public long getItemId(int position) {
					return position;
				}
		
				@Override
				public View getView(final int position, View convertView,
						ViewGroup parent) {
					ViewHolder holder;
					if (convertView == null) {
						convertView = View.inflate(AppLockActivity.this,
								R.layout.list_applock_item, null);
						holder = new ViewHolder();
						holder.ivIcon = (ImageView) convertView
								.findViewById(R.id.iv_icon);
						holder.tvName = (TextView) convertView
								.findViewById(R.id.tv_name);
						holder.ivLock = (ImageView) convertView
								.findViewById(R.id.iv_lock);
		
						convertView.setTag(holder);
					} else {
						holder = (ViewHolder) convertView.getTag();
					}
		
					final AppInfo info = getItem(position);
					holder.ivIcon.setImageDrawable(info.icon);
					holder.tvName.setText(info.name);

					if(isLocked) {
						holder.ivLock.setImageResource(R.drawable.unlock);
					}else {
						holder.ivLock.setImageResource(R.drawable.lock);
					}
		
					holder.ivLock.setOnClickListener(new OnClickListener() {
		
						@Override
						public void onClick(View v) {
							if (isLocked) {
								mDao.delete(info.packageName);// 从数据库删除记录
								mLockedList.remove(info);// 从已加锁集合删除元素
								mUnlockList.add(info);// 给未加锁集合添加元素
							} else {
								mDao.add(info.packageName);// 向数据库添加记录
								mLockedList.add(info);// 给已加锁集合添加元素
								mUnlockList.remove(info);// 从未加锁集合删除元素
							}
		
							// 刷新listview
							mLockedAdapter.notifyDataSetChanged();
							mUnlockAdapter.notifyDataSetChanged();
						}
					});
		
					return convertView;
				}
			}

	- 更新已加锁/未加锁数量

			/**
			 * 更新已加锁和未加锁数量
			 */
			private void updateAppNum() {
				tvUnLockNum.setText("未加锁软件:" + mUnlockList.size() + "个");
				tvLockedNum.setText("已加锁软件:" + mLockedList.size() + "个");
			}

			// 每次刷新listview前都会调用getCount方法,可以在这里更新数量
			@Override
			public int getCount() {
				updateAppNum();
				
				if (isLocked) {
					return mLockedList.size();
				} else {
					return mUnlockList.size();
				}
			}
	
	- 动画实现

			- 解决动画移动问题

			导致的原因，动画没有开始播放，界面就刷新了。
			动画播放需要时间的，动画没有播就变成了新的View对象。就播了新的View对象，
			让动画播放完后，再去更新页面；

			public AppLockAdapter(boolean isLocked) {
				this.isLocked = isLocked;
	
				// 右移
				mLockAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
						Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF,
						0, Animation.RELATIVE_TO_SELF, 0);
				mLockAnim.setDuration(500);
	
				// 左移
				mUnLockAnim = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
						0f, Animation.RELATIVE_TO_SELF, -1f,
						Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
						0);
				mUnLockAnim.setDuration(500);
			}

			holder.ivLock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isLocked) {
						view.startAnimation(mUnLockAnim);
						mUnLockAnim
								.setAnimationListener(new AnimationListener() {

									@Override
									public void onAnimationStart(
											Animation animation) {
									}

									@Override
									public void onAnimationRepeat(
											Animation animation) {
									}
									//监听动画结束事件
									@Override
									public void onAnimationEnd(
											Animation animation) {
										mDao.delete(info.packageName);// 从数据库删除记录
										mLockedList.remove(info);// 从已加锁集合删除元素
										mUnlockList.add(info);// 给未加锁集合添加元素

										// 刷新listview
										mLockedAdapter.notifyDataSetChanged();
										mUnlockAdapter.notifyDataSetChanged();
									}
								});
					} else {
						view.startAnimation(mLockAnim);
						mLockAnim.setAnimationListener(new AnimationListener() {

							@Override
							public void onAnimationStart(Animation animation) {
							}

							@Override
							public void onAnimationRepeat(Animation animation) {
							}

							//监听动画结束事件
							@Override
							public void onAnimationEnd(Animation animation) {
								mDao.add(info.packageName);// 向数据库添加记录
								mLockedList.add(info);// 给已加锁集合添加元素
								mUnlockList.remove(info);// 从未加锁集合删除元素
								// 刷新listview
								mLockedAdapter.notifyDataSetChanged();
								mUnlockAdapter.notifyDataSetChanged();
							}
						});
					}
				}
			});
			
## day12 ##
	- 看门狗
		
		- 看门狗原理介绍
		- 创建服务WatchDogService
		- 设置页面增加启动服务的开关
		- 看门狗轮询检测任务栈

				打印当前最顶上的activity

				/**
				 * 看门狗服务 需要权限: android.permission.GET_TASKS
				 * 
				 * @author Kevin
				 * 
				 */
				public class WathDogService extends Service {
				
					private boolean isRunning;// 表示线程是否正在运行
					private ActivityManager mAM;
				
					@Override
					public IBinder onBind(Intent intent) {
						return null;
					}
				
					@Override
					public void onCreate() {
						super.onCreate();
						mAM = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				
						isRunning = true;
						new Thread() {
							public void run() {
								while (isRunning) {// 看门狗每隔100毫秒巡逻一次
									List<RunningTaskInfo> runningTasks = mAM.getRunningTasks(1);// 获取正在运行的任务栈
									String packageName = runningTasks.get(0).topActivity
											.getPackageName();// 获取任务栈最上层activity的包名
									System.out.println("top Activity=" + packageName);
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							};
						}.start();
					}
				
					@Override
					public void onDestroy() {
						super.onDestroy();
						isRunning = false;// 结束线程
					}
				}

	- 轮询获取最近的task, 如果发现是加锁的,跳EnterPwdActivity

			if (mDao.find(packageName)) {// 查看当前页面是否在加锁的数据库中
				Intent intent = new Intent(WatchDogService.this,
						EnterPwdActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("packageName", packageName);
				startActivity(intent);
			}

			-----------------------------------

			/**
			 * 加锁输入密码页面
			 * 
			 * @author Kevin
			 * 
			 */
			public class EnterPwdActivity extends Activity {
			
				private TextView tvName;
				private ImageView ivIcon;
				private EditText etPwd;
				private Button btnOK;
			
				@Override
				protected void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					setContentView(R.layout.activity_enter_pwd);
			
					tvName = (TextView) findViewById(R.id.tv_name);
					ivIcon = (ImageView) findViewById(R.id.iv_icon);
					etPwd = (EditText) findViewById(R.id.et_pwd);
					btnOK = (Button) findViewById(R.id.btn_ok);
			
					Intent intent = getIntent();
					String packageName = intent.getStringExtra("packageName");
			
					PackageManager pm = getPackageManager();
					try {
						ApplicationInfo info = pm.getApplicationInfo(packageName, 0);// 根据包名获取应用信息
						Drawable icon = info.loadIcon(pm);// 加载应用图标
						ivIcon.setImageDrawable(icon);
						String name = info.loadLabel(pm).toString();// 加载应用名称
						tvName.setText(name);
					} catch (NameNotFoundException e) {
						e.printStackTrace();
					}
			
					btnOK.setOnClickListener(new OnClickListener() {
			
						@Override
						public void onClick(View v) {
							String pwd = etPwd.getText().toString().trim();
							if (!TextUtils.isEmpty(pwd)) {// 密码校验
								if (pwd.equals("123")) {
									finish();
								} else {
									Toast.makeText(EnterPwdActivity.this, "密码错误",
											Toast.LENGTH_LONG).show();
								}
							} else {
								Toast.makeText(EnterPwdActivity.this, "请输入密码",
										Toast.LENGTH_LONG).show();
							}
						}
					});
				}
			
			}

	
	- 重写返回事件,跳转到主页面

			//查看系统Launcher源码,确定跳转逻辑
			@Override
			public void onBackPressed() {
				// 跳转主页面
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				startActivity(intent);

				finish();//销毁当前页面
			}

	- 发送广播,看门狗跳过检测

			认证成功后,发送广播

			EnterPwdActivity.java

			// 发送广播,通知看门狗不要再拦截当前应用
			Intent intent = new Intent();
			intent.setAction("com.itheima.mobilesafeteach.ACTION_STOP_PROTECT");
			intent.putExtra("packageName", packageName);
			sendBroadcast(intent);

			-------------------------------------------

			WatchDogService.java
			
			class InnerReceiver extends BroadcastReceiver {

				@Override
				public void onReceive(Context context, Intent intent) {
					// 看门狗得到了消息，临时的停止对某个应用程序的保护
					mSkipPackageName = intent.getStringExtra("packageName");
				}
			}

			if (packageName.equals(mSkipPackageName)) {// 用过已经认证通过了,需跳过验证
					System.out.println("无需验证...");
					continue;
			}

	- 相关优化

			知识拓展：看门狗后台一直在运行，这样是比较耗电的。
			
			我们要优化的的话怎么做呢？
			在看门狗服务里，监听锁屏事件，如果锁屏了我就把看门狗停止（flag = false;）；屏幕开启了，我就让看门狗开始工作启动服务并且flag = true;；
			
			避免一次输入密码了不再输入；防止别人在我使用的时候，接着使用不用输入密码的情形；
			也可以在锁屏的时候把mSkipPackageName赋值为空就行了。

	- 利用activity启动模式修复密码输入bug

			1. 演示bug(进入手机卫士,按home退到后台,然后再打开加锁app,进入后发现跳转到手机卫士页面)
			2. 画图分析，正常情况下的任务栈和bug时的任务栈图；
			3. 解决问题；在功能清单文件EnterPwdActivity加上字段
			<activity android:name="com.itheima.mobilesafe.EnterPwdActivity" android:launchMode="singleInstance"/>
			4. 然后再画图分析正确的任务栈；

	- 隐藏最近打开的activity

			长按小房子键：弹出历史记录页面，就会列出最近打开的Activity;

			1. 演示由于最近打开的Activity导致的Bug;
			
			2. 容易暴露用户的隐私
			  最近打开的Activity，是为了用户可以很快打开最近打开的应用而设计的；2.2、2.3普及后就把问题暴露出来了，很容易暴露用户的隐私。比如你玩一些日本开发的游戏：吹裙子、扒衣服这类游戏。你正在玩这些有些，这个时候，爸妈或者大学女辅导员过来了，赶紧按小房子，打开背单词的应用，这时大学女辅导员走过来说，干嘛呢，把手机交出来，长按一下小房子键，这个时候很尴尬的事情就产生了。
			
				A：低版本是无法移除的。低版本记录最近8个；想要隐藏隐私，打开多个挤出去；
				B:4.0以后高版本就可以直接移除了。考虑用户呼声比较高。
			
			3. 设置不在最近任务列表显示activity
				<activity
				        android:excludeFromRecents="true"
				            android:name="com.itheima.mobilesafe.EnterPwdActivity"
				            android:launchMode="singleInstance" />
			
			4. 在装有腾讯管家的模拟器演示腾讯管理的程序锁功能；也没用现实最近的Activity,它也是这样做的。
			
			知识拓展，以后开发带有隐私的软件，或者软件名称不好听的应用，就可以加载在最近打开列表不包括字段.

	- 腾讯管家和手机卫士同时加锁,谁更快?

			腾讯管家会更快一些, 所以需要再进一步优化

	- 提高性能

		- 缩短每次巡逻时间

				//将100改为20
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
						e.printStackTrace();
				}
		- 不频繁调用数据库

				从数据库中读取所有已加锁应用列表,每次从集合中查询判断

				mLockedPackages = mDao.getInstance(this).findAll();// 查询所有已加锁的应用列表

				// if (mDao.find(packageName)) {// 查看当前页面是否在加锁的数据库中
				if (mLockedPackages.contains(packageName)) {}

		- 重新和腾讯管家比拼速度
		- 监听数据库变化, 更新集合

			- 增加另外一款软件进入程序锁。打开看看，是无法打开输入密码页面的；解析原因；
				这个时候就需要根据数据库的数据变化而改变集合的信息了，就用到了观察者；

			- 联想监听来电拦截时,监听通话日志变化的逻辑,解释原理

			- 具体实现

					AppLockDao.java

					// 数据库改变后发送通知
					mContext.getContentResolver().notifyChange(
							Uri.parse("content://com.itheima.mobilesafe/applockdb"), null);					

					-------------------------------------

					WatchDogService.java	

					// 监听程序锁数据库内容变化
					mObserver = new MyContentObserver(new Handler());
					getContentResolver().registerContentObserver(
							Uri.parse("content://com.itheima.mobilesafe/applockdb"), true,
							mObserver);		

					getContentResolver().unregisterContentObserver(mObserver);// 注销观察者		

					class MyContentObserver extends ContentObserver {
	
						public MyContentObserver(Handler handler) {
							super(handler);
						}
				
						@Override
						public void onChange(boolean selfChange) {
							System.out.println("数据变化了...");
							mLockedPackages = mDao.findAll();// 查询所有已加锁的应用列表
						}
				
					}
- 手机杀毒
	- 什么是病毒?

			计算机病毒是一个程序，一段可执行码。就像生物病毒一样，具有自我繁殖、互相传染以及激活再生等生物病毒特征。计算机病毒有独特的复制能力，它们能够快速蔓延，又常常难以根除。它们能把自身附着在各种类型的文件上，当文件被复制或从一个用户传送到另一个用户时，它们就随同文件一起蔓延开来。

	- 计算机第一个病毒

			诞生于麻省理工大学
			
	- 蠕虫病毒
		
			熊猫烧香，蠕虫病毒的一种，感染电脑上的很多文件；exe文件被感染，html文件被感染。
		 	主要目的：证明技术有多牛。写这种病毒的人越来越少了

	- 木马

			盗窃信息，盗号、窃取隐私、偷钱，玩了一个游戏，买了很多装备，监听你的键盘输入，下次进入的话，装备全部没了。
			主要目目的：挣钱，产生利益；

	- 灰鸽子

			主要特征，控制别人电脑，为我所有。比如挖金矿游戏挣钱的，控制几十万台机器为你干活。
			总会比银河处理器快的多。
			特点是：不知情情况下安装下的。

	- 所有的病毒，都是执行后才有危害，如果病毒下载了，没有安装运行，是没有危害的。	
	- 杀毒原理介绍

			定位出特殊的程序，把程序的文件给删除。
	
			王江民, 江民杀毒软件
			Kv300
			Kv300 干掉300个病毒
			
			开发kv300后很多人用盗版的。
			江民炸弹

	- 病毒怎么找到？-收集病毒的样本
		
			电信 网络运营商主节点 部署服务器集群（蜜罐）
			一组没有防火墙 没有安全软件 没有补丁的服务器, 主动联网,下载一些软件运行。这样情况下，特别容易中病毒。
			工作原理相当于：苍蝇纸

	- 360互联网云安全计划

			所有的用户都是你的蜜罐；
			收集的数据量就大大提高了；

			国内安全厂商，有些没有职业道德。
			收集一些个人隐私，或者商业机密的文件也收集过去 3Q大战

	- 传统杀毒软件的缺陷

			目前卡巴斯基病毒库已经有了2千多万病毒

			传统杀毒软件的缺陷： 病毒数据库越来越大；
			只能查杀已知的病毒，不能查杀未知病毒；

			360免杀
			写了一个木马，在加一个壳，加壳后360就识别不了了

	- 主动防御

			检查软件
			1.检查开机启动项
			2.检查注册表；
			3.检查进程列表

			病毒特征：
			1、开启启动
			2、隐藏自身
			3、监视键盘
			4、联网发邮件

			启发式扫描-扫描单个文件
			拷贝文件到虚拟机-相当于精简版的系统, 运行后检测是否具备病毒特点

	- 杀毒引擎

			优化后的数据库查询算法,优先扫描当下最常见的病毒, 速度快

	- Android上的杀毒软件

			大多数停留在基于数据库方式杀毒

			LBE主动防御方式杀毒。敏感权限扫描,敏感操作提示,和小米深度合作

			金山手机卫士病毒库

- 手机杀毒模块开发

	- 创建AntiVirusActivity
	- 布局文件开发

			<LinearLayout
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:orientation="horizontal"
	        android:padding="10dp" >
	
	        <FrameLayout
	            android:layout_width="80dp"
	            android:layout_height="80dp" >
	
	            <ImageView
	                android:id="@+id/imageView1"
	                android:layout_width="match_parent"
	                android:layout_height="match_parent"
	                android:src="@drawable/ic_scanner_malware" />
	
	            <ImageView
	                android:id="@+id/iv_scanning"
	                android:layout_width="match_parent"
	                android:layout_height="match_parent"
	                android:src="@drawable/act_scanning_03" />
	        </FrameLayout>
	
	        <LinearLayout
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"
	            android:gravity="center"
	            android:orientation="vertical" >
	
	            <TextView
	                android:id="@+id/tv_scan_status"
	                android:layout_width="wrap_content"
	                android:layout_height="wrap_content"
	                android:singleLine="true"
	                android:text="正在初始化8核杀毒引擎"
	                android:textColor="#000"
	                android:textSize="18sp" />
	
	            <ProgressBar
	                android:id="@+id/progressBar1"
	                style="?android:attr/progressBarStyleHorizontal"
	                android:layout_width="match_parent"
	                android:layout_height="wrap_content"
	                android:layout_marginLeft="10dp"
	                android:layout_marginRight="10dp" />
	        </LinearLayout>
	    </LinearLayout>

	- 扫描动画

			RotateAnimation anim = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
			anim.setDuration(1000);//间隔时间
			anim.setRepeatCount(Animation.INFINITE);//无限循环
			anim.setInterpolator(new LinearInterpolator());//匀速循环,不停顿
			ivScanning.startAnimation(anim);

	- 自定义进度条样式

			1. 查看android系统对Progressbar样式的定义

			开发环境\platforms\android-16\data\res\values\styles.xml,搜索Widget.Holo.ProgressBar.Horizontal->progress_horizontal_holo_light

			2. 拷贝xml文件,修改成自己的图片

			<layer-list xmlns:android="http://schemas.android.com/apk/res/android" >
			    <item
			        android:id="@android:id/background"
			        android:drawable="@drawable/security_progress_bg"/>
			    <item
			        android:id="@android:id/secondaryProgress"
			        android:drawable="@drawable/security_progress">
			    </item>
			    <item
			        android:id="@android:id/progress"
			        android:drawable="@drawable/security_progress">
			    </item>
			</layer-list>

			3. 将xml文件设置给Progressbar

			<ProgressBar
                android:id="@+id/progressBar1"
                style="?android:attr/progressBarStyleHorizontal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginLeft="10dp"
                android:layout_marginRight="10dp"
                android:progress="50"
                android:layout_marginTop="5dp"
                android:progressDrawable="@drawable/custom_progress" />
			
			4. 进度更新

				// 更新进度条
				new Thread() {
					public void run() {
						pbProgress.setMax(100);
		
						for (int i = 0; i <= 100; i++) {
							pbProgress.setProgress(i);
		
							try {
								Thread.sleep(30);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					};
				}.start();
			

- 获取系统安装包的MD5

		PackageManager pm = getPackageManager();
		// 获取所有已安装/未安装的包的安装包信息
		// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
		List<PackageInfo> packages = pm
					.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo packageInfo : packages) {
			//apk安装路径
			String apkPath = packageInfo.applicationInfo.sourceDir;
			//计算apk的md5
			String md5 = MD5Utils.getFileMd5(apkPath);
		}

		-------------------------------------------------

		/**
		 * 获取某个文件的md5
		 * @param path
		 * @return
		 */
		public static String getFileMd5(String path) {
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				FileInputStream in = new FileInputStream(path);
	
				int len = 0;
				byte[] buffer = new byte[1024];
	
				while ((len = in.read(buffer)) != -1) {
					digest.update(buffer, 0, len);
				}
	
				byte[] result = digest.digest();
	
				StringBuffer sb = new StringBuffer();
				for (byte b : result) {
					int i = b & 0xff;// 将字节转为整数
					String hexString = Integer.toHexString(i);// 将整数转为16进制
	
					if (hexString.length() == 1) {
						hexString = "0" + hexString;// 如果长度等于1, 加0补位
					}
	
					sb.append(hexString);
				}
	
				System.out.println(sb.toString());// 打印得到的md5
				return sb.toString();
	
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			return "";
		}


- 扫描病毒数据库

		AntiVirusDao.java

		/**
		 * 病毒数据库的封装
		 * 
		 * @author Kevin
		 * 
		 */
		public class AntiVirusDao {
		
			public static final String PATH = "data/data/com.itheima.mobilesafeteach/files/antivirus.db";
		
				/**
				 * 根据签名的md5判断是否是病毒
				 * 
				 * @param md5
				 * @return 返回病毒描述,如果不是病毒,返回null
				 */
				public static String isVirus(String md5) {
					SQLiteDatabase db = SQLiteDatabase.openDatabase(PATH, null,
							SQLiteDatabase.OPEN_READONLY);
			
					Cursor cursor = db.rawQuery("select desc from datable where md5=? ",
							new String[] { md5 });
			
					String desc = null;
					if (cursor.moveToFirst()) {
						desc = cursor.getString(0);
					}
			
					cursor.close();
					db.close();
					return desc;
				}
		}

- 扫描安装包并更新进度条

		int progress = 0;
		Random random = new Random();
		for (PackageInfo packageInfo : packages) {
			String name = packageInfo.applicationInfo.loadLabel(pm)
					.toString();

			String apkPath = packageInfo.applicationInfo.sourceDir;
			String md5 = MD5Utils.getFileMd5(apkPath);
			String desc = AntiVirusDao.isVirus(md5);

			if (desc != null) {
				// 是病毒
				System.out.println("是病毒....");
			} else {
				// 不是病毒
				System.out.println("不是病毒....");
			}

			progress++;
			pbProgress.setProgress(progress);

			try {
				Thread.sleep(50 + random.nextInt(50));//随机休眠一段时间
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

- 扫描过程中,更新扫描状态文字

		- 扫描前,强制休眠2秒,展示"正在初始化8核杀毒引擎"
		- 使用handler发送消息,更新TextView为:正在扫描:应用名称
		- 扫描结束后, 发送消息,更新TextView为:扫描完毕
		- 扫描结束后,关闭扫描的动画

- 扫描过程中,更新扫描文件列表

		- 布局文件中添加空的LinearLayout(竖直方向),动态给线性布局添加TextView
		- 使用ScrollView包裹线性布局,保证可以上下滑动

			 <ScrollView
		        android:layout_width="match_parent"
		        android:layout_height="match_parent" >
		
		        <LinearLayout
		            android:id="@+id/ll_scanning"
		            android:layout_width="match_parent"
		            android:layout_height="wrap_content"
		            android:orientation="vertical" >
		        </LinearLayout>
		    </ScrollView>

		- 如果发现是病毒, TextView需要展示为红色, 为了区分是不是病毒,可以把扫描的文件封装成一个对象ScanInfo

			class ScanInfo {
				public String packageName;
				public String desc;
				public String name;
				public boolean isVirus;
			}

			private Handler mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SCANNING:
						ScanInfo info = (ScanInfo) msg.obj;
						tvScanStatus.setText("正在扫描:" + info.name);
		
						TextView tvScan = new TextView(AntiVirusActivity.this);
						if (info.isVirus) {
							tvScan.setText("发现病毒:" + info.name);
							tvScan.setTextColor(Color.RED);
						} else {
							tvScan.setText("扫描安全:" + info.name);
						}
		
						llScanning.addView(tvScan);
						break;
					case SCANNING_FINISHED:
						tvScanStatus.setText("扫描完毕");
						ivScanning.clearAnimation();// 清除扫描的动画
						break;
					default:
						break;
					}
				};
			};

- 制作病毒

		制作两个apk文件,并将这两个apk文件的md5加入到病毒数据库中,这样的话就可以测试扫出病毒的情况了

		注意: 将原来的antivirus.db替换为新的文件后,一定要把app的数据清除后再运行,重新进行拷贝数据库的操作, 否则app仍找的是data/data目录下的旧版数据库!

- 创建病毒集合		
- 发现病毒后，提示用户删除病毒

		if (mVirusList.isEmpty()) {
			Toast.makeText(getApplicationContext(), "你的手机很安全了，继续加油哦!",
					Toast.LENGTH_SHORT).show();
		} else {
			showAlertDialog();
		}

		----------------------------

		/**
		 * 发现病毒后,弹出警告弹窗
		 */
		protected void showAlertDialog() {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("警告!");
			builder.setMessage("发现" + mVirusList.size() + "个病毒, 非常危险,赶紧清理!");
			builder.setPositiveButton("立即清理",
					new DialogInterface.OnClickListener() {
	
						@Override
						public void onClick(DialogInterface dialog, int which) {
							for (ScanInfo info : mVirusList) {
								// 卸载apk
								Intent intent = new Intent(Intent.ACTION_DELETE);
								intent.setData(Uri.parse("package:"
										+ info.packageName));
								startActivity(intent);
							}
						}
					});
	
			builder.setNegativeButton("下次再说", null);
			AlertDialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);// 点击弹窗外面,弹窗不消失
			dialog.show();
		}

- 处理横竖屏切换

		fn+ctrl+f11 切换模拟器横竖屏后, Activity的onCreate方法会从新走一次, 可以通过清单文件配置,Activity强制显示竖屏

		<activity
            android:name=".activity.AntiVirusActivity"
            android:screenOrientation="portrait" />

		或者, 可以显示横屏, 通过此配置可以不重新创建Activity

		<activity
            android:name=".activity.AntiVirusActivity"
            android:configChanges="orientation|screenSize|keyboardHidden" />
            
            

## Day13 ##
- 新建工程,获取缓存大小

	- 布局文件开发

		<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
		    xmlns:tools="http://schemas.android.com/tools"
		    android:layout_width="match_parent"
		    android:layout_height="match_parent"
		    android:orientation="vertical" >
		
		    <EditText
		        android:id="@+id/et_package"
		        android:layout_width="match_parent"
		        android:layout_height="wrap_content"
		        android:hint="请输入包名" >
		    </EditText>
		
		    <Button
		        android:id="@+id/btn_ok"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:text="确定" />
		
		    <TextView
		        android:id="@+id/tv_result"
		        android:layout_width="wrap_content"
		        android:layout_height="wrap_content"
		        android:text="查询结果" />
		
		</LinearLayout>

	- 查看系统设置源码, 查看清理缓存逻辑

		1. 导入Setting源码
		2. 查找清除缓存的逻辑

				Clear cache->clear_cache_btn_text->installed_app_details->InstalledAppDetails->cache_size_text-> mAppEntry.cacheSize->stats.cacheSize->stats->mStatsObserver->getPackageSizeInfo->查看PackageManager源码,跟踪方法getPackageSizeInfo,发现改方法隐藏

		3. 通过反射方式,调用PackageManager的方法

				public Method[] getMethods()返回某个类的所有公用（public）方法包括其继承类的公用方法，当然也包括它所实现接口的方法。

				public Method[] getDeclaredMethods()对象表示的类或接口声明的所有方法，包括公共、保护、默认（包）访问和私有方法，但不包括继承的方法。当然也包括它所实现接口的方法。

				//需要权限:android.permission.GET_PACKAGE_SIZE

				btnOk.setOnClickListener(new OnClickListener() {
	
					@Override
					public void onClick(View v) {
						String packageName = etPackage.getText().toString().trim();
						if (!TextUtils.isEmpty(packageName)) {
							PackageManager pm = getPackageManager();
							try {
								Method method = pm.getClass().getMethod(
										"getPackageSizeInfo", String.class,
										IPackageStatsObserver.class);
								method.invoke(pm, packageName, new MyObserver());
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getApplicationContext(), "输入内容不能为空!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

				-------------------------------------

				class MyObserver extends IPackageStatsObserver.Stub {

					// 在子线程运行
					@Override
					public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
							throws RemoteException {
						long cacheSize = pStats.cacheSize;
						long dataSize = pStats.dataSize;
						long codeSize = pStats.codeSize;
			
						String result = "缓存:"
								+ Formatter.formatFileSize(getApplicationContext(),
										cacheSize)
								+ "\n"
								+ "数据:"
								+ Formatter.formatFileSize(getApplicationContext(),
										dataSize)
								+ "\n"
								+ "代码:"
								+ Formatter.formatFileSize(getApplicationContext(),
										codeSize);
						System.out.println(result);
						Message msg = Message.obtain();
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				}


- 缓存清理模块开发

	- 新建页面CleanCacheActivity 
	- 布局文件开发

			<?xml version="1.0" encoding="utf-8"?>
			<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical" >
			    <RelativeLayout
			        android:layout_width="match_parent"
			        android:layout_height="50dp"
			        android:background="#8866ff00" >
			
			        <TextView
			            android:id="@+id/textView1"
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:layout_centerVertical="true"
			            android:layout_marginLeft="5dp"
			            android:text="缓存清理"
			            android:textColor="#000"
			            android:textSize="22sp" />
			        <Button
			            android:id="@+id/button1"
			            android:layout_width="wrap_content"
			            android:layout_height="wrap_content"
			            android:layout_alignParentRight="true"
			            android:layout_centerVertical="true"
			            android:layout_marginRight="5dp"
			            android:onClick="cleanCache"
			            android:text="立即清理" />
			    </RelativeLayout>
			    <ProgressBar
			        android:id="@+id/pb_progress"
			        style="?android:attr/progressBarStyleHorizontal"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:progressDrawable="@drawable/custom_progress" />
			    <TextView
			        android:id="@+id/tv_status"
			        android:layout_width="match_parent"
			        android:layout_height="wrap_content"
			        android:text="正在扫描:" />
			    <ScrollView
			        android:layout_width="match_parent"
			        android:layout_height="match_parent" >
			
			        <LinearLayout
			            android:id="@+id/ll_container"
			            android:layout_width="match_parent"
			            android:layout_height="wrap_content"
			            android:orientation="vertical" >
			        </LinearLayout>
			    </ScrollView>
			</LinearLayout>

	- 缓存页面逻辑

			private Handler mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					switch (msg.what) {
					case SCANNING:
						String name = (String) msg.obj;
						tvStatus.setText("正在扫描:" + name);
						break;
					case SHOW_CACHE_INFO:
						CacheInfo info = (CacheInfo) msg.obj;
						View itemView = View.inflate(getApplicationContext(),
								R.layout.list_cacheinfo_item, null);
						TextView tvName = (TextView) itemView
								.findViewById(R.id.tv_name);
						ImageView ivIcon = (ImageView) itemView
								.findViewById(R.id.iv_icon);
						TextView tvCache = (TextView) itemView
								.findViewById(R.id.tv_cache_size);
						ImageView ivDelete = (ImageView) itemView
								.findViewById(R.id.iv_delete);
		
						tvName.setText(info.name);
						ivIcon.setImageDrawable(info.icon);
						tvCache.setText("缓存大小:"
								+ Formatter.formatFileSize(getApplicationContext(),
										info.cacheSize));
		
						llContainer.addView(itemView);
						break;
					case SCANNING_FINISHED:
						tvStatus.setText("扫描完成");
						break;
		
					default:
						break;
					}
				};
			};

			/**
			 * 开始扫描
			 */
			private void startScan() {
				new Thread() {
					@Override
					public void run() {
						List<PackageInfo> packages = mPM
								.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		
						pbProgress.setMax(packages.size());// 设置进度条最大值为安装包的数量
						int progress = 0;
						for (PackageInfo packageInfo : packages) {
							try {
								Method method = mPM.getClass().getMethod(
										"getPackageSizeInfo", String.class,
										IPackageStatsObserver.class);
								method.invoke(mPM, packageInfo.packageName,
										new MyObserver());
							} catch (Exception e) {
								e.printStackTrace();
							}
		
							progress++;
							pbProgress.setProgress(progress);
		
							// 发送更新进度的消息
							Message msg = Message.obtain();
							msg.what = SCANNING;
							msg.obj = packageInfo.applicationInfo.loadLabel(mPM)
									.toString();
							mHandler.sendMessage(msg);
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
		
						// 发送扫描结束的消息
						mHandler.sendEmptyMessage(SCANNING_FINISHED);
					}
				}.start();
			}
		
			class MyObserver extends IPackageStatsObserver.Stub {
		
				// 在子线程运行
				@Override
				public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
						throws RemoteException {
					long cacheSize = pStats.cacheSize;// 获取缓存大小
					if (cacheSize > 0) {
						try {
							CacheInfo info = new CacheInfo();
							String packageName = pStats.packageName;
							info.packageName = packageName;
		
							ApplicationInfo applicationInfo = mPM.getApplicationInfo(
									packageName, 0);
							info.name = applicationInfo.loadLabel(mPM).toString();
							info.icon = applicationInfo.loadIcon(mPM);
							info.cacheSize = cacheSize;
		
							// 扫描到缓存应用时发送消息
							Message msg = Message.obtain();
							msg.what = SHOW_CACHE_INFO;
							msg.obj = info;
							mHandler.sendMessage(msg);
		
						} catch (NameNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}

			// 缓存对象的封装
			class CacheInfo {
				public String name;
				public String packageName;
				public Drawable icon;
				public long cacheSize;
			}

			-------------------------

			list_cacheinfo_item.xml

			<?xml version="1.0" encoding="utf-8"?>
			<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
			    android:layout_width="match_parent"
			    android:layout_height="wrap_content"
			    android:padding="5dp" >
			
			    <ImageView
			        android:id="@+id/iv_icon"
			        android:layout_width="40dp"
			        android:layout_height="40dp"
			        android:layout_alignParentLeft="true"
			        android:layout_alignParentTop="true"
			        android:src="@drawable/ic_launcher" />
			
			    <TextView
			        android:id="@+id/tv_name"
			        android:layout_width="wrap_content"
			        android:layout_height="wrap_content"
			        android:layout_alignParentTop="true"
			        android:layout_marginLeft="20dp"
			        android:layout_toRightOf="@+id/iv_icon"
			        android:text="应用名称"
			        android:textColor="#000"
			        android:textSize="16sp" />
			
			    <TextView
			        android:id="@+id/tv_cache_size"
			        android:layout_width="wrap_content"
			        android:layout_height="wrap_content"
			        android:layout_alignLeft="@+id/tv_name"
			        android:layout_below="@id/tv_name"
			        android:layout_marginTop="5dp"
			        android:text="缓存大小:"
			        android:textColor="#000"
			        android:textSize="16sp" />
			
			    <ImageView
			        android:id="@+id/iv_delete"
			        android:layout_width="45dp"
			        android:layout_height="45dp"
			        android:layout_alignParentRight="true"
			        android:layout_centerVertical="true"
			        android:src="@drawable/btn_black_number_delete_selector" />
			
			</RelativeLayout>

- 一键清理缓存

	 	这是用的Android的一个BUG，就是你得程序去申请很大的内存，比如直接申请10G，但是你得内存总共才1G，这时候系统为了满足你得要求，会去全盘清理缓存，清理完了发现还是达不到你得要求，那么就返回失败！！！！ 但是，我们的目的已经达成，就是要让他去清理全盘缓存

		/**
		 * 一键清理
		 * 
		 * @param view
		 */
		public void cleanAllCache(View view) {
			try {
				// 通过反射调用freeStorageAndNotify方法, 向系统申请内存
				Method method = mPM.getClass().getMethod(
						"freeStorageAndNotify", long.class,
						IPackageDataObserver.class);
				// 参数传Long最大值, 这样可以保证系统将所有app缓存清理掉
				method.invoke(mPM, Long.MAX_VALUE, new IPackageDataObserver.Stub() {
	
					@Override
					public void onRemoveCompleted(String packageName,
							boolean succeeded) throws RemoteException {
						System.out.println("flag==" + succeeded);
						System.out.println("packageName=" + packageName);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
-清理特定app缓存

	查看Setting源码,分析清除缓存按钮的逻辑

	实现代码:

	/**
	 * 删除单个文件的缓存 需要权限:<uses-permission
	 * android:name="android.permission.DELETE_CACHE_FILES"/>
	 * 
	 * @param packageName
	 */
	private void deleteCache(String packageName) {
		try {
			Method method = mPM.getClass().getMethod(
					"deleteApplicationCacheFiles", String.class,
					IPackageDataObserver.class);
			method.invoke(mPM, packageName, new IPackageDataObserver.Stub() {
				@Override
				public void onRemoveCompleted(String packageName,
						boolean succeeded) throws RemoteException {
					System.out.println("succeeded" + succeeded);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	注意加权限: <uses-permission android:name="android.permission.DELETE_CACHE_FILES"/>

	知识拓展：
	加上权限后仍然报错

- 跳转到某个系统应用界面清除缓存

		1. 看一下腾讯管家跳转到系统应用界面时的日志，并且对于Settings源代码说明意图；
		
		2. 代码实现：
		//启动到某个系统应用页面
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);//有无没影响
		intent.setData(Uri.parse("package:"+cacheInfo.packName));
		startActivity(intent);
- 流量统计

	- 流量统计介绍, pc网络连接流量展示(已发送,已接受)
	- 连接真机,查看文件proc/uid_stat,发现该目录下有很多以uid命名的文件夹
	- 用户id是安装应用程序的时候 操作系统赋给应用程序的
	- 获取uid方式

			1. 进入AppInfoProvider, 
				String name = packInfo.applicationInfo.loadLabel(pm).toString()+ packInfo.applicationInfo.uid;
			2. 将应用名称和uid拼成一个字符串输出,真机查看主流应用(微信,QQ)的uid
			3. 例如 QQ：10083, 进入QQ(10083)目录命令：cd 10083
			4. 进入QQ10083：cd 10110
				下载：168251
				上传：23544
 				tcp_rcv :代码下载的数据
 				tcp_snd：代表上传的数据

	- 手机安装360, 验证流量准确性
	- 创建TrafficeManagerActivity
	- 流量统计的api介绍

			TrafficStats.getMobileRxBytes();// 3g/2g下载总流量
			TrafficStats.getMobileTxBytes();// 3g/2g上传总流量
	
			TrafficStats.getTotalRxBytes();// wifi+手机下载流量
			TrafficStats.getTotalTxBytes();// wifi+手机上传总流量
	
			TrafficStats.getUidRxBytes(10085);// 某个应用下载流量
			TrafficStats.getUidTxBytes(10085);// 某个应用上传流量

			这里需要注意的是，通过 TrafficStats 获取的数据在手机重启的时候会被清空，所以，如果要对流量进行持续的统计需要将数据保存到数据库中，在手机重启时将数据读出进行累加即可

	- 流量报警原理简介

			流量校准的工作原理就给运营商发短信

			A：开启超额提醒
			B：设置每月流量套餐300MB
 			C：自动校准流量-流量短信设置
 			D：演示发短信给运营商；

	- 联网防火墙简介

			在linux上有一款强大的防火墙软件iptable
			360就是把这款软件内置了
			如果手机有root权限，把防火墙软件装到手机的内部，并且开启起来。
			以后就可以拦截某个应用程序的联网了。
			
			如果允许某个软件上网就什么也不做。如果不允许某个软件上网，就把这个软件的所有的联网操作都定向到本地，这时就不会产生流量了。

	- Android下的开源防火墙项目droidwall

			登录code.google.com,搜索droidwall
			svn地址: https://droidwall.googlecode.com/svn/
			svn检出, 需要翻墙

			常用开源代码网站: github.com, code.google.com

- 抽屉效果 SlidingDrawer

	- 基本实现
	
		    <SlidingDrawer
		        android:id="@+id/slidingDrawer1"
		        android:layout_width="match_parent"
		        android:layout_height="match_parent"
		        android:content="@+id/content"
		        android:handle="@+id/handle" >
		
				//指定抽屉把手
		        <ImageView
		            android:id="@id/handle"
		            android:layout_width="wrap_content"
		            android:layout_height="wrap_content"
		            android:src="@drawable/lock" />
		
				//指定抽屉内容
		        <LinearLayout
		            android:id="@id/content"
		            android:layout_width="match_parent"
		            android:layout_height="match_parent"
		            android:background="#9e9e9e"
		            android:gravity="center" >
		
		            <TextView
		                android:layout_width="wrap_content"
		                android:layout_height="wrap_content"
		                android:text="我是小抽屉" />
		        </LinearLayout>
		    </SlidingDrawer>

	- 把抽屉做成从右向左拉

			android:orientation="horizontal"

	- 实现腾讯抽屉竖直方向显示一小半功能

			只需在抽屉上方增加一个空view

			  <View
        		android:layout_width="match_parent"
       			android:layout_height="200dp" />

	- 水平方向显示一小半

			  <LinearLayout
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:orientation="horizontal" >
	
	        <View
	            android:layout_width="100dp"
	            android:layout_height="match_parent" />
	
	        <SlidingDrawer
	            android:id="@+id/slidingDrawer1"
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"
	            android:orientation="horizontal"
	            android:content="@+id/content"
	            android:handle="@+id/handle" >
	
	            <ImageView
	                android:id="@id/handle"
	                android:layout_width="wrap_content"
	                android:layout_height="wrap_content"
	                android:src="@drawable/lock" />
	
	            <LinearLayout
	                android:id="@id/content"
	                android:layout_width="match_parent"
	                android:layout_height="match_parent"
	                android:background="#9e9e9e"
	                android:gravity="center" >
	
	                <TextView
	                    android:layout_width="wrap_content"
	                    android:layout_height="wrap_content"
	                    android:text="我是小抽屉" />
	            </LinearLayout>
	        </SlidingDrawer>
	    </LinearLayout>

	- 小锁图片显示上面

		  <LinearLayout
                android:id="@id/handle"
                android:layout_width="wrap_content"
                android:layout_height="match_parent"
                android:orientation="vertical" >

                <ImageView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:src="@drawable/lock" />
          </LinearLayout>

- TabActivity的使用(知识拓展)

	- 创建CleanActivity, 继承TabActivity
	- 编写布局文件

			<?xml version="1.0" encoding="utf-8"?>
			<TabHost xmlns:android="http://schemas.android.com/apk/res/android"
			    android:id="@android:id/tabhost"
			    android:layout_width="match_parent"
			    android:layout_height="match_parent"
			    android:orientation="vertical" >
			
			    <LinearLayout
			        android:layout_width="match_parent"
			        android:layout_height="match_parent"
			        android:orientation="vertical" >
	
					//内容体
			        <FrameLayout
			            android:id="@android:id/tabcontent"
			            android:layout_width="match_parent"
			            android:layout_height="0dp"
			            android:layout_weight="1" >
			        </FrameLayout>
	
					//标签体
			        <TabWidget
			            android:id="@android:id/tabs"
			            android:layout_width="match_parent"
			            android:layout_height="wrap_content" >
			        </TabWidget>
			    </LinearLayout>
			
			</TabHost>

		------------------------------------

			/**
			 * 缓存清理主页面
			 * 
			 * @author Kevin
			 * 
			 */
			public class CleanActivity extends TabActivity {
			
				@Override
				protected void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					setContentView(R.layout.activity_clean_base);
			
					TabHost host = getTabHost();
			
					TabSpec tab1 = host.newTabSpec("缓存清理").setIndicator("缓存清理");
					TabSpec tab2 = host.newTabSpec("SD卡清理").setIndicator("SD卡清理");
			
					tab1.setContent(new Intent(this, CleanCacheActivity.class));
					tab2.setContent(new Intent(this, CleanSdcardActivity.class));
			
					host.addTab(tab1);
					host.addTab(tab2);
				}
			}

- sdcard清理

	- 查看金山缓存文件夹数据库clearpath.db
	- 原理介绍

			1. 查询数据库中的所有缓存文件目录
			2. 如果文件夹存在, 执行删除操作
			
- 自定义Application

	- 写一个demo

			/**
			 * 自定义全局Application 应用全局的初始化逻辑可以放在此处运行
			 * 这是一个单例类, 整个应用只有一个
			 * @author Kevin
			 */
			public class MyApplication extends Application {
			
				@Override
				public void onCreate() {
					super.onCreate();
					System.out.println("MyApplication onCreate");
				}
			
				public void doSomething() {
					System.out.println("doSomething....");
				}
			
			}

			----------------------

			public class MainActivity extends Activity {

				@Override
				protected void onCreate(Bundle savedInstanceState) {
					super.onCreate(savedInstanceState);
					setContentView(R.layout.activity_main);
					System.out.println("MainActivity onCreate");
			
					MyApplication app = (MyApplication) getApplication();// 获取自定义的Application
					app.doSomething();
				}
			}

			----------------------

			清单文件中配置

			  <application
       			 android:name=".MyApplication"


	- 手机卫士自定义Application

			MobileSafeApplication

- 全局捕获异常

	- 模拟异常, 比如int i = 1/0, 演示崩溃情况
	- 代码实现
	
			/**
			 * 自定义全局Application
			 * 
			 * @author Kevin
			 * 
			 */
			public class MobileSafeApplication extends Application {
			
				@Override
				public void onCreate() {
					super.onCreate();
			
					// 设置未捕获异常处理器
					Thread.setDefaultUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
				}
			
				class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {
			
					// 未捕获的异常都会走到此方法中
					// Throwable是Exception和Error的父类
					@Override
					public void uncaughtException(Thread thread, Throwable ex) {
						System.out.println("产生了一个未处理的异常, 但是被哥捕获了...");
						// 将异常日志输入到本地文件中, 找机会上传到服务器,供技术人员分析
						File file = new File(Environment.getExternalStorageDirectory(),
								"error.log");
						try {
							PrintWriter writer = new PrintWriter(file);
							ex.printStackTrace(writer);
							writer.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
			
						// 结束当前进程
						android.os.Process.killProcess(android.os.Process.myPid());
					}
				}
			}
		
- 代码混淆

	- 代码未混淆的前提下,打包,并进行反编译, 发现源码都可以看到, 很不安全
	- 找到项目根目录下的文件project.properties, 打开混淆注释
	
			proguard.config=${sdk.dir}/tools/proguard/proguard-android.txt:proguard-project.txt

	- 分析文件proguard-android.txt
	- 将proguard-android.txt文件拷贝到项目根目录,方便以后修改

			proguard.config=proguard-android.txt:proguard-project.txt

	- 重新打包并反编译,查看效果
	- 结论: 混淆后,会将类名,方法名编译成a,b,c,d等混乱的字母, 提高代码阅读成本,增强安全性

- 嵌入广告

	- 分析app, 广告公司, 广告平台的关系

			广告平台相当于中间商, 是app和广告公司的媒介, 抽成盈利

	- 盈利方式

		- 展示次数, 1000次 1毛5左右 , 1分钟展示3条广告
		- 点击次数, 1次 1毛5左右
		- 有效点击, 1次 1元左右

	- 广告公司

			有米, 百度, 360, 万普, panda

	- 国外广告公司

			StartApp
			
			


