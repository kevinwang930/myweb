<template>
  <div class="video-list">
    <h2>Video List</h2>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Title</th>
        <th>Duration</th>
        <th>Thumbnail</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="video in videos" :key="video.id">
        <td>{{ video.id }}</td>
        <td>{{ video.title }}</td>
        <td>{{ video.duration }}</td>
        <td><img :src="video.thumbnail" alt="Video Thumbnail" /></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'VideoList',
  data() {
    return {
      videos: []
    }
  },
  mounted() {
    this.fetchVideoList()
  },
  methods: {
    fetchVideoList() {
      fetch('http://localhost:8080/melon/artifact/list')
        .then(response => response.json())
        .then(data => {
          this.videos = data
        })
        .catch(error => {
          console.error('Error fetching video list:', error)
        })
    }
  }
}
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