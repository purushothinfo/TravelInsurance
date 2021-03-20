package com.piappstudio.travelinsurance.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.piappstudio.travelinsurance.R
import com.piappstudio.travelinsurance.common.TIApplication
import com.piappstudio.travelinsurance.common.TIBaseActivity
import com.piappstudio.travelinsurance.databinding.FragmentLoginBinding
import com.piappstudio.travelinsurance.util.Resource
import java.util.*

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private val viewModel by lazy {
        LoginViewModel(TIApplication.INSTANCE!!.repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentLogin = FragmentLoginBinding.inflate(inflater, container, false)
        fragmentLogin.viewModel = viewModel
        fragmentLogin.lifecycleOwner = this
        initUI(fragmentLogin)

        return fragmentLogin.root
    }

    private fun initUI(fragmentLogin: FragmentLoginBinding) {
        fragmentLogin.btnSignIn.setOnClickListener {
          viewModel.onClickLogin()
        }

        viewModel.isShowProgressBar.observe(viewLifecycleOwner, Observer {
            if (it) {
                (activity as TIBaseActivity).showProgressDialog("Login")
            } else {
                (activity as TIBaseActivity).dismissProgressDialog("Login")
            }
        })

        viewModel.isLoginSuccessful.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                viewModel.resetLoginSuccessful(false)
            }
        })
    }
}