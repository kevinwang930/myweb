<template>
  <div class="artifact-page">
    <video ref="videoElement" controls autoplay style="width: 100%; max-width: 640px;">
      Your browser does not support video playback.
    </video>
  </div>

</template>

<script setup>

import { useRoute } from 'vue-router'
import { onMounted, ref } from 'vue'
import Hls from 'hls.js'
import { getToken } from '@/utils/auth'

const route = useRoute()

const videoElement = ref(null);

onMounted(() => {

  initHls()

})


function initHls() {
  const artifactId = route.query?.artifactId
  const video = videoElement.value
  if (Hls.isSupported()) {
    const hls = new Hls()
    hls.loadSource(`http://localhost:8080/melon-video/artifacts/v1/m3u8?id=${artifactId}`)
    hls.attachMedia(video)
    video.play()
  }
}
</script>

<style>
/* Add any styles for your video component here */
</style>