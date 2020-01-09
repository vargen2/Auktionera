<template>
  <v-container>
    <div v-if="auction">
      <h2>{{ auction.title }}</h2>
      <p>{{ auction.description }}</p>
    </div>
  </v-container>
</template>

<script>
import { getAuction } from '@/js/ajax'
export default {
  data() {
    return {
      auction: {}
    }
  },
  async mounted() {
    const id = this.$route.params.id
    try {
      this.auction = await getAuction(id)
    } catch (error) {
      console.log(error)
    }
  }
}
</script>
