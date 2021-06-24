<template>

<v-data-table
    :headers="headers"
    :items="candidateVote"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'CandidateVote',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "candidateId", value: "candidateId" },
            { text: "electionId", value: "electionId" },
            { text: "no", value: "no" },
            { text: "party", value: "party" },
            { text: "name", value: "name" },
            { text: "campaigns", value: "campaigns" },
            { text: "votes", value: "votes" },
        ],
        candidateVote : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/candidatevotes')

      this.candidateVote = temp.data._embedded.candidatevotes;

    },
    methods: {
    }
  }
</script>

