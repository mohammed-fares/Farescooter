package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppLanguage
import com.example.model.AiMarketingCampaign
import com.example.model.Captain
import com.example.model.CaptainNeedRequest
import com.example.model.ChatMessage
import com.example.model.ComplaintTicket
import com.example.model.ContactInvite
import com.example.model.DeviceDownloadRecord
import com.example.model.ErrandDetails
import com.example.model.PackageDetails
import com.example.model.PaymentMethod
import com.example.model.PlaceItem
import com.example.model.SOSAlert
import com.example.model.SavedCard
import com.example.model.SavedWallet
import com.example.model.ScooterLocation
import com.example.model.ScooterVsCarComparison
import com.example.model.ServiceType
import com.example.model.TripOrder
import com.example.model.TripStatus
import com.example.model.UserProfile
import com.example.model.WalletTransaction
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class AppMode {
    CUSTOMER,
    CAPTAIN,
    ADMIN
}

enum class MapPinTarget {
    PICKUP,
    DESTINATION
}

enum class CustomerScreen {
    HOME,
    SEARCH_DESTINATION,
    PACKAGE_FORM,
    ERRAND_FORM,
    CONFIRM_ORDER,
    DISPATCHING,
    LIVE_TRACKING,
    TRIP_RATING,
    WALLET,
    HISTORY,
    PROFILE
}

