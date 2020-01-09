<template>
  <v-container>
    <h1>Account</h1>
    <template v-if="account">
      <p>{{ account.user.userName }}</p>
      <p>{{ account.email }}</p>
    </template>
  </v-container>
</template>
<script>
import { getAccount } from '@/js/ajax'
export default {
  data() {
    return {
      account: undefined
    }
  },
  async mounted() {
    try {
      this.account = await getAccount()
    } catch (error) {
      console.log(error)
    }
  }
}
</script>
