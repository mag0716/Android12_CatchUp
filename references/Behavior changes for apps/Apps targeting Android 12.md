# Behavior changes: Apps targeting Android 12

https://developer.android.com/about/versions/12/behavior-changes-12

* Foreground service launch restrictions：一部の例外を除き、バックグラウンド状態のアプリからフォアグラウンドサービスの起動ができなくなる
* App components containing intent filters must declare exported attribute：`intent-filter`が定義されているコンポーネントは`android:exported`の指定が必須になる
* Unsafe launches of nested intents：Strict modeで安全でない方でネストされた`Intent`の使用が検出されるようになった

## Privacy

### Modern SameSite cookie behaviors in WebView

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

### Safer exporting of components

`targetSdkVersion`をAndroid 12にしたアプリでは、`intent-filter`を利用しているactivities, services, broadcast receiversに対しては`android:exported`の指定が必須になる。

Warning：`android:exported`を指定していない場合、Logcatに`INSTALL_FAILED_VERIFICATION_FAILURE`が出力され、アプリはAndroid 12の端末にはインストールすることができない。

`android:exported`の指定がない場合、Logcatに以下のエラーメッセージが出力される。

```
Targeting S+ (version 10000 and above) requires that an explicit value for \
android:exported be defined when intent filters are present
```

### Pending intents must declare mutability

`targetSdkVersion`をAndroid 12にしたアプリでは、`PendingIntent`の生成時にmutabilityフラグ(`PendingIntent.FLAG_MUTABLE`, `PendingIntent.FLAG_IMMUTABLE`)の指定が必須となる。

mutabilityフラグの指定がない場合は、`IllegalArgumentException`となる。

#### Create immutable pending intents whenever possible

大抵のケースではイミュータブルな`PendingIntent`で生成するべき。

ただ以下のようなケースではミュータブルな`PendingIntent`で生成する必要がある。

* `PendingIntent`内のクリップデータの変更を要求するdirect reply actionを持った`Notification`
* システムが`FLAG_ACTIVITY_MULTIPLE_TASK`や`FLAG_ACTIVITY_NEW_DOCUMENT`を適用する `Bubbles`

他のアプリが`PendingIntent`を呼び出す際に同じコンポーネントが起動するように、ミュータブルな`PendingIntent`を生成する場合は明示的Intentの利用が強く推奨される。

#### Test the pending intent mutability change

mutabilityフラグの定義忘れを検出するために、Android Studio上にLintのWarningが出力される。

Developer Preview中はテストのために`PENDING_INTENT_EXPLICIT_MUTABILITY_REQUIRED`を非活性にすることができる。

### Unsafe launches of nested intents

プラットフォームのセキュリティ改善のため、Android 12ではネストされた`Intent`(別の`Intent`がextraに渡される`Intent`)からの安全でない起動を警告するデバッグ機能が提供される。
以下の両方を実行すると、`StrictMode`違反が発生する。

* アプリがネストされた`Intent`から渡された`Intent`を取得する
* 取得した`Intent`を使って即座に`startActivity()`, `startService()`, `bindService()`を呼び出す

#### Configure your app to detect unsafe launches of nested intents

ネストされた`Intent`からの安全でない起動をチェックするために、`detectUnsafeIntentLaunch()`を呼び出す。
Note：`detectAll()`を呼び出せば、`detectUnsafeIntentLaunch()`も自動的に呼び出される。

#### Use intents more responsibly

アプリがネストされた`Intent`で起動したいケースでは以下のように対応する。

* 内部のコンポーネントへの遷移：`exported=true`としない。
* アプリ間の遷移：`PendingIntent`で代用する

詳細な情報は https://medium.com/androiddevelopers/android-nesting-intents-e472fafc1933 を参照。

## Performance

### Foreground service launch restrictions

`targetSdkVersion`がAndroid 12のアプリでは、いくつかの特別なケースを除き、バックグラウンド中のアプリからフォアグラウンドサービスを起動すると例外が発生するようになる。
バックグラウンドでの起動については`WorkManager`の利用を検討する。

詳細は https://developer.android.com/about/versions/12/foreground-services を参照。

### Notification trampolines cannot be created from services or broadcast receivers

ユーザがNotificationを操作すると、いくつかのアプリはNotificationのタップに反応して処理が行われ最終的にapp componentを起動するケースがある。
このapp componentが notification trampolineとして知られている。

パフォーマンスとUXの改善のため、`targetSdkVersion`がAndroid 12のアプリでは、ServiceやBroadcast Receiverから`startActivity()`を実行しても、以下のエラーメッセージが表示されるだけで、`Activity`の起動は抑止される。

```
Indirect notification activity start (trampoline) from PACKAGE_NAME, \
this should be avoided for performance reasons.
```

#### Update your app

notification trampolineを使っている場合は以下の手順で移行する。

1. 以下のActivityのいずれかに関連づけられた`PendingIntent`を生成する
  * (preferred)Notificationをタップした後にユーザが見えるActivity
  * trampoline Activity、もしくは、Notificationタップ後にActivityを起動するActivity
1. 前の手順で生成した`PendingIntent`を使ってNotificationを生成する

#### Toggle the behavior

Developer Preview中にテストするときは`NOTIFICATION_TRAMPOLINE_BLOCK`を使って有効、無効を切り替えることができる。

## Non-SDK interface restrictions

* [Non-SDK interfaceを使っていないかのテスト方法](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces#test-for-non-sdk)
* [Updates to non-SDK interface restrictions in Android 12](https://developer.android.com/about/versions/12/non-sdk-12)
* [Restrictions on non-SDK interfaces](https://developer.android.com/guide/app-compatibility/restrictions-non-sdk-interfaces)

## Custom notification changes

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
