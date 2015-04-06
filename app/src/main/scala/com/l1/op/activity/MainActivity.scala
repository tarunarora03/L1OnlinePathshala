package com.l1.op.activity

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import com.l1.op.R

/**
 * Created by Tarun on 4/5/2015.
 */
class MainActivity extends Activity {

  override def onCreate(savedInstanceState: Bundle) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    val extras: Bundle = getIntent().getExtras()
    val userName = extras.getString("username")
    val textView = findViewById(R.id.username).asInstanceOf[TextView]
    textView.setText(userName)
  }
}
