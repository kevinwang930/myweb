<template>
  <div>video loading</div>
  <video ref="videoElement"></video>
</template>

<script>
// Import HLS.js only if it's available as a module
import Hls from 'hls.js';

export default {
  name: 'HlsVideo',
  mounted() {
    this.initHls();
  },
  methods: {
    initHls() {
      const video = this.$refs.videoElement;
      const videoSrc = 'https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8';

      if (video.canPlayType('application/vnd.apple.mpegurl')) {
        video.src = videoSrc;
      } else if (Hls.isSupported()) {
        const hls = new Hls();
        hls.loadSource(videoSrc);
        hls.attachMedia(video);
      }
    }
  }
}
</script>

<style>
/* Add any styles for your video component here */
</style>