const path = require('path');
const fs = require('fs');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const RemoveEmptyScriptsPlugin = require('webpack-remove-empty-scripts');

// [定数] webpack の出力オプションを指定します
// 'production' か 'development' を指定
const MODE = "production";

// ソースマップの利用有無(productionのときはソースマップを利用しない)
const enabledSourceMap = MODE === "development";

// エントリーポイントを動的に取得
function getEntryPoints(directory, ext) {
  const dirPath = path.resolve(__dirname, directory);
  const files = fs.readdirSync(dirPath).filter(file => file.endsWith(`.${ext}`));
  const entryPoints = {};
  files.forEach(file => {
    const name = file.replace(`.${ext}`, '');
    entryPoints[name] = path.resolve(dirPath, file);
  });
  console.log(`Entry points for ${ext}:`, entryPoints);
  return entryPoints;
}

// JSとSCSSのエントリーポイントをマージ
const entryPoints = {
  ...getEntryPoints('src/main/resources/js', 'js'),
  ...getEntryPoints('src/main/resources/scss', 'scss')
};

console.log('Final entry points:', entryPoints);

module.exports = {
  entry: entryPoints,

  // ファイルの出力設定
  output: {
    filename: (chunkData) => {
      // JSとSCSSで異なるディレクトリに出力
      return chunkData.chunk.name.endsWith('.scss') ? 'css/[name].css' : 'js/[name].js';
    },
    path: path.resolve(__dirname, 'src/main/resources/webapp')
  },

  module: {
    rules: [
      {
        test: /\.scss$/,
        use: [
          MiniCssExtractPlugin.loader,
          'css-loader',
          // Autoprefixer を利用する
          'postcss-loader',
          {
            loader: 'sass-loader',
            options: {
              implementation: require('sass'),
            }
          }
        ]
      },
      {
        // 拡張子 .js の場合
        test: /\.js$/,
        use: [
          {
            // Babel を利用する
            loader: "babel-loader",
          },
        ],
      },
    ],
  },

  plugins: [
    // 不要なJSファイルを削除する
    new RemoveEmptyScriptsPlugin(),
    // CSSファイルを外だしにする
    new MiniCssExtractPlugin({
      // ファイル名を設定
      filename: 'css/[name].css'
    }),

  ],
  // ソースマップの生成
  devtool: enabledSourceMap ? 'source-map' : false,

  // モード値を production に設定すると最適化された状態で、
  // development に設定するとソースマップ有効でJSファイルが出力される
  mode: MODE,

  // ES5(IE11等)向けの指定
  target: ["web", "es5"],

  // ローカル開発用環境を立ち上げる
  // 実行時にブラウザが自動的に localhost を開く
  devServer: {
    static: "src/main/resources/webapp",
    open: true
  }

};