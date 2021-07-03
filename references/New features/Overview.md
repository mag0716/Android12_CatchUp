# Features and APIs Overview

https://developer.android.com/about/versions/12/features

* [API diff report](https://developer.android.com/sdk/api_diff/s-dp1/changes)

## New experiences

### Widgets improvements

Android 12では既存のWidget APIが刷新されユーザーや開発者の体験を向上させている。

詳細は https://developer.android.com/about/versions/12/features/widgets

### Audio-coupled haptic effect

Android 12のアプリでは端末のバイブレーションを使用してオーディオから触覚フィードバックを生成することができる。
これはゲームやオーディオ体験においてより没入するための機会を提供する。

詳細情報は[HapticGenerator](https://developer.android.com/reference/android/media/audiofx/HapticGenerator)のリファレンスを参照。

### Splash screen API

Android 12ではすべてのアプリに新しいアプリ起動アニメーションが導入され、起動時点からのアプリ内動作、アプリアイコンを表示するスプラッシュ画面、アプリ本体への遷移が含まれる。詳細は https://developer.android.com/about/versions/12/features/splash-screen を参照。

### New phone call notifications allowing for ranking importance of incoming calls

Android 12では電話の着信のための`Notification.CallStyle`が追加される。このテンプレートを利用すると、ステータスバーに通話時間を示すチップが目立つように表示され、チップをタップすることで通話に戻ることができる。

ユーザーにとって、着信および通話は最も重要であるためこれらの通知は最上位に表示される。またこの順位により優先順位の高い通話を他のデバイスに転送できる可能性がある。

着信：`forIncomingCall()`
発信：`forOngoingCall()`
スクリーニング：`forScreeningCall()`

### Enriched image support for notifications

Android 12では`MessagingStyle()`, `BigPictureStyle()`の通知にアニメーション画像を提供することでユーザー体験を豊かにすることができる。また、ユーザーが Notification からメッセージを返信する際に画像付きメッセージを送信できるようになった。

### Rounded corner APIs

Android 12では、`RoundedCorner`と`WindowInsets.getRoundedCorner(int position)`が導入され、角丸の半径と中心点が提供されるようになり、これらを利用することで角が丸い画面でUI要素が切り捨てられるのを防ぐことができる。

角が丸くない画面でこれらのAPIを利用しても影響はない。

この機能を実装する場合、`WindowInsets.getRoundedCorner(int position)`を通じて`RoundedCorner`を取得する。アプリが画面全体を占有していない場合は、ウィンドウの境界を基準にして角丸の中心点が適用される。

### Picture in Picture (PiP) improvements

Android 12ではPicture-in-picture(PiP)モードのための新機能が導入される。詳細は https://developer.android.com/about/versions/12/features/pip-improvements を参照。

### Immersive mode improvements for gesture navigation

Android 12では、Immersive modeが簡素化されジェスチャーナビゲーションがより簡単になり、動画や読書などの体験との整合性が保たれるようになる。
ユーザが誤ってフルスクリーンを解除されないように保護することは引き続き可能だが、スワイプ操作1つで通常の画面へ戻れるようになる。

`BEHAVIOR_SHOW_BARS_BY_TOUCH`, `BEHAVIOR_SHOW_BARS_BY_SWIPE` は deprecated になり、`BEHAVIOR_DEFAULT` が加わる。

`BEHAVIOR_DEFAULT`：
  3ボタン：Android 11以前と見た目、動作は変わらない
  ジェスチャーナビゲーション：
    見た目：Android 11以前と同じ
    動作：ナビゲーションバーなどが非表示でも動作するようになる。Android 11ではスワイプ操作2回で通常モードに戻っていたが1回で可能になる

`BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE`：Android 12における変更はない
  `targetSdkVersion`がAndroid 11以前でAndroid 12の端末で動作：デフォルトでは`BEHAVIOR_SHOW_BARS_BY_SWIPE`となる
  `targetSdkVersion`がAndroid 12でAndroid 11以下の端末で動作：`BEHAVIOR_SHOW_BARS_BY_TOUCH`は`BEHAVIOR_SHOW_BARS_BY_SWIPE`として扱われる。`targetSdkVersion`を更新するタイミングで`BEHAVIOR_SHOW_BARS_BY_SWIPE`への変更が必要

### Rich content insertion

クリップボード、キーボードやドラッグアンドドロップでリッチなコンテンツを受け取れる unified API が追加される。
詳細は[Unified API for receiving content](https://developer.android.com/about/versions/12/features/unified-content-api)

## Camera

### Quad bayer camera sensor support

現在、多くのAndroid端末には、Quad/Nona Bayerパターンに代表される高解像度のカメラセンサーが搭載されており、画質や低照度性能の面で非常に高い柔軟性をそなている。Android 12ではサードパーティアプリがこれらのセンサーを最大限に活用できるようにAPIが追加された。このAPIはセンサーのユニークな動作をサポートしフル解像度または最大解像度モードとデフォルトモードで動作する際に異なるストリーム構成や組み合わせをサポートする可能性があることを考慮している。

## Graphics and images

### Provide apps direct access to tombstone traces

Android 12から`ApplicationExitInfo.getTraceInputStream()`を通じてプロトコルバッファーであるtombstoneにアクセスが可能になる。以前はadbを通じてしかアクセスできなかった。

### AVIF image support

Android 12ではAVIFをサポートする。
AVIFの詳細については[Jake Archibaldのブログ](https://jakearchibald.com/2020/avif-has-landed/)が参考になる。

### Easier blurs, color filters, and other effects

Android 12ではブラーやカラーフィルター、シェーダーエフェクトなどの共通的なグラフィックエフェクトを適用する`RenderEffect`が新たに追加される。
Android端末によっては処理能力の関係でサポートされない場合がある。

`View.setRenderEffect(RenderEffect)`を呼び出すことによって、`View`の基礎となる`RenderNode`にエフェクトを適用することもできる。

### Native animated image decoding

Android 12では、NDKの[ImageDecoder](https://developer.android.com/ndk/reference/group/image-decoder)が拡張され、GIFやWebPの全てのフレームのデコードができるようになった。
Android 11では最初の画像のみデコード可能だった。

* [API reference](https://developer.android.com/ndk/reference/group/image-decoder)
* [sample on GitHub](https://github.com/android/ndk-samples/tree/develop/webp/image-decoder)

## Media

### Compatible media transcoding

Android 12では幅広いプレイヤーをサポートするAVC(H.264)にHEVC(H.265), HDR(HDR10 and HDR 10+)を自動に変換することが可能になる。
詳細は[Compatible media transcoding](Compatible media transcoding)

### Performance class

Android 12から`Performance`クラスが追加された。
`Performance`クラスはAndroidデバイス毎に定義されたハードウェア性能を特定する。
開発者はデバイスのパフォーマンスを実行時にチェックすることでデバイス性能をフルに活用した体験を提供することができる。

詳細は https://developer.android.com/about/versions/12/features/performance-class

### Video encoding improvements

Android 12ではビデオエンコーディングの量子化パラメータの値を制御するための標準的なキーセットが定義される。

このキーはMediaFormat APIやNDK Mediaライブラリで利用できる。

また、ビデオエンコーダーに品質の最低基準値が設定されたので、極端な低品質にならないことが保証される。

### Audio focus

Android 12から他のアプリがオーディオフォーカスを持っている間にオーディオフォーカスをリクエストするとフレームワークが再生中のアプリをフェードアウトする。

詳細は https://developer.android.com/about/versions/12/features/audio-focus-improvements

### MediaDrm updates

現在の`MediaDrm`にセキュアなデコーダーコンポーネントが必要か判断するには以下の手順を実行する必要がある。

1. `MediaDrm`の作成
1. セッションID獲得のためにセッションを開く
1. セッションIDを使って`MediaCrypto`の作成
1. `MediaCrypto.requiresSecureDecoderComponent(mimeType)`

新たに追加された`requiresSecureDecoder(@NonNull String mime)`と`requiredSecureDecoder(@NonNull String mime, @SecurityLevel int level)`を使えばすぐに`MediaDrm`を作成することができるようになる。

## Security and privacy

### Bluetooth permissions

Android 12では`BLUETOOTH_SCAN`, `BLUETOOTH_ADVERTISE`, `BLUETOOTH_CONNECT`パーミッションが追加される。
これらのパーミッションは位置情報のアクセスが不要なためBluetoothデバイスとのアクセスがより簡単になる。

Note: `CompanionDeviceManager`はデバイスへの接続により合理化されたメソッドを提供する。
このシステムはアプリに変わってペアリングのためのUIを提供する。
ペアリングと接続の体験をよりコントロールしたい場合は新しいパーミッションを利用する。

詳細は https://developer.android.com/about/versions/12/features/bluetooth-permissions

### Privacy Dashboard

Android 12の端末ではシステム設定にPrivacy Dashboardが追加された。
この画面ではユーザは位置情報、カメラ、マイクにアクセスしたタイミングを見ることができる。
特定のデータにアクセスしたアプリがタイムラインとして表示される。

アプリは位置情報、カメラ、マイクにアクセスする理由をユーザに提示することができ、この理由はPrivacy Dashboardにも表示できる。

#### Show rationale for data access

位置情報、カメラ、マイクへアクセスする理由を説明するために以下のステップに従う必要がある。

* ユーザ説明用のActivityを追加する
* Intent Filterを追加する
* ユーザ説明用のActivityに何を表示すべきかを決定する
  * `ACTION_VIEW_PERMISSION_USAGE`を実行するとアプリは`EXTRA_PEMRMISSION_GROUP_NAME`の値を取得することができる
  * `ACTION_VIEW_PERMISSION_USAGE_FOR_PERIOD`を実行するとアプリは`EXTRA_PERMISSION_GROUP_NAME`, `EXTRA_ATTRIBUTION_TAGS`, `EXTRA_START_TIME`, `EXTRA_END_TIME`の値を取得できる

追加したintent filterに従ってユーザが情報にアクセスするためのアイコンが表示される。

* `VIEW_PERMISSION_USAGE`：システム設定のアプリのパーミッション画面にアイコンが追加される
* `VIEW_PERMISSION_USAGE_FOR_PERIOD`：Privacy Dashboardのアプリ名の横にアイコンが追加される

### Hide application overlay windows

Android 12では、`SYSTEM_ALERET_WINDOW`パーミッションが付与されたアプリのオーバーレイウィンドウを非表示にする機能が導入される。

`HIDE_OVERLAY_WINDOWS`パーーミッションを定義後、`setHideOverlayWindows()`を呼ぶことで、`TYPE_APPLICATION_OVERLAY`の全てのウィンドウを非表示にできる。アプリは取引確認フローなどセンシティブな画面を表示する際にこの方法を選択することができる。

`TYPE_APPLICATION_OVERLAY`のウィンドウを表示するアプリはpicture-in-picutreやbubblesなどもっと適切なユースケースがないかを考慮すべき。

### Known signers permission protection flag

Android 12では、Signature permissionsに`knownCerts`属性が導入され、宣言時に既知の署名情報のdigestsを参照することができる。

アプリはこの属性を宣言し、`protectionLevel`に新たに導入された`knownSigner`フラグを使用することができる。これを行うことで現在の署名者を含む要求元のアプリの署名者が`knownCerts`属性でパーミッションと共に宣言されたdigestsのいずれかに一致した場合にシステムが要求元アプリにパーミッションを付与する。

`knownSigner`フラグにより、デバイスやアプリはデバイスの製造・出荷時にパーミッションを許可することなく他のアプリに許可を与えることができる。

### Device properties attestation

Android 12では、認証証明書に記載されているデバイスのプロパティを検証できるアプリが拡張される。

Android 9では、Keymaster 4.0以上を使用するデバイスポリシー所有者(DPO)がデバイスの認証証明書に含まれるプロパティを検証できたが、Android 12からは`setDevicePropertiesAttestationIncluded()` を使って全てのアプリが以下のプロパティを検証できるようになる。

* `BRAND`
* `DEVICE`
* `MANUFACTURER`
* `MODEL`
* `PRODUCT`

### Secure lockscreen notification actions

Android 12では`Notification.Action.Builder`に`setAuthenticationRequired`が追加された。

このフラグが指定されたNotificationアクションをロック画面で呼び出すと常に認証がリクエストされる。これまではユーザーがNotificationアクションを起動して`Activity`を起動した場合や、direct replyの場合にのみ認証が要求されていた。

## Connectivity

### Bandwidth estimation improvements

Android 12では、`getLinkDownstreamBnadwidthKbps()`と`getLinkUpstreamBandwidthKbps()`で提供される帯域幅の見積もりが改善される。

### Keeping companion apps awake

Android 12では、デバイスを管理するためにコンパニオンアプリを起動したままにするためにAPIが導入され以下のようなことができるようになる。

* コンパニオンデバイスが範囲内にあるときにアプリを起動できるようにする
* デバイスが範囲内に止まっている間プロセスが継続して実行されることを保証する

このAPIを使う場合は `CompanionDeviceManager` を利用して接続している必要がある。
詳細は `CompanionDeviceManager.startObservingDevicePresence()`、`CompanionDevicService.onDeviceAppeared()` を参照。

### Companion Device Manager profiles

Android 12以降を対象としたアプリは腕時計に接続する際にCompanion Devices Profilesを使用できるようになり、デバイスタイプに応じたパーミッションの付与のステップを1つにまとめ登録プロセスをシンプルにすることができる。

付与された権限はデバイスが関連づけられている間だけ有効で、アプリを削除したり、関連づけを解除すると権限の許可も解除される。

詳細は `AssociationRequest.Builder.setDeviceProfile()` を参照。

### Wi-Fi Aware (NAN) enhancements

Wi-Fi認識に関するいくつかの改善が追加される。

* Android 12以上の端末で、`onServiceLost()`でサービスの停止、もしくは、圏外への移動を検知できる
* NAN Data Pathsのセットアップが変更され、より効果的になる
* リソース不足により接続要求をフレームワークが拒否しないように、`WifiAwareManager.getAvailableAwareResources()`を利用することが可能になる

### Concurrent Peer-to-Peer + Internet Connection

Android 12以降を対象としたデバイスが P2P接続を利用すると、接続を作成する際に既存のWi-Fi接続が切断されなくなる。
この機能がサポートされているかどうかは `WifiManager.isMultiStaConcurrencySupported()` を利用する。

メモ：STA = more concurrent stations

## Storage

### New directory for voice recordings

システムが認識した音声ファイルを保存するために`Environment.DIRECTORY_RECORDINGS`フォルダーが追加された。
アプリがこれらのファイルにアクセスするためには、`IS_RECORDING`フラグを使って探す必要がある。

### Media management access

ユーザはメディアファイルを編集するような特定のメディア管理アプリを信頼しているかもしれない。
`targetSdkVersion`がAndroid 11でデフォルトのギャラリーアプリがないデバイスでは、ファイルの修正、削除のたびに確認ダイアログを表示する必要があった。

`targetSdkVersion`がAndroid 12ではユーザに確認ダイアログの表示なしに以下の操作を可能とする権限を与えることができる。

* `createWriteRequest()`でのファイルの修正
* `createTrashRequest()`でのファイルのゴミ箱からの移動
* `createDeleteRequest()`でのファイルの削除

これをするためには以下のステップに従う必要がある。

* `MANAGE_MEDIA`パーミッションと`READ_EXTERNAL_STORAGE`パーミッションをマニフェストファイルに定義
  * 確認ダイアログなしに操作するために`ACCESS_MEDIA_LOCATION`も同様に定義
* アプリ内でパーミッションが必要なのかを説明するUIを用意する
* `ACCESS_REQUEST_MANAGE_MEDIA`を実行し、システム設定のMedia management apps画面へ遷移させる

### App storage access

アプリはユーザのデバイス上でアプリが保存しているデータを管理するActivityの定義、作成ができる。
アプリは`android:manageSpaceActivity`を利用することでカスタムした`manage space`Activityを定義する。
File ManagerアプリはこのActivityを`android:exported=false`担っていても呼び出すことができる。

Android 12では`MANAGE_EXTERNAL_STORAGE`, `QUERY_ALL_PACKAGES`パッケージ両方を持ったアプリは`getManageSpaceActivityIntent()`を利用することができ、他のアプリが定義した`manage space`Activityを送信することができる。

`getManageSpaceActivityIntent()`はパッケージ名とリクエストコードを渡すことで以下のいずれかを受け取る。

* `PendingIntent`：`manage space`Activityが定義されたアプリでは該当画面へ遷移するための`PendingIntent`が返される
* `null`：`manage space`Activityが定義されてない場合

### Extended file access support

`getMediaUri()`メソッドが既存の`ExternalStorageProvider`に加えて`MediaDocumentsProvider`もサポートするようになった。
システムはこれらを返す前に呼び出し側にこれらのURIsに対して許可をあたえる。

加えて`createWriteRequest()`によって許可されたURIsは`File`をサポートする。
これらのAPIはファイルの読み込み、書き込み、リネーム、削除機能を提供する。

## Core functionality

### Automatic app updates

Android 12では`PackageInstaller`APIを利用するアプリのために`setRequireUserAction()`メソッドが追加された。
このメソッドはユーザの確認なしにインストーラーアプリにアプリ更新を実行を可能にする。

### Device chipset information

Android 12では`Build.SOC_MANUFACTURER`, `Build.SOC_MODEL` が追加された。
