import Vue from 'vue'
import Vuetify from 'vuetify/lib'
import Vuelidate from 'vuelidate'
import colors from 'vuetify/lib/util/colors'

Vue.use(Vuelidate)
Vue.use(Vuetify)

export default new Vuetify({
  theme: {
    themes: {
      light: {
        primary: colors.purple,
        secondary: colors.grey.darken1,
        accent: colors.shades.black,
        error: colors.red.accent2,
        success: colors.green.accent3
      },
      dark: {
        primary: colors.blue.lighten3
      }
    }
  }
})
