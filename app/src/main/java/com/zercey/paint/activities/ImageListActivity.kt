package com.zercey.paint.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.DocumentSnapshot
import com.zercey.paint.CustomRecyclerView
import com.zercey.paint.R
import com.zercey.paint.adapter.ImageAdapter
import com.zercey.paint.databinding.ActivityImageListBinding
import com.zercey.paint.interfaces.OnGetRVListListener
import com.zercey.paint.interfaces.RequestRVItems
import com.zercey.paint.managers.ImageManager
import com.zercey.paint.model.UserImage
import java.lang.Exception

class ImageListActivity : AppCompatActivity(), OnGetRVListListener<UserImage, DocumentSnapshot>,
    RequestRVItems<DocumentSnapshot> {

    private lateinit var binding: ActivityImageListBinding

    private lateinit var customRecyclerView: CustomRecyclerView<ImageAdapter, UserImage, DocumentSnapshot>
    private lateinit var imageManager: ImageManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_image_list)

        imageManager = ImageManager()
        val images = ArrayList<UserImage>()

        customRecyclerView =
            CustomRecyclerView(
                this,
                ImageAdapter(images, this),
                images, this
            )

        customRecyclerView.initRecyclerView(
            binding.recyclerView,
            binding.swipeRefresh,
            binding.progress,
            binding.bottomProgressBar,
            binding.productNotAvailable
        )
    }


    override fun onGetList(l: MutableList<UserImage>?, bottomItem: DocumentSnapshot?) {
        customRecyclerView.addAll(l, bottomItem)
    }

    override fun onListEmpty() {
        customRecyclerView.setListEmpty()
    }

    override fun onFail(e: Exception?) {
        customRecyclerView.onFail()
    }

    override fun getFirstPage() {
        imageManager.getImages(this, null)
    }

    override fun getNextPage(bottomItem: DocumentSnapshot?) {
        imageManager.getImages(this, bottomItem)
    }
}

