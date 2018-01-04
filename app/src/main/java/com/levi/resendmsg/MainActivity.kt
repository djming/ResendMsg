package com.levi.resendmsg

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val sp = SpHelper(applicationContext)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeButtonText(sp.translate)

        editWrapper.hint = targetNum.hint
        targetNum.setText(sp.target, TextView.BufferType.EDITABLE)

        switchBtn.setOnClickListener({ v -> switchState() })
    }

    private fun switchState() {
        if (targetNum.text.toString().equals("")) {
            Toast.makeText(this, resources.getString(R.string.hint), Toast.LENGTH_SHORT).show()
            return
        }
        sp.translate = !sp.translate
        changeButtonText(sp.translate)
    }

    private fun changeButtonText(state: Boolean) =
            if (state)
                switchBtn.text = resources.getString(R.string.translating)
            else
                switchBtn.text = resources.getString(R.string.startTranslate)
}
