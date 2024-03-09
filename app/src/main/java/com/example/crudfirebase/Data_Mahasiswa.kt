package com.example.crudfirebase

class Data_Mahasiswa {
    var nim: String? = null
    var nama: String? = null
    var jurusan: String? = null
    var key: String? = null

    // Empty constructor to read data snapshot
    constructor()

    // Constructor with parameters to get input data from the user
    constructor(nim: String?, nama: String?, jurusan: String?) {
        this.nim = nim
        this.nama = nama
        this.jurusan = jurusan
    }
}
