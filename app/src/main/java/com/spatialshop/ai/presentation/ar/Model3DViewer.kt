package com.spatialshop.ai.presentation.ar

import android.graphics.PixelFormat
import android.view.ViewGroup
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.spatialshop.ai.presentation.theme.SpatialColors
import io.github.sceneview.SceneView
import io.github.sceneview.node.ModelNode

@Composable
fun Model3DViewer(
    modelPath: String,
    modifier: Modifier = Modifier
) {
    var isLoading by remember(modelPath) { mutableStateOf(true) }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AndroidView(
            factory = { context ->
                SceneView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setZOrderMediaOverlay(true)
                    holder.setFormat(PixelFormat.TRANSLUCENT)

                    modelLoader.loadModelInstanceAsync(modelPath) { instance ->
                        if (instance != null) {
                            clearChildNodes()
                            val node = ModelNode(modelInstance = instance).apply {
                                centerOrigin()
                                scaleToUnitCube(1.0f)
                            }
                            addChildNode(node)
                            isLoading = false
                        }
                    }
                }
            },
            update = { sceneView ->
                sceneView.clearChildNodes()
                isLoading = true
                sceneView.modelLoader.loadModelInstanceAsync(modelPath) { instance ->
                    if (instance != null) {
                        val node = ModelNode(modelInstance = instance).apply {
                            centerOrigin()
                            scaleToUnitCube(1.0f)
                        }
                        sceneView.addChildNode(node)
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            CircularProgressIndicator(
                color = SpatialColors.Primary,
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp
            )
        }
    }
}
