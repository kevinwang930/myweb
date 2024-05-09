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
      <tr v-for="artifact in artifacts" :key="artifact.id">
        <td>{{ artifact.id }}</td>
        <td>{{ artifact.artifactTitle }}</td>
        <td>{{ artifact.artifactDescription }}</td>
        <td>{{ artifact.artifactType }}</td>
        <td>
          <input type="file" multiple @change="upload(artifact, $event)" />
        </td>
        <td>
          <button @click="navigateToVideo(artifact)">Play Video</button>
        </td>
      </tr>
      </tbody>
    </table>
    <create-artifact-modal v-if="showCreateModal" @close="handleModalClose" />
  </div>
</template>

<script setup>
import request from '@/utils/request.js'
import { onMounted, ref } from 'vue'
import CreateArtifactModal from '@/components/CreateArtifactModal.vue'
import { useRoute, useRouter } from 'vue-router'

const artifacts = ref([])
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
      artifacts.value = resp?.rows
    })
    .catch((err) => {
      console.log(err)
    })
}

function uploadImages(artifact, event) {
  const images = event.target.files


  const data = new FormData()
  for (let i = 0; i < images.length; i++) {
    data.append('images', images[i])
  }

  request({
    url: 'melon/artifacts/images',
    method: 'post',
    params: {
      id: artifact.id
    },
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

function upload(artifact,event) {
  if (artifact.artifactType === 'VIDEO') {
    uploadVideo(artifact,event)
  } else if (artifact.artifactType === 'IMAGE') {
    uploadImages(artifact,event)
  }
}

function uploadVideo(artifact, event) {
  const file = event.target.files[0]
  request({
    url: `melon/artifacts/video`,
    method: 'post',
    timeout: 300000,
    data: file,
    params: {
      id: artifact.id,
      videoName: file?.name,
      hls: true
    },
    headers: {
      'Content-Type': 'video/mp4',
      'Content-Length': file.length,
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

function navigateToVideo(artifact) {
  router.push({ path: '/video', query: artifact })
}

function handleModalClose() {
  showCreateModal.value = false
  fetchVideoList()
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