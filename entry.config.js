const path = require('path');

module.exports = {
  'index': path.resolve(__dirname, 'src/main/resources/js/index.js'),
  'sub': path.resolve(__dirname, 'src/main/resources/js/sub.js'),
  'module': path.resolve(__dirname, 'src/main/resources/scss/module.scss'),
  'page/group-detail': path.resolve(__dirname, 'src/main/resources/scss/page/group-detail.scss'),
  'page/event-detail': path.resolve(__dirname, 'src/main/resources/scss/page/event-detail.scss'),
  'page/mypage': path.resolve(__dirname, 'src/main/resources/scss/page/mypage.scss'),
};