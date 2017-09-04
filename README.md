# Stroll   [![](https://jitpack.io/v/waws80/Stroll.svg)](https://jitpack.io/#waws80/Stroll)
第一个测试版本
#### 默认添加了如下权限：
```java
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

## Gradle

#### step1
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### step2
```
dependencies {
	compile 'com.github.waws80:Stroll:v1.3'
}

```


## Maven

#### step1
```
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

#### step2
```
<dependency>
    <groupId>com.github.waws80</groupId>
    <artifactId>Stroll</artifactId>
    <version>v1.3</version>
</dependency>


```

# use
## Kotlin
### step1
```
Stroll.install()
```
### step2
####  获取数据示例
```java
Stroll.get()
	.setBaseUrl("https://www.baidu.com")
	.setCallBack(object : StringCallBack {
	    override fun success(text: String) {
		StrollLog.msg(text)
	    }
	    override fun error(msg: String) {
		StrollLog.msg(msg)
	    }
	})
	.build()
```
##### or DSL 写法
```java
data {
    baseUrl = "https://www.baidu.com"
    result { text -> StrollLog.msg(text)
	StrollLog.msg(text)
    failer { msg -> StrollLog.msg(msg)
	StrollLog.msg(msg)
}
```

#### 下载文件示例
```java
Stroll.downloadFile()
                    .setBaseUrl("http://gdown.baidu.com/data/wisegame/a920cdeb1c1f59bc/baiduwangpan_527.apk")
                    .savePath("sdcard/Stroll", "a.apk")
                    .setCallBack(object : DownloadFileCallBack{
                        override fun start() {
                        }

                        override fun progress(pro: Int) {
                            StrollLog.msg("下载文件：进度为：$pro")
                        }
                        override fun complate() {
                            StrollLog.msg("下载文件：下载完成！")
                        }

                        override fun error(msg: String) {
                            StrollLog.msg("下载文件：下载出错！$msg")
                        }

                    })
                    .build()
```
##### or DSL 写法
```java
download {
	baseUrl = "http://gdown.baidu.com/data/wisegame/a920cdeb1c1f59bc/baiduwangpan_527.apk"
	savePath = "sdcard/Stroll"
	fileName = "a.apk"
	progress { pro ->
		StrollLog.msg("下载文件进度：$pro")
	}
	complate {
		StrollLog.msg("下载完成！")
	}
	failer { msg ->
		StrollLog.msg("下载出错：$msg")
	}
    }
```
#### 加载图片示例
##### 无回调
```java
val target = View(context)
val path = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1536086522,2785217828&fm=26&gp=0.jpg"
Stroll.loadImageWithUrl(target, path)
```
##### 有回调
```java
Stroll.loadImageWithUrl(targetView, url,true,object : ImageListener{
            override fun progress(progress: Int) {
                StrollLog.msg("下载进度：$progress")
            }

            override fun complate() {
	    StrollLog.msg("下载完成")
            }

            override fun error() {
	    StrollLog.msg("下载出错")
            }

        })
```

