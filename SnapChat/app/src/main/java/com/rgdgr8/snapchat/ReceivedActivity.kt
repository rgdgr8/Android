package com.rgdgr8.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ReceivedActivity : AppCompatActivity() {

    var emails : ArrayList<String> = ArrayList()
    var snaps: ArrayList<DataSnapshot> = ArrayList()
    var listView : ListView?=null

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.receiver_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.share) {
            val intent = Intent(applicationContext,ShareActivity::class.java)
            startActivity(intent)
        }else if (item.itemId==R.id.userList){
            val intent=Intent(applicationContext,UsersActivity::class.java)
            startActivity(intent)
        }else if(item.itemId==R.id.refresh){
            finish()
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        listView = findViewById(R.id.listview)
        listView?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().currentUser?.uid!!).child("snaps").addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val email = p0.child("from").value as String
                emails.add(email)
                adapter.notifyDataSetChanged()
                snaps.add(p0)
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                var i =0
                for (snap : DataSnapshot in snaps){
                    if (snap.key==p0.key){
                        snaps.removeAt(i)
                        emails.removeAt(i)
                    }
                    i++
                }
                adapter.notifyDataSetChanged()
            }

        })

        listView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val snapshot = snaps.get(i)

            var intent = Intent(this, SeeSnapActivity::class.java)

            intent.putExtra("imageName",snapshot.child("imageName").value as String)
            intent.putExtra("imageUrl",snapshot.child("imageUrl").value as String)
            intent.putExtra("message",snapshot.child("message").value as String)
            intent.putExtra("snapKey",snapshot.key)

            startActivity(intent)
        }
    }
}
