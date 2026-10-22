# Spatial Shopping AI - ProGuard Rules

# Keep data classes for Gson serialization
-keepclassmembers class com.spatialshop.ai.data.** { *; }
-keepclassmembers class com.spatialshop.ai.domain.model.** { *; }

# ARCore
-keep class com.google.ar.** { *; }

# SceneView
-keep class io.github.sceneview.** { *; }

# MediaPipe
-keep class com.google.mediapipe.** { *; }

# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Compose
-dontwarn androidx.compose.**

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
