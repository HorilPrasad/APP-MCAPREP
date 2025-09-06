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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
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
            NavigationHelper.navigateToTestSeries(requireActivity())
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}