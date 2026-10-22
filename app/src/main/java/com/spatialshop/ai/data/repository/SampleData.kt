package com.spatialshop.ai.data.repository

import com.spatialshop.ai.domain.model.Category
import com.spatialshop.ai.domain.model.FurnitureCollection
import com.spatialshop.ai.domain.model.Product

/**
 * Curated product catalog containing ONLY the exact 15 furniture items
 * corresponding directly to the provided 3D GLB models.
 * All prices in Indian Rupees (₹).
 * Dimensions in centimeters.
 * Colors are empty (no color choices, exact model texture used).
 */
object SampleData {

    val categories = listOf(
        Category("living_room", "Living Room", "sofa", "", 7),
        Category("bedroom", "Bedroom", "bed", "", 3),
        Category("dining_kitchen", "Dining & Kitchen", "dining", "", 2),
        Category("office", "Office", "desk", "", 3)
    )

    val collections = listOf(
        FurnitureCollection("col_living", "Living Room", "", 7, "Handcrafted furniture for your living space"),
        FurnitureCollection("col_bedroom", "Bedroom", "", 3, "Comfortable bedroom essentials"),
        FurnitureCollection("col_dining", "Dining & Kitchen", "", 2, "Elegant dining furniture"),
        FurnitureCollection("col_office", "Office", "", 3, "Modern ergonomic office setups")
    )

