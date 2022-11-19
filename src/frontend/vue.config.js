const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy:{
      '/api/v1':{
        target: 'http://localhost:8080',
        ws: true,
        changeOrigin: true
      }
    }
  }
})
