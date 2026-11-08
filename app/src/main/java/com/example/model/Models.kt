package com.example.model

enum class ServiceType(val code: String) {
    RIDE("ride"),
    SEND_PACKAGE("send_package"),
    BUY_AND_GET("buy_and_get")
}

enum class PaymentMethod(val code: String) {
    CASH("cash"),
    WALLET("wallet"),
    CREDIT_CARD("credit_card"),
    E_WALLET("e_wallet") // Electronic Wallets: Vodafone Cash, InstaPay, etc.
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
    val avatarColorHex: Long = 0xFF00B44A,
    val isVerified: Boolean = true,
    val hasSanitizedHelmet: Boolean = true,
    val nationalId: String = "29408150102938",
    val licenseStatus: String = "سارية (مرور الجيزة)",
    val criminalRecordClean: Boolean = true
)

data class SavedCard(
    val id: String,
    val cardNumberMasked: String,
    val cardHolder: String,
    val expiry: String,
    val cardBrand: String = "Visa", // Visa, Mastercard, Meeza
    val isDefault: Boolean = false
)

data class SavedWallet(
    val id: String,
    val provider: String, // "فودافون كاش", "إنستاباي InstaPay", "أورنج كاش", "اتصالات كاش"
    val walletNumber: String,
    val isDefault: Boolean = false
)

data class ComplaintTicket(
    val id: String,
    val tripId: String,
    val captainName: String,
    val category: String, // "سلوك الكابتن", "عدم توفير خوذة", "احتساب أجرة زيادة", "فقدان غرض", "أخرى"
    val description: String,
    val date: String,
    val status: String = "قيد المراجعة من الدعم"
)

data class ContactInvite(
    val id: String,
    val name: String,
    val phoneNumber: String,
    var isInvited: Boolean = false
)

data class UserProfile(
    val name: String = "محمد فاروق",
    val phoneNumber: String = "01098765432",
    val email: String = "mf.z.hussein.pal@gmail.com",
    val isLoggedIn: Boolean = true,
    val referralCode: String = "FARE50",
    val referralEarningsEgp: Double = 150.0,
    val completedRidesCount: Int = 18
)

data class CaptainNeedRequest(
    val id: String,
    val captainName: String,
    val captainPhone: String,
    val category: String,
    val description: String,
    val date: String,
    var isFulfilled: Boolean = false
)

data class DeviceDownloadRecord(
    val id: String,
    val deviceModel: String,
    val osType: String,
    val governorate: String,
    val appVersion: String,
    val downloadDate: String,
    val isOnline: Boolean
)

data class AiMarketingCampaign(
    val id: String,
    val platform: String,
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
    val pickupLocation: String = "مقر العمل - التحرير، وسط البلد",
    val deliveryLocation: String = "المهندسين، شارع جامعة الدول",
    val description: String = "مستندات عمل وعقد بيع معتمد",
    val receiverName: String = "أحمد فؤاد الشافعي",
    val receiverPhone: String = "01098765432",
    val weightKg: Float = 2.5f
)

data class ErrandDetails(
    val storeName: String = "صيدلية العزبي - فرع التحرير",
    val deliveryLocation: String = "مقر العمل - التحرير، وسط البلد",
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
    val discountAmount: Double = 0.0,
    val sosAlert: SOSAlert? = null,
    val chatMessages: List<ChatMessage> = emptyList(),
    val actualDurationSeconds: Int = 0,
    val complaint: ComplaintTicket? = null
)
