package com.example.getrealpathfile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.onimur.handlepathoz.HandlePathOz
import br.com.onimur.handlepathoz.HandlePathOzListener
import br.com.onimur.handlepathoz.model.PathOz
import com.example.getrealpathfile.databinding.ActivityMainBinding
import kotlinx.coroutines.FlowPreview


class MainActivity : AppCompatActivity(R.layout.activity_main),HandlePathOzListener.SingleUri {
    companion object{
        const val IMAGE_REQUEST_CODE=102
    }
    lateinit var binding: ActivityMainBinding
    lateinit var handlePath:HandlePathOz
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handlePath = HandlePathOz(this,this)

        binding.getImgBtn.setOnClickListener{
            openImg()
        }
    }

    override fun onRequestHandlePathOz(pathOz: PathOz, tr: Throwable?) {
        Toast.makeText(this,"real path is: ${pathOz.path}",Toast.LENGTH_LONG).show()
        Log.d("???????","RealPathIsHere :: ${pathOz.path}")
        handlePath.deleteTemporaryFiles()
        tr?.let {
            Toast.makeText(this@MainActivity,"error : $it",Toast.LENGTH_LONG).show()
        }
    }

    private fun openImg(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    @OptIn(FlowPreview::class)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            data?.data?.also { uri->
                handlePath.getRealPath(uri)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handlePath.onDestroy()
    }

}