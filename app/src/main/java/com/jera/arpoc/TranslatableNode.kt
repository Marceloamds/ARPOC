/*
 * Copyright (c) 2018 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish, 
 * distribute, sublicense, create a derivative work, and/or sell copies of the 
 * Software in any work that is designed, intended, or marketed for pedagogical or 
 * instructional purposes related to programming, coding, application development, 
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works, 
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jera.arpoc

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3

class TranslatableNode : Node() {

  var position: DroidPosition = DroidPosition.DOWN

  fun addOffset(x: Float = 0F, y: Float = 0F, z: Float = 0F) {
    val posX = localPosition.x + x
    val posY = localPosition.y + y
    val posZ = localPosition.z + z

    localPosition = Vector3(posX, posY, posZ)
  }

  fun pullUp() {
    if (position != DroidPosition.MOVING_UP && position != DroidPosition.UP) {
      animatePullUp()
    }
  }

  fun pullDown() {
    if (position != DroidPosition.MOVING_DOWN && position != DroidPosition.DOWN) {
      animatePullDown()
    }
  }

  private fun localPositionAnimator(vararg values: Any?): ObjectAnimator {
    return ObjectAnimator().apply {
      target = this@TranslatableNode
      name = "localPosition"
      duration = 250
      interpolator = LinearInterpolator()

      setAutoCancel(true)
      setObjectValues(*values)
      setEvaluator(VectorEvaluator())
    }
  }

  private fun animatePullUp() {
    val low = Vector3(localPosition)
    val high = Vector3(localPosition).apply { y = +.4F }

    val animation = localPositionAnimator(low, high)

    animation.addListener(object : Animator.AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) {}
      override fun onAnimationEnd(animation: Animator?) {
        position = DroidPosition.UP
      }
      override fun onAnimationCancel(animation: Animator?) {}
      override fun onAnimationStart(animation: Animator?) {
        position = DroidPosition.MOVING_UP
      }
    })

    animation.start()
  }

  private fun animatePullDown() {
    val low = Vector3(localPosition).apply { y = 0F }
    val high = Vector3(localPosition)

    val animation = localPositionAnimator(high, low)

    animation.addListener(object : Animator.AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) {}
      override fun onAnimationEnd(animation: Animator?) {
        position = DroidPosition.DOWN
      }
      override fun onAnimationCancel(animation: Animator?) {}
      override fun onAnimationStart(animation: Animator?) {
        position = DroidPosition.MOVING_DOWN
      }
    })

    animation.start()
  }
}