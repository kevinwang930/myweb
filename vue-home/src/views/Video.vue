<template>
  <div class="artifact-page">
    <video v-if="isVideo" :src="artifactSrc" controls></video>
    <img v-else :src="artifactSrc" alt="Artifact Image">
  </div>

</template>

<script setup>

import { useRoute } from 'vue-router'
import { onMounted, ref } from 'vue'
import request from '@/utils/request'
import Hls from 'hls.js'

const route = useRoute()
const artifactSrc = ref(null)
const isVideo = ref(true)

onMounted(() => {
  console.log(route.path)
  const artifactId = route.query?.id
  const artifactType = route.query?.artifactType
  isVideo.value = artifactType == 'VIDEO' ? true : false

  if (isVideo.value) {
    request({
      url: `melon/artifacts/video`,
      timeout: 300000,
      params: {
        artifactId: artifactId
      },
      method: 'get'
    })
      .then((resp) => {
        // Append the stream to the video element
        artifactSrc.value = resp
      })
      .catch((err) => {
        console.log(err)
      })
  } else {
    request({
      url: `melon/artifacts/images`,
      timeout: 300000,
      params: {
        id: artifactId
      },
      method: 'get'
    })
      .then((resp) => {
        // Append the stream to the video element
        artifactSrc.value = resp.data[0]
      })
      .catch((err) => {
        console.log(err)
      })
  }

})
//
// function fetchVideoData() {

// }
//
// function customLoader() {
//   this.load = function(context, config, callbacks) {
//     return request({
//       url: `/melon/artifacts/video`,
//       method: 'get',
//       params: {
//         artifactId: artifactId.value
//       }
//     })
//       .then(response)
//       .catch(callbacks.onError)
//   }
//
//   /** Abort any loading in progress. */
//   this.abort = function() {
//   }
//
//   /** Destroy loading context. */
//   this.destroy = function() {
//   }
// }
//
</script>

<style>
/* Add any styles for your video component here */
</style>