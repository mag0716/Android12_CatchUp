# Splash screens

https://developer.android.com/about/versions/12/features/splash-screen

Android 12では全てのアプリのための新しい起動アニメーションを有効にする`SplashScreen` APIが追加される。このAPIには起動時のアニメーション、スプラッシュ画面、アプリへの遷移が含まれる。

この新しい体験はすべてのアプリの起動時に標準的なデザイン要素をもたらすがカスタマイズも可能なのでアプリは独自のブランディングを維持することができる。

## How the splash screen works

ユーザがアプリを起動しアプリプロセスが起動していない(cold start)間やActivityがまだ作成されていない間(warm start)は以下のイベントが発生する。(スプラッシュ画面はhot startの間は表示されない)

1. システムは定義されたテーマとアニメーションを使いスプラッシュ画面を表示する
1. アプリの準備ができたらスプラッシュ画面は消え、アプリが起動される

## Elements and mechanics of the animation

アニメーションの要素はマニフェストファイル内にxmlリソースによって定義され、ライトモード、ダークモードそれぞれ存在する。

これらはウィンドウの背景、アニメーションするアイコン、アイコンの背景で構成されている。

* ウィンドウの背景は必須で、単一の不透明な色で構成される
* アプリアイコンはvector drawableであるべきで、静的、動的とも実現できる。アニメーションは最大1000msec
* アイコンの背景はオプションで、アイコンとウィンドウの背景との間にコントラストが必要な場合に便利

スプラッシュ画面のアニメーションの仕組みはenter, exitのアニメーションで構成されている。

* enterアニメーションはスプラッシュ画面が表示される際に利用され、システムによってコントロールされるのでカスタマイズできない
* exitアニメーションはスプラッシュ画面が非表示になるときに利用され、`SplashScreenView`とアニメーションアイコンにアクセスして、トランスフォーム、不透明度、色などを設定して任意のアニメーションが実行できる

## Customize the splash screen is your app

デフォルトでは`SplashScreen`は`windowBackground`とランチャーアイコンが利用される。アプリのテーマの属性を追加することでカスタマイズできる。

スプラッシュ画面をカスタマイズするには以下に従う

* テーマの属性を設定して見た目を変える
* 画面の表示を長くする
* スプラッシュ画面を非表示にするときのアニメーションをカスタマイズする

### Set a theme for the splash screen to change its appearance

Activityテーマに以下の属性を指定することでカスタマイズすることができる。`android:windowBackground`などの属性を使ったレガシーなスプラッシュ画面の実装がすでにある場合は、Android 12用の代替リソースの提供を検討する。

1. `windowSplashScreenBackground` に単一の色を指定する
1. `windowSplashScreenAnimatableIcon`にアイコンを指定する。アニメーションさせる場合は`AnimationDrawable`, `AnimatedVectorDrawable`を通じて実行する。
1. `windowSplashScreenAnimationDuration`にアニメーション時間を設定する。最大値は1000msec
1. `windowSplashScreenIconBackground`にアイコンの後ろの背景色を指定する。
1. `windowSplashScreenBrandingImage` にスプラッシュ画面の下部に表示される画像を設定する。デザインガイドラインではブランディング画像の使用を推奨している。

Note：推奨されるデザインガイドラインはブランディング画像は使用しない

### Keep the splash screen on-screen for longer periods

スプラッシュ画面はアプリの1フレーム目が描画されると非表示にされる。アプリ内のテーマ設定などの少量のデータをローカルディスクから非同期に読み込む必要がある場合は`ViewTreeObserver.OnPreDrawListener`を使用して、アプリが1フレーム目を描画するのを中断することができる。

### Customize the animation for dismissing the splash screen

`Activity.getSplashScreen` を通じてスプラッシュ画面のアニメーションをカスタマイズすることができる。

このコールバックの開始時はスプラッシュ画面嬢のアニエーションベクターが開始されているので、アプリの起動時間によってはアニメーションの途中である場合もある。`SplashScreenView.getIconAnimationStart`を使用するとアニメーションの開始時間を知ることができ、アニメーションの残り時間も計算することができる。

## 疑問点

* APIを通じてアプリの設定情報を取得する用途には使えない？
