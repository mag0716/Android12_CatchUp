# Features and APIs Overview

https://developer.android.com/about/versions/12/features

* [API diff report](https://developer.android.com/sdk/api_diff/s-dp1/changes)

## User experience

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

### Audio-coupled haptic effect

Android 12のアプリでは端末のバイブレーションを使用してオーディオから触覚フィードバックを生成することができる。
これはゲームやオーディオ体験においてより没入するための機会を提供する。

詳細情報は[HapticGenerator](https://developer.android.com/reference/android/media/audiofx/HapticGenerator)のリファレンスを参照。

### Picture-in-picture behavior improvements

Android 12ではPicture-in-picture(PiP)モードのための動作が改善される。詳細は https://developer.android.com/about/versions/12/features/pip-improvements を参照。

## New experiences

### Rich content insertion

クリップボード、キーボードやドラッグアンドドロップでリッチなコンテンツを受け取れる unified API が追加される。
詳細は[Unified API for receiving content](https://developer.android.com/about/versions/12/features/unified-content-api)

## Graphics and images

### Easier blurs, color filters, and other effects

Android 12ではブラーやカラーフィルター、シェーダーエフェクトなどの共通的なグラフィックエフェクトを適用する`RenderEffect`が新たにう追加される。
Android端末によっては処理能力の関係でサポートされない場合がある。

`View.setRenderEffect(RenderEffect)`を呼び出すことによって、`View`の基礎となる`RenderNode`にエフェクトを適用することもできる。

### Native animated image decoding

Android 12では、NDKの[ImageDecoder](https://developer.android.com/ndk/reference/group/image-decoder)が拡張され、GIFやWebPの全てのフレームのデコードができるようになった。
Android 11では最初の画像のみデコード可能だった。

* [API reference](https://developer.android.com/ndk/reference/group/image-decoder)
* [sample on GitHub](https://github.com/android/ndk-samples/tree/develop/webp/image-decoder)

### AVIF image support

Android 12ではAVIFをサポートする。
AVIFの詳細については[Jake Archibaldのブログ](https://jakearchibald.com/2020/avif-has-landed/)が参考になる。

## Media

### Compatible media transcoding

Android 12では幅広いプレイヤーをサポートするAVC(H.264)にHEVC(H.265), HDR(HDR10 and HDR 10+)を自動に変換することが可能になる。
詳細は[Compatible media transcoding](Compatible media transcoding)

### MediaDrm updates

現在の`MediaDrm`にセキュアなデコーダーコンポーネントが必要か判断するには以下の手順を実行する必要がある。

1. `MediaDrm`の作成
1. セッションID獲得のためにセッションを開く
1. セッションIDを使って`MediaCrypto`の作成
1. `MediaCrypto.requiresSecureDecoderComponent(mimeType)`

新たに追加された`requiresSecureDecoder(@NonNull String mime)`と`requiredSecureDecoder(@NonNull String mime, @SecurityLevel int level)`を使えばすぐに`MediaDrm`を作成することができるようになる。

## Security

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

### STA+STA connectivity support

Android 12以降を対象としたデバイスが P2P接続を利用すると、接続を作成する際に既存のWi-Fi接続が切断されなくなる。
この機能がサポートされているかどうかは `WifiManager.isMultiStaConcurrencySupported()` を利用する。

メモ：STA = more concurrent stations
