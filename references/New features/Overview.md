# Features and APIs Overview

https://developer.android.com/about/versions/12/features

* [API diff report](https://developer.android.com/sdk/api_diff/s-dp1/changes)

## New experiences

### Unified API for receiving content

クリップボード、キーボードやドラッグアンドドロップでリッチなコンテンツを受け取れる unified API が追加される。
詳細は[Unified API for receiving content](https://developer.android.com/about/versions/12/features/unified-content-api)

## Media

### Compatible media transcoding

Android 12では幅広いプレイヤーをサポートするAVC(H.264)にHEVC(H.265), HDR(HDR10 and HDR 10+)を自動に変換することが可能になる。
詳細は[Compatible media transcoding](Compatible media transcoding)

### AVIF image support

Android 12ではAVIFをサポートする。
AVIFの詳細については[Jake Archibaldのブログ](https://jakearchibald.com/2020/avif-has-landed/)が参考になる。

### Generate haptic effects from audio

Android 12では端末のバイブレーターを利用してオーディオから触覚フィードバックを生成することが可能になる。
これはゲームやオーディオ体験においてより没入するための機会を提供する。

詳細情報は[HapticGenerator](https://developer.android.com/reference/android/media/audiofx/HapticGenerator)のリファレンスを参照。

### Native ImageDecoder support for animated GIF and WebP

Android 12では、NDKの[ImageDecoder](https://developer.android.com/ndk/reference/group/image-decoder)が拡張され、GIFやWebPの全てのフレームのデコードができるようになった。
Android 11では最初の画像のみデコード可能だった。

* [API reference](https://developer.android.com/ndk/reference/group/image-decoder)
* [sample on GitHub](https://github.com/android/ndk-samples/tree/develop/webp/image-decoder)

## Security

### Device properties verification available in non-DPC apps

Android 9では、Keymaster 4.0以上を使用するデバイスポリシー所有者(DPO)がデバイスの認証証明書に含まれるプロパティを検証できたが、Android 12からは`setDevicePropertiesAttestationIncluded()` を使って全てのアプリが以下のプロパティを検証できるようになる。

* `BRAND`
* `DEVICE`
* `MANUFACTURER`
* `MODEL`
* `PRODUCT`

## Connectivity

### Wi-Fi Aware (NAN) enhancements

Wi-Fi認識に関するいくつかの改善が追加される。

* Android 12以上の端末で、`onServiceLost()`でサービスの停止、もしくは、圏外への移動を検知できる
* NAN Data Pathsのセットアップが変更され、より効果的になる
* リソース不足により接続要求をフレームワークが拒否しないように、`WifiAwareManager.getAvailableAwareResources()`を利用することが可能になる
