package wwan.a3

import javafx.scene.image.Image
import javafx.scene.image.ImageView

class EnemyBullet(x: Double, y: Double, type: Int) {
    val view = ImageView().apply {
        scaleX = 0.5
        scaleY = 0.5
        layoutX = x
        layoutY = y
    }
    init {
        when (type) {
            1 -> view.image = Image("bullet1.png")
            2 -> view.image = Image("bullet2.png")
            3 -> view.image = Image("bullet3.png")
        }
    }
    fun update(): Boolean {
        view.layoutY += ENEMY_BULLET_SPEED
        return view.layoutY < 750
    }
}