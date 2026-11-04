package com.example.data

enum class AppLanguage(val code: String, val displayName: String, val isRtl: Boolean) {
    ARABIC("ar", "العربية (Cairo)", true),
    ENGLISH("en", "English", false)
}

object AppStrings {
    fun get(key: String, lang: AppLanguage): String {
        val map = if (lang == AppLanguage.ARABIC) ARABIC_STRINGS else ENGLISH_STRINGS
        return map[key] ?: ENGLISH_STRINGS[key] ?: key
    }

    private val ARABIC_STRINGS = mapOf(
        // App identity & tagline in Egypt
        "app_name" to "فارسكوتر",
        "country_name" to "جمهورية مصر العربية",
        "tagline" to "اهرب من زحمة مصر.. وفر وقتك وفلوسك مع السكوتر الأسرع",
        "welcome_onboarding" to "مرحباً بك في فارسكوتر مصر.. تخطّ زحام المحور وأكتوبر ووفر وقتك ومجهودك.",

        // Role switch
        "role_customer" to "العميل",
        "role_captain" to "الكابتن الفارس",
        "role_admin" to "لوحة الإدارة الشاملة",
        "role_web" to "الموقع الإلكتروني",

        // Main Dashboard
        "where_to" to "رايح فين النهاردة؟",
        "what_to_deliver" to "عايز نبعتلك إيه أو نجيبلك إيه؟",
        "pickup_location" to "نقطة الانطلاق",
        "destination_location" to "الوجهة المطلوبة",
        "current_location" to "موقعي الحالي (وسط البلد / ميدان التحرير)",
        "set_destination" to "حدد وجهتك على الخريطة",
        "popular_places" to "أماكن سريعة وشائعة بالقاهرة والجيزة",
        "nearby_scooters" to "سكوترات نشطة حولك",

        // Scooter vs Car Comparison
        "scooter_vs_car_title" to "مقارنة السكوتر مع السيارة ⚡",
        "scooter_advantage" to "السكوتر أسرع مرتين في الزحام ويتفادى التكدس عبر الحارات الضيقة!",
        "scooter_time" to "وقت السكوتر",
        "car_time" to "وقت السيارة",
        "time_saved" to "توفير في الوقت",
        "fare_saved" to "توفير في الفلوس",
        "scooter_fast_badge" to "أسرع بنسبة 45% من التاكسي والسيارة",

        // Services
        "service_ride" to "مشوار فارسكوتر",
        "service_ride_desc" to "سكوتر سريع مع كابتن، خوذة معقمة إجبارية، وهروب تام من زحام كوبري 6 أكتوبر والمحور",
        "service_send_package" to "إرسال طرد",
        "service_send_package_desc" to "توصيل سريع للوثائق والأغراض حتى 15 كجم بأمان عبر صندوق السكوتر",
        "service_buy_and_get" to "شراء وتوصيل (Buy & Get)",
        "service_buy_and_get_desc" to "الكابتن يشتري طلباتك من الصيدلية أو السوبرماركت ويوصلها لحد بابك",

        // Fare & payment (Egyptian Pound)
        "estimated_fare" to "التكلفة التقديرية",
        "currency" to "ج.م",
        "estimated_time" to "الوقت المتوقع بالسكوتر",
        "estimated_distance" to "المسافة",
        "surge_active" to "ساعة ذروة نشطة (طلب عالي)",
        "payment_method" to "طريقة الدفع",
        "cash" to "نقداً (كاش)",
        "wallet" to "محفظة فارسكوتر",
        "credit_card" to "فيزا / ميزة / ماستركارد",
        "confirm_ride" to "تأكيد طلب فارسكوتر 🛵",
        "confirm_package" to "تأكيد إرسال الطرد 📦",
        "confirm_errand" to "تأكيد طلب الشراء والتوصيل 🛒",

        // Dispatching
        "dispatching_title" to "جاري البحث عن أقرب فارس سكوتر...",
        "dispatching_sub" to "نبحث في دائرة 3-5 كم في شوارع القاهرة لتصلك بأسرع وقت",
        "cancel_request" to "إلغاء الطلب",
        "rider_assigned" to "تم العثور على الفارس!",

        // Live tracking
        "rider_on_way" to "فارس السكوتر في طريقه إليك الآن! 🛵",
        "rider_arriving_in" to "الوصول خلال",
        "captain_info" to "بيانات الفارس",
        "rating" to "تقييم",
        "plate_number" to "رقم اللوحة",
        "scooter_model" to "طراز السكوتر",
        "call_captain" to "اتصال آمن",
        "chat_captain" to "محادثة فورية",
        "sos_button" to "استغاثة طوارئ SOS",
        "trip_in_progress" to "المشوار قيد التنفيذ الآن",
        "arrived_destination" to "وصلت بأمان إلى وجهتك",

        // Package Details Form
        "package_info_title" to "تفاصيل الطرد المُرسل",
        "package_desc" to "وصف محتويات الطرد (أوراق، هدية، ملابس...)",
        "receiver_name" to "اسم المستلم",
        "receiver_phone" to "رقم موبايل المستلم (01x)",
        "weight_notice" to "تنبيه أمان صارم: الحد الأقصى للوزن هو 15 كجم لضمان ثبات السكوتر وسعة الصندوق.",

        // Errand Form
        "errand_info_title" to "تفاصيل الشراء والتوصيل",
        "store_name" to "اسم الصيدلية أو المحل أو السوبرماركت",
        "items_list" to "قائمة المشتريات والمواصفات بالتفصيل",
        "max_budget" to "الميزانية التقديرية للمشتريات (ج.م)",

        // Safety notices
        "safety_helmet_badge" to "خوذة ركاب معقمة إضافية متوفرة مجاناً مع كل مشوار",
        "safety_title" to "معايير الأمان والتشغيل في مصر",
        "sos_confirm_title" to "تأكيد إرسال إشارة استغاثة طوارئ SOS",
        "sos_confirm_desc" to "سيتم بث موقعك الجغرافي اللحظي لغرفة عمليات فارسكوتر والتواصل المباشر مع شرطة النجدة (122).",
        "sos_send" to "إرسال الاستغاثة فوراً",
        "sos_cancel" to "إلغاء",
        "sos_triggered_msg" to "تم إرسال إشارة الاستغاثة لغرفة العمليات المركزية بمصر!",

        // In-app chat
        "chat_title" to "محادثة فورية مع الفارس",
        "chat_placeholder" to "اكتب رسالة للكابتن...",
        "chat_send" to "إرسال",

        // Trip completed & rating
        "trip_completed_title" to "وصلت بالسلامة!",
        "package_delivered_title" to "تم تسليم طردك بنجاح",
        "rate_captain" to "تقييم أداء الفارس",
        "tip_captain" to "إضافة إكرامية (بخشيش) للكابتن",
        "no_tip" to "بدون إكرامية",
        "finish_and_submit" to "إرسال التقييم والعودة",

        // Driver Mode
        "driver_online_status" to "حالة الفارس: متاح ومستعد لاستقبال المشاوير",
        "driver_offline_status" to "حالة الفارس: غير متاح (غير متصل)",
        "go_online" to "بدء العمل والاتصال بالشبكة",
        "go_offline" to "إنهاء الوردية",
        "incoming_order" to "طلب مشوار جديد بالقاهرة! 🛵",
        "accept_order" to "قبول المشوار (30 ثانية)",
        "decline_order" to "رفض",
        "driver_earnings_today" to "أرباح اليوم الصافية",
        "driver_completed_trips" to "مشاوير مكتملة",
        "driver_safety_check" to "فحص الأمان: هل الخوذة الإضافية المعقمة جاهزة؟",

        // Admin Dashboard & AI Marketing Suite
        "admin_title" to "غرفة العمليات ولوحة التحكم المركزية - مصر",
        "admin_tabs_devices" to "الأجهزة والتنزيلات",
        "admin_tabs_captains" to "إدارة الكباتن واحتياجاتهم",
        "admin_tabs_marketing" to "الذكاء الاصطناعي والدعاية",
        "admin_tabs_comms" to "التواصل والإشعارات",
        "admin_tabs_pricing" to "التسعير الذكي والذروة",

        "admin_total_downloads" to "إجمالي التنزيلات في مصر",
        "admin_active_devices" to "أجهزة نشطة الآن",
        "admin_devices_governorates" to "توزيع التنزيلات حسب المحافظات",
        "admin_captains_needs" to "طلبات واحتياجات الكباتن",
        "admin_captains_needs_sub" to "طلبات صيانة، شحن، خوذات إضافية، وسلف مالية",
        "admin_ai_marketing_title" to "أدوات الذكاء الاصطناعي للدعاية والتسويق الرقمي",
        "admin_ai_marketing_desc" to "توليد منشورات وحملات إعلانية تلقائية باللهجة المصرية لمنصات السوشيال ميديا",
        "admin_generate_ai_campaign" to "توليد حملة إعلانية جديدة بالذكاء الاصطناعي ✨",
        "admin_broadcast_title" to "مركز البث والمراسلة الفورية",
        "admin_broadcast_desc" to "إرسال إشعارات وعروض ترويجية لجميع العملاء والكباتن",
        "admin_send_broadcast_btn" to "إرسال إشعار جماعي الآن",

        // Web Portal
        "web_portal_title" to "الموقع الإلكتروني الرسمي - فارسكوتر مصر",
        "web_portal_subtitle" to "المنصة الأولى لرحلات وتوصيل السكوتر الذكي في القاهرة والجيزة وجميع محافظات مصر",
        "web_download_app" to "حمّل التطبيق الآن وابدأ رحلتك",
        "web_download_android" to "تحميل للأندرويد (Google Play)",
        "web_download_ios" to "تحميل للآيفون (App Store)",
        "web_download_apk" to "تحميل ملف APK مباشر",
        "web_launch_admin" to "الدخول إلى لوحة التحكم الإدارية",
        "web_why_scooter" to "ليه تختار السكوتر في زحمة مصر؟",
        "web_reason_1_title" to "نصف الوقت",
        "web_reason_1_desc" to "تتجاوز التكدسات المرورية في كوبري أكتوبر والمحور والدائري بسهولة تامة.",
        "web_reason_2_title" to "وفر 50% من مصاريفك",
        "web_reason_2_desc" to "تسعيرة أوفر بكثير من تاكسي السيارات وتطبيقات النقل التقليدية.",
        "web_reason_3_title" to "أمان كامل وخوذة معقمة",
        "web_reason_3_desc" to "كباتن مدربون، وتأمين على الرحلات، وخوذة رأس معقمة لكل راكب.",

        // Profile & Settings
        "profile_title" to "الملف الشخصي والإعدادات",
        "language_exclusive_notice" to "إعدادات اللغة - يمكنك تغيير لغة التطبيق حصرياً من هنا (يتم تطبيق التغيير فوراً)",
        "arabic_option" to "اللغة العربية (Cairo Font)",
        "english_option" to "English (Modern Sans)",
        "wallet_balance" to "رصيد المحفظة",
        "add_funds" to "شحن الرصيد",
        "trips_history" to "سجل المشاوير والطلبات",
        "help_and_support" to "خدمة العملاء والدعم الفني 24/7 (مصر)",
        "logout" to "تسجيل الخروج",

        // Common
        "close" to "إغلاق",
        "save" to "حفظ التعديلات",
        "back" to "رجوع",
        "minutes" to "دقيقة",
        "km" to "كم"
    )