data class FarascooterUiState(
    val language: AppLanguage = AppLanguage.ARABIC,
    val currentMode: AppMode = AppMode.CUSTOMER,
    val customerScreen: CustomerScreen = CustomerScreen.HOME,

    // Real-Time Ride Duration Counter (Ticks every second when ride begins)
    val rideDurationSeconds: Int = 0,
    val currentSpeedKmh: Int = 36,
    val distanceTraveledKm: Double = 0.0,

    // Interactive Map Pinning & Navigation State
    val mapPinTarget: MapPinTarget = MapPinTarget.PICKUP,
    val isMapPinningActive: Boolean = false,
    val pinnedLocationName: String = "ميدان التحرير، وسط البلد",
    val pinnedXRatio: Float = 0.49f,
    val pinnedYRatio: Float = 0.52f,
    val selectedNearbyScooter: ScooterLocation? = null,

    // Auth & Permissions State
    val isAuthCompleted: Boolean = true,
    val hasPermissionsGranted: Boolean = true,
    val userProfile: UserProfile = UserProfile(),

    // Popups & Dialogs
    val showPaymentSheet: Boolean = false,
    val showReferralDialog: Boolean = false,
    val showComplaintsSheet: Boolean = false,
    val complaintTargetTripId: String? = null,
    val complaintTargetCaptainName: String? = null,
    val selectedTripForDetail: TripOrder? = null,

    // Exact Pickup Spot (Customer can pin location)
    val exactPickupAddress: String = "ميدان التحرير، أمام مجمع التحرير، وسط البلد",
    val exactPickupXRatio: Float = 0.49f,
    val exactPickupYRatio: Float = 0.52f,

    // Wallet in EGP (جنيه مصري)
    val walletBalance: Double = 350.0,
    val walletTransactions: List<WalletTransaction> = listOf(
        WalletTransaction("w1", "شحن رصيد - فوري / ميزة", "Top-up - Fawry / Meeza", 200.0, true, "اليوم 10:15 ص"),
        WalletTransaction("w2", "مشوار سكوتر - وسط البلد ➔ الزمالك", "Scooter Ride - Downtown ➔ Zamalek", 28.0, false, "أمس 07:30 م"),
        WalletTransaction("w3", "توصيل طرد مستندات - المعادي", "Package Delivery - Maadi", 35.0, false, "3 نوفمبر")
    ),

    // Saved Bank Cards (Online 1-click payment)
    val savedCards: List<SavedCard> = listOf(
        SavedCard("c1", "•••• •••• •••• 4412", "MOHAMED FAROUK", "08/28", "Visa", isDefault = true),
        SavedCard("c2", "•••• •••• •••• 9031", "MOHAMED FAROUK", "11/27", "Mastercard", isDefault = false)
    ),

    // Saved Electronic Wallets (Vodafone Cash, InstaPay, Orange Money, Etisalat Cash)
    val savedWallets: List<SavedWallet> = listOf(
        SavedWallet("sw1", "فودافون كاش (Vodafone Cash)", "01098765432", isDefault = true),
        SavedWallet("sw2", "إنستاباي (InstaPay Egypt)", "mf.farouk@instapay", isDefault = false)
    ),

    // Complaint & Support Tickets
    val complaintTickets: List<ComplaintTicket> = listOf(
        ComplaintTicket(
            id = "TKT-8120",
            tripId = "FS-EG-1029",
            captainName = "كابتن سيف الدين المصري",
            category = "سلوك الكابتن أو القيادة غير الآمنة",
            description = "الكابتن كان مسرعاً قليلاً في كوبري قصر النيل، أرجو التنبيه بضرورة الالتزام بالسرعة القانونية.",
            date = "أمس 08:15 م",
            status = "تمت المراجعة والحل بنجاح"
        )
    ),

    // Contacts for Referral Invites
    val contactInvites: List<ContactInvite> = listOf(
        ContactInvite("ci1", "أحمد الشريف", "01011223344", isInvited = false),
        ContactInvite("ci2", "محمود فوزي", "01122334455", isInvited = true),
        ContactInvite("ci3", "م. كريم طارق", "01233445566", isInvited = false),
        ContactInvite("ci4", "سارة إبراهيم", "01544556677", isInvited = false)
    ),

    // Active nearby scooters in Cairo / Giza
    val nearbyScooters: List<ScooterLocation> = listOf(
        ScooterLocation("sc1", 0.42f, 0.46f, 45f, true, "فارس أحمد المصري"),
        ScooterLocation("sc2", 0.55f, 0.38f, 120f, true, "فارس إبراهيم حسن"),
        ScooterLocation("sc3", 0.62f, 0.54f, 210f, true, "فارس مصطفى محمود"),
        ScooterLocation("sc4", 0.34f, 0.60f, 310f, true, "فارس كريم عبد العزيز"),
        ScooterLocation("sc5", 0.68f, 0.44f, 90f, true, "فارس طارق الشناوي")
    ),

    // Egyptian Popular Destinations (Cairo & Giza)
    val popularPlaces: List<PlaceItem> = listOf(
        PlaceItem("p1", "برج القاهرة & الزمالك", "Cairo Tower & Zamalek", "الجزيرة، شارع البرج", "Zamalek, Al Borg St", 2.6, 0.44f, 0.48f),
        PlaceItem("p2", "ميدان التحرير & وسط البلد", "Tahrir Sq & Downtown", "كوبري قصر النيل", "Qasr El Nil Bridge", 1.8, 0.49f, 0.52f),
        PlaceItem("p3", "مدينة نصر - كايرو فيستيفال سيتي", "Nasr City & CFC", "شارع عباس العقاد والمحور", "Abbas El Akkad St", 7.4, 0.78f, 0.28f),
        PlaceItem("p4", "المهندسين - شارع جامعة الدول", "Mohandessin - Gameat El Dowal", "ميدان مصطفى محمود", "Mostafa Mahmoud Sq", 3.2, 0.32f, 0.45f),
        PlaceItem("p5", "المعادي - شارع 9 & دجلة", "Maadi - Degla St 9", "كورنيش النيل بالمعادي", "Maadi Corniche", 6.5, 0.46f, 0.75f),
        PlaceItem("p6", "الأهرامات - الجيزة", "Giza Pyramids", "طريق الهرم السياحي", "Al Haram Road", 9.2, 0.24f, 0.68f)
    ),

    // Selected destination
    val selectedDestination: PlaceItem? = null,
    val selectedServiceType: ServiceType = ServiceType.RIDE,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.WALLET,

    // Dynamic Surge Pricing
    val surgeMultiplier: Double = 1.0,

    // Active Trip
    val activeTrip: TripOrder? = null,
    val dispatchCountdownSec: Int = 30,
    val captainProgressRatio: Float = 0f,

    // Chat
    val chatInputText: String = "",
    val showChatDialog: Boolean = false,

    // SOS Emergency
    val showSosConfirmDialog: Boolean = false,
    val sosAlertNotification: String? = null,

    // Package & Errand Drafts
    val packageDraft: PackageDetails = PackageDetails(
        pickupLocation = "مقر العمل - التحرير، وسط البلد",
        deliveryLocation = "المهندسين، شارع جامعة الدول العربية",
        description = "مستندات عمل وعقد بيع معتمد",
        receiverName = "أحمد فؤاد الشافعي",
        receiverPhone = "01098765432",
        weightKg = 2.5f
    ),
    val errandDraft: ErrandDetails = ErrandDetails(
        storeName = "صيدليات العزبي - وسط البلد",
        itemsList = "1x بنادول إكسترا\n1x فيتامين سي فوار\n1x قطرة مرطبة للعين",
        estimatedBudget = 110.0
    ),

    // Driver / Captain Mode State
    val isDriverOnline: Boolean = true,
    val driverTodayEarnings: Double = 420.0,
    val driverCompletedCount: Int = 11,
    val hasDriverIncomingOrder: Boolean = false,
    val driverOrderCountdown: Int = 30,
    val driverHelmetConfirmed: Boolean = true,

    // Captains in Egypt
    val captainsList: List<Captain> = listOf(
        Captain("c1", "كابتن سيف الدين المصري", "01012345678", 4.96f, 1420, "ق هـ د 4192", "Xiaomi Pro Max Ultra (45 كم/س)", 0xFF00B44A, isVerified = true),
        Captain("c2", "كابتن أحمد شحاتة", "01123456789", 4.91f, 890, "ج ي ر 8821", "Ninebot Segway GT2", 0xFF2563EB, isVerified = true),
        Captain("c3", "كابتن كريم حسن", "01234567890", 4.88f, 740, "س ن م 3314", "Kaabo Mantis King", 0xFF00B44A, isVerified = true),
        Captain("c4", "كابتن محمود إبراهيم (جديد)", "01545678901", 4.75f, 28, "ط ع ف 9901", "Dualtron Spider", 0xFFD97706, isVerified = false)
    ),

    // Captain Needs & Requests
    val captainNeedRequests: List<CaptainNeedRequest> = listOf(
        CaptainNeedRequest("cn1", "كابتن سيف الدين المصري", "01012345678", "خوذة ركاب معقمة إضافية", "خوذة مقاس L إضافية معتمدة للراكب لتبديل الخوذة المستهلكة", "اليوم 09:00 ص", isFulfilled = true),
        CaptainNeedRequest("cn2", "كابتن أحمد شحاتة", "01123456789", "فحص وصيانة سكوتر", "طلب استبدال تيل الفرامل وفحص ضغط الإطارات في مركز صيانة الدقي", "اليوم 11:30 ص", isFulfilled = false),
        CaptainNeedRequest("cn3", "كابتن كريم حسن", "01234567890", "شاحن بطارية سريع", "طلب محول شحن سريع 48V لمحطة شحن مدينة نصر", "أمس", isFulfilled = false),
        CaptainNeedRequest("cn4", "كابتن محمود إبراهيم", "01545678901", "مكافأة تميز المشاوير", "إتمام 50 مشوار في أسبوع مع تقييم 5 نجوم (حافز 500 ج.م)", "2 نوفمبر", isFulfilled = true)
    ),

    // Device Downloads in Egypt (48,250 Total)
    val deviceDownloads: List<DeviceDownloadRecord> = listOf(
        DeviceDownloadRecord("d1", "Samsung Galaxy S24 Ultra", "Android 15", "القاهرة - التجمع الخامس", "v2.4.0", "منذ 4 دقائق", true),
        DeviceDownloadRecord("d2", "iPhone 16 Pro Max", "iOS 18.1", "الجيزة - الدقي", "v2.4.0", "منذ 12 دقيقة", true),
        DeviceDownloadRecord("d3", "Xiaomi 14T Pro", "Android 14", "القاهرة - مدينة نصر", "v2.3.9", "منذ 25 دقيقة", true),
        DeviceDownloadRecord("d4", "Oppo Reno 12 5G", "Android 14", "الإسكندرية - سموحة", "v2.4.0", "منذ ساعة", true),
        DeviceDownloadRecord("d5", "Realme 12 Pro+", "Android 14", "المنصورة - حي الجامعة", "v2.4.0", "اليوم 08:30 ص", false)
    ),

    // AI Marketing Campaigns
    val aiMarketingCampaigns: List<AiMarketingCampaign> = listOf(
        AiMarketingCampaign(
            id = "ai1",
            platform = "Facebook",
            targetArea = "كوبري أكتوبر والمحور والدائري (القاهرة والجيزة)",
            headline = "زهقت من وقفة كوبري أكتوبر والحر؟ 🥵",
            dialectCaption = "ليه تضيع ساعتين من يومك في زحمة التاكسي والعربيات؟ FareScooter بيوصلك في نص الوقت وبنص التكلفة! استخدم بروموكود (FARE50) وخد خصم 50 ج.م على رحلتك 🛵💨",
            hashtags = "#FareScooter #اهرب_من_الزحمة #سكوتر_القاهرة #مشاوير_مصر",
            suggestedBudgetEgp = 3500,
            estimatedReach = "140,000 مستخدم في القاهرة الكبرى",
            createdTime = "اليوم"
        )
    ),

    val sosIncidentLogs: List<SOSAlert> = emptyList(),
    val tripHistory: List<TripOrder> = emptyList()
)

class FarascooterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FarascooterUiState())
    val uiState: StateFlow<FarascooterUiState> = _uiState.asStateFlow()

    private var dispatchJob: Job? = null
    private var simulationJob: Job? = null
    private var riderMovementJob: Job? = null
    private var durationTickerJob: Job? = null

    init {
        val defaultLocale = Locale.getDefault().language
        val initialLang = if (defaultLocale.startsWith("ar")) AppLanguage.ARABIC else AppLanguage.ENGLISH
        _uiState.update { it.copy(language = initialLang) }

        startAmbientScooters()
    }

    private fun startAmbientScooters() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (true) {
                delay(3000)
                _uiState.update { state ->
                    val updated = state.nearbyScooters.map { sc ->
                        val dx = (Random.nextFloat() - 0.5f) * 0.012f
                        val dy = (Random.nextFloat() - 0.5f) * 0.012f
                        sc.copy(
                            xRatio = (sc.xRatio + dx).coerceIn(0.20f, 0.80f),
                            yRatio = (sc.yRatio + dy).coerceIn(0.20f, 0.80f)
                        )
                    }
                    state.copy(nearbyScooters = updated)
                }
            }
        }
    }

    // Language changing is strictly done in Profile Screen
    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    // Mode Switch (Customer, Captain, Admin)
    fun setAppMode(mode: AppMode) {
        _uiState.update { it.copy(currentMode = mode) }
    }

    fun setCustomerScreen(screen: CustomerScreen) {
        _uiState.update { it.copy(customerScreen = screen) }
    }

    // Auth & Permissions
    fun loginWithOtp(name: String, phone: String, email: String) {
        _uiState.update {
            it.copy(
                isAuthCompleted = true,
                userProfile = it.userProfile.copy(
                    name = name,
                    phoneNumber = phone,
                    email = email,
                    isLoggedIn = true
                )
            )
        }
    }

    fun skipAuthAsGuest() {
        _uiState.update { it.copy(isAuthCompleted = true) }
    }

    fun completePermissions() {
        _uiState.update { it.copy(hasPermissionsGranted = true) }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                isAuthCompleted = false,
                customerScreen = CustomerScreen.HOME
            )
        }
    }

    // Exact Location Pinning
    fun setExactPickupLocation(address: String, xRatio: Float = 0.49f, yRatio: Float = 0.52f) {
        _uiState.update {
            it.copy(
                exactPickupAddress = address,
                exactPickupXRatio = xRatio,
                exactPickupYRatio = yRatio
            )
        }
    }

    fun pinCurrentLocationGps() {
        val cairoSpots = listOf(
            "ميدان التحرير، أمام مجمع التحرير" to (0.49f to 0.52f),
            "الزمالك، شارع 26 يوليو بجوار نفق الجزيرة" to (0.44f to 0.48f),
            "المهندسين، تقاطع شارع جامعة الدول مع سوريا" to (0.32f to 0.45f),
            "الدقي، أمام محطة المترو والبحوث" to (0.38f to 0.50f),
            "المعادي، شارع 9 أمام محطة المترو" to (0.46f to 0.75f)
        )
        val spot = cairoSpots.random()
        setExactPickupLocation(spot.first, spot.second.first, spot.second.second)
    }

    // Real-Time Ride Duration Ticker (Updates every second when ride begins)
    fun startRideDurationTicker() {
        durationTickerJob?.cancel()
        _uiState.update { it.copy(rideDurationSeconds = 0, currentSpeedKmh = 36, distanceTraveledKm = 0.0) }
        durationTickerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.update { state ->
                    if (state.activeTrip?.status == TripStatus.IN_TRANSIT) {
                        val nextSec = state.rideDurationSeconds + 1
                        val speed = (34 + (nextSec % 7) - 3).coerceIn(24, 44)
                        val totalDist = state.activeTrip.distanceKm
                        val dist = (totalDist * state.captainProgressRatio).coerceAtMost(totalDist)
                        state.copy(
                            rideDurationSeconds = nextSec,
                            currentSpeedKmh = speed,
                            distanceTraveledKm = String.format(Locale.US, "%.2f", dist).toDouble()
                        )
                    } else {
                        state
                    }
                }
            }
        }
    }

    // Cairo Reverse Geocoding
    fun reverseGeocodeCairo(xRatio: Float, yRatio: Float): Pair<String, String> {
        val anchors = listOf(
            Triple(0.49f, 0.52f, "ميدان التحرير، وسط البلد" to "Tahrir Square, Downtown"),
            Triple(0.44f, 0.46f, "برج القاهرة، جزيرة الزمالك" to "Cairo Tower, Zamalek"),
            Triple(0.41f, 0.38f, "شارع 26 يوليو، الزمالك" to "26th of July St, Zamalek"),
            Triple(0.33f, 0.44f, "شارع جامعة الدول، المهندسين" to "Gameat El Dowal St, Mohandessin"),
            Triple(0.30f, 0.47f, "ميدان مصطفى محمود، المهندسين" to "Mostafa Mahmoud Sq, Mohandessin"),
            Triple(0.37f, 0.51f, "ميدان الدقي، شارع التحرير" to "Dokki Square, Tahrir St"),
            Triple(0.38f, 0.58f, "جامعة القاهرة، ميدان النهضة" to "Cairo University, Nahda Sq"),
            Triple(0.50f, 0.47f, "ميدان طلعت حرب، وسط البلد" to "Talaat Harb Sq, Downtown"),
            Triple(0.47f, 0.56f, "جاردن سيتي، كورنيش النيل" to "Garden City, Nile Corniche"),
            Triple(0.46f, 0.65f, "المنيل، شارع المنيل الرئيسي" to "Manial, Al Manial St"),
            Triple(0.46f, 0.76f, "المعادي، شارع 9 & دجلة" to "Maadi, Degla St 9"),
            Triple(0.74f, 0.30f, "كايرو فيستيفال سيتي، التجمع" to "CFC, New Cairo"),
            Triple(0.70f, 0.34f, "شارع عباس العقاد، مدينة نصر" to "Abbas El Akkad St, Nasr City"),
            Triple(0.24f, 0.68f, "طريق الأهرامات، الجيزة" to "Al Haram Road, Giza"),
            Triple(0.36f, 0.36f, "العجوزة، كورنيش النيل" to "Agouza, Nile Corniche"),
            Triple(0.43f, 0.49f, "كوبري قصر النيل، الجزيرة" to "Qasr El Nil Bridge, Gezira")
        )

        var bestDist = Float.MAX_VALUE
        var bestName = anchors.first().third

        anchors.forEach { (ax, ay, names) ->
            val dx = xRatio - ax
            val dy = yRatio - ay
            val dist = dx * dx + dy * dy
            if (dist < bestDist) {
                bestDist = dist
                bestName = names
            }
        }
        return bestName
    }

    fun onMapLocationTapped(xRatio: Float, yRatio: Float) {
        val names = reverseGeocodeCairo(xRatio, yRatio)
        val chosenName = if (_uiState.value.language == AppLanguage.ARABIC) names.first else names.second
        _uiState.update {
            it.copy(
                isMapPinningActive = true,
                pinnedXRatio = xRatio,
                pinnedYRatio = yRatio,
                pinnedLocationName = chosenName
            )
        }
    }

    fun confirmPinnedLocation() {
        val state = _uiState.value
        if (state.mapPinTarget == MapPinTarget.PICKUP) {
            setExactPickupLocation(state.pinnedLocationName, state.pinnedXRatio, state.pinnedYRatio)
        } else {
            val dist = kotlin.math.max(
                1.5,
                String.format(
                    Locale.US,
                    "%.1f",
                    kotlin.math.sqrt(
                        (state.pinnedXRatio - state.exactPickupXRatio) * (state.pinnedXRatio - state.exactPickupXRatio) +
                        (state.pinnedYRatio - state.exactPickupYRatio) * (state.pinnedYRatio - state.exactPickupYRatio)
                    ) * 14.0
                ).toDouble()
            )
            val destPlace = PlaceItem(
                id = "map_dest_${System.currentTimeMillis()}",
                nameAr = state.pinnedLocationName,
                nameEn = state.pinnedLocationName,
                addressAr = state.pinnedLocationName,
                addressEn = state.pinnedLocationName,
                distanceKm = dist,
                xRatio = state.pinnedXRatio,
                yRatio = state.pinnedYRatio
            )
            selectDestination(destPlace)
        }
        _uiState.update { it.copy(isMapPinningActive = false) }
    }

    fun setMapPinTarget(target: MapPinTarget) {
        _uiState.update { it.copy(mapPinTarget = target, isMapPinningActive = true) }
    }

    fun selectNearbyScooterPreview(scooter: ScooterLocation?) {
        _uiState.update { it.copy(selectedNearbyScooter = scooter) }
    }

    // Dialog & Sheet Toggles
    fun togglePaymentSheet(show: Boolean) {
        _uiState.update { it.copy(showPaymentSheet = show) }
    }

    fun toggleReferralDialog(show: Boolean) {
        _uiState.update { it.copy(showReferralDialog = show) }
    }

    fun toggleComplaintsSheet(show: Boolean, tripId: String? = null, captainName: String? = null) {
        _uiState.update {
            it.copy(
                showComplaintsSheet = show,
                complaintTargetTripId = tripId,
                complaintTargetCaptainName = captainName
            )
        }
    }

    fun openTripDetail(trip: TripOrder) {
        _uiState.update { it.copy(selectedTripForDetail = trip) }
    }

    fun dismissTripDetail() {
        _uiState.update { it.copy(selectedTripForDetail = null) }
    }

    // Payment Methods Handlers
    fun addSavedCard(card: SavedCard) {
        _uiState.update {
            val updated = it.savedCards.map { c -> c.copy(isDefault = false) } + card
            it.copy(
                savedCards = updated,
                selectedPaymentMethod = PaymentMethod.CREDIT_CARD
            )
        }
    }

    fun setDefaultCard(cardId: String) {
        _uiState.update { state ->
            val updated = state.savedCards.map { c ->
                c.copy(isDefault = c.id == cardId)
            }
            state.copy(savedCards = updated)
        }
    }

    fun addSavedWallet(wallet: SavedWallet) {
        _uiState.update {
            val updated = it.savedWallets.map { w -> w.copy(isDefault = false) } + wallet
            it.copy(
                savedWallets = updated,
                selectedPaymentMethod = PaymentMethod.E_WALLET
            )
        }
    }

    // Complaints Submission
    fun submitComplaint(ticket: ComplaintTicket) {
        _uiState.update {
            it.copy(
                complaintTickets = listOf(ticket) + it.complaintTickets
            )
        }
    }

    // Referral Contacts
    fun inviteContact(contactId: String) {
        _uiState.update { state ->
            val updated = state.contactInvites.map { ci ->
                if (ci.id == contactId) ci.copy(isInvited = true) else ci
            }
            val profile = state.userProfile.copy(
                referralEarningsEgp = state.userProfile.referralEarningsEgp + 50.0
            )
            state.copy(
                contactInvites = updated,
                userProfile = profile
            )
        }
    }

    fun selectDestination(place: PlaceItem) {
        _uiState.update {
            it.copy(
                selectedDestination = place,
                customerScreen = CustomerScreen.CONFIRM_ORDER
            )
        }
    }

    fun setMapCustomDestination(xRatio: Float, yRatio: Float) {
        val customPlace = PlaceItem(
            id = "custom_${System.currentTimeMillis()}",
            nameAr = "موقع مخصص على خريطة القاهرة",
            nameEn = "Custom Cairo Location",
            addressAr = "شارع رئيسي، محافظة القاهرة",
            addressEn = "Main Street, Cairo Governorate",
            distanceKm = 4.2,
            xRatio = xRatio,
            yRatio = yRatio
        )
        selectDestination(customPlace)
    }

    fun selectServiceType(type: ServiceType) {
        _uiState.update { it.copy(selectedServiceType = type) }
        when (type) {
            ServiceType.SEND_PACKAGE -> setCustomerScreen(CustomerScreen.PACKAGE_FORM)
            ServiceType.BUY_AND_GET -> setCustomerScreen(CustomerScreen.ERRAND_FORM)
            ServiceType.RIDE -> {
                if (_uiState.value.selectedDestination != null) {
                    setCustomerScreen(CustomerScreen.CONFIRM_ORDER)
                } else {
                    setCustomerScreen(CustomerScreen.SEARCH_DESTINATION)
                }
            }
        }
    }

    fun selectPaymentMethod(method: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun updatePackageDraft(draft: PackageDetails) {
        _uiState.update { it.copy(packageDraft = draft) }
    }

    fun confirmPackageForm() {
        if (_uiState.value.selectedDestination == null) {
            _uiState.update { it.copy(selectedDestination = it.popularPlaces[1]) }
        }
        setCustomerScreen(CustomerScreen.CONFIRM_ORDER)
    }

    fun updateErrandDraft(draft: ErrandDetails) {
        _uiState.update { it.copy(errandDraft = draft) }
    }

    fun confirmErrandForm() {
        if (_uiState.value.selectedDestination == null) {
            _uiState.update { it.copy(selectedDestination = it.popularPlaces[3]) }
        }
        setCustomerScreen(CustomerScreen.CONFIRM_ORDER)
    }

    // Request Order with Exact Pickup Pinning & Egypt Fare Calculation
    fun requestOrder() {
        val dest = _uiState.value.selectedDestination ?: _uiState.value.popularPlaces[0]
        val service = _uiState.value.selectedServiceType

        val scooterBase = when (service) {
            ServiceType.RIDE -> 12.0
            ServiceType.SEND_PACKAGE -> 10.0
            ServiceType.BUY_AND_GET -> 15.0
        }
        val distanceKm = dest.distanceKm

        val scooterDurationMin = (distanceKm * 2.2).toInt().coerceAtLeast(5)
        val carDurationMin = (distanceKm * 4.8).toInt().coerceAtLeast(15)
        val timeSavedMin = carDurationMin - scooterDurationMin

        val scooterKmRate = 3.5
        val scooterMinuteRate = 0.6
        val surge = _uiState.value.surgeMultiplier

        val scooterTotal = (scooterBase + (distanceKm * scooterKmRate) + (scooterDurationMin * scooterMinuteRate)) * surge
        val carTotal = (25.0 + (distanceKm * 7.5) + (carDurationMin * 1.2)) * surge
        val moneySaved = carTotal - scooterTotal

        val comparison = ScooterVsCarComparison(
            scooterDurationMin = scooterDurationMin,
            carDurationMin = carDurationMin,
            timeSavedMin = timeSavedMin,
            scooterFareEgp = String.format(Locale.US, "%.1f", scooterTotal).toDouble(),
            carFareEgp = String.format(Locale.US, "%.1f", carTotal).toDouble(),
            moneySavedEgp = String.format(Locale.US, "%.1f", moneySaved).toDouble()
        )

        val trip = TripOrder(
            id = "FS-EG-${Random.nextInt(1000, 9999)}",
            serviceType = service,
            pickupAddress = _uiState.value.exactPickupAddress,
            destinationAddress = if (_uiState.value.language == AppLanguage.ARABIC) dest.nameAr else dest.nameEn,
            pickupXRatio = _uiState.value.exactPickupXRatio,
            pickupYRatio = _uiState.value.exactPickupYRatio,
            destXRatio = dest.xRatio,
            destYRatio = dest.yRatio,
            distanceKm = distanceKm,
            durationMin = scooterDurationMin,
            carDurationMin = carDurationMin,
            baseFare = scooterBase + (distanceKm * scooterKmRate),
            surgeMultiplier = surge,
            finalFare = String.format(Locale.US, "%.1f", scooterTotal).toDouble(),
            carEstimatedFare = String.format(Locale.US, "%.1f", carTotal).toDouble(),
            comparison = comparison,
            paymentMethod = _uiState.value.selectedPaymentMethod,
            status = TripStatus.SEARCHING,
            captain = null,
            packageDetails = if (service == ServiceType.SEND_PACKAGE) _uiState.value.packageDraft else null,
            errandDetails = if (service == ServiceType.BUY_AND_GET) _uiState.value.errandDraft else null,
            chatMessages = listOf(
                ChatMessage("m1", false, if (_uiState.value.language == AppLanguage.ARABIC) "مساء الخير يا فندم، أنا الكابتن سيف في طريقي ليك إلى موقعك، والخوذة المعقمة جاهزة." else "Hello, Captain Saif on my way to your pinned spot with sanitized helmet ready.", "الآن")
            )
        )

        _uiState.update {
            it.copy(
                activeTrip = trip,
                customerScreen = CustomerScreen.DISPATCHING,
                dispatchCountdownSec = 30
            )
        }

        startDispatchCountdown()
    }

    private fun startDispatchCountdown() {
        dispatchJob?.cancel()
        dispatchJob = viewModelScope.launch {
            var seconds = 30
            while (seconds > 26) {
                delay(1000)
                seconds--
                _uiState.update { it.copy(dispatchCountdownSec = seconds) }
            }

            val matchedCaptain = _uiState.value.captainsList.first()
            _uiState.update { state ->
                val updatedTrip = state.activeTrip?.copy(
                    status = TripStatus.ACCEPTED,
                    captain = matchedCaptain
                )
                state.copy(
                    activeTrip = updatedTrip,
                    customerScreen = CustomerScreen.LIVE_TRACKING,
                    captainProgressRatio = 0.1f
                )
            }

            startTripProgressSimulation()
        }
    }

    fun cancelOrder() {
        dispatchJob?.cancel()
        riderMovementJob?.cancel()
        durationTickerJob?.cancel()
        _uiState.update {
            it.copy(
                activeTrip = null,
                customerScreen = CustomerScreen.HOME,
                captainProgressRatio = 0f,
                rideDurationSeconds = 0
            )
        }
    }

    private fun startTripProgressSimulation() {
        riderMovementJob?.cancel()
        riderMovementJob = viewModelScope.launch {
            delay(2000)
            _uiState.update { state ->
                state.copy(
                    activeTrip = state.activeTrip?.copy(status = TripStatus.ON_THE_WAY),
                    captainProgressRatio = 0.3f
                )
            }

            delay(3500)
            _uiState.update { state ->
                state.copy(
                    activeTrip = state.activeTrip?.copy(status = TripStatus.ARRIVED_PICKUP),
                    captainProgressRatio = 0.5f
                )
            }

            delay(3000)
            // Ride officially begins here!
            startRideDurationTicker()
            _uiState.update { state ->
                state.copy(
                    activeTrip = state.activeTrip?.copy(status = TripStatus.IN_TRANSIT),
                    captainProgressRatio = 0.65f
                )
            }

            var p = 0.65f
            while (p < 0.98f) {
                delay(1500)
                p += 0.08f
                _uiState.update { it.copy(captainProgressRatio = p) }
            }

            delay(1500)
            completeTrip()
        }
    }

    fun advanceTripMilestone() {
        val trip = _uiState.value.activeTrip ?: return
        when (trip.status) {
            TripStatus.ACCEPTED -> {
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.ON_THE_WAY), captainProgressRatio = 0.3f) }
            }
            TripStatus.ON_THE_WAY -> {
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.ARRIVED_PICKUP), captainProgressRatio = 0.5f) }
            }
            TripStatus.ARRIVED_PICKUP -> {
                startRideDurationTicker()
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.IN_TRANSIT), captainProgressRatio = 0.7f) }
            }
            TripStatus.IN_TRANSIT -> {
                completeTrip()
            }
            else -> {}
        }
    }

    private fun completeTrip() {
        durationTickerJob?.cancel()
        riderMovementJob?.cancel()
        val current = _uiState.value.activeTrip ?: return
        val completedTrip = current.copy(
            status = TripStatus.COMPLETED,
            actualDurationSeconds = _uiState.value.rideDurationSeconds
        )

        val newBalance = if (completedTrip.paymentMethod == PaymentMethod.WALLET) {
            (_uiState.value.walletBalance - completedTrip.finalFare).coerceAtLeast(0.0)
        } else {
            _uiState.value.walletBalance
        }

        val newHistory = listOf(completedTrip) + _uiState.value.tripHistory

        _uiState.update {
            it.copy(
                activeTrip = completedTrip,
                walletBalance = newBalance,
                tripHistory = newHistory,
                customerScreen = CustomerScreen.TRIP_RATING,
                captainProgressRatio = 1f
            )
        }
    }

    fun submitTripRating(stars: Int, tip: Double) {
        _uiState.update {
            it.copy(
                activeTrip = null,
                customerScreen = CustomerScreen.HOME,
                captainProgressRatio = 0f
            )
        }
    }

    // Chat
    fun toggleChatDialog(show: Boolean) {
        _uiState.update { it.copy(showChatDialog = show) }
    }

    fun updateChatInput(text: String) {
        _uiState.update { it.copy(chatInputText = text) }
    }

    fun sendChatMessage() {
        val text = _uiState.value.chatInputText.trim()
        if (text.isEmpty()) return

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = sdf.format(Date())
        val customerMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            isSenderCustomer = true,
            text = text,
            timeFormatted = time
        )

        _uiState.update { state ->
            val trip = state.activeTrip
            if (trip != null) {
                val updatedMessages = trip.chatMessages + customerMsg
                state.copy(
                    activeTrip = trip.copy(chatMessages = updatedMessages),
                    chatInputText = ""
                )
            } else {
                state.copy(chatInputText = "")
            }
        }

        viewModelScope.launch {
            delay(1500)
            val replyText = if (_uiState.value.language == AppLanguage.ARABIC) {
                "تمام يا فندم، أنا عديت من كوبري قصر النيل وفي شارعك عند موقعك خلال دقيقتين. 🛵"
            } else {
                "Got it, crossed Qasr El Nil bridge, at your location in 2 minutes. 🛵"
            }
            val replyMsg = ChatMessage(
                id = "msg_rep_${System.currentTimeMillis()}",
                isSenderCustomer = false,
                text = replyText,
                timeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
            )
            _uiState.update { s ->
                val t = s.activeTrip
                if (t != null) {
                    s.copy(activeTrip = t.copy(chatMessages = t.chatMessages + replyMsg))
                } else s
            }
        }
    }

    // SOS
    fun triggerSosDialog(show: Boolean) {
        _uiState.update { it.copy(showSosConfirmDialog = show) }
    }

    fun confirmSosAlert() {
        val trip = _uiState.value.activeTrip
        val alert = SOSAlert(
            id = "SOS-EG-${Random.nextInt(100, 999)}",
            tripId = trip?.id ?: "FS-EG-4412",
            timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            locationDescription = "القاهرة - كوبري 6 أكتوبر (إحداثيات 30.0444° N, 31.2357° E)",
            captainName = trip?.captain?.name ?: "كابتن سيف الدين",
            customerName = "عميل FareScooter",
            status = "ACTIVE_DISPATCHED"
        )

        _uiState.update {
            it.copy(
                showSosConfirmDialog = false,
                sosAlertNotification = if (it.language == AppLanguage.ARABIC) {
                    "تم إرسال إشارة الطوارئ SOS بنجاح لغرفة عمليات مصر وشرطة النجدة 122"
                } else {
                    "SOS Emergency Signal sent to Egypt Operations & Emergency 122"
                },
                sosIncidentLogs = listOf(alert) + it.sosIncidentLogs
            )
        }
    }

    fun dismissSosNotification() {
        _uiState.update { it.copy(sosAlertNotification = null) }
    }

    // Wallet
    fun addWalletFunds(amount: Double) {
        _uiState.update {
            val newBalance = it.walletBalance + amount
            val tx = WalletTransaction(
                id = "tx_${System.currentTimeMillis()}",
                titleAr = "شحن رصيد - فوري / كروت بنكية",
                titleEn = "Wallet Top-up - Fawry / Bank Card",
                amount = amount,
                isCredit = true,
                date = "الآن"
            )
            it.copy(
                walletBalance = newBalance,
                walletTransactions = listOf(tx) + it.walletTransactions
            )
        }
    }

    // Captain Mode
    fun toggleDriverOnline() {
        _uiState.update { it.copy(isDriverOnline = !it.isDriverOnline) }
    }

    fun toggleDriverHelmetCheck() {
        _uiState.update { it.copy(driverHelmetConfirmed = !it.driverHelmetConfirmed) }
    }

    fun simulateDriverIncomingOrder() {
        _uiState.update {
            it.copy(
                hasDriverIncomingOrder = true,
                driverOrderCountdown = 30
            )
        }
    }

    fun acceptDriverOrder() {
        _uiState.update {
            it.copy(
                hasDriverIncomingOrder = false,
                driverTodayEarnings = it.driverTodayEarnings + 45.0,
                driverCompletedCount = it.driverCompletedCount + 1
            )
        }
    }

    fun declineDriverOrder() {
        _uiState.update { it.copy(hasDriverIncomingOrder = false) }
    }

    // Admin & AI Marketing Suite
    fun setSurgeMultiplier(multiplier: Double) {
        _uiState.update { it.copy(surgeMultiplier = multiplier) }
    }

    fun toggleCaptainVerification(captainId: String) {
        _uiState.update { state ->
            val updated = state.captainsList.map { c ->
                if (c.id == captainId) c.copy(isVerified = !c.isVerified) else c
            }
            state.copy(captainsList = updated)
        }
    }

    fun fulfillCaptainNeed(needId: String) {
        _uiState.update { state ->
            val updated = state.captainNeedRequests.map { need ->
                if (need.id == needId) need.copy(isFulfilled = true) else need
            }
            state.copy(captainNeedRequests = updated)
        }
    }

    fun generateAiMarketingCampaign() {
        val platforms = listOf("Facebook", "TikTok", "Instagram", "X (Twitter)")
        val selectedPlatform = platforms.random()

        val headlines = listOf(
            "زحمة كوبري قصر النيل وميدان الجيزة؟ انساها مع FareScooter! ⚡",
            "سكوتر سريع تحت الطلب يوصلك في نص الوقت وبنص التكلفة 🛵",
            "الخوذة جاهزة، والسكوتر على الباب.. اطلب مشوارك الآن في القاهرة!",
            "وفر وقت الشغل والجامعة.. ليه تقف ساعة في المحور لما ممكن توصل في 14 دقيقة؟"
        )
        val captions = listOf(
            "ماتخليش الزحمة تعطل يومك! مع FareScooter، الكابتن بيجيلك في دقائق وياخدك لأي مكان في القاهرة والجيزة بأسرع طريق وأعلى أمان. كود خصم (FARE50) مستنيك! 🚀",
            "لو رايح الجامعة أو الشغل في ساعة الذروة، وفر فلوسك ووقتك واطلب مشوار FareScooter. خوذة معقمة مجانية وكابتن محترف بانتظارك دايماً 🛡️",
            "عايز تبعت أوراق مهمة أو طلب مستعجل؟ خدمة إرسال الطرود بالسكوتر بتضمنلك وصول أمان حتى 15 كجم بأسرع وقت 📦"
        )

        val newCampaign = AiMarketingCampaign(
            id = "ai_${System.currentTimeMillis()}",
            platform = selectedPlatform,
            targetArea = "القاهرة، الجيزة، الإسكندرية (المناطق المزدحمة)",
            headline = headlines.random(),
            dialectCaption = captions.random(),
            hashtags = "#FareScooter #مواصلات_القاهرة #أسرع_سكوتر #تخطي_الزحمة",
            suggestedBudgetEgp = Random.nextInt(1500, 4500),
            estimatedReach = "${Random.nextInt(80, 250)},000 مستخدم مستهدف",
            createdTime = "الآن"
        )

        _uiState.update {
            it.copy(aiMarketingCampaigns = listOf(newCampaign) + it.aiMarketingCampaigns)
        }
    }
}
