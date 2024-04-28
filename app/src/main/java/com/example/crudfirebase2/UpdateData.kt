package com.example.crudfirebase2

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UpdateData : AppCompatActivity() {
    private var database: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private var cekNIM: String? = null
    private var cekNama: String? = null
    private var cekJurusan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_data)
        supportActionBar?.title = "Update Data"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val new_nim = findViewById<EditText>(R.id.new_nim)
        val new_nama = findViewById<EditText>(R.id.new_nama)
        val new_jurusan = findViewById<EditText>(R.id.new_jurusan)
        val update = findViewById<Button>(R.id.update)

        update.setOnClickListener {
            cekNIM = new_nim.text.toString()
            cekNama = new_nama.text.toString()
            cekJurusan = new_jurusan.text.toString()

            if (isEmpty(cekNIM!!) || isEmpty(cekNama!!) || isEmpty(cekJurusan!!)) {
                Toast.makeText(this@UpdateData, "Data tidak boleh ada yang kosong", Toast.LENGTH_SHORT).show()
            } else {
                val setMahasiswa = data_mahasiswa()
                setMahasiswa.nim = new_nim.text.toString()
                setMahasiswa.nama = new_nama.text.toString()
                setMahasiswa.jurusan = new_jurusan.text.toString()
                updateMahasiswa(setMahasiswa)
            }
        }
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    private val data: Unit
        private get() {
            val getNIM = intent.getStringExtra("dataNIM")
            val getNama = intent.getStringExtra("dataNama")
            val getJurusan = intent.getStringExtra("dataJurusan")
            findViewById<EditText>(R.id.new_nim)?.setText(getNIM)
            findViewById<EditText>(R.id.new_nama)?.setText(getNama)
            findViewById<EditText>(R.id.new_jurusan)?.setText(getJurusan)
        }

    private fun updateMahasiswa(mahasiswa: data_mahasiswa) {
        val userID = auth!!.uid
        val getKey = intent.getStringExtra("getPrimaryKey")

        val updateMap = HashMap<String, Any>()
        updateMap["nim"] = mahasiswa.nim!!
        updateMap["nama"] = mahasiswa.nama!!
        updateMap["jurusan"] = mahasiswa.jurusan!!

        database!!.child("Admin")
            .child(userID!!)
            .child("Mahasiswa")
            .child(getKey!!)
            .updateChildren(updateMap)
            .addOnSuccessListener {
                findViewById<EditText>(R.id.new_nim)?.setText("")
                findViewById<EditText>(R.id.new_nama)?.setText("")
                findViewById<EditText>(R.id.new_jurusan)?.setText("")
                Toast.makeText(this@UpdateData, "Data Berhasil diubah", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@UpdateData, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("UpdateData", "Error updating data: ${e.message}")
            }
    }
}
