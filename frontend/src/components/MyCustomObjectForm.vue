<!-- MyCustomObjectForm.vue -->
<template>
  <div>
    <h1>My Custom Object Form</h1>
    <form @submit.prevent="submitForm">
      <label for="testString">Test String:</label>
      <input v-model="myCustomObject.testString" type="text" id="testString" />

      <label for="testInt">Test Integer:</label>
      <input v-model.number="myCustomObject.testInt" type="number" id="testInt" />

      <button type="submit">Submit</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import myCustomObject from "../scripts/objects/MyCustomObject.js"; // Adjust the path

export default {
  data() {
    return {
      myCustomObject: { ...myCustomObject },
    };
  },
  methods: {
    submitForm() {
      // Send the MyCustomObject to the backend
      axios.post('/api/receiveObject', this.myCustomObject)
          .then(response => {
            alert('Object submitted successfully: ' + response.data);
          })
          .catch(error => {
            alert('Error submitting object: ' + error);
          });
    },
  },
};
</script>

<style scoped>
/* Add your custom styles here */
</style>
