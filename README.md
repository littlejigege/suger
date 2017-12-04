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
AlbumPicker.with(this).selectedPicAndHandle { path -> 
	print("this is the path of the photo you choose  $path")
}
```
---

---




