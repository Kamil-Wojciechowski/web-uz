const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 3000,
    proxy:{
      '^/api':{
        target: 'http://localhost:8080/api/v1',
        ws: true,
        changeOrigin: true
      }
    }
  }
})
