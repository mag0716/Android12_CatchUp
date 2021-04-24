# Behavior changes: all apps

https://developer.android.com/about/versions/12/behavior-changes-all

## User experience

### Foreground service notifications UX delay

Android 12では、短時間で実行されるフォアグラウンドサービスにおいて通知が一瞬だけ表示されないように、通知を10秒遅らせることができるようになる。
以下の特徴を1つでも持つ場合は即座に通知が表示される

* アクションボタンを持っている
* `foregroundServiceType` が `connectedDevice`, `mediaPlayback`, `mediaProjection`, `phoneCall`
* NotificationのCategory属性に着信、地図、メディア再生を指定したユースケースを提供している
  * Note: 今後の Developer Preview で変わる可能性がある
* `setShowForegroundImmediately()` を呼び出している

### Immersive mode improvements for gesture navigation

Android 12では、Immersive modeが簡素化されジェスチャーナビゲーションがより簡単になり、動画や読書などの体験との整合性が保たれるようになる。
詳細は https://developer.android.com/about/versions/12/features#immersive-mode-improvements を参照。

### Web intent resolution

Android 12では、Webリンクをより効率的に利用するために未検証のドメインが含まれる場合でもデフォルトブラウザで開く。アプリは以下の方法でドメインの承認を得ることができる。

* [Android App Links](https://developer.android.com/training/app-links/verify-site-associations)を使ってドメインを検証する
* ユーザがシステムに対して自身のアプリで開く許可をリクエストする

詳細は https://developer.android.com/about/versions/12/web-intent-resolution を参照。

### Restrictive App Standby bucket

[App Standby Buckets](https://developer.android.com/topic/performance/appstandby) はアプリの使用状況に基づいてシステムがアプリのリソース共有の優先順を決めるのに役立つ。

bucketごとに優先順が表現されており、一番低い優先順ではシステムがアプリの実行に制限をかける。

Android 12から `restricted` という名前のbucketが追加され、より優先順が低いものとなる。

1. Active
1. Working set
1. Frequent
1. Rare
1. Restricted

システムはアプリの動作と利用状況を考慮してどのbucketに入れるかを決定する。アプリがより責任を持ってシステムリソースを利用する場合は `restricted` bucket に入れられる可能性はひくくなる。

ユーザと直接やり取りするアプリはより制限のゆるいbucketに配置する。

#### Power management restrictions

`restricted` bucketにあるアプリでは以下が制限される。

* 1日1回10分までジョブを実行することが可能。この間は他のアプリのジョブとグループ化される。
* `setInexactRepeating()`, `setAndAllowWhileIdle()`, `setWindow()`によって作られた不正確なアラームを1日1回実行することができる。
* High PriorityのFCMは1日に5回タイムリーに受け取ることができる。それ以降のFCMはNormal Priorityとして扱われるのでデバイスが節電モードになっていれば遅延する可能性がある

Note: 他のbucketと異なりこれらの電力管理の制限は充電中であっても適用される。ただし、充電中、アイドル中、課金されなネットワークにある場合は緩和される

#### Foreground services allowance

`restricted` bucketにあるアプリでもForeground Serviceを実行することは可能だが、`targetSdkVersion`をAndroid 12にすると[Foreground service launch restrictions](https://developer.android.com/about/versions/12/foreground-services)が影響する。

#### Check weather your app is in the restricted bucket

`getAppStandbyBucket()`の戻り値が`STANDBY_BUCKET_RESTRICTED`であれば、`restricted` bucketに配置されている。

#### Test the restricted bucket behavior

以下のコマンドを実行すると、`restricted` bucketに配置することができる。

```
adb shell am set-standby-bucket PACKAGE_NAME restricted
```

## Graphics and images

### Improved refresh rate switching

Android 12では、ディスプレイが新しいリフレッシュレートへのシームレスな移行(1-2秒間の黒い画面など視覚的な中断がない状態)をサポートしているかどうかに関わらず、`setFrameRate()`でリフレッシュレートの変更が可能になる。これまではディスプレイが対応していない場合は`setFrameRate()`が呼び出された後も通常は同じリフレッシュレートが利用されていた。`getAlternativeRefreshRates()`を呼び出すことでシームレスに移行ができるかどうかを判断できる。通常、`onDisplayChanged()`は切り替えが完了した後に呼び出されるがシームレスではない移行時に呼び出されるものもある。

## Security

### Untrusted touch events are blocked

Android 12では、セキュリティとUXを維持するために、一部の例外を除き、特定のウィンドウを通過するタッチはブロックされる

#### Affected apps

`FLAG_NOT_TOUCHABLE` をへの区するなどして、ウィンドウにタッチを通過させることを洗濯したアプリに影響する

* `TYPE_APPLICATION_OVERLAY`と`FLAG_NOT_TOUCHABLE`を使っている
* `Activity`のウィンドウに対して`FLAG_NOT_TOUCHABLE`を使っている
* `Toast`

#### Exceptions

以下のケースではタッチが通過する

* 自身のアプリ内で表示したウィンドウ
* 信頼されたウィンドウ
  * アクセシビリティ
  * IME
  * アシスタント
  * Note: `TYPE_APPLICATION_OVERLAY`は信頼されない
* ウィンドウのルートViewが`GONE`, `INVISIBLE`
* アルファ値が0.0
* システムが十分な透明度と判断したウィンドウ。DP1では0.8だが変更される可能性がある

#### Detect when an untrusted touch is blocked

ブロックを検知したら`Untrusted touch due to occlusion by PACKAGE_NAME`がLogcatに出力される

#### Test the changes

DP1ではデフォルトでブロックされており、以下のadbコマンドで切り替えることができる

無効化
```
# A specific app
adb shell am compat disable BLOCK_UNTRUSTED_TOUCHES com.example.app

# All apps
# If you'd still like to see a Logcat message warning when a touch would be
# blocked, use 1 instead of 0.
adb shell settings put global block_untrusted_touches 0
```

有効化
```
# A specific app
adb shell am compat reset BLOCK_UNTRUSTED_TOUCHES com.example.app

# All apps
adb shell settings put global block_untrusted_touches 2
```

### Apps can't close system dialogs

Android 12では、アプリとシステムとのやりとり時のユーザー操作を改善するために、いくつかの特別なケースを除き、`ACTION_CLOSE_SYSTEM_DIALOGS` はdeprecatedになり、`Intent`発行時に以下のいずれかの動作となる。

* `targetSdkVersion`がAndroid 12：`SecurityException`
* `targetSdkVersion`がAndroid 11以下：`Intent`は実行されず、Logcatにログが出力される。

#### Exceptions

以下のケースではAndroid 12でもシステムダイアログを閉じる

* instrumentation testを実行中
* `targetSdkVersion`が11以下で通知エリアの上部にウィンドウが表示されている
  * Note:`targetSdkVersion`が12の場合は、システムは通知エリアを自動的に閉じるので、`ACTION_CLOSE_SYSTEM_DIALOGS`の利用は不要
* `targetSdkVersion`が11以下で、ユーザが通知を操作しており、通知のアクションボタンを使用する可能性がある場合

## Updated non-SDK interface restrictions

* [Non-SDK interfaceを使っていないかのテスト方法](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces#test-for-non-sdk)
* [Updates to non-SDK interface restrictions in Android 12](https://developer.android.com/about/versions/12/non-sdk-12)
* [Restrictions on non-SDK interfaces](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces)
