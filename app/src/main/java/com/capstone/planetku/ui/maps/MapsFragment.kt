package com.capstone.planetku.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.capstone.planetku.R
import com.capstone.planetku.data.WasteLocation
import com.capstone.planetku.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.CircularBounds
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.api.net.SearchByTextRequest
import androidx.core.net.toUri

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private val wasteLocations = ArrayList<WasteLocation>()
    private var selectedLocation: WasteLocation? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        setupMapUI()
        loadInitialData()
        getMyLocation()

        mMap.setOnMapClickListener {
            binding.bottomSheetDetail.visibility = View.GONE
            selectedLocation = null
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupPlaces()

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        setupInteractions()
    }

    private fun setupPlaces() {
        try {
            val ai = requireContext().packageManager.getApplicationInfo(requireContext().packageName, PackageManager.GET_META_DATA)
            val apiKey = ai.metaData.getString("com.google.android.geo.API_KEY")
            if (apiKey != null && !Places.isInitialized()) {
                @Suppress("DEPRECATION")
                Places.initialize(requireContext(), apiKey)
            }
            placesClient = Places.createClient(requireContext())
        } catch (e: Exception) {
            Log.e("MapsFragment", "Places initialization failed", e)
        }
    }

    private fun setupMapUI() {
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun setupInteractions() {
        binding.btnSearchHere.setOnClickListener {
            searchWasteLocations()
        }

        binding.btnNavigate.setOnClickListener {
            selectedLocation?.let { loc ->
                val gmmIntentUri =
                    "google.navigation:q=${loc.location.latitude},${loc.location.longitude}".toUri()
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")

                if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                    startActivity(mapIntent)
                } else {
                    Toast.makeText(requireContext(), "Google Maps tidak ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun searchWasteLocations() {
        if (!::placesClient.isInitialized) return
        
        val center = mMap.cameraPosition.target
        binding.progressBar.visibility = View.VISIBLE
        
        val placeFields = listOf(
            Place.Field.ID, 
            Place.Field.DISPLAY_NAME, 
            Place.Field.LOCATION, 
            Place.Field.FORMATTED_ADDRESS
        )
        
        val circularBounds = CircularBounds.newInstance(center, 5000.0)
        
        val request = SearchByTextRequest.builder("Bank Sampah TPS", placeFields)
            .setLocationBias(circularBounds)
            .setMaxResultCount(15)
            .build()

        placesClient.searchByText(request)
            .addOnSuccessListener { response ->
                binding.progressBar.visibility = View.GONE
                wasteLocations.clear()
                mMap.clear()
                
                val places = response.places
                if (places.isEmpty()) {
                    Toast.makeText(requireContext(), "Tidak ditemukan TPS di area ini", Toast.LENGTH_SHORT).show()
                } else {
                    for (place in places) {
                        val latLng = place.location ?: continue
                        wasteLocations.add(WasteLocation(
                            place.displayName ?: "TPS",
                            latLng,
                            place.formattedAddress ?: "-",
                            "Kertas, Plastik, Logam", 
                            "08:00 - 16:00"
                        ))
                    }
                    showMarkers()
                }
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Log.e("MapsFragment", "Search failed", exception)
                Toast.makeText(requireContext(), "Gagal mencari: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadInitialData() {
        wasteLocations.add(WasteLocation("Bank Sampah Induk Jakarta Utara", LatLng(-6.122802, 106.899351), "Jl. Tarian Raya Barat, Klp. Gading", "Plastik, Kertas, Logam", "08:00 - 15:00"))
        showMarkers()
    }

    private fun showMarkers() {
        val boundsBuilder = LatLngBounds.Builder()

        for (wastePlace in wasteLocations) {
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(wastePlace.location)
                    .title(wastePlace.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
            marker?.tag = wastePlace
            boundsBuilder.include(wastePlace.location)
        }

        mMap.setOnMarkerClickListener { marker ->
            val data = marker.tag as? WasteLocation
            if (data != null) {
                showDetailSheet(data)
                selectedLocation = data
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(data.location, 15f))
            }
            true
        }
    }

    private fun showDetailSheet(data: WasteLocation) {
        binding.apply {
            tvPlaceName.text = data.name
            tvPlaceAddress.text = data.address
            tvWasteTypes.text = data.acceptedWaste
            tvOperationalHours.text = data.operationalHours
            bottomSheetDetail.visibility = View.VISIBLE
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
