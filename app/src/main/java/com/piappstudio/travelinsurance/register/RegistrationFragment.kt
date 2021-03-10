package com.piappstudio.travelinsurance.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.piappstudio.travelinsurance.R
import com.piappstudio.travelinsurance.databinding.FragmentRegistrationBinding


/**
 * A simple [Fragment] subclass.
 */
class RegistrationFragment : Fragment() {

    private val registerViewModel by lazy {
        ViewModelProvider(this).get(RegistrationViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val registerBinding = FragmentRegistrationBinding.inflate(inflater, container, false)
        registerBinding.registerModel = registerViewModel
        registerBinding.lifecycleOwner = this
        initUI(registerBinding)
        return registerBinding.root
    }

    private fun initUI(registrationBinding: FragmentRegistrationBinding) {
        registrationBinding.btnSignUp.setOnClickListener {
           navigateToDashboard(registrationBinding.etConfirmPassword.text.toString())
        }

        registrationBinding.etConfirmPassword.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                navigateToDashboard(v.text.toString())
                true
            } else false
        }
    }

    private fun navigateToDashboard(confirmPassword:String) {
        if (registerViewModel.isValidInput(confirmPassword)) {
            findNavController().navigate(R.id.action_registrationFragment_to_homeFragment)
        }
    }
}