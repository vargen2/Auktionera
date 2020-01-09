<template>
  <v-container>
    <h1>Create Auction</h1>
    <form class="mb-4">
      <v-text-field
        v-model="title"
        :error-messages="titleErrors"
        label="Title"
        solo
        required
        @input="$v.title.$touch()"
        @blur="$v.title.$touch()"
      ></v-text-field>
      <v-textarea
        v-model="description"
        :error-messages="descriptionErrors"
        label="Description"
        required
        solo
        @input="$v.description.$touch()"
        @blur="$v.description.$touch()"
      ></v-textarea>
      <v-text-field
        v-model="startPrice"
        :error-messages="startPriceErrors"
        label="Start Price"
        solo
        type="number"
        required
        @input="$v.startPrice.$touch()"
        @blur="$v.startPrice.$touch()"
      ></v-text-field>
      <v-text-field
        v-model="duration"
        :error-messages="durationErrors"
        label="Duration"
        solo
        type="number"
        required
        @input="$v.duration.$touch()"
        @blur="$v.duration.$touch()"
      ></v-text-field>
      <!-- <v-select
        v-model="select"
        :items="items"
        :error-messages="selectErrors"
        label="Item"
        required
        @change="$v.select.$touch()"
        @blur="$v.select.$touch()"
      ></v-select> -->
      <!-- <v-checkbox
        v-model="checkbox"
        :error-messages="checkboxErrors"
        label="Do you agree?"
        required
        @change="$v.checkbox.$touch()"
        @blur="$v.checkbox.$touch()"
      ></v-checkbox> -->

      <v-btn color="success" class="mr-2" @click="submit">submit</v-btn>
      <v-btn @click="clear">clear</v-btn>
    </form>
  </v-container>
</template>
<script>
import { validationMixin } from 'vuelidate'
import { required, maxLength, between } from 'vuelidate/lib/validators'
import { postAuction } from '@/js/ajax'
export default {
  mixins: [validationMixin],

  validations: {
    title: { required, maxLength: maxLength(20) },
    description: { required },
    startPrice: { required, between: between(1, 10000000) },
    duration: { required, between: between(4, 48) }
    // select: { required },
    // checkbox: {
    //   checked(val) {
    //     return val
    //   }
    // }
  },

  data: () => ({
    title: '',
    description: '',
    startPrice: 0,
    duration: 4
    // select: null,
    // items: ['Item 1', 'Item 2', 'Item 3', 'Item 4'],
    // checkbox: false
  }),

  computed: {
    // selectErrors() {
    //   const errors = []
    //   if (!this.$v.select.$dirty) return errors
    //   !this.$v.select.required && errors.push('Item is required')
    //   return errors
    // },
    titleErrors() {
      const errors = []
      if (!this.$v.title.$dirty) return errors
      !this.$v.title.maxLength && errors.push('Title must be at most 20 characters long')
      !this.$v.title.required && errors.push('Title is required.')
      return errors
    },
    descriptionErrors() {
      const errors = []
      if (!this.$v.description.$dirty) return errors
      !this.$v.description.required && errors.push('Description is required')
      return errors
    },
    startPriceErrors() {
      const errors = []
      if (!this.$v.startPrice.$dirty) return errors
      !this.$v.startPrice.required && errors.push('Start Price is required')
      !this.$v.startPrice.between && errors.push('Start Price min 1')
      return errors
    },
    durationErrors() {
      const errors = []
      if (!this.$v.duration.$dirty) return errors
      !this.$v.duration.required && errors.push('Duration is required')
      !this.$v.duration.between && errors.push('Duration min 4 hours')
      return errors
    }
  },

  methods: {
    async submit() {
      this.$v.$touch()
      let today = new Date()
      today.setHours(today.getHours() + this.duration + 1)
      const payLoad = {
        description: this.description,
        title: this.title,
        startPrice: this.startPrice,
        endsAt: today
      }
      try {
        const auction = await postAuction(payLoad)
        if (auction) {
          this.$router.push({ path: `/auction/${auction.id}` })
        }
      } catch (error) {
        console.log(error)
      }
    },
    clear() {
      this.$v.$reset()
      this.title = ''
      this.description = ''
      this.startPrice = 0
      this.duration = 4
      // this.select = null
      // this.checkbox = false
    }
  }
}
</script>