    val products = listOf(
        // === LIVING ROOM (7 Products) ===
        Product(
            id = "prod_001",
            name = "Deep Red Velvet Armchair",
            category = "Living Room",
            subcategory = "Chairs",
            price = 14999.0,
            originalPrice = 18999.0,
            discountPercent = 21,
            description = "Handcrafted deep red velvet armchair with plush cushioning, tapered wooden legs, and an ergonomic curved backrest. Perfect statement piece for modern living spaces.",
            images = listOf("https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 82.0,
            depthCm = 85.0,
            heightCm = 88.0,
            rating = 4.8,
            reviewCount = 145,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/armchair-velvet",
            modelUrl = "models/armchair_velvet.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_002",
            name = "Modern 3-Seater Sofa",
            category = "Living Room",
            subcategory = "Sofas",
            price = 34999.0,
            originalPrice = 44999.0,
            discountPercent = 22,
            description = "Spacious three-seater sofa with high-resilience foam cushioning, durable textured fabric upholstery, and a reinforced hardwood frame.",
            images = listOf("https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 210.0,
            depthCm = 90.0,
            heightCm = 85.0,
            rating = 4.9,
            reviewCount = 312,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/sofa-3seater",
            modelUrl = "models/sofa_3seater.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_003",
            name = "Nordic Wood Coffee Table",
            category = "Living Room",
            subcategory = "Tables",
            price = 8499.0,
            originalPrice = 11999.0,
            discountPercent = 29,
            description = "Minimalist Nordic style coffee table made from solid natural oak with smooth beveled edges and a lower storage shelf.",
            images = listOf("https://images.unsplash.com/photo-1533090481720-856c6e3c1fdc?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 110.0,
            depthCm = 60.0,
            heightCm = 45.0,
            rating = 4.7,
            reviewCount = 98,
            merchantName = "NordicLiving India",
            productUrl = "https://example.com/coffee-table",
            modelUrl = "models/coffee_table.glb",
            tags = listOf("popular")
        ),
        Product(
            id = "prod_004",
            name = "Solid Oak Accent Chair",
            category = "Living Room",
            subcategory = "Chairs",
            price = 5999.0,
            originalPrice = 7999.0,
            discountPercent = 25,
            description = "Artisanal solid oak chair with contoured seat and ergonomic back slats. Built with traditional mortise-and-tenon joinery.",
            images = listOf("https://images.unsplash.com/photo-1503602642458-232111445657?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 55.0,
            depthCm = 52.0,
            heightCm = 82.0,
            rating = 4.6,
            reviewCount = 76,
            merchantName = "WoodCraft Artisans",
            productUrl = "https://example.com/oak-chair",
            modelUrl = "models/chair_wooden_oak.glb",
            tags = listOf("classic")
        ),
        Product(
            id = "prod_005",
            name = "Contemporary Round Side Table",
            category = "Living Room",
            subcategory = "Tables",
            price = 4799.0,
            originalPrice = 6499.0,
            discountPercent = 26,
            description = "Compact circular end table with a brushed metal pedestal base and smooth matte surface. Fits effortlessly beside sofas and beds.",
            images = listOf("https://images.unsplash.com/photo-1532372320572-cda25653a26d?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 50.0,
            depthCm = 50.0,
            heightCm = 55.0,
            rating = 4.5,
            reviewCount = 64,
            merchantName = "NordicLiving India",
            productUrl = "https://example.com/round-side-table",
            modelUrl = "models/side_table_round.glb",
            tags = listOf("popular")
        ),
        Product(
            id = "prod_006",
            name = "Modern TV Console Unit",
            category = "Living Room",
            subcategory = "Media Storage",
            price = 18999.0,
            originalPrice = 24999.0,
            discountPercent = 24,
            description = "Low-profile entertainment unit designed for TVs up to 65 inches. Features cable management ports and dual storage compartments.",
            images = listOf("https://images.unsplash.com/photo-1594026112284-02bb6f3352fe?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 160.0,
            depthCm = 42.0,
            heightCm = 50.0,
            rating = 4.8,
            reviewCount = 118,
            merchantName = "UrbanCraft Furniture",
            productUrl = "https://example.com/tv-console",
            modelUrl = "models/tv_console.glb",
            tags = listOf("featured")
        ),
        Product(
            id = "prod_007",
            name = "Tall Wooden Bookshelf",
            category = "Living Room",
            subcategory = "Storage",
            price = 16499.0,
            originalPrice = 21999.0,
            discountPercent = 25,
            description = "Five-tier open shelving bookcase built from sustainable engineered wood with anti-tip wall anchoring hardware included.",
            images = listOf("https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 90.0,
            depthCm = 35.0,
            heightCm = 180.0,
            rating = 4.7,
            reviewCount = 89,
            merchantName = "WoodCraft Artisans",
            productUrl = "https://example.com/wooden-bookshelf",
            modelUrl = "models/bookshelf_wooden.glb",
            tags = listOf("storage")
        ),

        // === BEDROOM (3 Products) ===
        Product(
            id = "prod_008",
            name = "Queen Bed Frame - Monarch",
            category = "Bedroom",
            subcategory = "Beds",
            price = 28999.0,
            originalPrice = 36999.0,
            discountPercent = 21,
            description = "Contemporary queen bed frame featuring an upholstered linen headboard, sturdy steel support slats, and noise-reducing wooden legs.",
            images = listOf("https://images.unsplash.com/photo-1505693416388-ac5ce068fe85?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 160.0,
            depthCm = 205.0,
            heightCm = 110.0,
            rating = 4.9,
            reviewCount = 205,
            merchantName = "SleepWell Studios",
            productUrl = "https://example.com/queen-bed",
            modelUrl = "models/queen_bed.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_009",
            name = "3-Door Bedroom Wardrobe",
            category = "Bedroom",
            subcategory = "Wardrobes",
            price = 32999.0,
            originalPrice = 42999.0,
            discountPercent = 23,
            description = "Spacious three-door wardrobe with full-height hanging rails, customizable shelving, and bottom accessory drawers.",
            images = listOf("https://images.unsplash.com/photo-1558997519-83ea9252edf8?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 135.0,
            depthCm = 58.0,
            heightCm = 195.0,
            rating = 4.8,
            reviewCount = 134,
            merchantName = "SleepWell Studios",
            productUrl = "https://example.com/wardrobe-3door",
            modelUrl = "models/wardrobe_3door.glb",
            tags = listOf("storage")
        ),
        Product(
            id = "prod_010",
            name = "Minimalist Dressing Table with Mirror",
            category = "Bedroom",
            subcategory = "Vanities",
            price = 15999.0,
            originalPrice = 19999.0,
            discountPercent = 20,
            description = "Elegant vanity table with circular mirror and dual glide drawers for cosmetics, jewelry, and daily grooming essentials.",
            images = listOf("https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 100.0,
            depthCm = 45.0,
            heightCm = 140.0,
            rating = 4.7,
            reviewCount = 92,
            merchantName = "SleepWell Studios",
            productUrl = "https://example.com/dressing-table",
            modelUrl = "models/dressing_table.glb",
            tags = listOf("popular")
        ),

        // === DINING & KITCHEN (2 Products) ===
        Product(
            id = "prod_011",
            name = "Solid Wood 6-Seater Dining Table",
            category = "Dining & Kitchen",
            subcategory = "Dining Sets",
            price = 26999.0,
            originalPrice = 34999.0,
            discountPercent = 22,
            description = "Hand-finished solid sheesham wood dining table accommodating six people comfortably. Protected with a heat and stain-resistant satin seal.",
            images = listOf("https://images.unsplash.com/photo-1615066390971-03e4e1c36ddf?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 160.0,
            depthCm = 90.0,
            heightCm = 76.0,
            rating = 4.8,
            reviewCount = 143,
            merchantName = "WoodCraft Artisans",
            productUrl = "https://example.com/dining-table",
            modelUrl = "models/dining_table.glb",
            tags = listOf("bestseller")
        ),
        Product(
            id = "prod_012",
            name = "Wall-Mounted White Laminate Cabinet",
            category = "Dining & Kitchen",
            subcategory = "Cabinets",
            price = 11499.0,
            originalPrice = 14999.0,
            discountPercent = 23,
            description = "Sleek floating wall cabinet with dual touch-latch opening doors, scratch-resistant laminate surface, and heavy-duty concealed mounting brackets.",
            images = listOf("https://images.unsplash.com/photo-1595428774223-ef52624120d2?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 120.0,
            depthCm = 35.0,
            heightCm = 60.0,
            rating = 4.6,
            reviewCount = 67,
            merchantName = "ModernHaus India",
            productUrl = "https://example.com/wall-cabinet",
            modelUrl = "models/cabinet_wall_mounted.glb",
            tags = listOf("storage")
        ),

        // === OFFICE (3 Products) ===
        Product(
            id = "prod_013",
            name = "Pro-Ergonomic High-Back Office Chair",
            category = "Office",
            subcategory = "Chairs",
            price = 13499.0,
            originalPrice = 18999.0,
            discountPercent = 28,
            description = "Adjustable mesh office chair with dynamic lumbar support, 3D armrests, pneumatic height control, and smooth polyurethane caster wheels.",
            images = listOf("https://images.unsplash.com/photo-1580481077195-c22ae2b6d859?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 65.0,
            depthCm = 65.0,
            heightCm = 118.0,
            rating = 4.9,
            reviewCount = 280,
            merchantName = "ErgoWork India",
            productUrl = "https://example.com/ergonomic-chair",
            modelUrl = "models/office_chair_ergonomic.glb",
            tags = listOf("bestseller", "featured")
        ),
        Product(
            id = "prod_014",
            name = "Executive Modern Office Desk",
            category = "Office",
            subcategory = "Desks",
            price = 17999.0,
            originalPrice = 23999.0,
            discountPercent = 25,
            description = "Professional workstation with sturdy powder-coated steel legs, dual monitor grommets, and an expansive scratch-resistant desktop.",
            images = listOf("https://images.unsplash.com/photo-1518455027359-f3f8164ba6bd?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 140.0,
            depthCm = 70.0,
            heightCm = 75.0,
            rating = 4.8,
            reviewCount = 152,
            merchantName = "ErgoWork India",
            productUrl = "https://example.com/office-desk",
            modelUrl = "models/office_desk.glb",
            tags = listOf("featured")
        ),
        Product(
            id = "prod_015",
            name = "3-Drawer Metal Filing Cabinet",
            category = "Office",
            subcategory = "Storage",
            price = 8999.0,
            originalPrice = 11499.0,
            discountPercent = 21,
            description = "Heavy-gauge steel filing cabinet with anti-tilt interlocking drawers, full-extension ball-bearing runners, and a central security lock.",
            images = listOf("https://images.unsplash.com/photo-1507089947368-19c1da9775ae?w=800&auto=format&fit=crop&q=80"),
            colors = emptyList(),
            widthCm = 45.0,
            depthCm = 55.0,
            heightCm = 102.0,
            rating = 4.6,
            reviewCount = 74,
            merchantName = "ErgoWork India",
            productUrl = "https://example.com/filing-cabinet",
            modelUrl = "models/filing_cabinet.glb",
            tags = listOf("storage")
        )
    )

    fun getProductById(id: String): Product? = products.find { it.id == id }

    fun getProductsByCategory(category: String): List<Product> {
        val normalized = category.lowercase().replace(" ", "_").replace("&", "").replace("__", "_").trim('_')
        return products.filter {
            val catNormalized = it.category.lowercase().replace(" ", "_").replace("&", "").replace("__", "_").trim('_')
            catNormalized == normalized || it.category.equals(category, ignoreCase = true)
        }
    }
}
