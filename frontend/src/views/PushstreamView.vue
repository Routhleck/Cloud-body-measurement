<template>
  <div>
    <video ref="videoElement" autoplay></video>
    <button @click="startStreaming">Start Streaming</button>
  </div>
</template>

<script>
import flvjs from "flv.js";

export default {
  mounted() {
    this.setupPlayer();
  },
  methods: {
    setupPlayer() {
      const videoElement = this.$refs.videoElement;
      if (flvjs.isSupported()) {
        const flvPlayer = flvjs.createPlayer({
          type: "flv",
          url: "YOUR_RTMP_SERVER_URL",
        });
        flvPlayer.attachMediaElement(videoElement);
        flvPlayer.load();
      } else {
        console.error("FLV.js is not supported");
      }
    },
    startStreaming() {
      const videoElement = this.$refs.videoElement;
      const mediaStream = videoElement.captureStream();
      const mediaRecorder = new MediaRecorder(mediaStream, {
        mimeType: "video/webm",
      });

      const chunks = [];

      mediaRecorder.ondataavailable = (event) => {
        if (event.data && event.data.size > 0) {
          chunks.push(event.data);
        }
      };

      mediaRecorder.onstop = () => {
        const blob = new Blob(chunks, { type: "video/webm" });
        this.uploadStreamData(blob);
      };

      mediaRecorder.start();
    },
    async uploadStreamData(data) {
      try {
        const formData = new FormData();
        formData.append("file", data);

        const response = await fetch("YOUR_UPLOAD_API_URL", {
          method: "POST",
          body: formData,
        });

        console.log("Stream data uploaded:", response.data);
      } catch (error) {
        console.error("Error uploading stream data:", error);
      }
    },
  },
};
</script>
