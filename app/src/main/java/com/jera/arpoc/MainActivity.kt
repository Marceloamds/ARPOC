package com.jera.arpoc

import android.os.Bundle
import android.view.View
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
            .setSource(this, R.raw.tiger)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept {
                customRenderable = it
            }
            .exceptionally {
                Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG).show()
                null
            }

        val arFragment = supportFragmentManager.findFragmentById(R.id.ux_fragment) as ArFragment
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            if (customRenderable == null) {
                return@setOnTapArPlaneListener
            }
            binding.linear.visibility = View.GONE
            val anchorNode = AnchorNode(hitResult.createAnchor())
            anchorNode.setParent(arFragment.arSceneView.scene)
            TranslatableNode().apply {
                setParent(anchorNode)
                renderable = customRenderable
            }
        }
    }

    companion object {
        const val COL_NUM = 3
        const val ROW_NUM = 4
    }
}