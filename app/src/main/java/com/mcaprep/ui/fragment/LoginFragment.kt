package com.mcaprep.ui.fragment

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mcaprep.R
import com.mcaprep.data.remote.model.LoginRequest
import com.mcaprep.databinding.FragmentLoginBinding
import com.mcaprep.domain.model.User
import com.mcaprep.ui.viewmodel.AuthViewModel
import com.mcaprep.utils.NavigationHelper
import com.mcaprep.utils.extentions.observeResource
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentLoginBinding? = null
    // Expose a non-null binding reference (valid only between onCreateView and onDestroyView)
    private val binding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels()

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
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.loginResponse.observeResource(
            owner = viewLifecycleOwner,
            onLoading = { showLoading() },
            onSuccess = { loginData -> showUser(loginData) },
            onError = { errorMsg -> showError(errorMsg) }
        )
        binding.buttonLogin.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString()
            val password = binding.loginPasswordInput.editText?.text.toString()

            if (validateCredentials(email, password)) {
                authViewModel.login(LoginRequest(email, password))
            }
        }

        binding.signup.setOnClickListener {
            findNavController().navigate(R.id.register)
        }
    }

    private fun validateCredentials(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            binding.emailInput.error = "Email or password cannot be empty"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Invalid email address"
            return false
        }
        return true
    }

    private fun showLoading(){
        binding.loader.visibility = View.VISIBLE
        binding.loader.playAnimation()
    }

    private fun showUser(user: User) {
        binding.loader.visibility = View.GONE
        binding.loader.cancelAnimation()
        authViewModel.setLoggedIn(user.name)
        NavigationHelper.navigateToMain(requireActivity())
    }

    private fun showError(error: String) {
        binding.loader.visibility = View.GONE
        binding.loader.cancelAnimation()
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}