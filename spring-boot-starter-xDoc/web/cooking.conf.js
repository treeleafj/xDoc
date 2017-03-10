var cooking = require('cooking')
var prod = process.env.NODE_ENV == 'production';

cooking.set({
    entry: {
        'index': ['./src/index.js'],
        'common': ['vue', 'vue-resource', 'vue-router', 'babel-polyfill', 'element-ui', 'element-ui/lib/theme-default/index.css']
    },
    chunk: {
        'common': {name: 'common', filename: prod ? 'common.[chunkhash:7].js' : 'common.js'}
    },
    dist: prod ? '../target/classes/static/' : './dist',
    publicPath: '/',
    assetsPath: 'assets',
    extends: ['vue2'],
    template: './src/index.html',
    // dev
    devServer: {
        // 是否启用
        enable: true,
        publicPath: '/',
        port: 3000,
        stats: { colors: true },
        proxy: {
            '/api/*': {
                target: 'http://localhost:8080/xdoc'
            }
        },
        log: false,
        hot: true
    },
    // prod
    clean: true,
    hash: prod,
    extractCSS: true
})

module.exports = cooking.resolve()