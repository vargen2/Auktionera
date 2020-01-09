<template>
  <v-container>
    <v-row>
      <v-col cols="12" :md="8">
        <v-row no-gutters>
          <v-text-field class="mr-4" solo placeholder="search"></v-text-field>
          <v-btn height="48">Search</v-btn>
        </v-row>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12" :md="8">
        <auction-card v-for="auction in auctions" :key="auction.id" :auction="auction" :user="getUser" class="mb-4" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { getAuctions } from '@/js/ajax'
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      auctions: []
    }
  },
  components: {
    AuctionCard: () => import('@/components/AuctionCard')
  },
  computed: {
    ...mapGetters(['getUser'])
  },
  async mounted() {
    try {
      this.auctions = await getAuctions()
    } catch (error) {
      console.log(error)
    }
  }
}
</script>
