package com.rgdgr8.snapchat

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var emailedittext : EditText? = null
    var passwordedittext : EditText? = null
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailedittext=findViewById(R.id.userNameEditText)
        passwordedittext=findViewById(R.id.passwordEditText)

        if (auth.currentUser!=null){
            login()
        }

    }

    fun signup(view : View) {

        try {
            auth.signInWithEmailAndPassword(emailedittext?.text.toString(), passwordedittext?.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        login()
                    } else{
                        val dialog = AlertDialog.Builder(this)
                        dialog.setIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                        dialog.setTitle("PLEASE DOUBLE CHECK")
                        dialog.setMessage("Are you sure that you want to Sign Up\nwith given email and password?")
                        dialog.setPositiveButton("YES",DialogInterface.OnClickListener { dialog, which ->
                            auth.createUserWithEmailAndPassword(emailedittext?.text.toString(), passwordedittext?.text.toString())
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailedittext?.text.toString())
                                       login()
                                    }else if (passwordedittext?.text.toString().length <6){
                                        Toast.makeText(this,"SELECT A PASSWORD WITH GREATER THAN OR EQUAL TO 6 CHARACTERS!",Toast.LENGTH_LONG).show()
                                    }
                                }
                        })
                        dialog.setNegativeButton("NO",DialogInterface.OnClickListener { dialog, which ->dialog.cancel() })
                        dialog.show()
                    }
                }
        }catch (e : Exception){
            e.printStackTrace()
            Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
        }
    }

    fun login(){
        val intent = Intent(applicationContext,ReceivedActivity::class.java)
        startActivity(intent)
    }
}