    private val ENGLISH_STRINGS = mapOf(
        // App identity & tagline in Egypt
        "app_name" to "Farascooter",
        "country_name" to "Arab Republic of Egypt",
        "tagline" to "Escape Egypt traffic.. Save your time and money with smart scooter rides",
        "welcome_onboarding" to "Welcome to Farascooter Egypt.. Skip 6th October & Mehwar jams, save your time and effort.",

        // Role switch
        "role_customer" to "Customer",
        "role_captain" to "Captain / Rider",
        "role_admin" to "Control Suite",
        "role_web" to "Web Portal",

        // Main Dashboard
        "where_to" to "Where to in Egypt today?",
        "what_to_deliver" to "What can we deliver or get for you?",
        "pickup_location" to "Pickup point",
        "destination_location" to "Destination",
        "current_location" to "Current Location (Downtown / Tahrir Sq)",
        "set_destination" to "Select destination on map",
        "popular_places" to "Top Cairo & Giza Destinations",
        "nearby_scooters" to "Active Scooters Nearby",

        // Scooter vs Car Comparison
        "scooter_vs_car_title" to "Scooter vs Car Comparison ⚡",
        "scooter_advantage" to "Scooters reach destinations 2x faster by filtering through narrow lanes and bypass highway jams!",
        "scooter_time" to "Scooter ETA",
        "car_time" to "Car ETA",
        "time_saved" to "Time Saved",
        "fare_saved" to "Money Saved",
        "scooter_fast_badge" to "45% Faster than Cars & Taxis",

        // Services
        "service_ride" to "Farascooter Ride",
        "service_ride_desc" to "Fast scooter ride behind a captain, sanitized helmet included, bypass Cairo highway traffic",
        "service_send_package" to "Send Package",
        "service_send_package_desc" to "Fast & safe delivery for documents and items up to 15 kg via scooter cargo box",
        "service_buy_and_get" to "Buy & Get",
        "service_buy_and_get_desc" to "We buy from pharmacies or supermarkets and deliver to your doorstep in minutes",

        // Fare & payment (Egyptian Pound)
        "estimated_fare" to "Estimated Fare",
        "currency" to "EGP",
        "estimated_time" to "Scooter Est. Time",
        "estimated_distance" to "Distance",
        "surge_active" to "Peak Rush Hour Active (High Demand)",
        "payment_method" to "Payment Method",
        "cash" to "Cash",
        "wallet" to "Farascooter Wallet",
        "credit_card" to "Visa / Meeza / Mastercard",
        "confirm_ride" to "Confirm Farascooter 🛵",
        "confirm_package" to "Confirm Package Delivery 📦",
        "confirm_errand" to "Confirm Buy & Get Order 🛒",

        // Dispatching
        "dispatching_title" to "Finding nearest Farascooter...",
        "dispatching_sub" to "Searching Cairo streets within 3-5 km radius for fastest pickup",
        "cancel_request" to "Cancel Request",
        "rider_assigned" to "Captain Found!",

        // Live tracking
        "rider_on_way" to "Your Farascooter rider is on the way! 🛵",
        "rider_arriving_in" to "Arriving in",
        "captain_info" to "Captain Details",
        "rating" to "Rating",
        "plate_number" to "Plate Number",
        "scooter_model" to "Scooter Model",
        "call_captain" to "Masked Call",
        "chat_captain" to "Live Chat",
        "sos_button" to "SOS Emergency",
        "trip_in_progress" to "Trip in progress",
        "arrived_destination" to "Safely arrived at destination",

        // Package Details Form
        "package_info_title" to "Package Delivery Details",
        "package_desc" to "Package Description (Documents, gift, items...)",
        "receiver_name" to "Receiver Name",
        "receiver_phone" to "Receiver Phone Number (01x)",
        "weight_notice" to "Strict Safety Notice: Maximum allowed weight is 15 kg to maintain scooter balance and cargo capacity.",

        // Errand Form
        "errand_info_title" to "Buy & Get Details",
        "store_name" to "Pharmacy / Store / Supermarket Name",
        "items_list" to "Items List & Specifications",
        "max_budget" to "Estimated Purchase Budget (EGP)",

        // Safety notices
        "safety_helmet_badge" to "Mandatory sanitized passenger helmet provided on every ride",
        "safety_title" to "Safety & Operational Standards in Egypt",
        "sos_confirm_title" to "Confirm Emergency SOS Signal",
        "sos_confirm_desc" to "Your real-time GPS location will be broadcast to Farascooter Operations Control and Emergency 122.",
        "sos_send" to "Dispatch SOS Alert",
        "sos_cancel" to "Cancel",
        "sos_triggered_msg" to "SOS Alert dispatched to Central Operations in Cairo!",

        // In-app chat
        "chat_title" to "Direct Chat with Captain",
        "chat_placeholder" to "Type a message to captain...",
        "chat_send" to "Send",

        // Trip completed & rating
        "trip_completed_title" to "Arrived safely!",
        "package_delivered_title" to "Your package delivered safely",
        "rate_captain" to "Rate your captain",
        "tip_captain" to "Add optional tip for captain",
        "no_tip" to "No tip",
        "finish_and_submit" to "Submit & Back to Home",

        // Driver Mode
        "driver_online_status" to "Captain Status: Online & Available in Cairo",
        "driver_offline_status" to "Captain Status: Offline",
        "go_online" to "Go Online (Start Shift)",
        "go_offline" to "Go Offline",
        "incoming_order" to "New Cairo Ride Request! 🛵",
        "accept_order" to "Accept Order (30s)",
        "decline_order" to "Decline",
        "driver_earnings_today" to "Today's Net Earnings",
        "driver_completed_trips" to "Completed Trips",
        "driver_safety_check" to "Safety Check: Sanitized passenger helmet ready?",

        // Admin Dashboard & AI Marketing Suite
        "admin_title" to "Operations & Fleet Control Center - Egypt",
        "admin_tabs_devices" to "Devices & Downloads",
        "admin_tabs_captains" to "Captains & Needs",
        "admin_tabs_marketing" to "AI Marketing Suite",
        "admin_tabs_comms" to "Communications",
        "admin_tabs_pricing" to "Smart Pricing & Surge",

        "admin_total_downloads" to "Total App Downloads in Egypt",
        "admin_active_devices" to "Active Devices Now",
        "admin_devices_governorates" to "Downloads by Governorate",
        "admin_captains_needs" to "Captain Requests & Support",
        "admin_captains_needs_sub" to "Maintenance requests, battery charging, helmets, and bonus incentives",
        "admin_ai_marketing_title" to "AI Social Media & Ad Campaign Suite",
        "admin_ai_marketing_desc" to "Generate high-converting Egyptian dialect campaigns across Facebook, TikTok, Instagram & X",
        "admin_generate_ai_campaign" to "Generate New AI Social Campaign ✨",
        "admin_broadcast_title" to "Broadcast & Notification Center",
        "admin_broadcast_desc" to "Send promotional notifications to all customers and active captains",
        "admin_send_broadcast_btn" to "Send Broadcast Notification",

        // Web Portal
        "web_portal_title" to "Official Farascooter Egypt Portal",
        "web_portal_subtitle" to "The premier smart electric scooter ride & delivery platform across Cairo, Giza, and all Egypt",
        "web_download_app" to "Download Farascooter App Now",
        "web_download_android" to "Download for Android (Google Play)",
        "web_download_ios" to "Download for iPhone (App Store)",
        "web_download_apk" to "Download Direct APK",
        "web_launch_admin" to "Launch Admin Control Center",
        "web_why_scooter" to "Why Choose Scooter Rides in Egypt?",
        "web_reason_1_title" to "Half the Commute Time",
        "web_reason_1_desc" to "Bypass heavy congestion on 6th of October Bridge, Ring Road, and Mehwar effortlessly.",
        "web_reason_2_title" to "Save 50% on Fares",
        "web_reason_2_desc" to "Much more affordable than traditional cars, taxis, and ride-hailing apps.",
        "web_reason_3_title" to "Safety First & Sanitized Helmet",
        "web_reason_3_desc" to "Vetted captains, ride tracking, and a sanitized passenger helmet provided on every trip.",

        // Profile & Settings
        "profile_title" to "Profile & Settings",
        "language_exclusive_notice" to "Language Settings - You can change the app language exclusively from here (Changes apply instantly)",
        "arabic_option" to "العربية (Cairo Font)",
        "english_option" to "English (Modern Sans)",
        "wallet_balance" to "Wallet Balance",
        "add_funds" to "Add Funds",
        "trips_history" to "Ride & Order History",
        "help_and_support" to "Customer Support 24/7 (Egypt)",
        "logout" to "Sign Out",

        // Common
        "close" to "Close",
        "save" to "Save Changes",
        "back" to "Back",
        "minutes" to "min",
        "km" to "km"
    )
}
