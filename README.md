# FURN-AR (Spatial Shopping AI)

An AI-powered Augmented Reality (AR) spatial shopping and interior layout application built natively for Android using Jetpack Compose, Material 3, and CameraX.

FURN-AR allows users to visualize, place, scale, rotate, and interact with 3D furniture models directly in their living spaces with real-time room dimension fitting and spatial clearance validation.

---

## 🌟 Key Features

- **Live Camera AR Viewport**: Real-time back camera stream powered by CameraX with perspective room floor grid tracking.
- **3D Spatial Furniture Placement**: Direct tap-to-place interactions with true-to-scale 3D furniture models (`.glb`).
- **Intuitive Touch Gestures**:
  - **Drag**: Reposition furniture across the floor plane.
  - **Twist / Rotate**: Rotate orientation by degrees or use quick 45° increment controls.
  - **Pinch**: Rescale furniture with 100% True-Scale lock option.
- **Spatial Fit & Clearance Engine**: Real-time room clearance calculations (Front, Back, Left, Right distances) and fit restriction warnings.
- **Multi-Item Room Stacking**: Place multiple furniture items in the same AR session to evaluate complete room sets.
- **Catalog & Categories**: Comprehensive catalog across Living Room, Bedroom, Dining & Kitchen, and Office.
- **Color Variant Customization**: Switch furniture fabrics and wood finishes dynamically in AR.
- **Design Snapshots & Bookmarks**: Capture AR scene photos to the gallery and save custom room configurations.
- **Integrated Shopping Cart**: Directly add placed room setups to the cart.

---

## 🛠️ Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Toolkit**: Jetpack Compose & Material 3
- **Camera & AR**: AndroidX CameraX (Core, Camera2, Lifecycle, View)
- **Dependency Injection**: Dagger Hilt
- **Asynchronous / Reactive**: Kotlin Coroutines & StateFlow
- **Image Loading**: Coil Compose
- **Architecture**: Clean Architecture + MVI/MVVM pattern (Presentation, Domain, Data)

---

## 🚀 Building & Running

### Prerequisites
- Android Studio Ladybug / Koala or later
- JDK 17
- Android SDK 34 (compileSdk / targetSdk: 34, minSdk: 26)

### Build Debug APK
```bash
# Set JDK 17 if not set globally
./gradlew assembleDebug
```
The compiled APK will be located at:
```
app/build/outputs/apk/debug/app-debug.apk
```

---

## 📁 Repository Structure

```
├── app/
│   ├── src/main/
│   │   ├── assets/models/         # 3D GLB Furniture assets
│   │   ├── java/com/spatialshop/ai/
│   │   │   ├── app/               # Application & Hilt entry point
│   │   │   ├── core/              # Theme, Design System, Utils
│   │   │   ├── data/              # Repositories & Sample Data
│   │   │   ├── domain/            # Models, UseCases, Fit Evaluator
│   │   │   └── presentation/      # Compose Screens & ViewModels
│   │   │       ├── ar/            # AR Studio & Camera Preview
│   │   │       ├── home/          # Home discovery & banners
│   │   │       ├── productdetail/ # Product specifications & AR trigger
│   │   │       └── ...
│   │   └── AndroidManifest.xml
├── 3D Furniture Assets/           # Source 3D GLB models
├── build.gradle.kts
└── settings.gradle.kts
```
