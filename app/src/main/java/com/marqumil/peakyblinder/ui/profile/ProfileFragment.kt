package com.marqumil.peakyblinder.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.marqumil.peakyblinder.databinding.FragmentProfileBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.auth.UserViewModel
import com.marqumil.peakyblinder.ui.auth.login.LoginActivity
import com.marqumil.peakyblinder.ui.utils.ConfirmDialog
import com.orhanobut.hawk.Hawk

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        val userViewModel =
            ViewModelProvider(this).get(UserViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textProfile

        logOut(userViewModel)

        binding.apply {
            userViewModel.getUser()
            userViewModel.userState.observe(viewLifecycleOwner) {
                when (it) {
                    is ResultState.Success -> {
                        it.value.email?.let { it1 -> Log.d("user", it1) }
                        // set username
                        tvProfileName.text = it.value.name
                        tvEmail.text = it.value.email
                    }
                    is ResultState.Failure -> {
                        Log.d("user", it.throwable.toString())
                        tvProfileName.text = "User"
                        tvEmail.text = "User@"
                    }
                    is ResultState.Loading -> {
                        Log.d("user", "loading")
                        tvProfileName.text = "Please wait..."
                        tvEmail.text = "Please wait..."
                    }

                    else -> {}
                }
            }

            if (userViewModel.getUsername().toString() == "" || userViewModel.getUsername().toString() == "null" || userViewModel.getUsername().toString() == " ") {
                if (userViewModel.isUserSignedIn() == true) {
                    // set username
                    // cut the email name before @
                    val email = userViewModel.getEmail().toString()
                    val index = email.indexOf("@")
                    val name = email.substring(0, index)
                    tvProfileName.text = name
                    tvEmail.text = email
                } else {
                    // get user data
                    tvProfileName.text = "User"
                    tvEmail.text = "User@"
                }
            } else {
                tvProfileName.text = "User"
                tvEmail.text = "User@"
            }



        }

//        profileViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return root
    }

    private fun logOut(userViewModel: UserViewModel) {
        binding.apply {
            btnLogout.setOnClickListener {
                Log.d("user", "logout")
                val confirmDialog = ConfirmDialog()
                confirmDialog.title = "Keluar"
                confirmDialog.description = "Apakah anda yakin ingin keluar?"
                confirmDialog.positiveText = "Keluar"
                confirmDialog.negativeText = "Batal"
                confirmDialog.positiveCallback = {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    Hawk.deleteAll()
                    Hawk.destroy()
//                    userViewModel.logOut()
//                    userViewModel.logOutState.observe(viewLifecycleOwner) {
//                        when (it) {
//                            is ResultState.Success -> {
//                                Log.d("user", it.value.toString())
//                                startActivity(intent)
//                            }
//                            is ResultState.Failure -> {
//                                Log.d("user", it.throwable.toString())
//                            }
//                            is ResultState.Loading -> {
//                                Log.d("user", "loading")
//                            }
//
//                            else -> {}
//                        }
//                    }
                    startActivity(intent)
                }
                confirmDialog.negativeCallback = {
                    confirmDialog.dismiss()
                }
                confirmDialog.show((requireActivity()).supportFragmentManager, "confirm")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}