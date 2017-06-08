# FeatureAdapter
FeatureAdapter is an library for Android providing an optimized way to represent complex Android screens.
It offers a RecyclerView adapter that let's you define individual features. Each feature can define multiple items with multiple view types.

## Basic Usage

```java
TODO
```

Browse TODO sample.

## Setup

```groovy
   TODO
```

## Main features
The main features of Grox are:
* unify state management. All parts of an app, all screens for instance, can use Grox to unify their handling of state.
* allows time travel debugging, undo, redo, logging, etc. via middlewares.
* simple API. Grox is inspired by Redux & Flux but offers a simpler approach.
* easily integrated with Rx (Rx1 for now, Rx2 will follow soon). Note that it is also possible to use Grox without Rx.
* Grox only relies on a few concepts: States, Actions, Stores, MiddleWare &  Commands (detailed below).
* facilitates using immutable states, but without enforcing it for more flexibility. You can use any solution for immutability ([Auto-Value](https://github.com/google/auto/tree/master/value), [Immutables](https://immutables.github.io/), [Kotlin](https://discuss.kotlinlang.org/t/immutable/1032), etc..) or not use immutability at all if you don't want to.
* Grox can be used with the [Android Arch components](https://developer.android.com/arch), or without them.

## Links
* [CI](https://ci.groupondev.com/job/android/job/FeatureAdapter/)

## Credits 
The following people have been active contributors to the first version of FeatureAdapter:
* Shaheen Ghiassy
* Michael Ma
* Matthijs Mullender 
* Turcu Alin
* Samuel Guirado Navarro
* Keith Smyth 
* Stephane Nicolas
