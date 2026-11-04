package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppLanguage
import com.example.model.Captain
import com.example.model.ChatMessage
import com.example.model.ErrandDetails
import com.example.model.PackageDetails
import com.example.model.PaymentMethod
import com.example.model.PlaceItem
import com.example.model.SOSAlert
import com.example.model.ScooterLocation
import com.example.model.ServiceType
import com.example.model.TripOrder
import com.example.model.TripStatus
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

    // Wallet
    val walletBalance: Double = 185.0,
    val walletTransactions: List<WalletTransaction> = listOf(
        WalletTransaction("w1", "شحن رصيد - مدى", "Top-up - Mada", 100.0, true, "اليوم 09:30 ص"),
        WalletTransaction("w2", "مشوار فارسكوتر - وسط المدينة", "Ride - Downtown", 24.0, false, "أمس 08:15 م"),
        WalletTransaction("w3", "توصيل طرد - مستندات", "Package Delivery - Docs", 18.0, false, "3 نوفمبر")
    ),

    // Active nearby scooters
    val nearbyScooters: List<ScooterLocation> = listOf(
        ScooterLocation("sc1", 0.38f, 0.44f, 45f, true, "فارس أحمد"),
        ScooterLocation("sc2", 0.52f, 0.36f, 120f, true, "فارس عمر"),
        ScooterLocation("sc3", 0.65f, 0.58f, 210f, true, "فارس خالد"),
        ScooterLocation("sc4", 0.28f, 0.62f, 310f, true, "فارس زياد"),
        ScooterLocation("sc5", 0.72f, 0.42f, 90f, true, "فارس طارق")
    ),

    // Places
    val popularPlaces: List<PlaceItem> = listOf(
        PlaceItem("p1", "برج خليفة & دبي مول", "Burj Khalifa & Dubai Mall", "وسط المدينة، شارع المركز المالي", "Downtown, Financial Center St", 2.4, 0.62f, 0.34f),
        PlaceItem("p2", "مركز دبي المالي العالمي (DIFC)", "DIFC Dubai", "بوابة DIFC 4", "DIFC Gate 4", 3.1, 0.70f, 0.26f),
        PlaceItem("p3", "مترو الخليج التجاري", "Business Bay Metro", "شارع الشيخ زايد", "Sheikh Zayed Road", 1.8, 0.35f, 0.52f),
        PlaceItem("p4", "صيدلية أستر - وسط المدينة", "Aster Pharmacy - Downtown", "طريق البوليفارد", "Boulevard Walk", 1.2, 0.48f, 0.48f),
        PlaceItem("p5", "سيتي سنتر ديرة", "City Centre Deira", "شارع الميناء", "Port Saeed Road", 6.8, 0.82f, 0.18f)
    ),

    // Selected destination
    val selectedDestination: PlaceItem? = null,
    val selectedServiceType: ServiceType = ServiceType.RIDE,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.WALLET,

    // Dynamic Surge Pricing (Managed by Admin or Auto-calculated)
    val surgeMultiplier: Double = 1.2, // 1.2x Peak hours

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
        description = "مستندات عمل وعقد تجاري",
        receiverName = "سالم المنصوري",
        receiverPhone = "+971 50 123 4567",
        weightKg = 2.5f
    ),
    val errandDraft: ErrandDetails = ErrandDetails(
        storeName = "صيدلية ابن سينا - البوليفارد",
        itemsList = "1x مسكن ألم بنادول إكسترا\n1x فيتامين سي فوار\n1x بلاستر طبي معقم",
        estimatedBudget = 65.0
    ),

    // Driver / Captain Mode State
    val isDriverOnline: Boolean = true,
    val driverTodayEarnings: Double = 286.0,
    val driverCompletedCount: Int = 9,
    val hasDriverIncomingOrder: Boolean = false,
    val driverOrderCountdown: Int = 30,
    val driverHelmetConfirmed: Boolean = true,

    // Admin Dashboard State
    val captainsList: List<Captain> = listOf(
        Captain("c1", "كابتن سيف الدين الشامسي", "+971 50 889 1122", 4.94f, 842, "DXB-9281-S", "Xiaomi Pro Max Ultra 45km/h", 0xFF00E676, isVerified = true),
        Captain("c2", "كابتن زياد فهد", "+971 55 432 9876", 4.88f, 615, "DXB-4412-S", "Ninebot Segway GT2", 0xFF2979FF, isVerified = true),
        Captain("c3", "كابتن طارق عبد الله", "+971 52 331 4455", 4.97f, 1120, "DXB-7788-S", "Kaabo Mantis King", 0xFF00E676, isVerified = true),
        Captain("c4", "كابتن عمر يوسف (جديد)", "+971 54 112 3344", 4.70f, 14, "DXB-1029-S", "Dualtron Spider", 0xFFFFB300, isVerified = false)
    ),
    val sosIncidentLogs: List<SOSAlert> = emptyList(),

    // Past Trips History
    val tripHistory: List<TripOrder> = emptyList()
)

class FarascooterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FarascooterUiState())
    val uiState: StateFlow<FarascooterUiState> = _uiState.asStateFlow()

    private var dispatchJob: Job? = null
    private var simulationJob: Job? = null
    private var riderMovementJob: Job? = null

    init {
        // Detect system language: if Arabic is preferred, set Arabic, otherwise English
        val defaultLocale = Locale.getDefault().language
        val initialLang = if (defaultLocale.startsWith("ar")) AppLanguage.ARABIC else AppLanguage.ENGLISH
        _uiState.update { it.copy(language = initialLang) }

        // Start background ambient scooter movements on the map
        startAmbientScooters()
    }

    private fun startAmbientScooters() {
        simulationJob?.cancel()
        simulationJob = viewModelScope.launch {
            while (true) {
                delay(3000)
                _uiState.update { state ->
                    val updated = state.nearbyScooters.map { sc ->
                        val dx = (Random.nextFloat() - 0.5f) * 0.015f
                        val dy = (Random.nextFloat() - 0.5f) * 0.015f
                        sc.copy(
                            xRatio = (sc.xRatio + dx).coerceIn(0.15f, 0.85f),
                            yRatio = (sc.yRatio + dy).coerceIn(0.15f, 0.85f)
                        )
                    }
                    state.copy(nearbyScooters = updated)
                }
            }
        }
    }

    // Language
    fun setLanguage(lang: AppLanguage) {
        _uiState.update { it.copy(language = lang) }
    }

    fun toggleLanguage() {
        val next = if (_uiState.value.language == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
        setLanguage(next)
    }

    // App Mode (Customer, Captain, Admin)
    fun setAppMode(mode: AppMode) {
        _uiState.update { it.copy(currentMode = mode) }
    }

    // Navigation in Customer
    fun setCustomerScreen(screen: CustomerScreen) {
        _uiState.update { it.copy(customerScreen = screen) }
    }

    // Destination Selection
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
            nameAr = "نقطة محددة على الخريطة",
            nameEn = "Pinned Location on Map",
            addressAr = "شارع فرعي، وسط المدينة",
            addressEn = "Secondary Street, Downtown",
            distanceKm = 3.2,
            xRatio = xRatio,
            yRatio = yRatio
        )
        selectDestination(customPlace)
    }

    // Service Type & Payment
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

    // Package Form updates
    fun updatePackageDraft(draft: PackageDetails) {
        _uiState.update { it.copy(packageDraft = draft) }
    }

    fun confirmPackageForm() {
        // If destination not set yet, pick DIFC or Downtown default
        if (_uiState.value.selectedDestination == null) {
            _uiState.update { it.copy(selectedDestination = it.popularPlaces[1]) }
        }
        setCustomerScreen(CustomerScreen.CONFIRM_ORDER)
    }

    // Errand Form updates
    fun updateErrandDraft(draft: ErrandDetails) {
        _uiState.update { it.copy(errandDraft = draft) }
    }

    fun confirmErrandForm() {
        if (_uiState.value.selectedDestination == null) {
            _uiState.update { it.copy(selectedDestination = it.popularPlaces[3]) }
        }
        setCustomerScreen(CustomerScreen.CONFIRM_ORDER)
    }

    // Request Trip Dispatching
    fun requestOrder() {
        val dest = _uiState.value.selectedDestination ?: _uiState.value.popularPlaces[0]
        val service = _uiState.value.selectedServiceType
        val baseRate = when (service) {
            ServiceType.RIDE -> 18.0
            ServiceType.SEND_PACKAGE -> 15.0
            ServiceType.BUY_AND_GET -> 22.0
        }
        val distanceCost = dest.distanceKm * 2.5
        val surge = _uiState.value.surgeMultiplier
        val total = (baseRate + distanceCost) * surge

        val trip = TripOrder(
            id = "FS-${Random.nextInt(10000, 99999)}",
            serviceType = service,
            pickupAddress = if (_uiState.value.language == AppLanguage.ARABIC) "برج خليفة، بوليفارد الشيخ محمد بن راشد" else "Burj Khalifa, Sheikh Mohammed bin Rashid Blvd",
            destinationAddress = if (_uiState.value.language == AppLanguage.ARABIC) dest.nameAr else dest.nameEn,
            pickupXRatio = 0.45f,
            pickupYRatio = 0.52f,
            destXRatio = dest.xRatio,
            destYRatio = dest.yRatio,
            distanceKm = dest.distanceKm,
            durationMin = (dest.distanceKm * 2.8).toInt().coerceAtLeast(4),
            baseFare = baseRate + distanceCost,
            surgeMultiplier = surge,
            finalFare = String.format(Locale.US, "%.1f", total).toDouble(),
            paymentMethod = _uiState.value.selectedPaymentMethod,
            status = TripStatus.SEARCHING,
            captain = null,
            packageDetails = if (service == ServiceType.SEND_PACKAGE) _uiState.value.packageDraft else null,
            errandDetails = if (service == ServiceType.BUY_AND_GET) _uiState.value.errandDraft else null,
            chatMessages = listOf(
                ChatMessage("m1", false, if (_uiState.value.language == AppLanguage.ARABIC) "مرحباً بك، معك الكابتن سيف. الخوذة المعقمة جاهزة، وأنا في الطريق إليك." else "Hello, Captain Saif here. Sanitized helmet is ready, on my way to you.", "الآن")
            )
        )

        _uiState.update {
            it.copy(
                activeTrip = trip,
                customerScreen = CustomerScreen.DISPATCHING,
                dispatchCountdownSec = 30
            )
        }

        // Run dispatch countdown & match captain
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

            // Match Captain after 4 seconds (Simulated 30s matching flow)
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

            // Start live trip movement simulation
            startTripProgressSimulation()
        }
    }

    fun cancelOrder() {
        dispatchJob?.cancel()
        riderMovementJob?.cancel()
        _uiState.update {
            it.copy(
                activeTrip = null,
                customerScreen = CustomerScreen.HOME,
                captainProgressRatio = 0f
            )
        }
    }

    private fun startTripProgressSimulation() {
        riderMovementJob?.cancel()
        riderMovementJob = viewModelScope.launch {
            // Rider on the way to pickup
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

            // Passenger boards scooter -> in transit
            delay(3000)
            _uiState.update { state ->
                state.copy(
                    activeTrip = state.activeTrip?.copy(status = TripStatus.IN_TRANSIT),
                    captainProgressRatio = 0.65f
                )
            }

            // Gradual route progression
            var p = 0.65f
            while (p < 0.98f) {
                delay(1500)
                p += 0.08f
                _uiState.update { it.copy(captainProgressRatio = p) }
            }

            // Trip completed!
            delay(1500)
            completeTrip()
        }
    }

    fun advanceTripMilestone() {
        // Allows manual instant progression for testing / fast preview
        val trip = _uiState.value.activeTrip ?: return
        when (trip.status) {
            TripStatus.ACCEPTED -> {
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.ON_THE_WAY), captainProgressRatio = 0.3f) }
            }
            TripStatus.ON_THE_WAY -> {
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.ARRIVED_PICKUP), captainProgressRatio = 0.5f) }
            }
            TripStatus.ARRIVED_PICKUP -> {
                _uiState.update { it.copy(activeTrip = trip.copy(status = TripStatus.IN_TRANSIT), captainProgressRatio = 0.7f) }
            }
            TripStatus.IN_TRANSIT -> {
                completeTrip()
            }
            else -> {}
        }
    }

    private fun completeTrip() {
        riderMovementJob?.cancel()
        val current = _uiState.value.activeTrip ?: return
        val completedTrip = current.copy(status = TripStatus.COMPLETED)

        // Deduct from wallet if paid by wallet
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

    // Rating & Tip
    fun submitTripRating(stars: Int, tip: Double) {
        val current = _uiState.value.activeTrip
        val ratedTrip = current?.copy(rating = stars, tipAmount = tip)
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

        // Automated captain response simulation
        viewModelScope.launch {
            delay(1500)
            val replyText = if (_uiState.value.language == AppLanguage.ARABIC) {
                "تمام، أنا ملتزم بالمسار وسأكون عندك خلال دقائق معدودة. 🛵"
            } else {
                "Got it, following GPS route, arriving in a few moments. 🛵"
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

    // SOS Emergency
    fun triggerSosDialog(show: Boolean) {
        _uiState.update { it.copy(showSosConfirmDialog = show) }
    }

    fun confirmSosAlert() {
        val trip = _uiState.value.activeTrip
        val alert = SOSAlert(
            id = "SOS-${Random.nextInt(100, 999)}",
            tripId = trip?.id ?: "FS-8812",
            timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()),
            locationDescription = "الشارع الرئيسي - إحداثيات (25.1972° N, 55.2744° E)",
            captainName = trip?.captain?.name ?: "كابتن سيف",
            customerName = "عميل فارسكوتر",
            status = "ACTIVE_DISPATCHED"
        )

        _uiState.update {
            it.copy(
                showSosConfirmDialog = false,
                sosAlertNotification = if (it.language == AppLanguage.ARABIC) {
                    "تم إرسال إشارة الطوارئ SOS بنجاح للعمليات ورقم 999"
                } else {
                    "SOS Emergency Signal sent to Ops control & Emergency 999"
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
                titleAr = "شحن رصيد - بطاقة ائتمانية",
                titleEn = "Wallet Top-up - Card",
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

    // Driver / Captain Mode Controls
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
                driverTodayEarnings = it.driverTodayEarnings + 32.0,
                driverCompletedCount = it.driverCompletedCount + 1
            )
        }
    }

    fun declineDriverOrder() {
        _uiState.update { it.copy(hasDriverIncomingOrder = false) }
    }

    // Admin Controls
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

    fun resolveSosAlert(alertId: String) {
        _uiState.update { state ->
            val updated = state.sosIncidentLogs.map { alert ->
                if (alert.id == alertId) alert.copy(status = "RESOLVED") else alert
            }
            state.copy(sosIncidentLogs = updated)
        }
    }
}
