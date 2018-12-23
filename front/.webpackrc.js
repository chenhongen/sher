const proxyTarget = 'http://localhost:8081/sher';//

module.exports = {
  devServer: {
    proxy: {
      '/**': {
        target: proxyTarget,
        changeOrigin: true,
        bypass: function(req, res, proxyOpt) {
          // 添加 HTTP Header 标识 proxy 开启
          res.set('X-ICE-PROXY', 'on');
          res.set('X-ICE-PROXY-BY', proxyTarget);
        },
      },
    },
  },
  // 其他配置
  output: {
    publicPath: '/sher/'
  }
};
