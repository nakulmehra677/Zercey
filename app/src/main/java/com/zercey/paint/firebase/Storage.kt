package com.zercey.paint.firebase

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.zercey.paint.interfaces.ObjectListener
import java.util.*


class Storage {

    val storage = Firebase.storage
    val storageRef = storage.reference

    fun uploadImage(data: ByteArray, listener: ObjectListener<String>) {

        val ref = storageRef.child(UUID.randomUUID().toString() + ".png")
        ref.putBytes(data)
            .addOnFailureListener { exception ->
                listener.onFail(exception)

            }.addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener { uri ->
                    listener.onGetObject(uri.toString())
                }
            }
    }
}