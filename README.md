# suger a super useful kotlin Utils

[![](https://jitpack.io/v/littlejigege/suger.svg)](https://jitpack.io/#littlejigege/suger)


# Usage

## Step 1
> Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

  ## Step 2
> Add the dependency
```groovy
dependencies {
	        compile 'com.github.littlejigege:suger:v0.0.7'
		implementation 'com.google.code.gson:gson:2.8.2'
		compile "org.jetbrains.kotlinx:kotlinx-coroutines-android:0.19.2"
	}
```
## Step 3
> init it in your Application.class
```kotlin
override fun onCreate() {
        super.onCreate()
        Utils.init(this)
    }

```
Readme Progress
- [x] request permission
- [x] open local alubm
- [x] EasyNotifyAdapter
- [ ] ImageUtils
- [ ] ToastUtils


---
### Easy way to open System Alubm and choose photo

First create an activity extend AlbumPickerActivity

```  
//获取权限
Permission.STORAGE.doAfterGet(this){
//选择照片
AlbumPicker.with(this).selectedPicAndHandle { path -> 
	print("this is the path of the photo you choose  $path")
        }
}
```
---
### Easy way to deil with preference

```kotlin
		//easy to save
        Preference.save("settings") {
            "key" - "value"
            "key1" - "value1"
            //...
        }
        //easy to get
        Preference.get("settings","key" to "defaultValue")
```

### Easy way to deil with permission

first you need to extends PermissionActivity or PermissionCompatActivity

```kotlin
class MyActivity : PermissionActivity() {
}
```

Permission中有所有的危险权限组

下面用读写权限举例

你可以获取他:

```kotlin
Permission.STORAGE.get(this){
  //isPassed是回调，通过的话是true
            isPassed -> if(isPassed) showToast("ok") else showToast("no")
        }
```

你可以指定一段代码在获取某个权限后执行:

```kotlin
Permission.STORAGE.doAfterGet(this){
  //只有权限痛过才会执行，保证安全
            showToast("66666")
        }
```

你也可以单独检查是否获得某个权限:

```kotlin
Permission.STORAGE.has()
```


---




