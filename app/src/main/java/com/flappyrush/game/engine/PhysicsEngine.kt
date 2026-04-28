package com.flappyrush.game.engine

import com.flappyrush.game.objects.Bird
import com.flappyrush.game.objects.Background
import com.flappyrush.game.objects.PipeManager
import com.flappyrush.utils.Constants

class PhysicsEngine {

    fun update(bird: Bird, pipeManager: PipeManager, background: Background, deltaSeconds: Float): CollisionResult {
        bird.update(deltaSeconds)
        pipeManager.update(deltaSeconds)
        background.update(pipeManager.currentSpeed, deltaSeconds)

        return when {
            background.isGroundCollision(bird.y, Constants.BIRD_HEIGHT / 2f) -> CollisionResult.GROUND
            bird.y - Constants.BIRD_HEIGHT / 2f < 0f -> CollisionResult.CEILING
            pipeManager.checkCollision(bird) -> CollisionResult.PIPE
            else -> CollisionResult.NONE
        }
    }

    enum class CollisionResult { NONE, PIPE, GROUND, CEILING }
}
