<template>

<v-data-table
    :headers="headers"
    :items="myVote"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'MyVote',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "voteId", value: "voteId" },
            { text: "candidateId", value: "candidateId" },
            { text: "voterId", value: "voterId" },
            { text: "votingDate", value: "votingDate" },
        ],
        myVote : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/myvotes')

      this.myVote = temp.data._embedded.myvotes;

    },
    methods: {
    }
  }
</script>

