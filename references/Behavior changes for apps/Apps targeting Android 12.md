# Behavior changes: Apps targeting Android 12

https://developer.android.com/about/versions/12/behavior-changes-12

## User experience

### Picture-in-picture behavior improvements

Android 12ではPicture-in-pictureモードの動作が改善される。詳細は https://developer.android.com/about/versions/12/features/pip-improvements を参照。

### Custom notifications

Android 12では全てのカスタムNotificationの見た目が変更される。
以前は通知領域全体をカスタマイズできたので、ユーザーを混乱させたり異なるデバイス上でのレイアウトの互換性の問題を引き起こすアンチパターンとなっていた。

`targetSdkVersion`がAndroid 12のアプリでは、custom content viewsは利用せず、代わりにシステムが提供する標準的なテンプレートを適用することになる。
`Notification.DecoratedCustomViewStyle`はアイコン、拡大、縮小ボタン、アプリ名など他のNotificationと同じ要素を持つ。

カスタムNotificationを利用している場合は可能な限り新しいテンプレートを適用してテストすることが推奨される。

1. カスタムNotificationの変更を有効化する
  * `targetSdkVersion`に`S`を指定
  * コンパイル
  * Android 12の端末にインストール
1. カスタムViewを使った全てのNotificationをテストし期待通りの見た目になるかを確認する
1. カスタムNotificationで利用できるViewのサイズは以前よりも小さくなっているので、サイズ計算に注意
1. 優先度を`HIGH`に変更しHeads UpとしてNotificationを表示し、期待通りの見た目になるかを確認する

### Android App Links verification changes

`targetSdkVersion`がAndroid 12のアプリでは、Android App Linksの承認の仕方についていくつかの変更がある。これらの変更は信頼性と開発者、ユーザーがよりコントロールできるようにする。

https://developer.android.com/about/versions/12/web-intent-resolution#update-declarations は承認プロセスの変更をサポートし、https://developer.android.com/about/versions/12/web-intent-resolution#manually-invoke-domain-verification にてテストすることもできる。

## Privacy

### Approximate location

`targetSdkVersion`がAndroid 12のアプリではユーザはアプリがおおよその位置情報のみへアクセスするようにリクエストすることができる。

Note: `ACCESS_FINE_LOCATION`なしで`ACCESS_COARSE_LOCATION`のみのアプリには影響しない。

`targetSdkVersion`がAndroid 12のアプリが、`ACCESS_FINE_LOCATION`をリクエストする場合は`ACCESS_COARSE_LOCATION`も1つのリクエストとしてリクエストしてあげる必要がある。

`ACCESS_FINE_LOCATION`と`ACCESS_COARSE_LOCATION`を同時にリクエストするとシステムは以下の選択肢が新たに含まれるようになる。

* Precise：`ACCESS_FINE_LOCATION`が提供する正確な位置情報
* Approximate：`ACCESS_COARSE_LOCATION`が提供するおおよその位置情報

詳細は https://developer.android.com/about/versions/12/approximate-location

### App hibernation

Android 12ではAndroid 11で導入されたパーミッションの自動リセットが拡張される。
`targetSdkVersion`がAndroid 12のアプリをユーザが数ヶ月利用しないとシステムはパーミッションを自動でリセットし休止状態とする。

Note: `DeviceAdminService`を実装していたりデバイスオーナーとして動作しているアプリについては対象にならない。

休止状態は以下の特徴がある。

* システムはアプリのキャッシュを削除することでストレージ領域を最適化する
* アプリはバックグラウンドでのジョブが実行できない
* アプリはhigh-priorityを含むプッシュ通知を受け取れない

ユーザがアプリを操作すると休止状態を抜け、ジョブの作成やプッシュ通知の受信が再度可能になる。
ただし、休止状態前に実行していたジョブなどのリスケジュールは必要にいなる。
この動作は端末の設定アプリから手動でforce-stopsするのと類似している。
より簡単にサポートするためにはWorkManagerを利用するとよい。
休止状態から抜けた時とデバイスの再起動時に実行される`ACTION_BOOT_COMPLETED`契機でのリスケジュールすることができる。

