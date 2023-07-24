const path = require('path')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const {CleanWebpackPlugin} = require('clean-webpack-plugin')

module.exports = (env) => {
  return {
    mode: 'development',
    entry: {
      index: require.resolve('./src/index'),
    },
    devtool: 'inline-source-map',
    resolve: {
      extensions: ['.ts', '.js'],
    },
    module: {
      rules: [
        {
          test: /\.jsx?/,
          exclude: /node_module/,
          use: {
            loader: 'babel-loader',
            options: {
              presets: ['@babel/preset-env', '@babel/preset-react']
            },
          },
        },
        {
          test: /\.yaml$/,
          use: [
            {
              loader: 'json-loader'
            },
            {
              loader: 'yaml-loader',
              options: {
                asJSON: true
              },
            },
          ]
        },
        {
          test: /\.css$/,
          use: [
            {
              loader: 'style-loader'
            },
            {
              loader: 'css-loader'
            },
          ]
        }
      ]
    },
    plugins: [
      new CleanWebpackPlugin(),
      new HtmlWebpackPlugin({
        template: 'index.html',
        title: 'Swagger UI',
      }),
    ],
    output: {
      filename: '[name].[contenthash].js',
      clean: true,
      path: path.resolve(__dirname, 'dist'),
      publicPath: '/swagger-ui/',
    },
    optimization: {
      moduleIds: 'deterministic',
      runtimeChunk: 'single',
      splitChunks: {
        cacheGroups: {
          vendor: {
            test: /[\\/]node_modules[\\/]/,
            name: 'vendors',
            chunks: 'all',
          },
        },
      },
    },
  }
}
