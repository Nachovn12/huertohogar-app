# HuertoHogar App

Aplicación móvil Android para la venta de productos orgánicos del huerto.

## Descripción

HuertoHogar es una app de e-commerce diseñada para facilitar la compra de productos frescos y orgánicos directamente del huerto a tu hogar. La aplicación permite navegar por diferentes categorías de productos, agregar items al carrito, y realizar pedidos de forma sencilla.

## Características

- **Navegación intuitiva**: Interfaz moderna con barra de navegación inferior
- **Catálogo de productos**: Visualización de productos por categorías (Frutas, Verduras, Hierbas)
- **Carrito de compras**: Gestión de productos seleccionados con cantidades
- **Sistema de pedidos**: Realización y seguimiento de pedidos
- **Perfil de usuario**: Gestión de información personal y direcciones
- **Diseño responsivo**: Adaptación a diferentes tamaños de pantalla

## Tecnologías Utilizadas

- **Lenguaje**: Kotlin
- **Framework UI**: Jetpack Compose
- **Arquitectura**: MVVM (Model-View-ViewModel)
- **Navegación**: Navigation Component
- **Base de datos**: Room
- **Gestión de estado**: StateFlow
- **Gradle**: Sistema de construcción

## Estructura del Proyecto

```
app/
├── src/main/
│   ├── java/com/huertohogar/huertohogar_app/
│   │   ├── components/         # Componentes reutilizables
│   │   ├── model/             # Modelos de datos
│   │   ├── screen/            # Pantallas de la aplicación
│   │   ├── viewmodel/         # ViewModels
│   │   └── MainActivity.kt    # Actividad principal
│   └── res/                   # Recursos (layouts, imágenes, etc.)
└── build.gradle.kts           # Configuración de dependencias
```

## Requisitos

- Android Studio Hedgehog o superior
- JDK 17
- Android SDK 34 (API Level 34)
- Gradle 8.2

## Instalación

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Sincronizar las dependencias de Gradle
4. Ejecutar en un emulador o dispositivo físico

## Características Principales

### Pantallas

1. **Home**: Pantalla principal con productos destacados y categorías
2. **Productos**: Catálogo completo de productos
3. **Carrito**: Resumen de compra con cantidades y precios
4. **Pedidos**: Historial y seguimiento de pedidos realizados
5. **Perfil**: Información del usuario y configuración

### Funcionalidades

- Búsqueda de productos
- Filtrado por categorías
- Agregar/eliminar productos del carrito
- Modificar cantidades en el carrito
- Proceso de checkout
- Seguimiento de estado de pedidos
- Gestión de perfil de usuario

## Licencia

Este proyecto es un prototipo educativo desarrollado como proyecto universitario.

## Autor

Desarrollado por estudiante de Ingeniería Informática

