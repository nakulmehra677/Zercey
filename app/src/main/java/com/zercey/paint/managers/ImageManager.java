package com.zercey.paint.managers;

import android.graphics.Bitmap;

import com.google.firebase.firestore.DocumentSnapshot;
import com.zercey.paint.firebase.Firestore;
import com.zercey.paint.firebase.Storage;
import com.zercey.paint.interfaces.ObjectListener;
import com.zercey.paint.interfaces.OnGetRVListListener;
import com.zercey.paint.model.UserImage;

import java.io.ByteArrayOutputStream;

public class ImageManager {

    private Storage storage;
    private Firestore firestore;
    private FirebaseAuthManager authManager;

    public ImageManager() {
        authManager = new FirebaseAuthManager();

        storage = new Storage();
        firestore = new Firestore(authManager.getUId());
    }

    public void uploadImage(Bitmap bitmap, final ObjectListener<Boolean> listener) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storage.uploadImage(data, new ObjectListener<String>() {
            @Override
            public void onGetObject(String o) {

                firestore.uploadLink(o);
                listener.onGetObject(true);
            }

            @Override
            public void onFail(Exception e) {
                listener.onFail(e);
            }
        });
    }


    public void getImages(OnGetRVListListener<UserImage, DocumentSnapshot> listener, DocumentSnapshot bottomItem) {
        firestore.getProducts(listener, bottomItem);
    }
}
