# Rich content insertion

https://developer.android.com/about/versions/12/features/unified-content-api

新たに追加されるUnified APIでは画像や動画などのコンテンツをクリップボードやキーボード、ドラック&ドロップから簡単に受け取れるようになる。

`OnReceiveContentListener`を実装することでコンテンツを受け取った時にコールバックを受け取ることができる。

互換性維持のためにAndroidX(Core 1.5.0-beta1, Appcompat 1.3.0-beta-01)に追加されたAPIを使うことが推奨される。

## Overview

長押しで表示されるメニューやドラッグ&ドロップはそれぞれのAPIを持つので対応する場合にはそれぞれの実装が必要。
Unified APIは1つのAPIで実装できるようになる。
このアプローチではコンテンツの挿入などの手段が新たに追加された場合でもコードの変更なしでサポートすることができる。

## Implementation

新しいAPIは1つのメソッドをもつインタフェースである`OnReceiveContentListener`
古いAndroidプラットフォームに対応するために、AndroidX Coreライブラリ内にある`OnReceiveContentListener`の利用が推奨される。

APIを利用するためには、アプリでサポートする特定のコンテンツを明示したリスナを実装する。

## Comparison with the keyboard image API

Unified APIは[keyboard image API](https://developer.android.com/guide/topics/text/image-keyboard)の次バージョンのAPIとして考えることができ、keyboard image APIでできることはUnified APIでも可能。

### Supported features and API levels: Jetpack

keyboard image API：キーボードからの挿入のみ
unified API：全て対応可能、かつ、一部はAPI level 13以上であれば利用可能

### Supported features and API levels: native API

keyboard image API：キーボードからの挿入のみで、API level 25以上
unified API：全て対応可能だがAndroid 12以上のみ
