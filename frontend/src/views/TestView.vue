<template>
  <div class="video_container">
    <video ref="videoElement" controls></video>
  </div>
</template>

<script>
import flvjs from 'flv.js'

export default {
  mounted () {
    if (flvjs.isSupported()) {
      let videoElement = this.$refs.videoElement
      let flvPlayer = flvjs.createPlayer({
        type: 'flv',
        url: 'http://39.106.13.47:8080/live/111.live.flv'
      })
      flvPlayer.attachMediaElement(videoElement)
      flvPlayer.load()
      flvPlayer.play()
    }
  }
}
</script>

<style scoped>
.video_container{
  max-width: 100%;
  margin: 0 auto;
  padding: 200px;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
}
</style>