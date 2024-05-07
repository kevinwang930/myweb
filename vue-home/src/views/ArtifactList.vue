<template>
  <div class="video-list">
    <div class="actions">
      <button class="create-btn" @click="showCreateModal = true">Create</button>
    </div>
    <h2>artifact List</h2>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>description</th>
        <th>type</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="video in videos" :key="video.id">
        <td>{{ video.id }}</td>
        <td>{{ video.artifactTitle }}</td>
        <td>{{ video.artifactDescription }}</td>
        <td>{{ video.artifactType }}</td>
        <td>
          <input type="file" @change="uploadVideo(video.id, $event)" />
        </td>
        <td>
          <button @click="navigateToVideo(video.id)">Play Video</button>
        </td>
      </tr>
      </tbody>
    </table>
    <create-artifact-modal v-if="showCreateModal" @close="showCreateModal = false" />
  </div>
</template>

<script setup>
import request from '@/utils/request.js'
import { onMounted, ref } from 'vue'
import CreateArtifactModal from '@/components/CreateArtifactModal.vue'
import { useRoute, useRouter } from 'vue-router'

const videos = ref([])
const showCreateModal = ref(false)
const router = useRouter()


function fetchVideoList() {
  request({
    url: 'melon/artifacts/list',
    headers: {
      isToken: true,
      repeatSubmit: false
    },
    method: 'get'
  })
    .then((resp) => {
      videos.value = resp?.rows
    })
    .catch((err) => {
      console.log(err)
    })
}

function uploadVideo(artifactId, event) {
  const file = event.target.files[0]

  request({
    url: `melon/artifacts/video`,
    method: 'post',
    data: file,
    params: {
      artifactId: artifactId,
      videoName: file?.name
    },
    headers: {
      'Content-Type': 'application/octet-stream'
    }
  })
    .then((resp) => {
      // Handle successful upload
      console.log('Video uploaded successfully')
    })
    .catch((err) => {
      console.log(err)
    })
}

function navigateToVideo(artifactId) {
  router.push({ path: '/video', query: { artifactId: String(artifactId) } })
}

onMounted(fetchVideoList)
</script>

<style>
.video-list {
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}

img {
  max-width: 100px;
  height: auto;
}
</style>