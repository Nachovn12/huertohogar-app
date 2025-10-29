# HuertoHogar — Android App

Aplicación Android de HuertoHogar: tienda móvil para distribuir productos frescos y orgánicos desde el productor hasta el consumidor.

---

## Resumen rápido
HuertoHogar es una aplicación escrita en Kotlin usando Jetpack Compose (Material3). Implementa un catálogo de productos, filtrado por categorías, detalle de producto, carrito de compras en memoria, navegación entre pantallas y consumo de una API REST (Retrofit). El proyecto sigue un patrón MVVM ligero con `ProductViewModel`, `ProductRepository` y `Retrofit` como capa de red.

---

## Características principales (extraídas del código)
- UI construida con Jetpack Compose (Material3).
- Navegación con `NavHost` y rutas definidas en `navigation/AppNavGraph.kt`.
- Pantallas incluidas:
  - `SplashScreen` — pantalla inicial.
  - `IntroScreen` — onboarding.
  - `LoginScreen` / `RegisterScreen` — flujo de autenticación (actualmente con credenciales de prueba en `LoginScreen`).
  - `HomeScreen` — lista principal con banner, búsqueda, categorías y sección "Más vendido".
  - `CategoriaScreen` — listado por categoría.
  - `DetalleProductoScreen` — vista detallada de producto.
  - `CartScreen` — carrito local en memoria.
  - `AddressScreen` — (pantalla para direcciones / envío).

- Modelo de datos principal: `Product` (id, name, description, price, stock, imageUrl, category, etc.).
- Capa de red: `ProductApiService` con endpoints `/api/products` y `/api/products/{id}`.
- `RetrofitInstance` apunta por defecto a `http://10.0.2.2:8081/` (modo dev + emulador Android).
- `ProductViewModel` expone `products`, `selectedProduct` y un carrito con lógica para agregar, actualizar y eliminar items.
- Imágenes cargadas con `Coil` (`AsyncImage`).

---

## Arquitectura y decisiones técnicas
- Lenguaje: Kotlin
- UI: Jetpack Compose + Material3
- Patrón: MVVM (ViewModel + Repository)
- Comunicación con backend: Retrofit + Gson
- Gestión de imágenes: Coil
- Concurrency: Kotlin Coroutines / Flow

Motivación:
- Compose acelera iteración UI y permite un diseño más declarativo.
- MVVM separa UI y lógica de negocio para facilitar pruebas y mantenimiento.

---

## Requisitos locales
- JDK 11 (u 17 si tu gradle/Android plugin lo requiere; verifica `build.gradle.kts`).
- Android Studio (recomendado) con Android SDK instalado.
- Emulador Android o dispositivo físico.
- Gradle wrapper incluido (`gradlew.bat`).

---

## Cómo ejecutar la app (desarrollo local — Windows PowerShell)
1. Abre una terminal y sitúate en la raíz del proyecto (el directorio que contiene este README). Ejemplos:

```powershell
# Windows PowerShell (ejemplo genérico)
Set-Location -Path 'C:\ruta\a\HuertoHogar_App'
# Alternativamente, desde cualquier terminal:
cd /ruta/a/HuertoHogar_App
```

