package com.example.data

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    ARABIC("ar", "العربية", true),
    ENGLISH("en", "English", false)
}

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        val map = if (lang == AppLanguage.ARABIC) ARABIC_STRINGS else ENGLISH_STRINGS
        return map[key] ?: ENGLISH_STRINGS[key] ?: key
    }

    private val ARABIC_STRINGS = mapOf(
        // App identity & tagline
        "app_name" to "فارسكوتر",
        "tagline" to "تخطّ الزحام ووفر وقتك ومجهودك",
        "welcome_onboarding" to "مرحباً بك في فارسكوتر.. تخطّ الزحام ووفر وقتك ومجهودك.",

        // Role switch
        "role_customer" to "العميل",
        "role_captain" to "الكابتن الفارس",
        "role_admin" to "لوحة الإدارة",

        // Main Dashboard
        "where_to" to "أين تريد الذهاب اليوم؟",
        "what_to_deliver" to "ماذا تريد أن نرسل أو نجلب لك؟",
        "pickup_location" to "موقع الانطلاق",
        "destination_location" to "الوجهة المستهدفة",
        "current_location" to "موقعي الحالي (برج خليفة / وسط المدينة)",
        "set_destination" to "حدد وجهتك",
        "popular_places" to "وجهات متكررة وسريعة",
        "nearby_scooters" to "سكوترات نشطة حولك",

        // Services
        "service_ride" to "مشوار فارسكوتر",
        "service_ride_desc" to "سكوتر سريع مع كابتن، خوذة معقمة مجانية، وتجاوز تام للازدحام",
        "service_send_package" to "إرسال طرد",
        "service_send_package_desc" to "توصيل آمن وسريع للأغراض والوثائق حتى 15 كجم",
        "service_buy_and_get" to "شراء وتوصيل أغراض",
        "service_buy_and_get_desc" to "نشتري احتياجاتك من الصيدلية أو السوبرماركت ونحضرها إليك",

        // Fare & payment
        "estimated_fare" to "التكلفة التقديرية",
        "currency" to "ريال",
        "estimated_time" to "الوقت المتوقع",
        "estimated_distance" to "المسافة",
        "surge_active" to "وقت ذروة (طلب مرتفع)",
        "payment_method" to "طريقة الدفع",
        "cash" to "نقداً (كاش)",
        "wallet" to "محفظة فارسكوتر",
        "credit_card" to "بطاقة ائتمانية",
        "confirm_ride" to "تأكيد طلب فارسكوتر 🛵",
        "confirm_package" to "تأكيد إرسال الطرد 📦",
        "confirm_errand" to "تأكيد طلب الشراء والتوصيل 🛒",

        // Dispatching
        "dispatching_title" to "جاري البحث عن أقرب فارس سكوتر...",
        "dispatching_sub" to "نبحث في نطاق 3-5 كم للحصول على أسرع وصول",
        "cancel_request" to "إلغاء الطلب",
        "rider_assigned" to "تم العثور على الفارس!",

        // Live tracking
        "rider_on_way" to "فارس سكوتر الخاص بك في طريقه إليك الآن! 🛵",
        "rider_arriving_in" to "الوصول خلال",
        "captain_info" to "معلومات الفارس",
        "rating" to "تقييم",
        "plate_number" to "رقم اللوحة",
        "scooter_model" to "طراز السكوتر",
        "call_captain" to "اتصال آمن",
        "chat_captain" to "محادثة مشفرة",
        "sos_button" to "استغاثة طوارئ SOS",
        "trip_in_progress" to "الرحلة قيد التنفيذ الآن",
        "arrived_destination" to "وصلت بأمان إلى وجهتك",

        // Package Details Form
        "package_info_title" to "تفاصيل الطرد المُرسل",
        "package_desc" to "وصف المحتوى (أوراق، هدية، ملابس...)",
        "receiver_name" to "اسم المستلم",
        "receiver_phone" to "رقم جوال المستلم",
        "weight_notice" to "تنبيه أمان: الحد الأقصى للوزن هو 15 كجم، بما يتناسب مع صندوق السكوتر.",

        // Errand Form
        "errand_info_title" to "تفاصيل الشراء والتوصيل",
        "store_name" to "اسم المحل / الصيدلية / السوبرماركت",
        "items_list" to "قائمة المشتريات والطلبات بالتفصيل",
        "max_budget" to "الميزانية التقديرية للشراء",

        // Safety notices
        "safety_helmet_badge" to "خوذة معقمة مجانية إجبارية مع كل مشوار",
        "safety_title" to "معايير الأمان والتشغيل الفائق",
        "sos_confirm_title" to "تأكيد إرسال إشارة استغاثة طوارئ",
        "sos_confirm_desc" to "سيتم فوراً مشاركة موقعك المباشر مع غرفة العمليات ولوحة الإدارة والاتصال بالطوارئ.",
        "sos_send" to "إرسال الاستغاثة فوراً",
        "sos_cancel" to "تراجع",
        "sos_triggered_msg" to "تم إرسال إشارة الطوارئ بنجاح! فريق الدعم يتصل بك الآن.",

        // In-app chat
        "chat_title" to "محادثة فورية مع الفارس",
        "chat_placeholder" to "اكتب رسالة إلى الفارس...",
        "chat_send" to "إرسال",

        // Trip completed & rating
        "trip_completed_title" to "وصلت الرحلة بنجاح!",
        "package_delivered_title" to "وصل طردك بأمان، يرجى تقييم الفارس",
        "rate_captain" to "قيّم تجربة الفارس",
        "tip_captain" to "إضافة بخشيش اختياري للفارس",
        "no_tip" to "بدون بخشيش",
        "finish_and_submit" to "إرسال التقييم والعودة",

        // Driver Mode
        "driver_online_status" to "حالة الفارس: متاح لاستقبال الطلبات",
        "driver_offline_status" to "حالة الفارس: غير متاح (غير متصل)",
        "go_online" to "الاتصال بالشبكة (بدء العمل)",
        "go_offline" to "إنهاء الوردية (غير متصل)",
        "incoming_order" to "طلب جديد وارد! 🛵",
        "accept_order" to "قبول الطلب (30 ثانية)",
        "decline_order" to "رفض الطلب",
        "driver_earnings_today" to "أرباح اليوم",
        "driver_completed_trips" to "مشاوير منجزة",
        "driver_safety_check" to "فحص السلامة: هل الخوذة الإضافية جاهزة ومعقمة؟",
        "driver_navigate_pickup" to "التوجه لنقطة الانطلاق",
        "driver_start_trip" to "بدء الرحلة مع العميل",
        "driver_complete_trip" to "إنهاء الرحلة واستلام الأجرة",

        // Admin Dashboard
        "admin_title" to "لوحة التحكم والعمليات الميدانية",
        "admin_fleet_overview" to "مراقبة أسطول السكوترات النشطة",
        "admin_active_scooters" to "سكوترات نشطة الآن",
        "admin_surge_control" to "التسعير الديناميكي وقت الذروة (Surge)",
        "admin_surge_desc" to "رفع الأسعار تلقائياً لجذب الفرسان للمناطق المزدحمة",
        "admin_captains_verification" to "توثيق واعتماد الكباتن والمستندات",
        "admin_captains_desc" to "فحص الهوية، رخصة القيادة، ملكية السكوتر، والفيش الجنائي",
        "admin_approve" to "اعتماد وتوثيق",
        "admin_suspend" to "إيقاف مؤقت",
        "admin_sos_log" to "سجل بلاغات الطوارئ والسلامة",
        "admin_sos_none" to "لا توجد بلاغات طوارئ نشطة حالياً، جميع الرحلات آمنة.",

        // Profile & Settings
        "profile_title" to "الملف الشخصي والإعدادات",
        "language_settings" to "إعدادات اللغة - اختر لغة التطبيق المفضلة (سيتم تطبيق التغييرات فوراً)",
        "arabic_option" to "العربية (Cairo Font)",
        "english_option" to "English (Modern Sans)",
        "wallet_balance" to "رصيد المحفظة",
        "add_funds" to "شحن الرصيد",
        "trips_history" to "سجل المشاوير والطلبات",
        "help_and_support" to "المساعدة والدعم الفني 24/7",
        "logout" to "تسجيل الخروج",

        // Common
        "close" to "إغلاق",
        "save" to "حفظ التعديلات",
        "back" to "رجوع",
        "minutes" to "دقيقة",
        "km" to "كم"
    )

    private val ENGLISH_STRINGS = mapOf(
        // App identity & tagline
        "app_name" to "Farascooter",
        "tagline" to "Skip the traffic, save your time and effort",
        "welcome_onboarding" to "Welcome to Farascooter.. Skip the traffic, save your time and effort.",

        // Role switch
        "role_customer" to "Customer",
        "role_captain" to "Captain / Rider",
        "role_admin" to "Admin Dashboard",

        // Main Dashboard
        "where_to" to "Where to?",
        "what_to_deliver" to "What can we deliver or get for you?",
        "pickup_location" to "Pickup location",
        "destination_location" to "Destination location",
        "current_location" to "Current Location (Burj Khalifa / Downtown)",
        "set_destination" to "Set your destination",
        "popular_places" to "Quick & Popular Places",
        "nearby_scooters" to "Active Scooters Nearby",

        // Services
        "service_ride" to "Farascooter Ride",
        "service_ride_desc" to "Fast scooter ride behind a captain, sanitized helmet included, bypass all jams",
        "service_send_package" to "Send Package",
        "service_send_package_desc" to "Fast & safe delivery for items and docs up to 15 kg",
        "service_buy_and_get" to "Buy & Get",
        "service_buy_and_get_desc" to "We buy from pharmacies or supermarkets and deliver to your door",

        // Fare & payment
        "estimated_fare" to "Estimated Fare",
        "currency" to "SAR",
        "estimated_time" to "Est. Time",
        "estimated_distance" to "Distance",
        "surge_active" to "Peak Surge Active (High Demand)",
        "payment_method" to "Payment Method",
        "cash" to "Cash",
        "wallet" to "Farascooter Wallet",
        "credit_card" to "Credit Card",
        "confirm_ride" to "Confirm Farascooter 🛵",
        "confirm_package" to "Confirm Package Delivery 📦",
        "confirm_errand" to "Confirm Buy & Get Order 🛒",

        // Dispatching
        "dispatching_title" to "Finding your nearest Farascooter...",
        "dispatching_sub" to "Searching within 3-5 km radius for fastest pickup",
        "cancel_request" to "Cancel Request",
        "rider_assigned" to "Captain Found!",

        // Live tracking
        "rider_on_way" to "Your Farascooter rider is on his way! 🛵",
        "rider_arriving_in" to "Arriving in",
        "captain_info" to "Captain Details",
        "rating" to "Rating",
        "plate_number" to "Plate Number",
        "scooter_model" to "Scooter Model",
        "call_captain" to "Masked Call",
        "chat_captain" to "Encrypted Chat",
        "sos_button" to "SOS Emergency",
        "trip_in_progress" to "Trip in progress",
        "arrived_destination" to "Safely arrived at destination",

        // Package Details Form
        "package_info_title" to "Package Delivery Details",
        "package_desc" to "Package Description (Documents, gift, items...)",
        "receiver_name" to "Receiver Name",
        "receiver_phone" to "Receiver Phone Number",
        "weight_notice" to "Safety Notice: Maximum allowed weight is 15 kg, fitting the scooter cargo box.",

        // Errand Form
        "errand_info_title" to "Buy & Get Details",
        "store_name" to "Store / Pharmacy / Supermarket Name",
        "items_list" to "Items List & Specifications",
        "max_budget" to "Estimated Purchase Budget",

        // Safety notices
        "safety_helmet_badge" to "Mandatory sanitized passenger helmet provided on every ride",
        "safety_title" to "Top Safety & Operational Standards",
        "sos_confirm_title" to "Confirm Emergency SOS Signal",
        "sos_confirm_desc" to "Your real-time GPS location will be immediately broadcast to the operations control room and emergency services.",
        "sos_send" to "Dispatch SOS Alert",
        "sos_cancel" to "Cancel",
        "sos_triggered_msg" to "SOS Alert dispatched! Operations team is contacting you right now.",

        // In-app chat
        "chat_title" to "Direct Chat with Captain",
        "chat_placeholder" to "Type a message to captain...",
        "chat_send" to "Send",

        // Trip completed & rating
        "trip_completed_title" to "Trip completed successfully!",
        "package_delivered_title" to "Your package delivered safely, please rate your rider",
        "rate_captain" to "Rate your captain",
        "tip_captain" to "Add optional tip for captain",
        "no_tip" to "No tip",
        "finish_and_submit" to "Submit & Back to Home",

        // Driver Mode
        "driver_online_status" to "Captain Status: Online & Available",
        "driver_offline_status" to "Captain Status: Offline",
        "go_online" to "Go Online (Start Shift)",
        "go_offline" to "Go Offline",
        "incoming_order" to "New Order Request! 🛵",
        "accept_order" to "Accept Order (30s)",
        "decline_order" to "Decline",
        "driver_earnings_today" to "Today's Earnings",
        "driver_completed_trips" to "Trips Completed",
        "driver_safety_check" to "Safety Check: Is the sanitized passenger helmet ready?",
        "driver_navigate_pickup" to "Navigate to Pickup",
        "driver_start_trip" to "Start Trip with Passenger",
        "driver_complete_trip" to "Complete Trip & Collect Fare",

        // Admin Dashboard
        "admin_title" to "Operations & Fleet Control Room",
        "admin_fleet_overview" to "Live Active Fleet Monitor",
        "admin_active_scooters" to "Active Scooters Now",
        "admin_surge_control" to "Peak Surge Pricing Control",
        "admin_surge_desc" to "Dynamically incentivize riders to cluster around high demand zones",
        "admin_captains_verification" to "Captain & Document Verification",
        "admin_captains_desc" to "Verify National ID, Driving License, Scooter Ownership, and Background Check",
        "admin_approve" to "Approve & Verify",
        "admin_suspend" to "Suspend",
        "admin_sos_log" to "Emergency SOS Logs",
        "admin_sos_none" to "No active emergency alerts. All trips operating safely.",

        // Profile & Settings
        "profile_title" to "Profile & Settings",
        "language_settings" to "Language Settings - Choose your preferred app language (Changes apply instantly)",
        "arabic_option" to "العربية (Cairo Font)",
        "english_option" to "English (Modern Sans)",
        "wallet_balance" to "Wallet Balance",
        "add_funds" to "Add Funds",
        "trips_history" to "Ride & Order History",
        "help_and_support" to "Help & 24/7 Support",
        "logout" to "Sign Out",

        // Common
        "close" to "Close",
        "save" to "Save Changes",
        "back" to "Back",
        "minutes" to "min",
        "km" to "km"
    )
}
