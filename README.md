# Pastelería App - Mil Sabores 🍰

Aplicación móvil Android para la gestión y venta de productos de la Pastelería Mil Sabores. Desarrollada con **Kotlin** y **Jetpack Compose**, siguiendo la arquitectura **MVVM** y **Clean Architecture**.

Esta aplicación funciona en conjunto con el backend [back-pasteleria](../back-pasteleria).

## 📱 Características

### Cliente
- **Catálogo de Productos**: Visualización de pasteles y dulces disponibles.
- **Detalle de Producto**: Información detallada, precio e imágenes.
- **Carrito de Compras**: Agregar productos y gestionar el pedido.
- **Perfil de Usuario**: Gestión de información personal y direcciones.
- **Historial de Pedidos**: Visualización de compras anteriores.

### Administración
- **Gestión de Usuarios**: Listado y administración de usuarios (AdminUserListScreen).
- **Gestión de Productos**: Crear, editar y eliminar productos.
- **Reportes**: Visualización de ventas y estadísticas.

## 🛠 Tech Stack

- **Lenguaje**: Kotlin
- **UI**: Jetpack Compose (Material3)
- **Arquitectura**: MVVM + Clean Architecture
- **Inyección de Dependencias**: Hilt
- **Red**: Retrofit + OkHttp + Gson
- **Base de Datos Local**: Room
- **Asincronía**: Coroutines + Flow
- **Navegación**: Navigation Compose
- **Carga de Imágenes**: Coil
- **Testing**: JUnit4, Mockk, Espresso

## 📋 Requisitos Previos

- Android Studio.
- JDK 17.
- Dispositivo Android o Emulador (Min SDK 24).
- Backend `back-pasteleria` en ejecución.

## 🚀 Configuración e Instalación

### 1. Configurar el Backend
Para que la aplicación funcione correctamente, el backend debe estar ejecutándose localmente o en un servidor accesible.

Sigue las instrucciones en `../back-pasteleria/README.md` para levantar el servidor Spring Boot.

> **Nota para Emulador**: La aplicación está configurada para conectarse a `http://10.0.2.2:8080/api/v1/`, que es la dirección IP especial del emulador de Android para acceder al `localhost` de tu computadora.

### 2. Clonar y Abrir el Proyecto
```bash
git clone <url-del-repo>
cd Pasteleria_APP
```
Abre la carpeta `Pasteleria_APP` en Android Studio.

### 3. Sincronizar Gradle
Al abrir el proyecto, Android Studio intentará descargar las dependencias. Si no lo hace automáticamente, ve a **File > Sync Project with Gradle Files**.

### 4. Ejecutar la App
1. Selecciona un dispositivo o emulador en la barra de herramientas.
2. Haz clic en el botón **Run** (▶️).

## 📂 Estructura del Proyecto

```
app/src/main/java/com/pasteleria_app/pasteleria_app/
├── data/               # Capa de Datos (Repositorios, API, Room)
├── di/                 # Módulos de Hilt (Inyección de Dependencias)
├── domain/             # Capa de Dominio (Modelos, Casos de Uso)
├── presentation/       # Capa de Presentación (UI)
│   ├── components/     # Componentes Componibles Reutilizables
│   ├── navigation/     # Grafo de Navegación
│   ├── screens/        # Pantallas de la App
│   ├── theme/          # Tema y Estilos (Color, Type, Shape)
│   └── viewmodel/      # ViewModels
├── utils/              # Utilidades y Constantes
├── MainActivity.kt     # Punto de entrada de la App
└── PasteleriaApp.kt    # Clase de Aplicación (Hilt)
```
