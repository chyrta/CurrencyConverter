# Currency Converter

A fully-reactive currency converter, utilizing Model-View-Intent pattern.

## Screenshots

<p>
  <img src="art/screenshot.png" width="45%">
  <img src="art/converterdemo.gif" width="45%">
</p>


## Architecture

This project utilizes MVI pattern according to the official sample by [oldergod](https://github.com/oldergod/android-architecture)

## Unit tests

To run unit tests, use this command:

```
./gradlew test
```

## Linting

To perform linting of the code:

```
./gradlew ktlint
```

Also, there is another command to fix the code automatically:

```
./gradlew ktlintFormat
```

## Libraries

* RxJava 2
* Retrofit
* Koin
