package com.jera.arpoc

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.jera.arpoc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setupUi()
    }

    private var customRenderable: ModelRenderable? = null

    private fun setupUi() {
        ModelRenderable.builder()
            .setSource(
                this,
                R.raw.out
            )
            .setIsFilamentGltf(true)
            .build()
            .thenAccept {
                customRenderable = it
                customRenderable?.isShadowCaster = false
                customRenderable?.isShadowReceiver = false
            }
            .exceptionally {
                Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG).show()
                null
            }
        val arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        arFragment.arSceneView.isLightDirectionUpdateEnabled = false
        arFragment.arSceneView.isLightEstimationEnabled = false
        arFragment.arSceneView.planeRenderer.isShadowReceiver = false
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->

            if (customRenderable == null) {
                return@setOnTapArPlaneListener
            }

            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(arFragment.arSceneView.scene)
            TranslatableNode().apply {
                setParent(anchorNode)
                addOffset(0f, 2f, -5.2f)
                renderable = customRenderable
            }
        }
    }

    companion object {
        const val COL_NUM = 3
        const val ROW_NUM = 4
    }
}