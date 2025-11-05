<div align="center">
  <img src="app/src/main/res/drawable/logo_huerto_hogar.png" alt="HuertoHogar Logo" width="200"/>
  
  # 🌱 HuertoHogar App
  
  ### App móvil para comprar productos orgánicos directo del huerto
  
  [![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-purple.svg)](https://kotlinlang.org)
  [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-brightgreen.svg)](https://developer.android.com/jetpack/compose)
  [![Android](https://img.shields.io/badge/Android-14%20(API%2034)-green.svg)](https://developer.android.com)
  [![License](https://img.shields.io/badge/License-Educational-blue.svg)]()
  
</div>

---

## 📱 Sobre el Proyecto

Hola! Este es nuestro proyecto. **HuertoHogar** es una app de e-commerce que creamos para aprender desarrollo móvil Android con Kotlin y Jetpack Compose.

La idea es simple: hacer más fácil comprar productos orgánicos frescos desde tu celular. Es como tener el huerto en tu bolsillo 🌿

### 🎯 Lo que queríamos lograr

Crear una app de verdad funcional donde puedas:
- Ver productos organizados por categorías
- Agregarlos al carrito
- Hacer un checkout completo
- Ver el historial de tus pedidos

Todo con una interfaz bonita y fácil de usar.

---

## ✨ Funcionalidades principales

Lo que puedes hacer en la app:

### 🛒 Comprar productos
- Ver un catálogo con fotos y descripciones de cada producto
- Buscar productos (incluso funciona si escribes con o sin tildes!)
- Filtrar por categorías: Frutas, Verduras, Hierbas, Lácteos, etc.
- Agregar productos al carrito con las cantidades que quieras
- Ver toda la info del producto: temporada, dificultad de cultivo, tiempo de cosecha

### 🎨 Interfaz
- Diseño moderno hecho con Jetpack Compose (lo más nuevo de Android)
- Se adapta a cualquier tamaño de pantalla
- Navegación con barra inferior (típica de apps profesionales)
- Animaciones suaves cuando cambias de pantalla

### 👤 Cuenta de usuario
- Perfil personalizable con tus datos
- Guarda tus direcciones de entrega
- Historial completo de todos tus pedidos
- Te saluda diferente según la hora del día (un detalle que nos gustó agregar)

### 📦 Sistema de pedidos
- Proceso de checkout completo con validación de datos
- Puedes elegir método de pago (tarjeta, transferencia, efectivo)
- Envío gratis si compras más de $30.000
- Estados del pedido: Procesando → En camino → Entregado
- Recibes un número de orden para seguimiento

---

## 🛠️ Tecnologías que usamos

Esto fue lo que aprendimos e implementamos:

**Lenguaje y Framework:**
- Kotlin 1.9.0 (el lenguaje oficial de Android)
- Jetpack Compose (la forma moderna de crear interfaces en Android)
- Material 3 (sistema de diseño de Google)

**Arquitectura:**
- Patrón MVVM (Model-View-ViewModel)
- StateFlow para manejar el estado reactivo
- Repository Pattern para organizar los datos

**Librerías importantes:**
```kotlin
// Para la interfaz
androidx.compose.ui
androidx.compose.material3
androidx.navigation.compose

// Para el ViewModel
androidx.lifecycle.viewmodel-compose

// Para cargar imágenes
io.coil-kt:coil-compose

// Para JSON
com.google.code.gson:gson
```

**Herramientas:**
- Android Studio Hedgehog
- Gradle 8.2
- Git para trabajar en equipo
- JDK 17

---

## 📂 Cómo está organizado el código

```
app/src/main/java/com/huertohogar/huertohogar_app/
├── MainActivity.kt              # Punto de entrada de la app
├── components/                  # Componentes que reutilizamos
│   └── AppBottomNavigationBar.kt
├── model/                       # Clases de datos
│   ├── Product.kt
│   ├── Pedido.kt
│   └── UserProfile.kt
├── viewmodel/                   # Lógica de negocio
│   ├── ProductViewModel.kt
│   ├── ProductViewModelFactory.kt
│   └── UserViewModel.kt
├── repository/                  # Manejo de datos
│   ├── LocalProductRepository.kt
│   └── ProductDataSource.kt
├── screen/                      # Todas las pantallas
│   ├── SplashScreen.kt
│   ├── HomeScreen.kt
│   ├── CategoriaScreen.kt
│   ├── DetalleProductoScreen.kt
│   ├── CartScreen.kt
│   ├── CheckoutScreen.kt
│   ├── PedidosScreen.kt
│   └── ProfileScreen.kt
├── theme/                       # Colores y estilos
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
├── navigation/                  # Navegación entre pantallas
│   └── AppNavGraph.kt
└── utils/                       # Funciones útiles
    └── FormatUtils.kt

app/src/main/res/
├── drawable/                    # Imágenes de productos
├── mipmap-*/                   # Íconos de la app
└── assets/
    └── products.json           # Base de datos de productos
```

---

## 🚀 Cómo ejecutar el proyecto

### Lo que necesitas tener instalado:

- Android Studio Hedgehog o más nuevo
- JDK 17 (viene con Android Studio)
- Android SDK API 34
- Git

### Pasos para correrlo:

1. **Clona el repo**
   ```bash
   git clone https://github.com/Nachovn12/huertohogar-app.git
   cd huertohogar-app
   ```

2. **Ábrelo en Android Studio**
   - File → Open
   - Selecciona la carpeta del proyecto
   - Espera que Gradle sincronice todo (puede tardar un rato la primera vez)

3. **Verifica el SDK**
   - Ve a File → Project Structure
   - Asegúrate que todo apunte al SDK correcto

4. **Compila el proyecto**
   ```bash
   ./gradlew build
   ```

5. **Ejecuta la app**
   - Conecta tu celular Android o abre un emulador
   - Dale al botón Run (▶️) o presiona Shift + F10
   - Listo! La app debería instalarse y abrirse

---

## 🎨 Sobre el diseño

Elegimos una paleta de colores verde porque va con la temática del huerto:

```kotlin
Verde principal: #23AA49      // El verde de la marca
Verde claro: #2ECC71          // Para detalles
Rojo: #FF314A                 // Para los precios (llama la atención)
Fondo: #F8F9FA                // Gris muy clarito
```

La tipografía es Roboto (la clásica de Material Design) en sus variantes Bold, Regular y Light.

---

## 🤔 Lo que aprendimos

Este proyecto nos sirvió un montón para:
- Entender cómo funciona Jetpack Compose (al principio costó, pero después es re intuitivo)
- Aplicar MVVM de verdad, no solo en teoría
- Manejar estados reactivos con StateFlow
- Hacer que una app sea responsiva
- Trabajar con navegación entre pantallas
- Gestionar un proyecto mediano con Git

---

## 📦 Compilar APK

Si quieres compilar la app:

```bash
# Para versión debug (más rápida)
./gradlew assembleDebug

# Para versión release (optimizada)
./gradlew assembleRelease
```

El APK queda en: `app/build/outputs/apk/`

---

## 📄 Licencia

Este es un proyecto educativo que hicimos para Desarrollo de aplicaciones moviles.

El objetivo es aprender y demostrar lo que sabemos hacer con Android, Kotlin y Jetpack Compose.

---

## 👨‍💻 Autores

**Ignacio Valeria**
- GitHub: [@Nachovn12](https://github.com/Nachovn12)
- Me encargué más del diseño UI/UX y las pantallas

**Benjamín Flores**
- GitHub: [@BenjaFlores379](https://github.com/BenjaFlores379)
- Se enfocó más en la lógica y arquitectura

**Proyecto**: [HuertoHogar App](https://github.com/Nachovn12/huertohogar-app)

---
