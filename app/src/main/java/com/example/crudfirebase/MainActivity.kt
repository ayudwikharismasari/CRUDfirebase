package com.example.crudfirebase
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.example.crudfirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize button click listeners
        binding.logout.setOnClickListener(this)
        binding.save.setOnClickListener(this)
        binding.showdata.setOnClickListener(this)

        // Initialize Firebase Authentication instance
        auth = FirebaseAuth.getInstance()
    }

    private fun isEmpty(s: String): Boolean {
        return TextUtils.isEmpty(s)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.save -> {
                val getUserID = auth!!.currentUser!!.uid
                // Get an instance of the Firebase Database
                val database = FirebaseDatabase.getInstance()
                // Get the input data from the user
                val getNIM: String = binding.nim.text.toString()
                val getNama: String = binding.nama.text.toString()
                val getJurusan: String = binding.jurusan.text.toString()
                // Get a reference to the database
                val getReference: DatabaseReference = database.reference
                // Check if any data is empty
                if (isEmpty(getNIM) || isEmpty(getNama) || isEmpty(getJurusan)) {
                    // If any data is empty, display a short message
                    Toast.makeText(this@MainActivity, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show()
                } else {
                    // If all data is provided, save it to the database
                    // Save data reference in the database based on the user ID
                    getReference.child("Admin").child(getUserID).child("Mahasiswa").push()
                        .setValue(Data_Mahasiswa(getNIM, getNama, getJurusan))
                        .addOnCompleteListener { task ->
                            // This event occurs when the user successfully saves their data in the database
                            if (task.isSuccessful) {
                                // Clear input fields after data is saved
                                binding.nim.text.clear()
                                binding.nama.text.clear()
                                binding.jurusan.text.clear()
                                Toast.makeText(this@MainActivity, "Data Tersimpan", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@MainActivity, "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
            R.id.logout -> {
                // Statement program untuk logout/keluar
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@MainActivity, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@MainActivity, "Logout Gagal", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            R.id.showdata -> {
                // Statement program untuk menampilkan data
            }
        }
    }
}