<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
      flat
    >
      <div class="d-flex align-center">
        <h2>Ejemplo Mapas</h2>
      </div>
    </v-app-bar>

    <v-main>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>

export default {
  name: 'App',

  data: () => ({
    //
  }),
};
</script>
