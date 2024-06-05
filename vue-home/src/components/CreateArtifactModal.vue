<template>
  <div class="modal">
    <div class="modal-content">
      <span class="close-button" @click="$emit('close')">&times;</span>
      <h2>Create Artifact</h2>
      <form @submit.prevent="createArtifact">
        <div class="form-group">
          <label for="title">Title:</label>
          <input type="text" id="title" v-model="title" required>
        </div>
        <div class="form-group">
          <label for="description">description:</label>
          <input type="text" id="description" v-model="description" required>
        </div>
        <div class="form-group">
          <label for="type">type:</label>
          <input type="text" id="type" v-model="type" required>
        </div>
        <input  type="file" @change="onFileChange($event)" />
        <button type="submit">Create</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref,defineEmits } from 'vue'
import request from '@/utils/request.js'
const title = ref('')
const description = ref('')
const type = ref('')
const cover = ref(null)
const emit = defineEmits(['close'])

const createArtifact = () => {
  const formData = new FormData()
  formData.append('artifactTitle', title.value)
  formData.append('artifactDescription', description.value)
  formData.append('artifactType', type.value)
  if (cover.value) formData.append('cover', cover.value)


  request({
    url: 'melon-video/artifacts/v1/artifact',
    method: 'post',
    data: formData,
    headers: {'Content-Type': 'multipart/form-data'}
  })
    .then((resp) => {
      // Handle successful creation
      emit('close')
    })
    .catch((err) => {
      console.log(err)
    })
}

function onFileChange(event) {
  cover.value = event.target.files[0]
}
</script>

<style>
.modal {
  display: block;
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 30%;
}

.close-button {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close-button:hover,
.close-button:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}

.form-group {
  margin-bottom: 10px;
}
</style>