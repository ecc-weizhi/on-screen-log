on-screen-log
==================
[ ![Download](https://api.bintray.com/packages/ecc-weizhi/maven/on-screen-log/images/download.svg) ](https://bintray.com/ecc-weizhi/maven/on-screen-log/_latestVersion)

A simple to use logging library for when you want to display logs on screen (instead of in logcat).

![Demo of on-screen-log adapter](../master/siteassets/adapter.gif "on-screen-log adapter") ![Demo of on-screen-log notification](../master/siteassets/notification.gif "on-screen-log notification")

## Feature
- Provides a ready-to-use `RecyclerView.Adapter` that you can use right away in a `RecyclerView` of your choice. 
- Display log snippet in notification
- Clicking notification will display logs in activity.
- Has support for [Timber](https://github.com/JakeWharton/timber)

## Install
Add a new dependency in your `build.gradle` file
```
dependencies {
    implementation 'app.eccweizhi:onscreenlog:0.0.11'
}
```

#### Timber module
Additionally, on-screen-log includes a module for using it with [Timber](https://github.com/JakeWharton/timber).
```
dependencies {
    // no need to add onscreenlog if you are already adding onscreenlog-timber
    implementation 'app.eccweizhi:onscreenlog-timber:0.0.11'
}
```

## Usage

Initialize an instance of OnScreenLog to be reused throughout the app.
```
OnScreenLog onScreenLog = OnScreenLog.builder()
        .context(this)        // compulsory. You can use any Context (Application, Activity etc)
        .notificationId(1)    // compulsory. Provides a notification id for notification
        .capacity(500)        // optional. Max number of log messages. Earliest logged message will be dropped.
        .outputToLogcat(true) // optional. If true, display log messages in logcat
        .build();
```

#### Log messages
To log messages, use `OnScreenLog` the same way as Android `Log` class.
```
OnScreenLog onScreenLog;
...

onScreenLog.v("tag", "this is a verbose log");
onScreenLog.d("tag", "this is a debug log");
onScreenLog.i("tag", "this is a info log");
onScreenLog.w("tag", "this is a warn log");
onScreenLog.e("tag", "this is a error log");
onScreenLog.wtf("tag", "this is a wtf log");
```

#### Adapter
`OnScreenLog` provides an adapter that you can simply used to ouput log messages to any `RecyclerView`.
```
OnScreenLog onScreenLog;
...
RecyclerView recyclerView = findViewById(R.id.myRecyclerView);
recyclerView.setAdapter(onScreenLog.getAdapter());
```

#### Notification
`OnScreenLog` will display logs as notification automagically. Just tap on the notification to view logs.

#### Timber support (only for `onscreenlog-timber`)
Create an `OnScreenLoggingTree` and plant it.
```
OnScreenLog onScreenLog;
...

OnScreenLoggingTree tree = new OnScreenLoggingTree(onScreenLog);
Timber.plant(tree);
```

You can now log messages using Timber.
```
Timber.v("this is a verbose log");
Timber.d("this is a debug log");
Timber.i("this is a info log");
Timber.w("this is a warn log");
Timber.e("this is a error log");
Timber.wtf("this is a wtf log");
```