#### Request user to disable hibernation

アプリのユースケースが休止状態の影響が予想される場合、アプリに休止状態とパーミッションの自動リセットを除外するリクエストをユーザに送信することができる。
ユーザがアプリを操作せずにバックグラウンドで動作することを期待しているような以下のアプリの場合に有用。

* 家族の位置情報を定期的に提供する
* 端末とサーバのデータを同期する
* TVなどのスマートデバイスと連携する
* Watchなどのデバイスとペアリングする

リクエストするために`Intent.ACTION_APPLICATION_DETAILS_SETTINGS`を含むIntentを実行する。

Note: Intentを発生させる前になぜシステム設定に移動するのかを通知するUIを見せることを検討した方がよい。

#### Test the hibernation behavior

休止状態をテストするためにはadbコマンドを利用する。

### Motion sensors are rate-limited

潜在的にユーザーのセンシティブな情報になりうるデータを保護するため、`targetSdkVersion`をAndroid 12にしたアプリでは加速度、ジャイロ、地磁気センサーから取得した値は更新レートが200Hzに制限される。

それ以上高いレートが必要になる場合は、`HIGH_SAMPLING_RATE_SENSORS`パーミッションの定義が必要になり、パーミッション定義がない場合は`SecurityException`が発生する。

Note: もしマイクへのアクセスを無効にしている場合、`HIGH_SAMPLING_RATE_SENSORS`の定義に関わらずモーションセンサーと位置センサーは制限される。

### Data access auditing

Android 11で追加されたData access auditing APIはアプリのユースケースベースのタグを生成することができる。
これらのタグは特定のデータアクセスをアプリがしているかどうかを簡単に調べることができる。

`targetSdkVersion`がAndroid 12のアプリでは、これらのタグをマニフェストファイルに定義する必要がある。
もし定義していない場合はシステムは`null`がタグとして利用される。

### Modern SameSite cookies in WebView

ChromiumのサードパーティCookieの扱いの変更が`WebView`に取り込まれる。

`SameSite`属性がリクエストを同一サイト以外にも送るかどうかを制御できるようになり、意図しないクロスサイト共有から保護するための変更が含まれる。

* `SameSite`属性が指定されていない場合は`SameSite=Lax`として扱われる
* `SameSite=None`は`Secure`属性と合わせて指定する必要がある
* HTTPとHTTPS間のリンクはクロスサイトリクエストとして扱われるようになり、`SameSite=None; Secure`と指定されない限りCookieは送信されない

開発者は重要なユーザーフローにおけるクロスサイトCookieの依存関係を特定し、`SameSite`属性を適切に指定する必要がある。

詳細は

