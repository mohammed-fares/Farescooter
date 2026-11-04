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

data class ScooterVsCarComparison(
    val scooterDurationMin: Int,
    val carDurationMin: Int,
    val timeSavedMin: Int,
    val scooterFareEgp: Double,
    val carFareEgp: Double,
    val moneySavedEgp: Double
)

data class ScooterLocation(
    val id: String,
    val xRatio: Float,
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
    val avatarColorHex: Long = 0xFF00C853,
    val isVerified: Boolean = true,
    val hasSanitizedHelmet: Boolean = true,
    val nationalId: String = "29408150102938",
    val licenseStatus: String = "سارية (مرور الجيزة)",
    val criminalRecordClean: Boolean = true
)

data class CaptainNeedRequest(
    val id: String,
    val captainName: String,
    val captainPhone: String,
    val category: String, // "خوذة جديدة", "صيانة سكوتر", "بطارية إضافية", "مكافأة تميز"
    val description: String,
    val date: String,
    var isFulfilled: Boolean = false
)

data class DeviceDownloadRecord(
    val id: String,
    val deviceModel: String,
    val osType: String, // "Android 15", "iOS 18"
    val governorate: String, // "القاهرة", "الجيزة", "الإسكندرية", "المنصورة"
    val appVersion: String,
    val downloadDate: String,
    val isOnline: Boolean
)

data class AiMarketingCampaign(
    val id: String,
    val platform: String, // "Facebook", "TikTok", "Instagram", "X (Twitter)"
    val targetArea: String,
    val headline: String,
    val dialectCaption: String,
    val hashtags: String,
    val suggestedBudgetEgp: Int,
    val estimatedReach: String,
    val createdTime: String
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
    val estimatedBudget: Double = 80.0
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
    val carDurationMin: Int,
    val baseFare: Double,
    val surgeMultiplier: Double,
    val finalFare: Double,
    val carEstimatedFare: Double,
    val comparison: ScooterVsCarComparison,
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
