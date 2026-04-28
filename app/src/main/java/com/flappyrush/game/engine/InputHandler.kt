package com.flappyrush.game.engine

import android.view.MotionEvent
import com.flappyrush.game.objects.Bird

class InputHandler(private val bird: Bird) {

    fun onTouch(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            bird.flap()
            return true
        }
        return false
    }
}