* [SameSite Cookies Explained](https://web.dev/samesite-cookies-explained/)
* [Schemeful SameSite](https://web.dev/schemeful-samesite/)

#### Test SameSite behaviors in your apps

`WebView`を使っているかCookieを扱っている場合はAndroid 12の`WebView`でテストし、必要があれば`SameSite`の新しい動作に更新する。

ログインや埋め込みコンテンツ、サインイン、購入、認証フローなどでユーザがセキュアなページに遷移する際に問題がないかを注意する。

以下の手順で`SameSite`の新しい動作を有効化する。

* [WebView devtools](https://chromium.googlesource.com/chromium/src/+/HEAD/android_webview/docs/developer-ui.md#launching-webview-devtools)の`toggling the UI flag webview-enable-modern-cookie-same-site`で手動で有効化する
  * `WebView`のバージョンが`89.0.4385.0`以上
* `targetSdkVersion`をAndroid 12に変更する
* Note:DP1の既知の問題により`Schemeful Same-Site`の変更についてテストすることはできない

#### Other resources

`SameSite`の詳細な情報は https://www.chromium.org/updates/same-site を参照

### ADB backup restrictions

アプリのプライベートデータの保護のために、Android 12では`adb backup`コマンドのデフォルト動作が変わる。
`targetSdkVersion`をAndroid 12にしたアプリの場合、`adb backup`を実行するとアプリのデータは除外される。

`adb backup`でテストするためには`android:debuggable=true`にする必要がある。

Caution：リリースするアプリでは保護のために`android:debuggable=false`とすること。

## Security

### Safer component exporting

`targetSdkVersion`をAndroid 12にしたアプリでは、`intent-filter`を利用しているactivities, services, broadcast receiversに対しては`android:exported`の指定が必須になる。

Warning：`android:exported`を指定していない場合、Logcatに`INSTALL_FAILED_VERIFICATION_FAILURE`が出力され、アプリはAndroid 12の端末にはインストールすることができない。

#### Messages in Android Studio

`android:exported`の指定がない場合、Logcatに以下のエラーメッセージが出力される。

##### Android Studio 2020.3.1 Canary 11 or later

まず、Lintでwarningが表示される

```
When using intent filters, please specify android:exported as well
```

コンパイル時には以下のエラーメッセージが出力される。

```
Manifest merger failed : Apps targeting Android 12 and higher are required \
to specify an explicit value for android:exported when the corresponding \
component has an intent filter defined.
```

##### Older versions of Android Studio

インストール時にエラーメッセージがLogcatに出力される。

```
Installation did not succeed.
The application could not be installed: INSTALL_FAILED_VERIFICATION_FAILURE
List of apks:
[0] '.../build/outputs/apk/debug/app-debug.apk'
Installation failed due to: 'null'
```

### Pending intents mutability

`targetSdkVersion`をAndroid 12にしたアプリでは、`PendingIntent`の生成時にmutabilityフラグ(`PendingIntent.FLAG_MUTABLE`, `PendingIntent.FLAG_IMMUTABLE`)の指定が必須となる。

mutabilityフラグの指定がない場合は、`IllegalArgumentException`となる。

#### Create immutable pending intents whenever possible

大抵のケースではイミュータブルな`PendingIntent`で生成するべき。

ただ以下のようなケースではミュータブルな`PendingIntent`で生成する必要がある。

* `PendingIntent`内のクリップデータの変更を要求するdirect reply actionを持った`Notification`
* `CarAppExtendor`を利用したAndroid Auto Framework関連の`Notification`
* システムが`FLAG_ACTIVITY_MULTIPLE_TASK`や`FLAG_ACTIVITY_NEW_DOCUMENT`を適用する `Bubbles`
* `requestLocationUpdates()`などのAPIを呼び出すことでの位置情報のリクエスト
* `AlarmManager`でのアラームのスケジューリング
  * mutableだと`EXTRA_ALARM_COUNT`の追加が可能になる

他のアプリが`PendingIntent`を呼び出す際に同じコンポーネントが起動するように、ミュータブルな`PendingIntent`を生成する場合は明示的Intentの利用が強く推奨される。

#### Test the pending intent mutability change

mutabilityフラグの定義忘れを検出するために、Android Studio上にLintのWarningが出力される。

Developer Preview中はテストのために`PENDING_INTENT_EXPLICIT_MUTABILITY_REQUIRED`を非活性にすることができる。

### Unsafe intent launches

プラットフォームのセキュリティ改善のため、Android 12ではネストされた`Intent`(別の`Intent`がextraに渡される`Intent`)からの安全でない起動を警告するデバッグ機能が提供される。

#### About nested intents

ネストされた`Intent`とは他の`Intent`が渡された`Intent`のことで以下の両方を実行すると、`StrictMode`違反が発生する。

* アプリがネストされた`Intent`から渡された`Intent`を取得する
* 取得した`Intent`を使って即座に`startActivity()`, `startService()`, `bindService()`を呼び出す

#### Configure your app to detect unsafe launches of nested intents

ネストされた`Intent`からの安全でない起動をチェックするために、`detectUnsafeIntentLaunch()`を呼び出す。
Note：`detectAll()`を呼び出せば、`detectUnsafeIntentLaunch()`も自動的に呼び出される。

##### Use intents more responsibly

アプリがネストされた`Intent`で起動したいケースでは以下のように対応する。

* `Intent`内では必要なextraのみをコピーし、必要なバリデーションを行う。
* 内部のコンポーネントへの遷移：`exported=true`としない。
* アプリ間の遷移：`PendingIntent`で代用する

詳細な情報は https://medium.com/androiddevelopers/android-nesting-intents-e472fafc1933 を参照。

## Performance

### Foreground service launch restrictions

`targetSdkVersion`がAndroid 12のアプリでは、いくつかの特別なケースを除き、バックグラウンド中のアプリからフォアグラウンドサービスを起動すると例外が発生するようになる。
バックグラウンドでの起動については`WorkManager`の利用を検討する。

詳細は https://developer.android.com/about/versions/12/foreground-services を参照。

### Exact alarm permission

アプリがシステムリソースを節約することを推奨するため、`targetSdkVersion`がAndroid 12のアプリでは正確なアラームを設定するためには`SCHEDULE_EXACT_ALARM`パーミッションを持つ必要があり、パーミッションがない場合は`SecurityException`となる。

正確なアラームはユーザが直面する機能でのみ利用すべき。

ユーザとシステムは`Alarm & reminders`の特別なアプリアクセスを取り消すことができ、取り消すとアプリは停止しアラームはキャンセルされる。

`Alarm & reminders`が許可されると、システムは`ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED`が通知されるので、アプリは以下に従って`BroadcastReceiver`を実装する必要がある。

1. `canScheduleExactAlarms()`を呼び出すことでアプリが権限があるかを確認する
1. アプリは必要があれば現在の状態に従ってリスケジュールする。`ACTION_BOOT_COMPLETED`を受け取った時にもリスケジュールしておく必要がある。

Caution： アプリが設定したアラームをシステムが正確にトリガーしようとするとデバイスはDozeから離脱するためバッテリーなどリソースを大量に消費する。さらにシステムはこれらの要求をバッチ処理として扱うことができない。

アプリのユースケースで絶対に正確なアラームが必要かどうかを検討すること。長時間の作業やネットワークアクセスが必要なケースでは`WorkManager`を利用し、Doze状態の時に作業が必要なケースでは正確ではないアラームを利用する。

#### Exact alarms and inexact alarms

* exact alarm
  * `setAlarmClock()`
  * `setExact()`
  * `setExactAndAllowWhileIdle()`
* inexact alarm
  * `set()`
  * `setInexactRepeating()`
  * `setAndAllowWhileIdle()`
  * `setWindow()`

#### Acceptable use cases for this permission

以下のようなケースでは正確なアラームを利用すべき。

* 時計やタイマーアプリ
* ユーザーが正確なタイミングでアクションを予約することができるアプリ

Android 12では正確なアラームは重要で時間的制約のある中断とみなされるため、https://developer.android.com/about/versions/12/foreground-services には影響されない。

#### Ask users to grant the app access

パーミッションが許可済みかどうかは`canScheduleExactAlarms()`を利用する。必要であれば、`ACTION_REQUEST_SCHEDULE_EXACT_ALARM` を利用し端末の設定アプリへ移動することもできる。

#### Enable the behavior change

テスト目的で無効化するためには以下のいずれかの方法を利用する

* 開発者オプションで `REQUIRE_EXACT_ALARM_PERMISSION` をOFFにする
* `adb shell am compat disable REQUIRE_EXACT_ALARM_PERMISSION PACKAGE_NAME`

### Notification trampolines restrictions

ユーザがNotificationを操作すると、いくつかのアプリはNotificationのタップに反応して処理が行われ最終的にapp componentを起動するケースがある。
このapp componentが notification trampolineとして知られている。

パフォーマンスとUXの改善のため、`targetSdkVersion`がAndroid 12のアプリでは、ServiceやBroadcast Receiverから`startActivity()`を実行しても、以下のエラーメッセージが表示されるだけで、`Activity`の起動は抑止される。

```
Indirect notification activity start (trampoline) from PACKAGE_NAME, \
this should be avoided for performance reasons.
```

#### Identify which app components act as notification trampolines

アプリをテストする際、Notificationをタップ後、どの`Service`、もしくは、`BroadcastReceiver`がnotification trampolineとして機能したかを以下のadbコマンドで特定することができる。

```
adb shell dumpsys activity service \
  com.android.systemui/.dump.SystemUIAuxiliaryDumpService
```

`NotifInteractionLog`が含まれるセクションに表示される。

#### Update your app

notification trampolineを使っている場合は以下の手順で移行する。

1. 以下のActivityのいずれかに関連づけられた`PendingIntent`を生成する
  * (preferred)Notificationをタップした後にユーザが見えるActivity
  * trampoline Activity、もしくは、Notificationタップ後にActivityを起動するActivity
1. 前の手順で生成した`PendingIntent`を使ってNotificationを生成する

#### Toggle the behavior

Developer Preview中にテストするときは`NOTIFICATION_TRAMPOLINE_BLOCK`を使って有効、無効を切り替えることができる。

## Backup and restore

`targetSdkVersion`がAndroid 12以上のアプリではバックアップ、リストアの動作の仕方が変わる。詳細は https://developer.android.com/about/versions/12/backup-restore を参照。

## Connectivity

### Concurrent Peer-to-Peer + Internet Connection

Android 12からPeer-to-Peerとインターネットの同時接続をサポートするデバイスが同時接続を維持できるようになった。
この機能は`targetSdkVersion`がAndroid 12以上の全てのアプリで自動的に有効になり、それ以下のAPIレベルのアプリでは従来の動作のままでWi-FiネットワークがPeerデバイスに接続する前に切断される。

#### Compatibility

`WifiManager.getConnectionInfo()`が1つのネットワークのみの`WifiInfo`を返すようになり、以下のように変更されている。

* 1つのWi-Fiネットワークが有効な場合は`WifiInfo`が返却される
* 1つ以上のWi-Fiネットワークが有効でPeerデバイスとの接続がある場合はPeerデバイスの`WifiInfo`が返却される
* 1つ以上のWi-Fiネットワークが有効でPeerデバイスとの接続がない場合はプライマリーの`WifiInfo`が返却される

同時接続をサポートするデバイスでより良いUXを提供するために、`WifiManager.getConnectionInfo()`の代わりに`NetworkCallback.oonCapabilitiesChanged()`の利用が推奨。

#### Enable screen off for NFC payments

`targetSdkVersion`がAndroid 12以上のアプリでは`requireDeviceScreenOn=false`でスクリーンがオンにしなくてもNFCでの支払いが可能になる。
詳細は https://developer.android.com/guide/topics/connectivity/nfc/hce#ScreenOffBehavior

## Vendor libraries

### Vendor-supplied native shared libraries

`targetSdkVersion`がAndroid 12以降のアプリではシリコンベンダーやデバイスメーカーが提供する[Non-NDK native shared libraries](https://source.android.com/devices/tech/config/namespaces_libraries#adding-additional-native-libraries)以外はデフォルトではアクセスできない。これらのライブラリを利用するには`<uses-native-library>`を定義する必要がある。

`targetSdkVersion`がAndroid 11以下のアプリではデフォルトでアクセス可能。

## Updated non-SDK restrictions

* [Non-SDK interfaceを使っていないかのテスト方法](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces#test-for-non-sdk)
* [Updates to non-SDK interface restrictions in Android 12](https://developer.android.com/about/versions/12/non-sdk-12)
* [Restrictions on non-SDK interfaces](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces)
