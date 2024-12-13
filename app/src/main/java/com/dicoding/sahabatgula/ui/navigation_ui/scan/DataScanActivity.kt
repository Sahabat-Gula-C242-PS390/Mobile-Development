package com.dicoding.sahabatgula.ui.navigation_ui.scan

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.databinding.ActivityDataScanBinding
import com.dicoding.sahabatgula.helper.SharedPreferencesHelper

class DataScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDataScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        val nameScan = intent.getStringExtra("name")
        val karboScan  = intent.getIntExtra("karbo", 0)
        val lemakScan  = intent.getDoubleExtra("lemak", 0.0)
        val gulaScan  = intent.getIntExtra("gula", 0)
        val proteinScan  = intent.getIntExtra("protein", 0)

        binding.productName.text = nameScan
        binding.karboConsume.text = karboScan.toString()
        binding.lemakConsume.text = lemakScan.toString()
        binding.gulaConsume.text = gulaScan.toString()
        binding.proteinConsume.text = proteinScan.toString()

        binding.btnCatatKonsumsi.setOnClickListener {
            nameScan?.let {
                SharedPreferencesHelper.saveScanData(this, nameScan, karboScan, lemakScan, gulaScan, proteinScan)
                val data = SharedPreferencesHelper.getScanData(this)
                Log.d("DataScanActivity", "Data retrieved: $data")
                if(data.isNotEmpty()) {
                    Toast.makeText(this, "$data", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Data gagal disimpan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setActionBar() {
        supportActionBar?.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.white_bg_action_bar))
        supportActionBar?.elevation = 0f
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.apply {
            val textView = TextView(this@DataScanActivity).apply {
                text = "Kembali"
                textSize = 16f
                setTypeface(typeface, Typeface.NORMAL)
                setTextColor(ContextCompat.getColor(context, R.color.black))
                setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                gravity = Gravity.START
            }
            setCustomView(
                textView,
                ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.START
                )

            )
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}