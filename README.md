# Welcome to the MeLi UI Library

### This library consists of two modules
#### - Legacy module (Deprecated)
You can include this module by adding the dependency to your build.gradle
```gradle
compile com.mercadolibre.android:ui_legacy:1.1.0
```

#### - UI module (New)
You can include this module by adding the dependency to your build.gradle
```gradle
compile com.mercadolibre.android:ui:4.X.X
```
##### For more detailed information please check out [the wiki](https://github.com/mercadolibre/fury_mobile-android-ui/wiki/UI-Home)


## Requirements
This library needs Android API 14+ (4.0+) to work.

## Usage
This repo contains an [sample android application](exampleApp), with examples on how to use the different widgets available in the library.

## Material Design
Interesting links and examples of how to integrate the new Material Design in an Android App.

- http://www.google.com/design/spec/material-design/introduction.html#introduction-principles
- https://plus.google.com/u/0/photos/+ChrisBanes/albums/6076324541583323089

- Example App
  - http://android-developers.blogspot.com.ar/2014/08/material-design-in-2014-google-io-app.html
  - https://github.com/google/iosched

- App Compat (Material Design)
  - http://android-developers.blogspot.com.ar/2014/10/appcompat-v21-material-design-for-pre.html

## Color palette
Check out the [wiki](https://github.com/mercadolibre/fury_mobile-android-ui/wiki/Color-Palette) for the color palette.


## How to make a local publish
```
./gradlew build publishToMavenLocal
```
Pro tip: first change the version name in ```gradle.properties``` to ```X.XX.X-LOCAL```