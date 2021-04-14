package com.rgdgr8.snapchat

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class UsersActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    var emails : ArrayList<String> = ArrayList();
    var textView : TextView? =null
    var button : Button?=null
    var listView : ListView? = null

    companion object{
        var selectable =0
        var alreadyselected=0
        var keys : ArrayList<String> = ArrayList();
    }

    fun showHide(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE){
            View.INVISIBLE
        } else{
            View.VISIBLE
        }
    }
    fun done(view: View){
        selectable=0
        showHide(textView!!)
        showHide(button!!)
        val intent = Intent(applicationContext,ReceivedActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (selectable==0) {
            if (item.itemId == R.id.logout) {
                auth.signOut()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            } else if (item.itemId == R.id.share) {
                val intent = Intent(applicationContext, ShareActivity::class.java)
                startActivity(intent)
            }
        }else{
            Toast.makeText(this, "Please click on DONE first!", Toast.LENGTH_SHORT).show()
        }

        if(item.itemId==R.id.refresh){
            finish()
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        listView = findViewById(R.id.listview)
        listView?.adapter = adapter
        textView=findViewById(R.id.textView)
        button=findViewById(R.id.button)


        FirebaseDatabase.getInstance().reference.child("users")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                }

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val email = p0.child("email").value as String
                    emails.add(email)
                    keys.add(p0.key!!)
                    adapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {}

            })

        if (selectable == 1) {

            showHide(textView!!)
            showHide(button!!)

            listView?.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->

                    val snapMap: Map<String, String> = mapOf(
                        "from" to FirebaseAuth.getInstance().currentUser!!.email!!,
                        "imageName" to intent.getStringExtra("imageName"),
                        "imageUrl" to intent.getStringExtra("imageUrl"),
                        "message" to intent.getStringExtra("message")
                    )
                    FirebaseDatabase.getInstance().getReference().child("users")
                        .child(keys.get(position)).child("snaps").push().setValue(snapMap)
                    Toast.makeText(this, "Sent!", Toast.LENGTH_SHORT).show()
                }
        }else{
            listView?.onItemClickListener= AdapterView.OnItemClickListener { parent, view, position, id ->

                val dialog = AlertDialog.Builder(this)
                dialog.setIcon(R.drawable.common_google_signin_btn_icon_dark_normal)
                dialog.setTitle("ARE YOU SURE?")
                dialog.setMessage("Send SNAP to "+emails.get(position)+" ?")
                dialog.setPositiveButton("YES",DialogInterface.OnClickListener { dialog, which ->
                    alreadyselected=1
                    val intent = Intent(applicationContext,ShareActivity::class.java)
                    intent.putExtra("keyposition",position)
                    startActivity(intent)
                })
                dialog.setNegativeButton("NO",DialogInterface.OnClickListener { dialog, which ->dialog.cancel() })
                dialog.show()

            }
        }
    }
}
