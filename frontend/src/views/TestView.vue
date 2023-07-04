<template>
  <div class="container">
    <h1>请保持整个身体在屏幕中</h1>
    <div id="video-container">
      <img id="video-feed" :src="videoFeedURL" style="height: 200px; width: 300px;">
    </div>
  </div>

  <div id="sport"></div>

  <div id="goal"></div>

  <div id="buttons">
    <button id="start-button" @click="openRequest">开始</button>
    <button id="stop-button" @click="stopRequest">停止</button>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      videoFeedURL: 'http://127.0.0.1:5000/video_feed', // 图像流的URL
    };
  },
  methods: {
    openRequest() {
      const requestData = { command: 'open capture' };

      axios.post('http://127.0.0.1:5000/your-backend-endpoint', requestData)
        .then(response => {
          console.log('请求成功！发送的JSON数据：', requestData);
          console.log('服务器响应：', response.data);
          this.updateVideoFeedURL(); // 请求成功后更新图像流的URL
        })
        .catch(error => {
          console.error('请求失败！');
          console.error(error);
        });
    },
    stopRequest() {
      const requestData = { command: 'stop capture' };

      axios.post('http://127.0.0.1:5000/your-backend-endpoint', requestData)
        .then(response => {
          console.log('请求成功！发送的JSON数据：', requestData);
          console.log('服务器响应：', response.data);
          this.updateVideoFeedURL(); // 请求成功后更新图像流的URL
        })
        .catch(error => {
          console.error('请求失败！');
          console.error(error);
        });
    },
    updateVideoFeedURL() {
      // 为了实时更新图像流，每次更新URL时添加一个随机查询参数
      this.videoFeedURL = `http://127.0.0.1:5000/video_feed?t=${Date.now()}`;
    },
  },
};
</script>
