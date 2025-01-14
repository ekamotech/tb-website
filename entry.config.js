const path = require('path');

module.exports = {
  'index': path.resolve(__dirname, 'src/main/resources/js/index.js'),
  'sub': path.resolve(__dirname, 'src/main/resources/js/sub.js'),
  'module': path.resolve(__dirname, 'src/main/resources/scss/module.scss'),
  'page/start': path.resolve(__dirname, 'src/main/resources/scss/page/start.scss'),
  'page/group': path.resolve(__dirname, 'src/main/resources/scss/page/group.scss'),
  'page/group-detail': path.resolve(__dirname, 'src/main/resources/scss/page/group-detail.scss'),
  'page/event': path.resolve(__dirname, 'src/main/resources/scss/page/event.scss'),
  'page/event-detail': path.resolve(__dirname, 'src/main/resources/scss/page/event-detail.scss'),
  'page/favorite': path.resolve(__dirname, 'src/main/resources/scss/page/favorite.scss'),
  'page/calendar': path.resolve(__dirname, 'src/main/resources/scss/page/calendar.scss'),
};