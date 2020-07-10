package com.zercey.paint.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.zercey.paint.interfaces.OnGetRVListListener
import com.zercey.paint.model.UserImage

class Firestore(val uid: String) {

    val db = Firebase.firestore

    fun uploadLink(link: String) {
        db.collection("Images")
            .document().set(
                hashMapOf(
                    "image" to link,
                    "uid" to uid
                )
            );

    }

    fun getProducts(
        listener: OnGetRVListListener<UserImage, DocumentSnapshot>,
        bottomItem: DocumentSnapshot?
    ) {
        var query: Query = db.collection("Images")

        if (bottomItem != null) {
            query = query.startAfter(bottomItem)
        }
        query = query.limit(20)
        query = query.whereEqualTo("uid", uid)

        query.get()
            .addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    val list = ArrayList<UserImage>()

                    for (document in documents) {
                        val l = document.toObject(UserImage::class.java)
                        list.add(l)
                    }
                    val lastVisible = documents.documents[documents.size() - 1]
                    listener.onGetList(list, lastVisible)
                } else {
                    listener.onListEmpty()
                }
            }
            .addOnFailureListener { exception ->
                listener.onFail(exception)
            }
    }
}