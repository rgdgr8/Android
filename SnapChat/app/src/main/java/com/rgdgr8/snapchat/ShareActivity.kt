package com.rgdgr8.snapchat

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream
import java.net.URL
import java.util.*


class ShareActivity : AppCompatActivity() {

    var snapImage : ImageView? = null
    var messageText : EditText? =null
    var images=UUID.randomUUID().toString()+".jpg"

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun choose(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }

    fun next(view: View) {

        try {
            snapImage?.isDrawingCacheEnabled = true
            snapImage?.buildDrawingCache()
            val bitmap = (snapImage?.drawable as BitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            var uploadTask =
                FirebaseStorage.getInstance().reference.child("pics/"+images)
                    .putBytes(data)
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
                Toast.makeText(this, "Upload Failed!?", Toast.LENGTH_SHORT).show()

            }.addOnSuccessListener {

                var url : Uri? = null

                if (UsersActivity.alreadyselected == 0) {

                    it.storage.downloadUrl.addOnCompleteListener(OnCompleteListener {
                        url=it.result
                        val intent = Intent(this, UsersActivity::class.java)
                        intent.putExtra("message", messageText?.text.toString())
                        intent.putExtra("imageName", images)
                        intent.putExtra("imageUrl",url.toString())
                        UsersActivity.selectable = 1
                        startActivity(intent)
                    })
                } else {
                    UsersActivity.alreadyselected=0

                    it.storage.downloadUrl.addOnCompleteListener(OnCompleteListener {
                        url=it.result
                        val snapMap: Map<String, String> = mapOf(
                            "from" to FirebaseAuth.getInstance().currentUser!!.email!!,
                            "imageName" to images,
                            "imageUrl" to url.toString(),
                            "message" to messageText?.text.toString()
                        )

                        FirebaseDatabase.getInstance().getReference().child("users")
                            .child(UsersActivity.keys.get(intent.getIntExtra("keyposition", -1)))
                            .child("snaps").push().setValue(snapMap)
                        finish()
                        Toast.makeText(this, "Sent!", Toast.LENGTH_SHORT).show()

                    })
                }

            }
        }catch (e : java.lang.Exception){
            e.printStackTrace()
            Toast.makeText(this,"IMAGE CANNOT BE EMPTY",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val selectedImage = data!!.data

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                snapImage?.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share)

        snapImage = findViewById(R.id.imageView)
        messageText = findViewById(R.id.editText)
    }
}
