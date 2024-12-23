package com.example.astteroidgame

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import java.util.*

class GameManager(
    private val gameGrid: GridLayout,
    private val livesLayout: LinearLayoutCompat,
    private val context: Context
) {

    private val ROWS = 6
    private val NUM_COLS = 3

    private var playerColumn = 1
    private var obstacleList: MutableList<Astroid> = mutableListOf()
    private var lives = 3

    private var asteroidCreationCounter = 0
    private val handler = Handler(Looper.getMainLooper())


    fun initializeGame() {
        playerColumn = 1
        obstacleList.clear()
        lives = 3

        handler.postDelayed(object : Runnable {
            override fun run() {
                updateGame()
                handler.postDelayed(this, 1000)
            }
        }, 1000)
    }


    private fun updateGame() {
        updateObstacles()
        if (asteroidCreationCounter % 2 == 0) addAstroid()
        updateGridImages()
        checkCollision()
        asteroidCreationCounter++
    }


   private fun addAstroid() {
        val randomColumn = Random().nextInt(NUM_COLS)
        obstacleList.add(Astroid(0, randomColumn))
    }

    private fun clearGrid() {
        for (i in 0 until gameGrid.childCount) {
            val cellImageView = gameGrid.getChildAt(i) as ImageView
            if (cellImageView.tag != "player") {
                cellImageView.setImageResource(0)
            }
        }
    }

    private fun updateGridImages() {
        clearGrid()
        for (astroid in obstacleList) {
            val cellId = context.resources.getIdentifier(
                "cell${astroid.row * NUM_COLS + astroid.col + 1}", "id", context.packageName
            )
            val astroidImageView = (context as Activity).findViewById<ImageView>(cellId)
            astroidImageView.setImageResource(R.drawable.obstacle)
        }
    }


    private fun updatePlayerImage() {
        clearPlayerImages()
        val playerId = context.resources.getIdentifier(
            "player${playerColumn + 1}", "id", context.packageName
        )
        val cellImageView = (context as Activity).findViewById<ImageView>(playerId)
        cellImageView.visibility = View.VISIBLE
    }


    private fun clearPlayerImages() {
        for (c in 0 until NUM_COLS) {
            val playerId = context.resources.getIdentifier(
                "player${c + 1}", "id", context.packageName
            )
            val cellImageView = (context as Activity).findViewById<ImageView>(playerId)
            cellImageView.visibility = View.INVISIBLE
        }
    }


    private fun checkCollision() {
        val obstaclesCopy = ArrayList(obstacleList)
        obstaclesCopy.forEach { astroid ->
            if (astroid.row == ROWS - 1 && astroid.col == playerColumn) {
                collision()
            }
        }
    }


    private fun collision() {
        clearObstacleList()
        lives--
        updateLivesUI()
        vibrate() // רטט
        showToast("BOOM !!! Lives remaining: $lives")
    }


    private fun updateLivesUI() {
        when (lives) {
            2 -> livesLayout.getChildAt(0).visibility = View.INVISIBLE
            1 -> livesLayout.getChildAt(1).visibility = View.INVISIBLE
            0 -> livesLayout.getChildAt(2).visibility = View.INVISIBLE
        }
    }


    private fun vibrate() {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    private fun updateObstacles() {
        obstacleList.removeAll { astroid ->
            if (astroid.row == ROWS - 1) {
                true
            } else {
                astroid.row++
                false
            }
        }
    }


    fun movePlayerRight() {
        if (playerColumn < NUM_COLS - 1) {
            playerColumn++
            checkCollision()
            updatePlayerImage()
        }
    }


    fun movePlayerLeft() {
        if (playerColumn > 0) {
            playerColumn--
            checkCollision()
            updatePlayerImage()
        }
    }

    private fun clearObstacleList() {
        obstacleList.clear()
    }
}




