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
        <td>{{ artifact.info.id }}</td>
        <td>{{ artifact.info.artifactTitle }}</td>
        <td>{{ artifact.info.artifactDescription }}</td>
        <td>{{ artifact.info.artifactType }}</td>
        <td>
          <input type="file" multiple @change="upload(artifact, $event)" />
        </td>
        <td>
          <button @click="navigateToVideo(artifact)">Play</button>
        </td>
        <td>
          <button @click="m3u8(artifact)">Play hls</button>
        </td>
        <td>
          <button @click="onDelete(artifact)">delete</button>
        </td>
        <td>
          <button @click="onDownload(artifact)">download</button>
        </td>
      </tr>
      </tbody>
    </table>
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="fetchVideoList"
    />
    <create-artifact-modal v-if="showCreateModal" @close="handleModalClose" />
  </div>
</template>

<script setup>
import request from '@/utils/request.js'
import { onMounted, ref } from 'vue'
import CreateArtifactModal from '@/components/CreateArtifactModal.vue'
import pagination from '@/components/Pagination/index.vue'
import { useRoute, useRouter } from 'vue-router'
import qs from 'qs'
import { createWriteStream } from 'streamsaver';

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  operIp: undefined,
  title: undefined,
  operName: undefined,
  businessType: undefined,
  status: undefined
});
const total = ref(0);
const artifacts = ref([])
const showCreateModal = ref(false)
const router = useRouter()

function fetchVideoList() {
  request({
    url: 'melon-video/artifacts/v1/list',
    headers: {
      isToken: true,
      repeatSubmit: false
    },
    method: 'get',
    params: {
      pageNum: queryParams.value.pageNum,
      pageSize: queryParams.value.pageSize,
      orderByColumn: "artifactTitle"
    }
  })
    .then((resp) => {
      artifacts.value = resp?.rows
      total.value = resp.total
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
    url: `melon-video/artifacts/v1/video`,
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

function m3u8(artifact) {
  router.push({ path: '/videohls', query: {"artifactId": artifact.info.id} })
}

function onDelete(artifact) {
  request({
    url: `melon/artifacts/v1/artifact`,
    method: 'delete',
    params: {
      ids: [artifact.id,],
    },
    paramsSerializer: function(params) {
      return qs.stringify(params,{arrayFormat: 'comma'})
    }
  })
    .then((resp) => {
      // Handle successful upload
      console.log('Video delete successfully')
      fetchVideoList()
    })
    .catch((err) => {
      console.log(err)
    })
}

function onDownload(artifact) {
  request({
    url: `melon/artifacts/v1/video`,
    method: 'get',
    params: {
      id: artifact.id,
    },
  })
    .then((resp) => {
      // Handle successful upload
      const address = resp?.data
      if (address && address.length) {
        const fileStream =  createWriteStream('output.mp4')
        const writer = fileStream.getWriter();
        fetch(address).then(response => {
          const reader = response.body.getReader();
          function push() {
            reader.read().then(({done, data}) => {
              if (done) {
                writer.close();
                return;
              }
              writer.write(data);
              push();
            })
          }
          push();
        })
      }


    })
    .catch((err) => {
      console.log(err)
    })
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