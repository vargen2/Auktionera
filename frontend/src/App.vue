<template>
  <v-app>
    <v-app-bar dense app color="primary" dark absolute>
      <div class="d-flex align-center">
        <v-btn to="/" text active-class>
          <span>
            Auctions
          </span>
        </v-btn>
        <v-btn v-if="isLoggedIn" to="/create" text>
          <span class="">Create</span>
        </v-btn>
      </div>

      <v-spacer></v-spacer>

      <div v-if="!isLoggedIn">
        <v-btn href="http://localhost:8080/oauth2/authorize/google?redirect_uri=http://localhost:8081" text>
          <span class="mr-2">Login</span>
          <v-icon>mdi-account-box-outline</v-icon>
        </v-btn>
      </div>
      <div v-else-if="isLoggedIn">
        <v-btn to="/account" icon>
          <v-icon>mdi-account-box-outline</v-icon>
        </v-btn>
        <v-btn icon @click="logout">
          <v-icon>mdi-exit-to-app</v-icon>
        </v-btn>
      </div>
    </v-app-bar>

    <v-content>
      <router-view />
    </v-content>
    <v-footer padless>
      <v-col class="pa-1"
        ><v-btn small to="/about" text active-class>
          <span class="">About</span>
        </v-btn>
      </v-col>
    </v-footer>
  </v-app>
</template>

<script>
import { ACCESS_TOKEN } from '@/js/constants'
import { getAccount } from '@/js/ajax'
export default {
  name: 'App',

  data: () => ({
    //
  }),
  computed: {
    isLoggedIn() {
      return this.$store.getters.isLoggedIn
    }
  },
  async beforeCreate() {
    const token = this.$route.query.token
    if (token) {
      localStorage.setItem(ACCESS_TOKEN, token)
    }
    if (localStorage.getItem(ACCESS_TOKEN)) {
      this.$store.commit('setLoggedIn', true)
      try {
        const account = await getAccount()
        if (account.user) {
          this.$store.commit('setUser', account.user)
        }
      } catch (error) {
        console.log(error)
      }
    }
  },
  methods: {
    logout() {
      localStorage.removeItem(ACCESS_TOKEN)
      this.$store.commit('setLoggedIn', false)
      this.$store.commit('setUser', null)
    }
  }
}
</script>
