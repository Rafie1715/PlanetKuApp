package com.capstone.planetku.ui.carbonemission

object CarbonCalculator {
    private const val EF_ELECTRICITY = 0.85
    private const val EF_TRANSPORTATION = 0.19
    private const val EF_ORGANIC_WASTE = 0.53
    private const val EF_INORGANIC_WASTE = 0.12

    fun calculateTotal(
        electricityKwh: Double,
        distanceKm: Double,
        organicWasteKg: Double,
        inorganicWasteKg: Double
    ): Double {
        return (electricityKwh * EF_ELECTRICITY) +
                (distanceKm * EF_TRANSPORTATION) +
                (organicWasteKg * EF_ORGANIC_WASTE) +
                (inorganicWasteKg * EF_INORGANIC_WASTE)
    }

}
