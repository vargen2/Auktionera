import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    loggedIn: false,
    user: null
  },
  getters: {
    isLoggedIn: state => state.loggedIn,
    getUser: state => state.user
  },
  mutations: {
    setLoggedIn(state, value) {
      state.loggedIn = value
    },
    setUser(state, value) {
      state.user = value
    }
  },
  actions: {},
  modules: {}
})
