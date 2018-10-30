package com.kinar.firstopencvapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    private var loaderCallBackMain: LoaderCallbackInterface = LoaderCallbak(this)
    private var actionMode = 0
    var openCvMat: Mat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        actionMode = intent.getIntExtra(HomeActivity.ACTION_MODE, 0)
    }

    override fun onResume() {
        super.onResume()
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, loaderCallBackMain)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.menu.menu_main -> {
            val intent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
            startActivityForResult(intent, SELECT_PHOTO)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SELECT_PHOTO -> {
                if (resultCode == Activity.RESULT_OK) data?.data?.run { proceedSelectPhotoAction(this) }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun proceedSelectPhotoAction(uri: Uri) {

        fun processMeanBluring(selectedImage: Bitmap, src: Mat) {
            Imgproc.blur(src, src, Size(3.0, 3.0))
            val processedBitmap = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(src, processedBitmap)
            ivImage.setImageBitmap(selectedImage)
            ivImageProcessed.setImageBitmap(processedBitmap)
        }

        try {
            contentResolver.openInputStream(uri)?.let { imageStream ->
                val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                openCvMat = Mat(selectedImage.height, selectedImage.width, CvType.CV_8UC4)
                Utils.bitmapToMat(selectedImage, openCvMat)
                when (actionMode) {
                    HomeActivity.MEAN_BLUR -> { openCvMat?.let { processMeanBluring(selectedImage, it) } }
                    else -> Toast.makeText(this, "There is no matching action mode...", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    class LoaderCallbak(context: Context) : BaseLoaderCallback(context) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                }
                else -> super.onManagerConnected(status)
            }
        }
    }

    companion object {
        private const val SELECT_PHOTO = 123456
    }
}
