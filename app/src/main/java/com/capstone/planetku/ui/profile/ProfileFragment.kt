package com.capstone.planetku.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.capstone.planetku.ui.LoginRegisterActivity
import com.capstone.planetku.ui.about.AboutActivity
import com.capstone.planetku.databinding.FragmentProfileBinding
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale
import com.bumptech.glide.Glide
import java.io.File

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserProfile()
        setupActions()
    }

    override fun onResume() {
        super.onResume()
        setupUserProfile()
    }

    private fun setupUserProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "Pengguna PlanetKu")
        val email = sharedPreferences.getString("email", "user@email.com")
        val role = sharedPreferences.getString("role", "Member")
        val photoPath = sharedPreferences.getString("profile_photo_path", null)

        binding.tvName.text = name
        binding.tvEmail.text = email
        binding.tvRole.text = role
        
        if (photoPath != null) {
            Glide.with(this).load(File(photoPath)).into(binding.ivProfile)
        } else {
            binding.ivProfile.setImageResource(com.capstone.planetku.R.drawable.ic_profile)
        }
        
        updateStats()
    }

    private fun updateStats() {
        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val totalWaste = sharedPreferences.getInt("total_waste_sorted", 0)
        val savedCarbon = sharedPreferences.getFloat("total_carbon_saved", 0.0f)

        binding.tvStatWaste.text = totalWaste.toString()
        binding.tvStatCarbon.text = String.format(Locale.getDefault(), "%.1f kg", savedCarbon)
    }

    private fun setupActions() {
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnAbout.setOnClickListener {
            val intent = Intent(requireContext(), AboutActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Keluar")
            .setMessage("Apakah Anda yakin ingin keluar?")
            .setPositiveButton("Ya") { _, _ ->
                performLogout()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun performLogout() {
        FirebaseAuth.getInstance().signOut()

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit { clear() }

        val intent = Intent(requireContext(), LoginRegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
