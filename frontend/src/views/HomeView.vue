<template>
  <v-container>
    <v-row>
      <v-col cols="3">
        <v-navigation-drawer
          absolute
          permanent
          clipped
          left
          width="300px"
        >
          <v-list-item two-line>
            <v-list-item-content>
              <v-list-item-title>Coordenadas seleccionadas</v-list-item-title>
              <v-list-item-subtitle>
                <div class="py-3">Punto (Lon,Lat): <b>{{point}}</b></div>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item two-line>
            <v-list-item-content>
              <v-list-item-title>Crear Perro</v-list-item-title>
              <v-list-item-subtitle>
                <v-text-field
                  class="mt-3"
                  label="Nombre perro"
                  outlined
                  dense
                  v-model="name"
                />
                <v-btn
                  elevation="3"
                  small
                  color="primary"
                  @click="createPoint"
                >Crear</v-btn>
                <v-alert
                  class="mt-3"
                  text
                  dense
                  color="teal"
                  icon="mdi-check"
                  border="left"
                  v-show="create_dog_alert"
                  transition="expand-x-transition"
                >
                  {{message}}
                </v-alert>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item two-line>
            <v-list-item-content>
              <v-list-item-title>Filtrar por región</v-list-item-title>
              <v-list-item-subtitle>
                  <v-select
                    class="mt-5"
                    :items="regions"
                    label="Regiones"
                    dense
                    item-text="name"
                    item-value="id"
                    outlined
                    v-model="selected_region"
                    @change="getPointsByRegion($event)"
                  ></v-select>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
          <v-list-item two-line>
            <v-list-item-content>
              <v-list-item-title>Perro seleccionado</v-list-item-title>
              <v-list-item-subtitle class="mt-2">
                <div class="py-3">Punto (Lon,Lat): <b>{{selected_point}}</b></div>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title>Perros cercanos</v-list-item-title>
              <v-list-item-subtitle class="mt-2">
                <v-select
                  class="mt-5"
                  :items="getNumbers()"
                  label="Cantidad"
                  dense
                  outlined
                  v-model="qty"
                ></v-select>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-list-item>
            <v-list-item-content>
              <v-list-item-title>Perros en un radio</v-list-item-title>
              <v-list-item-subtitle class="mt-2">
                <v-select
                    class="mt-5"
                    :items="getRadius()"
                    label="Radio (metros)"
                    dense
                    outlined
                    v-model="radius"
                  ></v-select>
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-divider></v-divider>
        </v-navigation-drawer>
      </v-col>
      <v-col cols="9">
        <div class="home">
          <div id="mapid" ></div>
          <div class="mt-4">
            <v-row>
              <v-col cols="6">
                <v-card
                  style="height: 100%;width:400px;"
                >
                  <v-card-title>
                    <h2 class="font-weight-medium text--primary">Perros cercanos</h2>
                  </v-card-title>
                  <v-card-subtitle>
                    Cantidad: {{qty}}
                  </v-card-subtitle>
                  <v-card-text>
                    lista
                  </v-card-text>
                </v-card>
              </v-col>
              <v-col cols="6">
                <v-card
                  style="height: 100%;width:400px;"
                >
                <v-card-title>
                    <h2 class="font-weight-medium text--primary">Perros en un radio</h2>
                  </v-card-title>
                  <v-card-subtitle>
                    Radio: {{radius}} metros
                  </v-card-subtitle>
                  <v-card-text>
                    lista
                  </v-card-text>
                </v-card>
              </v-col>
            </v-row>
          </div>
        </div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
//Importaciones
import axios from 'axios';
import 'leaflet/dist/leaflet'; //librería leaflet
import 'leaflet/dist/leaflet.css'; //css leaflet
var icon = require('leaflet/dist/images/marker-icon.png'); //ícono de marcadores
//Se crea objeto ícono con el marcador
var LeafIcon = L.Icon.extend({
  options: {iconSize:[25, 41], iconAnchor:[12, 41], popupAnchor: [-3, -41]}
});
var myIcon = new LeafIcon({iconUrl: icon});

