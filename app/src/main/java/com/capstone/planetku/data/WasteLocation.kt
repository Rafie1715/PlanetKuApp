package com.capstone.planetku.data

import com.google.android.gms.maps.model.LatLng

data class WasteLocation(
    val name: String,
    val location: LatLng,
    val address: String,
    val acceptedWaste: String,
    val operationalHours: String
)