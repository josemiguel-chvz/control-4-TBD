# Instrucciones

## Base de datos

Crear una base de datos llamada "control_4_tbd" para cargar el backup

```
psql -h localhost -U postgres -c "CREATE DATABASE control_4_tbd"
```

Cargar backup con regiones y perros

```
cd db
psql -h localhost -U postgres -d control_4_tbd -f bd_control.sql
cd ..
```

## Backend

Build
```
cd backend
./gradlew
```
Ejecutar
```
./gradlew bootRun
```

## Frontend


Build
```
cd frontend
npm install
```
Ejecutar
```
npm run serve
```

## Taller de Bases de Datos

En esta carpeta se encuentran los códigos de proyectos vistos en clases

* Ejemplo backend [Consultas a BD Geolocalizada](./backend)

* Ejemplo frontend [Visualización de datos geolocalizados en un mapa](./frontend)
