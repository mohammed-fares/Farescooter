package com.example.model

enum class ServiceType(val code: String) {
    RIDE("ride"),
    SEND_PACKAGE("send_package"),
    BUY_AND_GET("buy_and_get")
}

enum class PaymentMethod(val code: String) {
    CASH("cash"),
    WALLET("wallet"),
    CREDIT_CARD("credit_card")
}

enum class TripStatus {
    IDLE,
    SEARCHING,
    ACCEPTED,
    ON_THE_WAY,
    ARRIVED_PICKUP,
    IN_TRANSIT,
    COMPLETED,
    CANCELLED
}

data class ScooterLocation(
    val id: String,
    val xRatio: Float, // 0.0f to 1.0f on map canvas
    val yRatio: Float,
    val heading: Float = 0f,
    val isAvailable: Boolean = true,
    val captainName: String
)

data class Captain(
    val id: String,
    val name: String,
    val phone: String,
    val rating: Float,
    val totalRides: Int,
    val plateNumber: String,
    val scooterModel: String,
    val avatarColorHex: Long = 0xFF00E676,
    val isVerified: Boolean = true,
    val hasSanitizedHelmet: Boolean = true,
    val nationalId: String = "1098452319",
    val licenseStatus: String = "سارية المفعول (Valid)",
    val criminalRecordClean: Boolean = true
)

data class PlaceItem(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val addressAr: String,
    val addressEn: String,
    val distanceKm: Double,
    val xRatio: Float,
    val yRatio: Float
)

data class PackageDetails(
    val description: String = "",
    val receiverName: String = "",
    val receiverPhone: String = "",
    val weightKg: Float = 3.5f
)

data class ErrandDetails(
    val storeName: String = "",
    val itemsList: String = "",
    val estimatedBudget: Double = 50.0
)

data class ChatMessage(
    val id: String,
    val isSenderCustomer: Boolean,
    val text: String,
    val timeFormatted: String
)

data class WalletTransaction(
    val id: String,
    val titleAr: String,
    val titleEn: String,
    val amount: Double,
    val isCredit: Boolean,
    val date: String
)

data class SOSAlert(
    val id: String,
    val tripId: String,
    val timestamp: String,
    val locationDescription: String,
    val captainName: String,
    val customerName: String,
    var status: String = "ACTIVE_DISPATCHED"
)

data class TripOrder(
    val id: String,
    val serviceType: ServiceType,
    val pickupAddress: String,
    val destinationAddress: String,
    val pickupXRatio: Float,
    val pickupYRatio: Float,
    val destXRatio: Float,
    val destYRatio: Float,
    val distanceKm: Double,
    val durationMin: Int,
    val baseFare: Double,
    val surgeMultiplier: Double,
    val finalFare: Double,
    val paymentMethod: PaymentMethod,
    val status: TripStatus,
    val captain: Captain?,
    val packageDetails: PackageDetails? = null,
    val errandDetails: ErrandDetails? = null,
    val rating: Int = 0,
    val tipAmount: Double = 0.0,
    val sosAlert: SOSAlert? = null,
    val chatMessages: List<ChatMessage> = emptyList()
)
