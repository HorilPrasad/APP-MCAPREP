package com.mcaprep.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mcaprep.databinding.ActivityProfileBinding
import com.mcaprep.domain.model.User
import com.mcaprep.ui.viewmodel.UserViewModel
import com.mcaprep.utils.calculateExpiryDate
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    private val userViewModel: UserViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userViewModel.getCurrentUser()

        userViewModel.user.observeResource(
            this,
            onSuccess = {user -> onSuccess(user)},
            onError = {
                binding.loader.visibility = View.GONE
                binding.mainLayout.visibility = View.GONE
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            },
            onLoading = {
                binding.loader.visibility = View.VISIBLE
                binding.mainLayout.visibility = View.GONE
            }
        )



    }

    private fun onSuccess(data: User) {
        binding.loader.visibility = View.GONE
        binding.mainLayout.visibility = View.VISIBLE

        binding.name.text = data.name
        binding.emailId.text = data.email
        binding.mobileNumber.text = data.phone

        binding.plan.text = data.plan
        val expiryDate = calculateExpiryDate(data.startDate, data.duration)
        binding.planValidity.text = "Valid till $expiryDate"


        val finalUrl = "https://ui-avatars.com/api/?name=${data.name}&background=F66&color=045&size=128"
        Glide.with(this)
            .load(finalUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .skipMemoryCache(true)
            .centerCrop()
            .into(binding.userImage)

    }
}