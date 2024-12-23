package com.example.astteroidgame


import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Initialize the GameManager
        val gameManager = GameManager(
            findViewById(R.id.gameGrid),
            findViewById(R.id.lives_layout),
            this
        )

        gameManager.initializeGame()

        // Set onClickListeners for the buttons
        findViewById<android.widget.Button>(R.id.btnLeft).setOnClickListener {
            gameManager.movePlayerLeft()
        }
        findViewById<android.widget.Button>(R.id.btnRight).setOnClickListener {
            gameManager.movePlayerRight()
        }
    }
}

