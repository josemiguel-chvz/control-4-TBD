<template>
  <div class="home">
    <h1>Ejemplo Mapas</h1>
    <div>{{point}}
      <input type="text" v-model="name" placeholder="nombre" />
      <button type="button" @click="createPoint">Crear</button>
    </div>
    <div>{{message}}</div>
    <div id="mapid" ></div>
    <div>
      <v-row>
        
      </v-row>
    </div>
  </div>
</template>
<script>
//Importaciones
import 'leaflet/dist/leaflet'; //librería leaflet
import 'leaflet/dist/leaflet.css'; //css leaflet
var icon = require('leaflet/dist/images/marker-icon.png'); //ícono de marcadores
//Se crea objeto ícono con el marcador
var LeafIcon = L.Icon.extend({
          options: {iconSize:[25, 41], iconAnchor:[12, 41], popupAnchor: [-3, -41]}
      });
var myIcon = new LeafIcon({iconUrl: icon});

//librería axios
import axios from 'axios';
export default {
  name: 'Home',
  data:function(){
    return{
      latitude:null, //Datos de nuevo punto
      longitude:null,
      name:'',
      points:[], //colección de puntos cargados de la BD
      message:'',
      mymap:null, //objeto de mapa(DIV),
      regions: [] //Regiones
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
        this.regiones = response.data;
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
        this.message = `${this.name} fue creado con éxito con id: ${id}`;
        this.name = '';
        this.clearMarkers(this.mymap);
        this.getPoints(this.mymap)

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
          p.addTo(map)
        })
      } catch (error) {
        console.log('error', error);
      }
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
  height: 600px;
  width:800px;
  margin-top: 50px;
}
</style>
