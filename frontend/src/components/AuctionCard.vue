<template>
  <v-card class="mb-4">
    <v-card-title>{{ auction.title }}</v-card-title>
    <v-card-text
      ><p class="text--primary">{{ auction.description }}</p>
    </v-card-text>

    <v-card-actions class="pa-4 pt-0">
      <v-row align="center" no-gutters>
        <span class="text--primary mr-2">{{ auction.seller.userName }}</span>
        <!-- <v-rating class="mr-2" v-model="rating" dense half-increments readonly></v-rating> -->
        <v-btn text>reviews</v-btn>
      </v-row>
    </v-card-actions>
    <v-card-text class="pt-0">
      <v-row align="center" no-gutters>
        <span class="text--primary">Price: {{ currentBid }}</span>
        <v-btn text>Bids</v-btn>
      </v-row>
    </v-card-text>
    <v-card-actions v-if="auction.state === 'InProgress'" class="pa-4 pt-0">
      <v-row align="start" no-gutters>
        <v-text-field
          dense
          class="bid-input mr-4"
          :disabled="!canUserBuy"
          v-model="bidPrice"
          solo
          type="number"
          required
        ></v-text-field>
        <v-btn height="38" class="mr-4" :disabled="!canUserBuy" :loading="isBidding" @click="bid">Bid</v-btn>
        <v-btn v-if="hasBuyout" height="38" class="ml-0" :disabled="!canUserBuy" @click="buyout"
          >Buyout {{ auction.buyoutPrice }}</v-btn
        >
      </v-row>
    </v-card-actions>
  </v-card>
</template>

<script>
import { postBid } from '@/js/ajax'
export default {
  props: {
    auctionprop: { type: Object, required: true },
    user: { type: Object, required: false }
  },
  data() {
    return {
      auction: this.auctionprop,
      isBidding: false,
      isBuyout: false,
      bidPrice: Math.ceil(
        (this.auctionprop.currentBid === 0 ? this.auctionprop.startPrice : this.auctionprop.currentBid) * 1.1
      )
    }
  },
  computed: {
    canUserBuy() {
      if (this.user) {
        return this.user.id !== this.auction.seller.id
      }
      return false
    },
    hasBuyout() {
      return this.auction.buyoutPrice !== 0
    },
    currentBid() {
      return this.auction.currentBid === 0 ? this.auction.startPrice : this.auction.currentBid
    }
  },
  methods: {
    async bid() {
      if (this.isBidding === true) {
        return
      }
      this.isBidding = true
      const payLoad = {
        currentPrice: this.currentBid,
        bidPrice: this.bidPrice,
        auctionId: this.auction.id
      }
      try {
        const response = await postBid(payLoad)
        this.auction = response
        this.bidPrice = Math.ceil(
          (this.auction.currentBid === 0 ? this.auction.startPrice : this.auction.currentBid) * 1.1
        )
      } catch (error) {
        console.log(error)
      }
      this.isBidding = false
    },
    async buyout() {
      if (this.isBuyout === true) {
        return
      }
      this.isBuyout = true
      const payLoad = {
        currentPrice: this.currentBid,
        bidPrice: this.auction.buyoutPrice,
        auctionId: this.auction.id
      }
      try {
        const response = await postBid(payLoad)
        this.auction = response
        this.bidPrice = Math.ceil(
          (this.auction.currentBid === 0 ? this.auction.startPrice : this.auction.currentBid) * 1.1
        )
      } catch (error) {
        console.log(error)
      }
      this.isBuyout = false
    }
  }
}
</script>

<style>
.bid-input {
  max-width: 120px;
}
</style>
