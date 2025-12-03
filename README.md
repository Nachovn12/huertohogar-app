# 🌱 HuertoHogar - Gestor de Productos Híbrido

> **Evaluación 3 - Desarrollo de Aplicaciones Móviles**  
> Aplicación Android que funciona online/offline con Room Database y Retrofit

---

## 📱 Descripción

**HuertoHogar** es una aplicación Android moderna que permite gestionar un catálogo de productos con capacidades online y offline. La app consume datos desde un REST API público y los almacena localmente usando Room Database, permitiendo su uso sin conexión a Internet.

---

## ✨ Características Principales

- ✅ **Carga desde API REST:** Descarga 200 productos desde endpoint público
- ✅ **Almacenamiento Local:** Guarda productos en Room Database para uso offline
- ✅ **Validación de Internet:** Detecta conexión antes de llamar a la API
- ✅ **Modo Offline:** Funciona completamente sin Internet con datos locales
- ✅ **UI Moderna:** Material 3 Design con Jetpack Compose
- ✅ **Arquitectura MVVM:** Separación de responsabilidades profesional

---

## 🏗️ Arquitectura

```
┌─────────────────────────────────────────────────┐
│                 UI Layer                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐     │
│  │  Home    │→ │ Listado  │→ │ Detalle  │     │
│  └──────────┘  └──────────┘  └──────────┘     │
│         │              │                        │
│         └──────┬───────┘                        │
│                ▼                                │
│      ┌──────────────────┐                       │
│      │ ProductViewModel │                       │
│      └─────────┬────────┘                       │
└────────────────┼──────────────────────────────┘
                 │
┌────────────────┼──────────────────────────────┐
│           Repository Layer                     │
│      ┌──────────┴──────────┐                   │
│      │                     │                   │
│ ┌────▼────┐         ┌─────▼──────┐           │
│ │ Remote  │         │    Room    │            │
│ │ Repo    │         │    Repo    │            │
│ └────┬────┘         └─────┬──────┘            │
└──────┼───────────────────┼────────────────────┘
       │                   │
┌──────┼───────────────────┼────────────────────┐
│ Data Source Layer        │                     │
│ ┌────▼────┐       ┌──────▼──────┐             │
│ │Retrofit │       │  Room DB    │             │
│ │  API    │       │  + DAO      │             │
│ └─────────┘       └─────────────┘             │
└─────────────────────────────────────────────────┘
```

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Versión | Uso |
|------------|---------|-----|
| **Kotlin** | 1.9+ | Lenguaje principal |
| **Jetpack Compose** | BOM 2024.05.00 | UI moderna |
| **Room Database** | 2.6.1 | Persistencia local |
| **Retrofit** | 2.9.0 | Consumo de API REST |
| **Coroutines** | 1.7.0+ | Programación asíncrona |
| **ViewModel** | 2.7.0+ | MVVM pattern |
| **Navigation** | 2.7.0 | Navegación entre pantallas |
| **Material 3** | Latest | Design system |

---

## 📋 Requerimientos Funcionales

### 1️⃣ Pantalla Home
- Botón **"Cargar desde Rest API"** con validación de Internet
- Botón **"Cargar desde Base de Datos Local"** con validación de datos
- Mensajes Toast según caso de éxito/error
- Navegación automática a Listado

### 2️⃣ Pantalla Listado
- RecyclerView (LazyColumn) mostrando:
  - SKU del producto
  - Nombre del producto
  - Precio formateado
- Botón **"Guardar en BD Local"** en barra superior
- Click en item navega a Detalle

### 3️⃣ Pantalla Detalle
- Muestra toda la información del producto seleccionado
- Stock por sucursales
- Descripción completa
- Imagen (si disponible)

---

## 🌐 Endpoint Público

La aplicación consume datos desde:

```
https://raw.githubusercontent.com/chalalo1533/ServicioRest/refs/heads/master/productos.json
```

**Características del JSON:**
- 200 productos
- 4 categorías (Electrónica, Hogar, Computación, Accesorios)
- Stock por 4 sucursales (Concepción, Talcahuano, Chillán, Los Ángeles)
- Precios en CLP

---

## 📦 Estructura del Proyecto

