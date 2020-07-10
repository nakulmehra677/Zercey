package com.zercey.paint.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.zercey.paint.R
import com.zercey.paint.databinding.ActivityHomeBinding
import com.zercey.paint.interfaces.ObjectListener
import com.zercey.paint.managers.FirebaseAuthManager
import com.zercey.paint.managers.ImageManager
import java.lang.Exception

class HomeActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var currPaint: ImageButton
    private lateinit var imageManager: ImageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
//
        setSupportActionBar(binding.toolbar)

        binding.newBtn.setOnClickListener(this)
        binding.eraseBtn.setOnClickListener(this)
        binding.drawBtn.setOnClickListener(this)
        binding.saveBtn.setOnClickListener(this)


        currPaint = binding.paintColors.getChildAt(0) as ImageButton

        imageManager = ImageManager()

    }


    fun paintClicked(view: View) {
        if (view != currPaint) {
            val color = view.tag.toString()
            binding.drawing.setColor(color)
            currPaint = view as ImageButton
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.draw_btn) {
            binding.drawing.setupDrawing()
        }
        if (v.id == R.id.erase_btn) {
            binding.drawing.setErase(true)
            binding.drawing.setBrushSize(binding.drawing.lastBrushSize)
        }
        if (v.id == R.id.new_btn) {
            binding.drawing.startNew()
        }
        if (v.id == R.id.save_btn) {
            binding.drawing.isDrawingCacheEnabled = true
            binding.drawing.buildDrawingCache()

            showProgressDialog()
            imageManager.uploadImage(
                binding.drawing.drawingCache,
                object : ObjectListener<Boolean> {
                    override fun onGetObject(o: Boolean?) {
                        hideProgressDialog()
                        toast("Drawing uploaded!")
                    }

                    override fun onFail(e: Exception?) {
                        hideProgressDialog()
                        toast("Failed to upload image")
                    }

                })

            binding.drawing.destroyDrawingCache()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.nav_see_all_drawing -> {
                startActivity(Intent(this@HomeActivity, ImageListActivity::class.java))
                true
            }
            R.id.nav_log_out -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        this.let {
            val builder = AlertDialog.Builder(it)

            builder.apply {
                setTitle("Are you sure you want to log out?")
                setPositiveButton(
                    "Log out"
                ) { _, _ ->
                    val firebaseAuthManager = FirebaseAuthManager()
                    firebaseAuthManager.logout()

                    startActivity(Intent(this@HomeActivity, SplashActivity::class.java))
                    finish()

                }
                setNegativeButton(
                    "Cancel"
                ) { _, _ ->
                }
            }

            builder.create()
            builder.show()
        }
    }

}

