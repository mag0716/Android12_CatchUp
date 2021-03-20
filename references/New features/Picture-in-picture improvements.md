# Picture in Picture (PiP) improvements

https://developer.android.com/about/versions/12/features/pip-improvements

## Behavior improvements for single- and double-tapping

Android 12ではPiPモードでのシングルタップ、ダブルタップの動作が以下のように改善される。

* シングルタップ：以前は拡大され、コントロールが表示されていたのを、コントロールだけ表示されるようになる
* ダブルタップ：フルスクリーンモードに戻っていたのを、PiPのウィンドウサイズを最大サイズとの切り替えになる

## New features

### New API flag for smoother transition to PiP mode in gesture navigation

Android 12では新たに`setAutoEnterEnabled`が追加され、ジェスチャーナビゲーションで上にスワイプしたときにスムーズにPiPモードに移行が可能になる。以前は、PiPウィンドウがフェードする前にホーム画面へのアニメーションで待たされていた。

実装は以下の手順

1. `PictureInPictureParams.Builder`に`setAutoEnterEnabled`を利用する
  * Note:`setAutoEnterEnabled`を有効化すると、明示的に`onUserLeaveHint`で`enterPictureInPictureMode`を呼び出さなくてよくなる
1. `setPictureInPictureParams`を呼び出す
1. 一時停止状態だったらPiPモードに入れないようにするなどあれば、`setAutoEnterEnabled(false)`を呼び出す

### New API flag to disable seamless resizing for non-video content

Android 12では`SeamlessResizeEnabled`が追加され、PiPウィンドウ内の動画でないコンテンツのリサイズ時にスムーズなクロスフェーディングが提供される。以前は、動画でないコンテンツのリサイズ時に違和感のあるアニメーションになることがあった。

`setSeamlessResizeEnabled`フラグは後方互換性のためにデフォルトが`true`となっている。動画のコンテンツには`true`とし、動画以外のコンテンツの場合は`false`とすること。

### Support for smoother animations when exiting out of PiP mode

Android 12では、`SourceRectHint`がPiPモードから終了時のスムーズなアニメーションの実装のために再利用される。終了時、システムはPiPに移行時に利用された`Rect`やアプリによって更新された`Rect`に関わらず現在利用可能な`SourceRectHint`を利用してアニメーションを作成する。

実装は以下の手順

1. `PictureInPictureParams`に`sourceRectHint`と`aspectRatio`を渡す
1. 必要であれば、`addOnLayoutChangeListener`でレイアウト変更を検知して、PiPモードの終了が始まる前に`sourceRectHint`を更新する。

### Support for new gestures

Android 12では、PiPウィンドウを隠したり、ピンチでのズームをサポートする

* ユーザーが画面端にドラッグするとPiPウィンドウを隠すことができる
* PiPウィンドウをピンチでリサイズすることができる
