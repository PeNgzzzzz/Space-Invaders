package wwan.a3

import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

class Enemy(x: Double, y: Double, type: Int) {
    val view = ImageView()
    val bullets = mutableListOf<EnemyBullet>()
    val type = type
    val classLoader = Thread.currentThread().contextClassLoader
    init {
        if (type == 1) {
            view.image = Image("enemy1.png")
            view.scaleX = 0.43
            view.scaleY = 0.5
            view.layoutX = x
            view.layoutY = y
        } else if (type == 2) {
            view.image = Image("enemy2.png")
            view.scaleX = 0.5
            view.scaleY = 0.5
            view.layoutX = x + 5
            view.layoutY = y
        } else {
            view.image = Image("enemy3.png")
            view.scaleX = 0.6
            view.scaleY = 0.45
            view.layoutX = x + 15
            view.layoutY = y
        }
    }
    public fun update(p: Player, gamePane: Pane) {
        var it = bullets.iterator()
        while (it.hasNext()) {
            val bullet = it.next()
            if (!bullet.update()) {
                gamePane.children.remove(bullet.view)
                it.remove()
                COUNT_ENEMY_BULLETS--
            }
        }
        it = bullets.iterator()
        while (it.hasNext()) {
            val bullet = it.next()
            if (p.isHit(bullet.view.layoutX, bullet.view.layoutY)) {
                gamePane.children.remove(bullet.view)
                p.view.layoutX = kotlin.random.Random.nextDouble(0.0, 1150.0)
                it.remove()
                MediaPlayer(Media(classLoader.getResource("explosion.wav")?.toString())).play()
                LIVES--
                livesLabel.text = "Lives: $LIVES"
            }
        }
    }
    public fun shoot(gamePane: Pane) {
        val bullet = EnemyBullet(view.layoutX + 50, view.layoutY + 60, type)
        bullets.add(bullet)
        gamePane.children.add(bullet.view)
    }
    public fun isHit(x: Double, y: Double): Boolean {
        return (x > view.layoutX) && (x < view.layoutX + 80) && (y > view.layoutY) && (y < view.layoutY + 50)
    }
}