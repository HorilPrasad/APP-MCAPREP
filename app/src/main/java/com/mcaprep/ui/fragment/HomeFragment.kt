package com.mcaprep.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import com.faltenreich.skeletonlayout.createSkeleton
import com.mcaprep.BuildConfig
import com.mcaprep.R
import com.mcaprep.databinding.FragmentHomeBinding
import com.mcaprep.ui.adapter.CarouselAdapter
import com.mcaprep.ui.adapter.CarouselItem
import com.mcaprep.ui.viewmodel.AuthViewModel
import com.mcaprep.utils.NavigationHelper
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs
@AndroidEntryPoint
class HomeFragment : Fragment() {

    // Declare a binding variable
    private var _binding: FragmentHomeBinding? = null
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private val authViewModel: AuthViewModel by viewModels()
    private var skeleton: Skeleton? = null

    // Expose a non-null binding reference (valid only between onCreateView and onDestroyView)
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.footer.appVersion.text = BuildConfig.VERSION_NAME
        binding.skeletonLayout.createSkeleton()
        skeleton =  binding.carouselViewPager.applySkeleton(R.layout.item_carousel)

        binding.nimcet.setOnClickListener {
            NavigationHelper.navigateToTestSeries(requireActivity(), "NIMCET")
        }
        binding.cuet.setOnClickListener {
            NavigationHelper.navigateToTestSeries(requireActivity(), "CUETMCA")
        }
        binding.mahCet.setOnClickListener {
            NavigationHelper.navigateToTestSeries(requireActivity(), "MAHMCA")
        }

        authViewModel.notificationImageResponse.observeResource(
            owner = viewLifecycleOwner,
            onLoading = { showLoading() },
            onSuccess = { carouselItems -> showCarousel(carouselItems) },
            onError = { errorMsg -> showError(errorMsg) }
        )
        authViewModel.notificationImage()
    }

    private fun showError(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
        binding.skeletonLayout.showOriginal()
        skeleton?.showOriginal()
    }

    private fun showCarousel(items: List<CarouselItem>) {
        binding.carouselViewPager.adapter = CarouselAdapter(requireContext(), items)
        binding.dotsIndicator.attachTo(binding.carouselViewPager)
        binding.skeletonLayout.showOriginal()
        binding.carouselViewPager.setPageTransformer { page, position ->
            val scale = 1 - abs(position) * 0.25f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.5f + (1 - abs(position) * 0.5f)
        }
        startAutoScroll()
    }


    private fun startAutoScroll() {
        runnable = object : Runnable {
            override fun run() {

                val nextItem = (binding.carouselViewPager.currentItem + 1) % (binding.carouselViewPager.adapter?.itemCount ?: 1)
                binding.carouselViewPager.setCurrentItem(nextItem,true)
                handler.postDelayed(this, 4000) // 3 seconds delay
            }
        }
        handler.postDelayed(runnable!!, 4000) // Start after 3 seconds
    }


    private fun showLoading() {
        binding.skeletonLayout.showSkeleton()
        skeleton?.showSkeleton()
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}