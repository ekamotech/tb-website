const path = require('path');
const fs = require('fs');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const RemoveEmptyScriptsPlugin = require('webpack-remove-empty-scripts');
const entryPoints = require('./entry.config.js');

// [定数] webpack の出力オプションを指定します
// 'production' か 'development' を指定
const MODE = "production";

// ソースマップの利用有無(productionのときはソースマップを利用しない)
const enabledSourceMap = MODE === "development";

module.exports = {
  entry: entryPoints,

  // ファイルの出力設定
  output: {
    filename: (pathData) => {
      const name = pathData.chunk.name;
      if (name.endsWith('.scss')) {
        return `css/${name.replace(/\.scss$/, '.css')}`;
      } else {
        return `js/${name}.js`;
      }
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
      {
        test: /\.(png|jpg|gif|svg)$/,
        // ファイルとして扱う
        type: 'asset/resource',
        generator: {
          // 出力先ディレクトリとファイル名の指定
          filename: 'images/[name][ext]'
        }
      },
      {
        test: /\.woff2$/,
        type: 'asset/resource',
        generator: {
          filename: 'fonts/[name][ext]'
        }
      },
    ],
  },

  plugins: [
    // 不要なJSファイルを削除する
    new RemoveEmptyScriptsPlugin(),
    new MiniCssExtractPlugin({
      filename: (pathData) => {
        const name = pathData.chunk.name;
        return `css/${name}.css`;
      }
    })

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
    open: true,
    hot: true,
  },

  // 画像フォルダへのエイリアスを指定
  resolve: {
    alias: {
      '@images': path.resolve(__dirname, 'src/main/resources/webapp/images'),
      '@fonts': path.resolve(__dirname, 'src/main/resources/webapp/fonts')
    }
  }

};