2. Asegúrate de que `local.properties` apunta a tu SDK local (Android Studio lo genera automáticamente). No comitees `local.properties`.
# HuertoHogar — App móvil Android

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) [![Kotlin](https://img.shields.io/badge/Kotlin-1.8-blue.svg)](https://kotlinlang.org/) [![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-brightgreen.svg)](https://developer.android.com/jetpack/compose)

Una aplicación nativa Android diseñada para llevar productos frescos y orgánicos desde pequeños productores hasta la puerta de los clientes. Este repositorio contiene el código del cliente móvil (Kotlin + Jetpack Compose) y está pensado como base para un MVP de e‑commerce local.

Índice
------
- [Demo](#demo)
- [Qué hace la app](#qué-hace-la-app)
- [Tecnologías](#tecnologías)
- [Instalación rápida](#instalación-rápida)
- [Configuración del backend](#configuración-del-backend)
- [Arquitectura y estructura del código](#arquitectura-y-estructura-del-código)
- [Buenas prácticas y seguridad](#buenas-prácticas-y-seguridad)
- [Contribuir](#contribuir)
- [Roadmap (próximos pasos)](#roadmap-próximos-pasos)
- [Licencia](#licencia)
- [Contacto](#contacto)

Demo
----
_Aquí puedes añadir una captura de pantalla o GIF del flujo principal (home, detalle y carrito)._ 

Qué hace la app
---------------
- Catálogo de productos con imágenes, precios y stock.
- Filtrado por categoría (Frutas, Verduras, Orgánicos, Lácteos).
- Página detalle por producto.
- Carrito de compras local (añadir, modificar cantidad, eliminar).
- Flujo básico de autenticación (pantallas de login y registro).
- Selección de ciudad/ubicación para entregas.
- Integración con una API REST para obtener productos.

Tecnologías
-----------
- Kotlin
- Jetpack Compose (Material 3)
- Retrofit + Gson (API REST)
- Coil (carga de imágenes)
- Kotlin Coroutines & Flow (concurrencia)
- Arquitectura: MVVM (ViewModel + Repository)

Instalación rápida
------------------
Requisitos mínimos:
- JDK 11 o superior
- Android Studio con SDK instalado

Pasos básicos (terminal):

```powershell
# Sitúate en la carpeta del proyecto (la que contiene este README)
Set-Location -Path 'C:\ruta\a\HuertoHogar_App'

# Compilar APK debug
.\gradlew.bat assembleDebug

# Instalar en emulador/dispositivo conectado
.\gradlew.bat installDebug
```

Recomendación: usa Android Studio para ejecutar el proyecto con el emulador y visualizar logs y herramientas de layout.

Configuración del backend
-------------------------
Durante desarrollo `RetrofitInstance` apunta por defecto a `http://10.0.2.2:8081/` (esto permite que el emulador se conecte al `localhost` del host). Para entornos diferentes:

- Extrae la URL base a `buildConfigField` en tu `build.gradle` (o `gradle.properties`) y lee `BuildConfig.BASE_URL` desde `RetrofitInstance`.
- Si tu API está en otra máquina o en la nube, actualiza la URL apropiadamente.

Arquitectura y estructura del código
-----------------------------------
- Paquete principal: `com.huertohogar.huertohogar_app` (módulo `app`).
- Navegación: `navigation/AppNavGraph.kt` (NavHost y rutas).
- ViewModel: `viewmodel/ProductViewModel.kt` — expone productos, producto seleccionado y carrito.
- Repository: `repository/ProductRepository.kt` — capa de acceso a `ProductApiService`.
- Network: `network/RetrofitInstance.kt` y `network/ProductApiService.kt`.
- Modelos: `model/Product.kt`.
- Pantallas (Jetpack Compose): `screen/*` — `HomeScreen`, `CategoriaScreen`, `DetalleProductoScreen`, `CartScreen`, `LoginScreen`, `RegisterScreen`, `SplashScreen`, `IntroScreen`, `AddressScreen`.

Buenas prácticas y seguridad
---------------------------
- NO subir `local.properties` ni archivos de keystore (`*.jks`, `*.keystore`) al repositorio.
- Elimina credenciales de prueba antes de publicar (actualmente hay credenciales de demo en `LoginScreen.kt`).
- Almacena secretos (keystore passwords, API keys) en GitHub Secrets y consúmelos desde CI.
- Añade `ktlint`/`detekt` y pruebas unitarias para mantener calidad.

Contribuir
----------
1. Fork / Clona el repo.
2. Crea una rama con el prefijo acorde: `feature/`, `fix/`, `chore/`.
3. Abre un Pull Request hacia `main` con descripción y checklist.
4. Revisión y CI obligatorio antes de merge (proteger `main`).

Roadmap (próximos pasos)
-----------------------
- Externalizar `BASE_URL` y añadir flavors build (dev/prod).
- Persistir carrito localmente (Room o DataStore).
- Autenticación real y manejo seguro de tokens.
- Tests unitarios del `ProductViewModel` y pruebas instrumentadas UI.
- Integración CI/CD: build en PRs, linting y tests automáticos.

Licencia
--------
Este proyecto está licenciado bajo la MIT License — ver `LICENSE`.

Contacto
--------
Para dudas o contribuciones: abre un Issue o Pull Request en el repositorio.

---

Si quieres, puedo añadir: una sección de screenshots, un badge de CI (cuando lo configures), o un archivo `CONTRIBUTING.md` con plantilla de PR y reglas de revisión.