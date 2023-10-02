package wwan.a3

import javafx.scene.image.ImageView

class PlayerBullet(x: Double, y: Double) {
    val view = ImageView("player_bullet.png").apply {
        scaleX = 0.5
        scaleY = 0.5
        layoutX = x
        layoutY = y
    }
    var speed = 6.0
    fun update(): Boolean {
        view.layoutY -= speed
        return view.layoutY > 0
    }
}