# Splash screens

https://developer.android.com/about/versions/12/features/splash-screen

Android 12で全てのアプリの起動アニメーションを有効にする`SplashScreen`APIが追加された。

この体験はアプリ起動毎に標準のデザイン要素を提供するが、ブランディングを維持するためのカスタマイズもできるようになっている。

## How the splash screen works

アプリのプロセスが実行されていない状態(cold start)や`Activity`がまだ生成状態(warm start)にユーザがアプリを起動すると以下のイベントが発生する。(hot startではスプラッシュ画面は表示されない)

1. システムは定義されたテーマとアニメーションを利用しスプラッシュ画面を表示する
1. アプリの準備ができたら、スプラッシュ画面を閉じアプリが表示される

## Elements and mechanics of the animation

アニメーション要素はマニフェストファイル内にXMLリソースファイルとして定義し、light/darkモードのいずれもそんざいする。

これらはwindow background, アニメーションするアプリアイコン、アイコンの背景を統一する。

以下の点に注意する必要がある。

* アプリアイコン：vector drawableで静的でも動的にもできる。アニメーション時間は無制限でもできるが、1000msecを超えないことが推奨。デフォルトではランチャーアイコンが使用される
* アイコンの背景：オプション。アイコンとwindow backgroundにコントラストが必要な場合に有益。adaptive iconを利用している場合は、window backgroundとに十分なコントラストがある場合に表示される
* adaptive icon：foregroundの1/3はマスクされる
* window background：不透明な1色を指定。window backgroundが設定されていて、無地である場合は設定されてない場合にデフォルトで使用される

スプラッシュ画面のアニメーションはenterとexitアニメーションで構成される

* enter：システムビューからスプラッシュ画面までで構成されている。システムによって制御されているのでカスタマイズ不可
* exit：スプラッシュ画面を隠すためのアニメーションで構成されている。カスタマイズする場合は`SplashScreenView`とそのアイコンにアクセスする。カスタマイズした場合はアニメーションが終了したら手動でスプラッシュ画面を削除する必要がある

## Customize the splash screen in your app

デフォルトでは`SplashScreen`は`windowBackground`とランチャーアイコンが利用される。スプラッシュ画面のカスタマイズはテーマにattributeの追加によって完了する。

カスタマイズは以下が可能

* テーマにattributesを設定によって見た目を変更
* スプラッシュ画面の表示時間を長くする
* スプラッシュ画面を消すためのアニメーション

### Set a theme for the splash screen to change its appearance

Activityのテーマに以下のattributeを指定することでカスタマイズ可能。
`android:windowBackground`などを利用してすでにスプラッシュ画面を実装している場合はAndroid 12用に大体リソースの提供を検討する。

1. `windowSplashScreenBackground`：スプラッシュ画面の背景
1. `windowSplashScreenAnimatedIcon`：アニメーションするアイコン
1. `windowSplashScreenAnimationDuration`：スプラッシュ画面の表示時間。最大1000msec
1. `windowSplashScreenIconBackground`：アイコンの後ろの背景色
1. `windowSplashScreenBrandingImage`：スプラッシュ画面下部のブランド画像

Note：デザインガイドラインではブランデイング画像の利用は推奨されていない

### Keep the splash screen on-screen for longer periods

スプラッシュ画面は1フレーム描画されてすぐに非表示になる。
ローカルディスクなどからアプリのテーマ設定などの小さなデータを読み込む必要がある場合は`ViewTreeObserver.OnPreDrawListener`を利用することで非表示になるのを待機させることができる。

### Customize the animation for dismissing the splash screen

`Activity.getSplashScreen`を通してスプラッシュ画面のアニメーションをカスタマイズできる。

コールバックの最初によって、スプラッシュ画面嬢のanimated vector drawableは開始されている。
アプリ起動の時間に依存してアニメーションが途中である場合がある。
アニメーションがいつ始まったかは`SplashScreenView.getIconAnimationStartMillis`を利用することで求めることができる。