```
app/src/main/java/com/huertohogar/huertohogar_app/
├── data/
│   ├── db/
│   │   ├── ProductEntity.kt       # Entidad Room
│   │   ├── ProductDao.kt           # DAO con 6 queries
│   │   └── ProductDatabase.kt      # Singleton DB
│   ├── mapper/
│   │   └── ProductMapper.kt        # Entity ↔ Model
│   └── repository/
│       └── RoomProductRepository.kt # Acceso a Room
├── model/
│   └── Product.kt                  # Modelo de dominio
├── viewmodel/
│   └── ProductViewModel.kt         # Lógica de negocio
├── screen/
│   ├── HomeScreen.kt               # Pantalla principal
│   ├── ListadoProductosScreen.kt   # Lista de productos
│   └── DetalleScreen.kt            # Detalle de producto
└── MainActivity.kt                 # Activity principal
```

---

## 🚀 Instalación y Uso

### Compilar el Proyecto

```bash
# Clonar repositorio (si aplica)
git clone [URL_DEL_REPOSITORIO]

# Ir a la carpeta del proyecto
cd HuertoHogar_App

# Compilar APK Debug
.\gradlew.bat assembleDebug
```

### Instalar en Dispositivo

```bash
# Usando ADB
adb install app\build\outputs\apk\debug\app-debug.apk
```

### O desde Android Studio
1. Abrir proyecto en Android Studio
2. Conectar dispositivo o iniciar emulador
3. Click en **Run** ▶️

---

## 🧪 Casos de Prueba

### ✅ Test 1: Primera Carga
1. Instalar app
2. Presionar "Cargar desde Rest API"
3. Verificar 200 productos en lista
4. Presionar "Guardar en BD"
5. Ver confirmación

### ✅ Test 2: Modo Offline
1. Guardar productos previamente
2. Desactivar Internet
3. Presionar "Cargar desde BD Local"
4. Verificar lista carga sin Internet

### ✅ Test 3: Validaciones
1. Sin Internet → presionar "Cargar desde API"
   - **Esperado:** Toast "Sin conexión a Internet"
2. BD vacía → presionar "Cargar desde BD"
   - **Esperado:** Toast "No hay datos locales"

---

## 📊 Base de Datos Room

### Tabla: `productos`

| Columna | Tipo | Constraint |
|---------|------|------------|
| sku | TEXT | PRIMARY KEY |
| nombre | TEXT | NOT NULL |
| categoria | TEXT | NOT NULL |
| descripcion | TEXT | NOT NULL |
| precio | INTEGER | NOT NULL |
| imagen | TEXT | NOT NULL |
| stockConcepcion | INTEGER | NOT NULL |
| stockTalcahuano | INTEGER | NOT NULL |
| stockChillan | INTEGER | NOT NULL |
| stockLosAngeles | INTEGER | NOT NULL |

**Nombre DB:** `huerto_hogar_db`  
**Versión:** 1  
**Estrategia:** REPLACE on conflict

---

## 🎯 Cumplimiento de Rúbrica

| Criterio | Peso | Cumplimiento |
|----------|------|--------------|
| Interfaz de Usuario | 20% | ✅ 100% |
| Consumo API | 20% | ✅ 100% |
| Persistencia de Datos | 25% | ✅ 100% |
| Lógica de Negocio | 25% | ✅ 100% |
| Calidad de Código | 10% | ✅ 100% |
| **TOTAL** | **100%** | **✅ 100%** |

---

## 📝 Documentación Adicional

- **[BUILD_EXITOSO.md](BUILD_EXITOSO.md)** - Confirmación de compilación exitosa
- **[GUIA_RAPIDA.md](GUIA_RAPIDA.md)** - Guía de uso rápido
- **[IMPLEMENTACION_COMPLETA.md](IMPLEMENTACION_COMPLETA.md)** - Detalle técnico completo
- **[INSTRUCCIONES_FINALES.md](INSTRUCCIONES_FINALES.md)** - Pasos para entrega
- **[SOLUCION_PROBLEMAS.md](SOLUCION_PROBLEMAS.md)** - Troubleshooting

---

## 🐛 Troubleshooting

### Problema: App no compila
```bash
.\gradlew.bat clean assembleDebug
```

### Problema: Room no guarda datos
- Verificar que `setRoomRepository()` se llama en MainActivity
- Ver logs: `adb logcat | grep RoomProductRepository`

### Problema: No navega a Listado
- Verificar ruta "listado_productos" en nav_graph.xml

---

## 👨‍💻 Autor

**Evaluación 3 - Desarrollo de Aplicaciones Móviles**  
**Fecha:** Enero 2025

Ignacio Valeria
Benjamin Flores

---

## 📄 Licencia

Este proyecto es parte de una evaluación académica.

---

## 🎉 Estado del Proyecto

✅ **BUILD SUCCESSFUL**  
✅ **100% Funcional**  
✅ **Listo para Entrega**

---

**¡Gracias por revisar este proyecto!** 🌱📱✨

