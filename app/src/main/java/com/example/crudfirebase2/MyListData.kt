package com.example.crudfirebase2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerView.Adapter<*>
    private lateinit var layoutManager: RecyclerView.LayoutManager

    private val database = FirebaseDatabase.getInstance()
    private val dataMahasiswa = ArrayList<data_mahasiswa>()
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_list_data)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar?.title = "Data Mahasiswa"

        recyclerView = findViewById(R.id.datalist)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        auth = FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }

    private fun GetData() {
        Toast.makeText(applicationContext, "Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show()

        val getUserID = auth?.currentUser?.uid
        val getReference = database.getReference()

        getUserID?.let { userID ->
            getReference.child("Admin").child(userID).child("Mahasiswa")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataMahasiswa.clear()
                            for (snapshot in dataSnapshot.children) {
                                val mahasiswa = snapshot.getValue(data_mahasiswa::class.java)
                                mahasiswa?.key = snapshot.key
                                mahasiswa?.let { dataMahasiswa.add(it) }
                            }

                            adapter = RecyclerViewAdapter(dataMahasiswa, this@MyListData)
                            recyclerView.adapter = adapter
                            adapter.notifyDataSetChanged()

                            Toast.makeText(applicationContext, "Data Berhasil Dimuat", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Toast.makeText(applicationContext, "Data Gagal Dimuat", Toast.LENGTH_LONG).show()
                        Log.e("MyListActivity", "${databaseError.details} ${databaseError.message}")
                    }
                })
        }
    }

    private fun MyRecyclerView() {
        recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }
}
