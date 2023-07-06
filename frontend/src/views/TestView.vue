<template>
  <div>
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