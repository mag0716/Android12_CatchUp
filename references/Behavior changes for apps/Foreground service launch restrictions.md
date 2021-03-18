# Foreground service launch restrictions

https://developer.android.com/about/versions/12/foreground-services

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

## Cases where foreground service launches from the background are allowed

以下の状況ではアプリがバックグラウンドであってもフォアグラウンドサービスを起動することができる。

* Activityなどユーザが見える状態から遷移してきた
* FCMを使って優先度が高い通知を受け取った
* ユーザがBubble, Notification, Widget, Activityなどアプリに関連するUIを操作した
* geofencingやactivity recognitionに関連するイベントを受け取っている
* デバイス再起動後にBroadcast receiverで`ACTION_BOOT_COMPLETED`を受け取っている
* drive ownersやprofile ownersなどのシステムの役割や権限をもつアプリ
* Companion Device Managerを利用している
* `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`をリクエストし、ユーザが端末の設定画面でバッテリーの最適化を無効化している
  * バッテリーの最適化の画面へ遷移させるために`ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATION`を発行し、バッテリーの最適化を無効化している
* アプリが https://developer.android.com/guide/components/activities/background-starts#exceptions の条件を満たしている
  * ただし、バックスタック内にタスクが存在しているという条件は除く
