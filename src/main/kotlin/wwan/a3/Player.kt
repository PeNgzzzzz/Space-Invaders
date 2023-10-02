package wwan.a3

import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer

class Player {
    val classLoader = Thread.currentThread().contextClassLoader
    val view = ImageView("player.png")
    var speed = 3.0
    var direction = ""
    val bullets = mutableListOf<PlayerBullet>()
    init {
        view.scaleX = 0.5
        view.scaleY = 0.5
        view.layoutY = 710.0
        view.layoutX = 600.0
    }
    public fun update(enemies: MutableList<Enemy>, gamePane: Pane) {
        if (direction == "L" && view.layoutX > 0.0) {
            view.layoutX -= speed
        } else if (direction == "R" && view.layoutX < 1150.0) {
            view.layoutX += speed
        }
        val it = bullets.iterator()
        while (it.hasNext()) {
            val bullet = it.next()
            if (!bullet.update()) {
                gamePane.children.remove(bullet.view)
                it.remove()
            }
        }
        val it1 = bullets.iterator()
        while (it1.hasNext()) {
            val bullet = it1.next()
            var it2 = enemies.iterator()
            while (it2.hasNext()) {
                val enemy = it2.next()
                if (enemy.isHit(bullet.view.layoutX, bullet.view.layoutY)) {
                    gamePane.children.remove(bullet.view)
                    gamePane.children.remove(enemy.view)
                    enemy.bullets.forEach { gamePane.children.remove(it.view) }
                    it1.remove()
                    it2.remove()
                    MediaPlayer(Media(classLoader.getResource("invaderkilled.wav")?.toString())).play()
                    ENEMY_SPEED *= 1.05
                    SCORE += 10 * LEVEL
                    scoreLabel.text = "Score: $SCORE"
                    break
                }
            }
        }
        for (enemy in enemies) {
            if (enemy.isHit(view.layoutX, view.layoutY)) {
                gamePane.children.remove(enemy.view)
                for (bullet in enemy.bullets) gamePane.children.remove(bullet.view)
                view.layoutX = kotlin.random.Random.nextDouble(0.0, 1150.0)
                enemies.remove(enemy)
                MediaPlayer(Media(classLoader.getResource("explosion.wav")?.toString())).play()
                ENEMY_SPEED *= 1.025
                SCORE += 10 * LEVEL
                LIVES--
                scoreLabel.text = "Score: $SCORE"
                livesLabel.text = "Lives: $LIVES"
                break
            }
        }
    }
    public fun shoot(): Boolean {
        if (bullets.size < 2) {
            val bullet = PlayerBullet(view.layoutX + 55, view.layoutY - 25)
            bullets.add(bullet)
            return true
        }
        return false
    }
    public fun isHit(x: Double, y: Double): Boolean {
        return (x > view.layoutX) && (x < view.layoutX + 80) && (y > view.layoutY) && (y < view.layoutY + 50)
    }
}