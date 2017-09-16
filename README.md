# suger

[![](https://jitpack.io/v/littlejigege/suger.svg)](https://jitpack.io/#littlejigege/suger)


哥哥们，有好东西都往里面放吧

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
	        compile 'com.github.littlejigege:suger:v0.0.2'
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
