module.exports = {
  presets: [
    // プリセットを指定することで、新しいESをES5に変換
    "@babel/preset-env",
    // React の JSX を解釈
    "@babel/react",

  ],
  sourceType: "unambiguous"
}