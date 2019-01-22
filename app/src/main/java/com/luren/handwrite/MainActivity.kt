package com.luren.handwrite

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        back.setOnClickListener { writer.back() }
        clear.setOnClickListener { writer.clear() }
        save.setOnClickListener {
            ivPreView.setImageBitmap(writer.drawingCache )
        }
    }
}
