# IosSwipeSearchCompose [![](https://jitpack.io/v/commandiron/swipe-search-compose.svg)](https://jitpack.io/#commandiron/swipe-search-compose)

Add Ios Swipe Search Component in Android Jetpack Compose.

## How it looks

## Usage

```kotlin
val text = remember { mutableStateOf("") }
SwipeSearch(
    modifier = Modifier.fillMaxSize(),
    textValue = text.value,
    onValueChange = {
        text.value = it
    }
)
```


## Setup
1. Open the file `settings.gradle` (it looks like that)
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // add jitpack here 👇🏽
        maven { url 'https://jitpack.io' }
       ...
    }
} 
...
```
2. Sync the project
3. Add dependencies
```groovy
dependencies {
    implementation 'com.github.commandiron:swipe-search-compose:1.0.0'
}
```
