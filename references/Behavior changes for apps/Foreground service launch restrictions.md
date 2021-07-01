# Foreground service launch restrictions

https://developer.android.com/about/versions/12/foreground-services

`targetSdkVersion`がAndroid 12を対象としたアプリはいくつかの特殊なケースを除き、バックグラウンドでの実行中にフォアグラウンドサービスを開始できなり、`ForegroundServiceNotAllowedException`がスローされる。

Note： アプリが`Context.startForegroundService()`を呼び出して、他のアプリのフォアグラウンドサービスを起動した場合、両方のアプリの`targetSdkVersion`がAndroid 12となっている場合のみこの制約が適用される。

## Recommended alternative to foreground services: WorkManager

アプリがこの変更により影響を受ける場合、`WorkManager`への移行が推奨される。
Android 12のbeta版がリリースされるとき、`WorkManager`は優先度の高いバックグラウンドのタスクを開始するための推奨された解決策となっている。

`WorkManager` 2.7.0から、Expedited jobsの`Worker`の定義に`setExpedited()`を呼び出すことができる。
このAPIはAndroid 12のデバイス上ではExpedited jobsとして使われ、Android 11以下ではフォアグラウンドサービスとして使われる。

開発者が意図的に実装できるように、`CoroutineWorker.setForeground()`, `ListenableWorker.setForegroundAsync()`はdeprecatedになるので、`setExpedited()`への移行が推奨される。

サンプルコードは https://github.com/android/architecture-components-samples/tree/android-s/WorkManagerSample を参考。

## Expedited jobs

Android 12で新たに追加されたExpedited jobsはアプリが重要なタスクを実行すると同時にシステムがリソースへのアクセスをよりよく制御できるようにする。フォアグラウンドサービスとJobSchedulerの中間のような特徴を持つ。

* Battery SaverやDozeなどのシステムの電源管理制約に影響を受けない
* システムは実行可能であれば即座に開始する

### Expedited jobs might be deferred

システムは可能な限り早くExpedited jobsを実行を試みるが、実行中のジョブが多すぎる場合やシステムリソースが不足している場合はジョブの開始を延期する可能性がある。

一般的に、システムが以下のいずれかの状況になったときに延期される

* システムの読み込みが高負荷
* Expedited jobsの割当制限を超えた

## Effects on Alarm Manager APIs

一般的に`targetSdkVersion`がAndroid 12のアプリではアラームを使ってフォアグラウンドサービスを開始することはできない。

ただし、正確なアラームがなった時にフォアグラウンドサービスを開始することはできる。正確なアラームを設定するには`SCHEDULE_EXACT_ALARM`パーミッションを宣言する必要がある。

## Cases where foreground service launches from the background are allowed

以下の状況ではアプリがバックグラウンドであってもフォアグラウンドサービスを起動することができる。

* Activityなどユーザが見える状態から遷移してきた
* アプリは既存のタスクのバックスタックにActivityがある場合を除き、バックグラウンドからActivityを開始することができる
* FCMを使って優先度が高い通知を受け取った
* ユーザがBubble, Notification, Widget, Activityなどアプリに関連するUIを操作した
* geofencingやactivity recognitionに関連するイベントを受け取っている
* デバイス再起動後にBroadcast receiverで`ACTION_BOOT_COMPLETED`, `ACTION_LOCKED_BOOT_COMPLETED`, `ACTION_MY_PACKAGE_REPLACED`を受け取っている
* Broadcast receiverで`ACTION_TIMEZONE_CHANGED`, `ACTION_TIME_CHANGED`, `ACTION_LOCALE_CHANGED`を受け取っている
* `BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`パミーッションが必要なBluetoothのbroadcastを受け取っている
* device ownersやprofile ownersなどのシステムの役割や権限をもつアプリ
* Companion Device Managerを利用している
  * コンパニオンデバイスが近くにある時にシステムがアプリを起動するためには https://developer.android.com/about/versions/12/features#keep-awake を実装する
* `START_STICKY`, `START_REDELIVER_INTENT`のいずれかを起動することでstickyフォアグラウンドサービスを使用する
  * stickyフォアグラウンドサービスはシステムによってアプリが強制終了されるとシステムが自動的にアプリを再起動する
* ユーザーがアプリのバッテリー最適化を無効化している
  * ユーザーに設定してもらうために`ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS`を利用する
