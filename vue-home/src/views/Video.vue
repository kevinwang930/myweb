<template>
  <div class="video-page">
    <video ref="videoPlayer" controls>
    </video>
  </div>
</template>

<script setup>

import { useRoute } from 'vue-router'
import request from '@/utils/request.js'
import { onMounted, ref } from 'vue'

const route = useRoute()
const videoPlayer = ref(null)
const artifactId = ref(null)
import Hls from 'hls.js'
import { videojs } from 'video.js';

onMounted(() => {
  console.log(route.path)
  artifactId.value = route.query?.artifactId
  // if (artifactId?.value) {
  //   initHls()
  // }

})

function fetchVideoData() {
  request({
    url: `melon/artifacts/video`,
    params: {
      artifactId: artifactId.value
    },
    method: 'get',
    responseType: 'stream' // Set the response type to 'stream'
  })
    .then((resp) => {
      // Append the stream to the video element
      resp.data.pipe(videoPlayer.value)
    })
    .catch((err) => {
      console.log(err)
    })
}

function customLoader() {
  this.load = function(context, config, callbacks) {
    return request({
      url: `/melon/artifacts/video`,
      method: 'get',
      params: {
        artifactId: artifactId.value
      }
    })
      .then(response)
      .catch(callbacks.onError)
  }

  /** Abort any loading in progress. */
  this.abort = function() {
  }

  /** Destroy loading context. */
  this.destroy = function() {
  }
}

function initHls() {
  const video = videoPlayer.value
  if (Hls.isSupported()) {
    const hls = new Hls({
      loader: customLoader
    })

    hls.loadSource(`http://localhost:8080/melon/artifacts/video?artifactId=${artifactId.value}`)
    hls.attachMedia(video)
    video.play()

  } else if (video.canPlayType('application/vnd.apple.mpegurl')) {
    video.src = videoUrl
    video.addEventListener('canplaythrough', () => {
      video.play()
    })
  }

}
</script>

<style>
/* Add any styles for your video component here */
</style>