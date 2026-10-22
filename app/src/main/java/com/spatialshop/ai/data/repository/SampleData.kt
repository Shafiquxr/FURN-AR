package com.spatialshop.ai.data.repository

import com.spatialshop.ai.domain.model.Category
import com.spatialshop.ai.domain.model.FurnitureCollection
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor

/**
 * Sample product data for the initial catalog.
 * All prices in Indian Rupees (₹).
 * Dimensions in centimeters.
 */
object SampleData {

    val categories = listOf(
        Category("living_room", "Living Room", "sofa", "", 8),
        Category("bedroom", "Bedroom", "bed", "", 4),
        Category("dining_kitchen", "Dining & Kitchen", "dining", "", 3),
        Category("office", "Office", "desk", "", 3)
    )

    val collections = listOf(
        FurnitureCollection("col_living", "Living Room", "", 120, "Stylish furniture for your living space"),
        FurnitureCollection("col_bedroom", "Bedroom", "", 95, "Comfortable bedroom essentials"),
        FurnitureCollection("col_dining", "Dining & Kitchen", "", 68, "Elegant dining furniture"),
        FurnitureCollection("col_office", "Office", "", 54, "Modern office setups")
    )

    val products = listOf(
        // === LIVING ROOM ===
        Product(
            id = "prod_001",
            name = "Delphine Velvet Accent Chair",
            category = "Living Room",
            subcategory = "Chairs",
            price = 9499.0,
            originalPrice = 12999.0,
            discountPercent = 27,
            description = "The Delphine Accent Chair blends modern elegance with everyday comfort. Upholstered in soft velvet with a sturdy solid wood frame, it's the perfect addition to any living space. Features tapered wooden legs and a plush cushioned seat for hours of comfortable seating.",
            images = listOf(
                "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=600&auto=format&fit=crop&q=80",
                "https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=600&auto=format&fit=crop&q=80"
            ),
            colors = listOf(
                ProductColor("Mustard Yellow", "#E8A020"),
                ProductColor("Ivory Cream", "#D4C5A9"),
                ProductColor("Slate Gray", "#6B6B6B"),
                ProductColor("Forest Green", "#2E5D3C")
            ),
            widthCm = 78.0,
            depthCm = 76.0,
            heightCm = 72.0,
            rating = 4.8,
            reviewCount = 145,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/delphine-chair",
            modelUrl = "models/armchair_velvet.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_002",
            name = "Minimalist Lounge Chair",
            category = "Living Room",
            subcategory = "Chairs",
            price = 8999.0,
            originalPrice = 11499.0,
            discountPercent = 22,
            description = "A sleek minimalist lounge chair crafted with premium fabric and a lightweight metal frame. Its clean lines and ergonomic design make it a perfect fit for contemporary living rooms.",
            images = listOf("https://images.unsplash.com/photo-1567538096630-e0c55bd6374c?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Charcoal Black", "#333333"),
                ProductColor("Light Gray", "#C0C0C0"),
                ProductColor("Teal Blue", "#367588")
            ),
            widthCm = 65.0,
            depthCm = 70.0,
            heightCm = 80.0,
            rating = 4.6,
            reviewCount = 320,
            merchantName = "ModernHaus India",
            productUrl = "https://example.com/minimalist-lounge",
            modelUrl = "models/chair_wooden_oak.glb",
            tags = listOf("bestseller")
        ),
        Product(
            id = "prod_003",
            name = "Vintage Wooden Chair",
            category = "Living Room",
            subcategory = "Chairs",
            price = 7599.0,
            originalPrice = 9999.0,
            discountPercent = 24,
            description = "Handcrafted vintage wooden chair with a curved backrest and natural wood finish. Made from sustainable sheesham wood with traditional joinery techniques.",
            images = listOf("https://images.unsplash.com/photo-1503602642458-232111445657?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Natural Wood", "#B8860B"),
                ProductColor("Walnut Brown", "#5C4033"),
                ProductColor("Honey Oak", "#C49A6C")
            ),
            widthCm = 55.0,
            depthCm = 58.0,
            heightCm = 82.0,
            rating = 4.5,
            reviewCount = 210,
            merchantName = "Artisan Woodcraft",
            productUrl = "https://example.com/vintage-chair",
            modelUrl = "models/chair_wooden_oak.glb",
            tags = listOf("bestseller")
        ),
        Product(
            id = "prod_004",
            name = "Modern 3-Seater Sofa",
            category = "Living Room",
            subcategory = "Sofas",
            price = 24999.0,
            originalPrice = 34999.0,
            discountPercent = 29,
            description = "Luxurious 3-seater sofa with high-density foam cushions and premium leatherette upholstery. Features a solid teak wood frame and chrome-finished legs for a modern aesthetic.",
            images = listOf("https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Cream White", "#F5F5DC"),
                ProductColor("Midnight Blue", "#191970"),
                ProductColor("Burgundy Red", "#800020")
            ),
            widthCm = 200.0,
            depthCm = 85.0,
            heightCm = 82.0,
            rating = 4.9,
            reviewCount = 420,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/modern-sofa",
            modelUrl = "models/sofa_3seater.glb",
            tags = listOf("featured", "premium")
        ),
        Product(
            id = "prod_005",
            name = "Compact Coffee Table",
            category = "Living Room",
            subcategory = "Tables",
            price = 5499.0,
            originalPrice = 7999.0,
            discountPercent = 31,
            description = "A stylish compact coffee table with a marble-finish top and gold-toned metal legs. Perfect for small living rooms and apartments.",
            images = listOf("https://images.unsplash.com/photo-1533090161767-e6ffed986c88?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("White Marble", "#F0EDE5"),
                ProductColor("Black Marble", "#2C2C2C")
            ),
            widthCm = 90.0,
            depthCm = 50.0,
            heightCm = 42.0,
            rating = 4.4,
            reviewCount = 178,
            merchantName = "HomeDécor India",
            productUrl = "https://example.com/coffee-table",
            modelUrl = "models/coffee_table.glb",
            tags = listOf("deal")
        ),
        Product(
            id = "prod_006",
            name = "Round Side Table",
            category = "Living Room",
            subcategory = "Tables",
            price = 3299.0,
            originalPrice = 4999.0,
            discountPercent = 34,
            description = "Elegant round side table with a wooden top and powder-coated metal frame. Lightweight and easy to move around.",
            images = listOf("https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Natural Oak", "#C49A6C"),
                ProductColor("Dark Walnut", "#3E2723")
            ),
            widthCm = 45.0,
            depthCm = 45.0,
            heightCm = 55.0,
            rating = 4.3,
            reviewCount = 92,
            merchantName = "ModernHaus India",
            productUrl = "https://example.com/round-side-table",
            modelUrl = "models/side_table_round.glb"
        ),
        Product(
            id = "prod_007",
            name = "TV Console Unit",
            category = "Living Room",
            subcategory = "Storage",
            price = 12999.0,
            originalPrice = 17999.0,
            discountPercent = 28,
            description = "Sleek TV console with open shelving and concealed cable management. Made from engineered wood with a matte laminate finish.",
            images = listOf("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Walnut Brown", "#5C4033"),
                ProductColor("White Oak", "#E8DCC8")
            ),
            widthCm = 140.0,
            depthCm = 40.0,
            heightCm = 50.0,
            rating = 4.6,
            reviewCount = 156,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/tv-console",
            modelUrl = "models/tv_console.glb"
        ),
        Product(
            id = "prod_008",
            name = "Wooden Bookshelf",
            category = "Living Room",
            subcategory = "Storage",
            price = 8499.0,
            originalPrice = 11999.0,
            discountPercent = 29,
            description = "A 5-tier open bookshelf in solid sheesham wood with a warm honey finish. Industrial-style design with clean geometric lines.",
            images = listOf("https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Honey Finish", "#C49A6C"),
                ProductColor("Dark Walnut", "#3E2723")
            ),
            widthCm = 80.0,
            depthCm = 30.0,
            heightCm = 180.0,
            rating = 4.5,
            reviewCount = 134,
            merchantName = "Artisan Woodcraft",
            productUrl = "https://example.com/bookshelf",
            modelUrl = "models/bookshelf_wooden.glb"
        ),

        // === BEDROOM ===
        Product(
            id = "prod_009",
            name = "Queen Bed Frame – Monarch",
            category = "Bedroom",
            subcategory = "Beds",
            price = 18999.0,
            originalPrice = 25999.0,
            discountPercent = 27,
            description = "The Monarch queen bed frame features a tufted upholstered headboard and solid wood slat support. No box spring needed. Available in multiple fabric colors.",
            images = listOf("https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Light Gray", "#C0C0C0"),
                ProductColor("Navy Blue", "#1B3A5C"),
                ProductColor("Blush Pink", "#DE98AB")
            ),
            widthCm = 160.0,
            depthCm = 200.0,
            heightCm = 100.0,
            rating = 4.7,
            reviewCount = 289,
            merchantName = "SleepWell Home",
            productUrl = "https://example.com/queen-bed",
            modelUrl = "models/queen_bed.glb",
            tags = listOf("premium")
        ),
        Product(
            id = "prod_010",
            name = "Bedside Table – Luna",
            category = "Bedroom",
            subcategory = "Tables",
            price = 3999.0,
            originalPrice = 5999.0,
            discountPercent = 33,
            description = "Compact bedside table with two drawers and a natural wood finish. Features soft-close drawer mechanisms and rounded edges.",
            images = listOf("https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Natural Wood", "#B8860B"),
                ProductColor("White", "#FFFFFF")
            ),
            widthCm = 45.0,
            depthCm = 40.0,
            heightCm = 55.0,
            rating = 4.4,
            reviewCount = 167,
            merchantName = "HomeDécor India",
            productUrl = "https://example.com/bedside-table",
            modelUrl = "models/side_table_round.glb"
        ),
        Product(
            id = "prod_011",
            name = "3-Door Wardrobe – Aria",
            category = "Bedroom",
            subcategory = "Wardrobes",
            price = 22999.0,
            originalPrice = 29999.0,
            discountPercent = 23,
            description = "Spacious 3-door wardrobe with mirror panel, hanging rods, and adjustable shelves. Engineered wood with premium laminate finish.",
            images = listOf("https://images.unsplash.com/photo-1558997519-83ea9252def8?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Classic Walnut", "#5C4033"),
                ProductColor("Alpine White", "#F0F0F0")
            ),
            widthCm = 135.0,
            depthCm = 55.0,
            heightCm = 200.0,
            rating = 4.6,
            reviewCount = 198,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/wardrobe",
            modelUrl = "models/wardrobe_3door.glb",
            tags = listOf("premium")
        ),
        Product(
            id = "prod_012",
            name = "Dressing Table – Vanity Pro",
            category = "Bedroom",
            subcategory = "Dressing Tables",
            price = 9999.0,
            originalPrice = 13999.0,
            discountPercent = 29,
            description = "Modern dressing table with LED-lit mirror, three storage drawers, and a cushioned stool. Perfect for daily grooming routines.",
            images = listOf("https://images.unsplash.com/photo-1616486338812-3dadae4b4ace?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Matte White", "#F5F5F5"),
                ProductColor("Rose Gold Accent", "#B76E79")
            ),
            widthCm = 100.0,
            depthCm = 45.0,
            heightCm = 140.0,
            rating = 4.8,
            reviewCount = 234,
            merchantName = "ModernHaus India",
            productUrl = "https://example.com/dressing-table",
            modelUrl = "models/dressing_table.glb"
        ),

        // === DINING & KITCHEN ===
        Product(
            id = "prod_013",
            name = "4-Seater Dining Table – Haven",
            category = "Dining & Kitchen",
            subcategory = "Dining Tables",
            price = 11999.0,
            originalPrice = 15999.0,
            discountPercent = 25,
            description = "Elegant 4-seater dining table with a solid mango wood top and tapered legs. Features a food-safe lacquer finish.",
            images = listOf("https://images.unsplash.com/photo-1615066390971-03e4e1c36ddf?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Natural Mango", "#C49A6C"),
                ProductColor("Dark Mahogany", "#420D09")
            ),
            widthCm = 120.0,
            depthCm = 75.0,
            heightCm = 76.0,
            rating = 4.7,
            reviewCount = 165,
            merchantName = "Artisan Woodcraft",
            productUrl = "https://example.com/dining-table",
            modelUrl = "models/dining_table.glb",
            tags = listOf("featured")
        ),
        Product(
            id = "prod_014",
            name = "Dining Chair Set (Set of 2)",
            category = "Dining & Kitchen",
            subcategory = "Dining Chairs",
            price = 6499.0,
            originalPrice = 8999.0,
            discountPercent = 28,
            description = "Set of 2 dining chairs with curved wooden backrests and padded PU leather seats. Ergonomic design for comfortable dining.",
            images = listOf("https://images.unsplash.com/photo-1503602642458-232111445657?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Tan Leather", "#D2B48C"),
                ProductColor("Black", "#1A1A1A")
            ),
            widthCm = 45.0,
            depthCm = 52.0,
            heightCm = 88.0,
            rating = 4.5,
            reviewCount = 123,
            merchantName = "HomeDécor India",
            productUrl = "https://example.com/dining-chairs",
            modelUrl = "models/chair_wooden_oak.glb"
        ),
        Product(
            id = "prod_015",
            name = "Kitchen Storage Rack",
            category = "Dining & Kitchen",
            subcategory = "Storage",
            price = 4999.0,
            originalPrice = 6999.0,
            discountPercent = 29,
            description = "4-tier kitchen storage rack with powder-coated steel frame and bamboo shelves. Perfect for organizing pantry essentials.",
            images = listOf("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Black & Bamboo", "#2C2C2C"),
                ProductColor("White & Bamboo", "#F0F0F0")
            ),
            widthCm = 60.0,
            depthCm = 35.0,
            heightCm = 150.0,
            rating = 4.3,
            reviewCount = 87,
            merchantName = "HomeDécor India",
            productUrl = "https://example.com/kitchen-rack",
            modelUrl = "models/cabinet_wall_mounted.glb"
        ),

        // === OFFICE ===
        Product(
            id = "prod_016",
            name = "Office Desk – Zenith",
            category = "Office",
            subcategory = "Desks",
            price = 7999.0,
            originalPrice = 10999.0,
            discountPercent = 27,
            description = "A spacious work desk with built-in cable management, monitor riser, and two side drawers. Made from durable engineered wood with a scratch-resistant surface.",
            images = listOf("https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Oak & White", "#E8DCC8"),
                ProductColor("Walnut & Black", "#3E2723")
            ),
            widthCm = 120.0,
            depthCm = 60.0,
            heightCm = 75.0,
            rating = 4.6,
            reviewCount = 245,
            merchantName = "WorkSpace Pro",
            productUrl = "https://example.com/office-desk",
            modelUrl = "models/office_desk.glb",
            tags = listOf("featured")
        ),
        Product(
            id = "prod_017",
            name = "Ergonomic Office Chair – Aero",
            category = "Office",
            subcategory = "Chairs",
            price = 12999.0,
            originalPrice = 16999.0,
            discountPercent = 24,
            description = "High-back ergonomic chair with adjustable lumbar support, headrest, and 3D armrests. Breathable mesh back with memory foam seat.",
            images = listOf("https://images.unsplash.com/photo-1580481077195-c3a821a58875?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Black Mesh", "#1A1A1A"),
                ProductColor("Gray Mesh", "#6B6B6B"),
                ProductColor("Blue Mesh", "#2E4A8E")
            ),
            widthCm = 65.0,
            depthCm = 65.0,
            heightCm = 115.0,
            rating = 4.8,
            reviewCount = 389,
            merchantName = "WorkSpace Pro",
            productUrl = "https://example.com/ergonomic-chair",
            modelUrl = "models/office_chair_ergonomic.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_018",
            name = "Filing Cabinet – 3-Drawer",
            category = "Office",
            subcategory = "Storage",
            price = 5499.0,
            originalPrice = 7499.0,
            discountPercent = 27,
            description = "Compact 3-drawer filing cabinet with lock mechanism and smooth ball-bearing slides. Powder-coated steel construction.",
            images = listOf("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=600&auto=format&fit=crop&q=80"),
            colors = listOf(
                ProductColor("Matte Black", "#1A1A1A"),
                ProductColor("White", "#F5F5F5")
            ),
            widthCm = 40.0,
            depthCm = 50.0,
            heightCm = 65.0,
            rating = 4.2,
            reviewCount = 76,
            merchantName = "WorkSpace Pro",
            productUrl = "https://example.com/filing-cabinet",
            modelUrl = "models/filing_cabinet.glb"
        )
    )

    fun getProductById(id: String): Product? = products.find { it.id == id }

    fun getProductsByCategory(category: String): List<Product> =
        products.filter { it.category.equals(category, ignoreCase = true) }

    fun getBestSellers(): List<Product> =
        products.filter { "bestseller" in it.tags }.take(6)

    fun getFeaturedProducts(): List<Product> =
        products.filter { "featured" in it.tags }

    fun getDeals(): List<Product> =
        products.filter { (it.discountPercent ?: 0) >= 25 }.sortedByDescending { it.discountPercent }

    fun searchProducts(query: String): List<Product> =
        products.filter {
            it.name.contains(query, ignoreCase = true) ||
            it.category.contains(query, ignoreCase = true) ||
            it.description.contains(query, ignoreCase = true)
        }
}