export default {
  name: 'Home',
  data() {
    return {
      latitude:null, //Datos de nuevo punto
      longitude:null,
      name:'',
      points:[], //colección de puntos cargados de la BD
      message:'',
      create_dog_alert: false,
      mymap:null, //objeto de mapa(DIV),
      regions: [], //Regiones
      selected_region: 0,
      selected_point_latitude: null,
      selected_point_longitude: null,
      radius: 0,
      qty:0
    }
  },
  computed:{
    point(){ // función computada para representar el punto seleccionado
      if(this.latitude && this.longitude){
        let lat = this.latitude.toFixed(3);
        let lon = this.longitude.toFixed(3);
        return `(${lat}, ${lon})`;
      }else{
        return '';
      }
    },
    selected_point() {
      if(this.selected_point_latitude && this.selected_point_longitude){
        let lat = this.selected_point_latitude.toFixed(3);
        let lon = this.selected_point_longitude.toFixed(3);
        return `(${lat}, ${lon})`;
      }else{
        return '';
      }
    }
  },
  methods:{
    clearMarkers:function(){ //eliminar marcadores
      this.points.forEach(p=>{
        this.mymap.removeLayer(p);
      })
      this.points = [];
    },
    async getRegions(){
      try {
        let response = await axios.get('http://localhost:8080/regions');
        this.regions = response.data;
        let all_regions_option = {
          "name": "Todas las regiones",
          "id": 0
        };
        this.regions.push(all_regions_option);
      } catch (error) {
        console.log('error', error);
      }
    },
    async createPoint(){ //Crear un nuevo punto
      this.message = '';

      let newPoint ={
        name: this.name,
        latitude: this.latitude,
        longitude: this.longitude
      }

      try {
        let response = await axios.post('http://localhost:8080/dogs' ,newPoint);
        console.log('response', response.data);
        let id = response.data.id;
        this.create_dog_alert = true;
        this.message = `${this.name} fue creado`;
        this.name = '';
        this.clearMarkers(this.mymap);
        this.getPoints(this.mymap);

      } catch (error) {
        console.log('error', error);
        this.message = 'Ocurrió un error'
      }
    },
    async getPoints(map){
      try {
        //se llama el servicio
        let response = await axios.get('http://localhost:8080/dogs');
        let dataPoints = response.data;
        //Se itera por los puntos
        dataPoints.forEach(point => {

          //Se crea un marcador por cada punto
          let p =[point.latitude, point.longitude]
          let marker = L.marker(p, {icon:myIcon}) //se define el ícono del marcador
          .bindPopup(point.name) //Se agrega un popup con el nombre

          //Se agrega a la lista
          this.points.push(marker);
        });

        //Los puntos de la lista se agregan al mapa
        this.points.forEach(p=>{
          p.addTo(map).on('click', this.selectMarker);
        })
      } catch (error) {
        console.log('error', error);
      }
    },
    async getPointsByRegion(region_gid) {
      try {
        let center =  null;
        this.clearMarkers(this.mymap);
        if (region_gid === 0) {
          this.getPoints(this.mymap);
          center = [-33.456, -70.648];
        } else {
          let response = await axios.get('http://localhost:8080/dogs?region_id='+region_gid);
          let dataPoints = response.data;
          dataPoints.forEach(point => {

            //Se crea un marcador por cada punto
            let p =[point.latitude, point.longitude]
            let marker = L.marker(p, {icon:myIcon}) //se define el ícono del marcador
            .bindPopup(point.name) //Se agrega un popup con el nombre

            //Se agrega a la lista
            this.points.push(marker);
          });

          //Los puntos de la lista se agregan al mapa
          this.points.forEach(p=>{
            p.addTo(this.mymap).on('click', this.selectMarker);
          });
          center = [dataPoints[0].latitude,dataPoints[0].longitude];
        }
        this.mymap.flyTo(center, 8);
      } catch (error) {
          console.log('error', error);
      }
    },
    selectMarker(marker) {
      this.selected_point_latitude = marker.latlng.lat;
      this.selected_point_longitude = marker.latlng.lng;
    },
    getNumbers() {
      let start = 0;
      let end = 10;
      let numbers = [];
      let i = start;
      for(i=start; i<=end; i++) {
        numbers.push(i);
      }
      if ( i !== end) {
        console.log(i);
      }
      return numbers;
    },
    getRadius() {
      let start = 100;
      let end = 1500;
      let step = 200;
      let radius = [];
      let i = start;
      for(i=start; i<=end; i=i+step) {
        radius.push(i);
      }
      if ( i !== end) {
        console.log(i);
      }

      return radius;
    }
  },
  mounted:function(){
    let _this = this;
    //Se asigna el mapa al elemento con id="mapid"
    this.mymap = L.map('mapid').setView([-33.456, -70.648], 7);
    //Se definen los mapas de bits de OSM
    L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png', {
      attribution: '&copy; <a href="http://osm.org/copyright">OpenStreetMap</a> contributors',
      maxZoom: 10
    }).addTo(this.mymap);

    //Evento click obtiene lat y long actual
    this.mymap.on('click', function(e) {
      _this.latitude = e.latlng.lat;
      _this.longitude =e.latlng.lng;
    });

    //Se agregan los puntos mediante llamada al servicio
    this.getPoints(this.mymap);
    this.getRegions();
  }
}
</script>

<style>
.home{
  display:flex;
  flex-direction: column;
  align-items: center;
}
/* Estilos necesarios para definir el objeto de mapa */
#mapid {
  height: 500px;
  width:700px;
  margin: 0 auto;
}
</style>
