package wwan.a3

import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.input.KeyCode
import javafx.scene.layout.*
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.stage.Stage

var SCORE = 0
var LIVES = 3
var LEVEL = 1
var scoreLabel = Label("Score: $SCORE").apply {
    layoutX = 50.0
    font = Font.font("Roboto Mono", 25.0)
    style = "-fx-text-fill: white;"
}
var livesLabel = Label("Lives: $LIVES").apply {
    layoutX = 1000.0
    font = Font.font("Roboto Mono", 25.0)
    style = "-fx-text-fill: white;"
}
var levelLabel = Label("Level: $LEVEL").apply {
    layoutX = 1150.0
    font = Font.font("Roboto Mono", 25.0)
    style = "-fx-text-fill: white;"
}
const val P = 5
var ENEMY_SPEED = 0.5
var ENEMY_BULLET_SPEED = 5.0
var COUNT_ENEMY_BULLETS = 0

class SpaceInvaders : Application() {
    private var player = Player()
    private var enemies = mutableListOf<Enemy>()
    private val home = initHomePage()
    private val status = Pane(scoreLabel, livesLabel, levelLabel)
    private var gamePane = Pane()
    private val classLoader = Thread.currentThread().contextClassLoader
    private val game = BorderPane().apply {
        top = status
        center = gamePane
        background = Background(BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY))
    }
    private var pStage = Stage()
    private val timer = object : AnimationTimer() {
        override fun handle(now: Long) {
            update()
            check()
        }
    }
    private val gameScene = Scene(game,1280.0, 800.0)
    private val homeScene = Scene(home,1280.0, 800.0)
    override fun start(stage: Stage) {
        pStage = stage
        gameScene.apply {
            setOnKeyReleased { _ ->
                player.direction = ""
            }
        }
        homeScene.apply {
            setOnKeyPressed { e ->
                if (e.code.equals(KeyCode.ENTER)) {
                    initGamePane(1)
                    stage.scene = gameScene
                    startGame()
                } else if (e.code.equals(KeyCode.Q)) {
                    Platform.exit()
                } else if (e.code.equals(KeyCode.DIGIT1)) {
                    initGamePane(1)
                    stage.scene = gameScene
                    startGame()
                } else if (e.code.equals(KeyCode.DIGIT2)) {
                    initGamePane(2)
                    stage.scene = gameScene
                    startGame()
                } else if (e.code.equals(KeyCode.DIGIT3)) {
                    initGamePane(3)
                    stage.scene = gameScene
                    startGame()
                }
            }
        }
        stage.title = "Space Invaders"
        stage.scene = homeScene
        stage.isResizable = false
        stage.show()
    }
    private fun initHomePage(): Pane {
        val logo = ImageView("logo.png").apply {
            layoutX = 385.0
            layoutY = 10.0
        }
        val t1 = Label("Instructions").apply {
            font = Font.font("Roboto Mono", FontWeight.BOLD, 40.0)
            layoutX = 530.0
            layoutY = 350.0
        }
        val t2 = Label("ENTER - Start Game").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 540.0
            layoutY = 450.0
        }
        val t3 = Label("A or < - Move ship to the left").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 510.0
            layoutY = 490.0
        }
        val t4 = Label("D or > - Move ship to the right").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 505.0
            layoutY = 530.0
        }
        val t5 = Label("SPACE - Fire!").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 565.0
            layoutY = 570.0
        }
        val t6 = Label("Q - Quit Game").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 560.0
            layoutY = 610.0
        }
        val t7 = Label("1 or 2 or 3 - Start Game at specific level").apply {
            font = Font.font("Roboto Mono", 20.0)
            layoutX = 470.0
            layoutY = 650.0
        }
        val info = Label("Implemented by Wilson Wan, Student ID: 20876446").apply {
            font = Font.font("Roboto Mono", 15.0)
            layoutX = 470.0
            layoutY = 780.0
        }
        return Pane(logo, t1, t2, t3, t4, t5, t6, t7, info).apply {
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
        }
    }
    private fun initGamePane(level: Int) {
        gameScene.apply {
            setOnKeyPressed { e ->
                if (e.code.equals(KeyCode.A) || e.code.equals(KeyCode.LEFT)) {
                    player.direction = "L"
                } else if (e.code.equals(KeyCode.D) || e.code.equals(KeyCode.RIGHT)) {
                    player.direction = "R"
                } else if (e.code.equals(KeyCode.Q)) {
                    Platform.exit()
                } else if (e.code.equals(KeyCode.SPACE)) {
                    if (player.shoot()) gamePane.children.add(player.bullets[player.bullets.size - 1].view)
                } else {

                }
            }
        }
        if (level == 1) {
            ENEMY_SPEED = 0.5
            ENEMY_BULLET_SPEED = 5.0
        } else if (level == 2) {
            ENEMY_SPEED = 0.65
            ENEMY_BULLET_SPEED = 6.0
        }  else {
            ENEMY_SPEED = 0.8
            ENEMY_BULLET_SPEED = 7.0
        }
        COUNT_ENEMY_BULLETS = 0
        LEVEL = level
        SCORE = 0
        LIVES = 3
        player = Player()
        enemies = mutableListOf<Enemy>()
        scoreLabel.text = "Score: $SCORE"
        livesLabel.text = "Lives: $LIVES"
        levelLabel.text = "Level: $LEVEL"
        for (i in 0..10) {
            val curX = 60.0 * i
            var curY = 50.0
            enemies.add(Enemy(curX, curY, 3))

            curY += 50
            enemies.add(Enemy(curX, curY, 2))

            curY += 50
            enemies.add(Enemy(curX, curY, 2))

            curY += 50
            enemies.add(Enemy(curX, curY, 1))

            curY += 50
            enemies.add(Enemy(curX, curY, 1))
        }
        gamePane.children.clear()
        gamePane.apply {
            children.add(player.view)
            enemies.forEach { this.children.add(it.view) }
            prefWidth = 1000.0
        }
    }
    private fun startGame() {
        timer.start()
    }
    private fun update() {
        player.update(enemies, gamePane)
        if (enemies.size != 0) {
            val chooseShip = (0 until enemies.size).random()
            if (reachEdge()) {
                if (!descend()) lose()
                enemies[chooseShip].shoot(gamePane)
                MediaPlayer(Media(classLoader.getResource("fastinvader1.wav")?.toString())).play()
                COUNT_ENEMY_BULLETS++
                ENEMY_SPEED *= -1
            } else {
                val chooseProb = (0 until 2000).random()
                if (COUNT_ENEMY_BULLETS < 3 && chooseProb < P * LEVEL) {
                    enemies[chooseShip].shoot(gamePane)
                    COUNT_ENEMY_BULLETS++
                    when (LEVEL) {
                        1 -> MediaPlayer(Media(classLoader.getResource("fastinvader2.wav")?.toString())).play()
                        2 -> MediaPlayer(Media(classLoader.getResource("fastinvader3.wav")?.toString())).play()
                        3 -> MediaPlayer(Media(classLoader.getResource("fastinvader4.wav")?.toString())).play()
                    }
                }
                for (i in 0 until enemies.size) {
                    enemies[i].view.layoutX += ENEMY_SPEED
                }
            }
            for (i in 0 until enemies.size) {
                enemies[i].update(player, gamePane)
            }
        }
    }
    private fun descend(): Boolean {
        for (enemy in enemies) {
            enemy.view.layoutY += 25
            if (enemy.view.layoutY > player.view.layoutY) {
                return false
            }
        }
        return true
    }
    private fun reachEdge(): Boolean {
        for (enemy in enemies) {
            if ( (enemy.view.layoutX > 1150 && ENEMY_SPEED > 0) || (enemy.view.layoutX < 0 && ENEMY_SPEED < 0) ) {
                return true
            }
        }
        return false
    }
    private fun check() {
        if (LIVES == 0) lose()
        else if (enemies.isEmpty()) {
            if (LEVEL < 3) nextLevel()
            else win()
        }
    }
    private fun lose() {
        timer.stop()
        val title = Label("GAME OVER!").apply {
            layoutX = 185.0
            layoutY = 20.0
            font = Font.font("Roboto Mono", 40.0)
        }
        val info = Label("You Failed at Level $LEVEL").apply {
            layoutX = 185.0
            layoutY = 100.0
            font = Font.font("Roboto Mono", 25.0)
        }
        var score = Label("Final Score: $SCORE").apply {
            layoutX = 215.0
            layoutY = 150.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t1 = Label("ENTER - Start New Game").apply {
            layoutX = 170.0
            layoutY = 200.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t2 = Label("I - Back to Instruction").apply {
            layoutX = 195.0
            layoutY = 250.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t3 = Label("Q - Quit Game").apply {
            layoutX = 225.0
            layoutY = 300.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t4 = Label("1 or 2 or 3 - Start Game at specific level").apply {
            layoutX = 105.0
            layoutY = 350.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val message = Pane(title, info, score, t1, t2, t3, t4).apply {
            layoutX = 350.0
            layoutY = 200.0
            prefWidth = 595.0
            prefHeight = 400.0
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
        }
        gamePane.children.add(message)
        gameScene.apply {
            setOnKeyPressed { e ->
                if (e.code.equals(KeyCode.ENTER)) {
                    initGamePane(LEVEL)
                    startGame()
                } else if (e.code.equals(KeyCode.I)) {
                    pStage.scene = homeScene
                } else if (e.code.equals(KeyCode.Q)) {
                    Platform.exit()
                } else if (e.code.equals(KeyCode.DIGIT1)) {
                    initGamePane(1)
                    startGame()
                } else if (e.code == KeyCode.DIGIT2) {
                    initGamePane(2)
                    startGame()
                } else if (e.code == KeyCode.DIGIT3) {
                    initGamePane(3)
                    startGame()
                } else {

                }
            }
        }
    }
    private fun nextLevel() {
        timer.stop()
        val title = Label("WELL DONE!").apply {
            layoutX = 185.0
            layoutY = 20.0
            font = Font.font("Roboto Mono", 40.0)
        }
        val info = Label("You Completed Level $LEVEL").apply {
            layoutX = 185.0
            layoutY = 100.0
            font = Font.font("Roboto Mono", 25.0)
        }
        var score = Label("Current Score: $SCORE").apply {
            layoutX = 200.0
            layoutY = 150.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t1 = Label("ENTER - Continue with Next Level").apply {
            layoutX = 120.0
            layoutY = 200.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t2 = Label("I - Back to Instruction").apply {
            layoutX = 195.0
            layoutY = 250.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t3 = Label("Q - Quit Game").apply {
            layoutX = 225.0
            layoutY = 300.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val message = Pane(title, info, score, t1, t2, t3).apply {
            layoutX = 350.0
            layoutY = 200.0
            prefWidth = 595.0
            prefHeight = 400.0
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
        }
        gamePane.children.add(message)
        val curScore = SCORE
        gameScene.apply {
            setOnKeyPressed { e ->
                if (e.code.equals(KeyCode.ENTER)) {
                    initGamePane(LEVEL + 1)
                    SCORE = curScore
                    scoreLabel.text = "Score: $SCORE"
                    startGame()
                } else if (e.code.equals(KeyCode.I)) {
                    pStage.scene = homeScene
                } else if (e.code.equals(KeyCode.Q)) {
                    Platform.exit()
                } else {

                }
            }
        }
    }
    private fun win() {
        timer.stop()
        val title = Label("CONGRATULATIONS!").apply {
            layoutX = 100.0
            layoutY = 20.0
            font = Font.font("Roboto Mono", 40.0)
            style = "-fx-text-fill: red;"
        }
        val info = Label("You Completed All Three Levels").apply {
            layoutX = 125.0
            layoutY = 100.0
            font = Font.font("Roboto Mono", 25.0)
        }
        var score = Label("Final Score: $SCORE").apply {
            layoutX = 210.0
            layoutY = 150.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t1 = Label("ENTER - Start New Game").apply {
            layoutX = 170.0
            layoutY = 200.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t2 = Label("I - Back to Instruction").apply {
            layoutX = 195.0
            layoutY = 250.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t3 = Label("Q - Quit Game").apply {
            layoutX = 225.0
            layoutY = 300.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val t4 = Label("1 or 2 or 3 - Start Game at specific level").apply {
            layoutX = 105.0
            layoutY = 350.0
            font = Font.font("Roboto Mono", 25.0)
        }
        val message = Pane(title, info, score, t1, t2, t3, t4).apply {
            layoutX = 350.0
            layoutY = 200.0
            prefWidth = 595.0
            prefHeight = 400.0
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
        }
        gamePane.children.add(message)
        gameScene.apply {
            setOnKeyPressed { e ->
                if (e.code.equals(KeyCode.ENTER)) {
                    initGamePane(LEVEL)
                    startGame()
                } else if (e.code.equals(KeyCode.I)) {
                    pStage.scene = homeScene
                } else if (e.code.equals(KeyCode.Q)) {
                    Platform.exit()
                } else if (e.code.equals(KeyCode.DIGIT1)) {
                    initGamePane(1)
                    startGame()
                } else if (e.code == KeyCode.DIGIT2) {
                    initGamePane(2)
                    startGame()
                } else if (e.code == KeyCode.DIGIT3) {
                    initGamePane(3)
                    startGame()
                } else {

                }
            }
        }
    }
}
