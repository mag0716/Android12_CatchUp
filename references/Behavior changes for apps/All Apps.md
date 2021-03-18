# Behavior changes: all apps

https://developer.android.com/about/versions/12/behavior-changes-all

## User experience

### Immersive mode improvements for gesture navigation

Android 12では、Immersive modeが簡素化されジェスチャーナビゲーションがより簡単になり、動画や読書などの体験との整合性が保たれるようになる。
詳細は https://developer.android.com/about/versions/12/features#immersive-mode-improvements を参照。

### Foreground service notifications UX delay

Android 12では、短時間で実行されるフォアグラウンドサービスにおいて通知が一瞬だけ表示されないように、通知を10秒遅らせることができるようになる。
以下の特徴を1つでも持つ場合は通知が表示される

* アクションボタンを持っている
* `foregroundServiceType` が `connectedDevice`, `mediaPlayback`, `mediaProjection`, `phoneCall`
* NotificationのCategory属性に着信、地図、メディア再生を指定したユースケースを提供している
  * Note: 今後の Developer Preview で変わる可能性がある
* `setShowForegroundImmediately()` を呼び出している

## Privacy

### Netlink MAC restriction

Android 12ではシステム以外の全てのアプリに対して、変更不可能な識別子であるデバイスのMACアドレスへのアクセスを制限する。

`targetSdkVersion`が12：nullが返る
`targetSdkVersion`が11以前：固定の値(02:00:00:00:00:00)が返る

低レベルなAPI(`NetworkInterface`, `getifaddrs()`など)ではなく`ConnectivityManager`を使うべき。
`NetworkInterface.getHardwareAddress()`を利用すると、Logcatに`CompatibilityChangeReporter: Compat change id reported: 170188668;`が出力される
`RETURN_NULL_HARDWARE_ADDRESS`を変更することで戻り値をnullと固定値で切り替えることができる

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
