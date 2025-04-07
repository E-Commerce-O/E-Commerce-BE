-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.4.28-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for cdweb
CREATE DATABASE IF NOT EXISTS `cdweb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `cdweb`;

-- Dumping structure for table cdweb.address
CREATE TABLE IF NOT EXISTS `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `distric_id` bigint(20) NOT NULL,
  `distric_name` varchar(255) DEFAULT NULL,
  `province_id` bigint(20) NOT NULL,
  `province_name` varchar(255) DEFAULT NULL,
  `ward_id` bigint(20) NOT NULL,
  `ward_name` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKda8tuywtf0gb6sedwk7la1pgi` (`user_id`),
  CONSTRAINT `FKda8tuywtf0gb6sedwk7la1pgi` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.address: ~0 rows (approximately)

-- Dumping structure for table cdweb.cart
CREATE TABLE IF NOT EXISTS `cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9emlp6m95v5er2bcqkjsw48he` (`user_id`),
  CONSTRAINT `FKl70asp4l4w0jmbm1tqyofho4o` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.cart: ~0 rows (approximately)

-- Dumping structure for table cdweb.cart_item
CREATE TABLE IF NOT EXISTS `cart_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `cart_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `product_color_id` bigint(20) DEFAULT NULL,
  `product_size_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1uobyhgl1wvgt1jpccia8xxs3` (`cart_id`),
  KEY `FKjcyd5wv4igqnw413rgxbfu4nv` (`product_id`),
  KEY `FKrgbw6vdh8jqbrmplf0g4u1eep` (`product_color_id`),
  KEY `FK7efcv6f1jpjksufhxm6klklr5` (`product_size_id`),
  CONSTRAINT `FK1uobyhgl1wvgt1jpccia8xxs3` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`id`),
  CONSTRAINT `FK7efcv6f1jpjksufhxm6klklr5` FOREIGN KEY (`product_size_id`) REFERENCES `product_size` (`id`),
  CONSTRAINT `FKjcyd5wv4igqnw413rgxbfu4nv` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKrgbw6vdh8jqbrmplf0g4u1eep` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.cart_item: ~0 rows (approximately)

-- Dumping structure for table cdweb.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.category: ~2 rows (approximately)
INSERT INTO `category` (`id`, `description`, `image_path`, `name`) VALUES
	(1, 'string', 'string', 'aosomi'),
	(2, 'Các thể loại quần', 'imagePath', 'Quần'),
	(3, 'Đây là áo thun', 'string', 'Áo thun');

-- Dumping structure for table cdweb.category_apply_of_vouhcer
CREATE TABLE IF NOT EXISTS `category_apply_of_vouhcer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) DEFAULT NULL,
  `voucher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2usg799ysvs71ippsh13dgt79` (`category_id`),
  KEY `FKcwocae8usq7nmve2mw4l286t4` (`voucher_id`),
  CONSTRAINT `FK2usg799ysvs71ippsh13dgt79` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),
  CONSTRAINT `FKcwocae8usq7nmve2mw4l286t4` FOREIGN KEY (`voucher_id`) REFERENCES `voucher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.category_apply_of_vouhcer: ~0 rows (approximately)

-- Dumping structure for table cdweb.orders
CREATE TABLE IF NOT EXISTS `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `delivery_method` varchar(255) DEFAULT NULL,
  `delivery_price` double NOT NULL,
  `status` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_detail_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK29ccmfm0xbvm1u1j0htr1qbxd` (`order_detail_id`),
  KEY `FKel9kyl84ego2otj2accfd8mr7` (`user_id`),
  CONSTRAINT `FK19p8j74c74j5717x1m1i8wsf3` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`),
  CONSTRAINT `FKel9kyl84ego2otj2accfd8mr7` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.orders: ~0 rows (approximately)

-- Dumping structure for table cdweb.order_detail
CREATE TABLE IF NOT EXISTS `order_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_decrease` double NOT NULL,
  `ship_decrease` double NOT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `product_voucher_id` bigint(20) DEFAULT NULL,
  `ship_voucher_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKq6bwjb8y711vixrxinko7wmnw` (`order_id`),
  KEY `FKworhp4pd7gwxpn8l493chbxe` (`address_id`),
  KEY `FKhs2ar2pk25769sfav1kdcnj0x` (`product_voucher_id`),
  KEY `FK9apsojf0und36k6muhwwcgadp` (`ship_voucher_id`),
  CONSTRAINT `FK9apsojf0und36k6muhwwcgadp` FOREIGN KEY (`ship_voucher_id`) REFERENCES `voucher` (`id`),
  CONSTRAINT `FKhs2ar2pk25769sfav1kdcnj0x` FOREIGN KEY (`product_voucher_id`) REFERENCES `voucher` (`id`),
  CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKworhp4pd7gwxpn8l493chbxe` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.order_detail: ~0 rows (approximately)

-- Dumping structure for table cdweb.order_item
CREATE TABLE IF NOT EXISTS `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `discount` int(11) NOT NULL,
  `original_price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product_color_id` bigint(20) DEFAULT NULL,
  `product_size_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKt4dc2r9nbvbujrljv3e23iibt` (`order_id`),
  KEY `FK551losx9j75ss5d6bfsqvijna` (`product_id`),
  KEY `FKbtxf5g8lvyw7pgwq713rtgagh` (`product_color_id`),
  KEY `FKhhtfmjxqj7awt1qmiydpp4n9e` (`product_size_id`),
  CONSTRAINT `FK551losx9j75ss5d6bfsqvijna` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKbtxf5g8lvyw7pgwq713rtgagh` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `FKhhtfmjxqj7awt1qmiydpp4n9e` FOREIGN KEY (`product_size_id`) REFERENCES `product_size` (`id`),
  CONSTRAINT `FKt4dc2r9nbvbujrljv3e23iibt` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.order_item: ~0 rows (approximately)

-- Dumping structure for table cdweb.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `published` bit(1) NOT NULL,
  `slug` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `default_discount` int(11) NOT NULL,
  `default_price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product: ~9 rows (approximately)
INSERT INTO `product` (`id`, `brand`, `created_at`, `description`, `name`, `published`, `slug`, `updated_at`, `category_id`, `default_discount`, `default_price`) VALUES
	(9, 'string', NULL, 'string', 'string', b'0', 'string', NULL, 1, 0, 0),
	(10, 'string', NULL, 'string', 'string', b'0', 'string', NULL, 1, 0, 0),
	(11, 'string', NULL, 'string', 'string', b'0', 'string', NULL, 1, 0, 0),
	(12, 'string', NULL, 'string', 'string', b'0', 'string', NULL, 1, 0, 0),
	(13, 'string', NULL, 'string', 'string', b'0', 'string', NULL, 1, 0, 0),
	(14, 'string', '2025-04-06 19:18:40.000000', 'Áo sơ mi', 'Áo sơ mi', b'0', 'ao_so_mi', NULL, 1, 0, 0),
	(15, 'string', '2025-04-06 19:25:33.000000', 'Áo sơ mi', 'Áo sơ mi', b'0', 'ao_so_mi', NULL, 1, 0, 150000),
	(16, 'string', '2025-04-06 19:26:57.000000', 'Áo sơ mi', 'Áo sơ mi', b'0', 'ao_so_mi', NULL, 1, 20, 150000),
	(17, 'Quang Híu', '2025-04-07 15:42:30.000000', 'Đây là quần tây', 'Quần tây', b'0', 'quang_tay', NULL, 2, 15, 200000),
	(18, 'Quang Híu', '2025-04-07 20:48:05.000000', 'Áo thun thoáng mát dành cho nam', 'Áo thun nam', b'0', 'ao_thun_nam', NULL, 3, 10, 75000);

-- Dumping structure for table cdweb.product_color
CREATE TABLE IF NOT EXISTS `product_color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `color_name` varchar(255) DEFAULT NULL,
  `color_code` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_color: ~26 rows (approximately)
INSERT INTO `product_color` (`id`, `color_name`, `color_code`) VALUES
	(1, 'string', NULL),
	(2, 'string', NULL),
	(3, 'string', NULL),
	(4, 'string', NULL),
	(5, 'string', NULL),
	(6, 'string', NULL),
	(8, 'red', NULL),
	(9, 'blue', NULL),
	(10, 'green', NULL),
	(11, 'red', NULL),
	(12, 'yellow', NULL),
	(13, 'blue', NULL),
	(14, 'red', NULL),
	(15, 'yellow', NULL),
	(16, 'blue', NULL),
	(17, 'red', NULL),
	(18, 'yellow', NULL),
	(19, 'blue', NULL),
	(20, 'Trắng', NULL),
	(21, 'Đen', NULL),
	(22, 'Xanh', NULL),
	(23, 'Trắng', NULL),
	(24, 'Đen', NULL),
	(25, 'Xanh', NULL),
	(26, 'Trắng', NULL),
	(27, 'Đen', NULL),
	(28, 'Xanh', NULL),
	(29, 'Đen', NULL),
	(30, 'Xám', NULL),
	(31, 'Trắng', '#123123'),
	(32, 'Đen', '#312321');

-- Dumping structure for table cdweb.product_colors
CREATE TABLE IF NOT EXISTS `product_colors` (
  `product_id` bigint(20) NOT NULL,
  `colors_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK7tlkx6ot4yy6ap0no9au3wb1r` (`colors_id`),
  KEY `FKrxtutgloy8nt7w2k50fnmu3ji` (`product_id`),
  CONSTRAINT `FKe1kwy2m1ytoxls9xgfhm7fo9o` FOREIGN KEY (`colors_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `FKrxtutgloy8nt7w2k50fnmu3ji` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_colors: ~23 rows (approximately)
INSERT INTO `product_colors` (`product_id`, `colors_id`) VALUES
	(9, 8),
	(9, 9),
	(9, 10),
	(10, 11),
	(10, 12),
	(10, 13),
	(11, 14),
	(11, 15),
	(11, 16),
	(13, 17),
	(13, 18),
	(13, 19),
	(14, 20),
	(14, 21),
	(14, 22),
	(15, 23),
	(15, 24),
	(15, 25),
	(16, 26),
	(16, 27),
	(16, 28),
	(17, 29),
	(17, 30),
	(18, 31),
	(18, 32);

-- Dumping structure for table cdweb.product_detail
CREATE TABLE IF NOT EXISTS `product_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `discount` int(11) NOT NULL,
  `price` double NOT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product_color_id` bigint(20) DEFAULT NULL,
  `product_size_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKilxoi77ctyin6jn9robktb16c` (`product_id`),
  KEY `FKqvxhhk9dsfufrdslh3dj416pw` (`product_color_id`),
  KEY `FKrj9c0x4mf6wuur1g16m2eqqta` (`product_size_id`),
  CONSTRAINT `FKilxoi77ctyin6jn9robktb16c` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKqvxhhk9dsfufrdslh3dj416pw` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `FKrj9c0x4mf6wuur1g16m2eqqta` FOREIGN KEY (`product_size_id`) REFERENCES `product_size` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_detail: ~41 rows (approximately)
INSERT INTO `product_detail` (`id`, `discount`, `price`, `product_id`, `product_color_id`, `product_size_id`) VALUES
	(1, 0, 0, NULL, 11, 5),
	(2, 0, 0, 11, 14, 6),
	(3, 0, 0, 11, 15, 6),
	(4, 0, 0, 11, 16, 6),
	(5, 0, 0, 13, 17, 7),
	(6, 0, 0, 13, 17, 8),
	(7, 0, 0, 13, 18, 7),
	(8, 0, 0, 13, 18, 8),
	(9, 0, 0, 13, 19, 7),
	(10, 0, 0, 13, 19, 8),
	(11, 0, 0, 14, 20, 9),
	(12, 0, 0, 14, 20, 10),
	(13, 0, 0, 14, 20, 11),
	(14, 0, 0, 14, 21, 9),
	(15, 0, 0, 14, 21, 10),
	(16, 0, 0, 14, 21, 11),
	(17, 0, 0, 14, 22, 9),
	(18, 0, 0, 14, 22, 10),
	(19, 0, 0, 14, 22, 11),
	(20, 0, 150000, 15, 23, 12),
	(21, 0, 150000, 15, 23, 13),
	(22, 0, 150000, 15, 23, 14),
	(23, 0, 150000, 15, 24, 12),
	(24, 0, 150000, 15, 24, 13),
	(25, 0, 150000, 15, 24, 14),
	(26, 0, 150000, 15, 25, 12),
	(27, 0, 150000, 15, 25, 13),
	(28, 0, 150000, 15, 25, 14),
	(29, 20, 150000, 16, 26, 15),
	(30, 20, 150000, 16, 26, 16),
	(31, 20, 150000, 16, 26, 17),
	(32, 20, 150000, 16, 27, 15),
	(33, 20, 150000, 16, 27, 16),
	(34, 20, 150000, 16, 27, 17),
	(35, 20, 150000, 16, 28, 15),
	(36, 20, 150000, 16, 28, 16),
	(37, 20, 150000, 16, 28, 17),
	(38, 25, 125000, 17, 29, 18),
	(39, 15, 200000, 17, 29, 19),
	(40, 15, 200000, 17, 30, 18),
	(41, 15, 200000, 17, 30, 19),
	(42, 10, 75000, 18, 31, 20),
	(43, 10, 75000, 18, 31, 21),
	(44, 10, 75000, 18, 32, 20),
	(45, 10, 75000, 18, 32, 21);

-- Dumping structure for table cdweb.product_image
CREATE TABLE IF NOT EXISTS `product_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_image: ~21 rows (approximately)
INSERT INTO `product_image` (`id`, `image_path`) VALUES
	(4, 'string'),
	(5, 'string'),
	(6, 'string'),
	(7, 'string'),
	(8, 'string'),
	(9, 'string'),
	(10, 'string'),
	(11, 'string'),
	(12, 'string'),
	(13, 'image2'),
	(14, 'image1'),
	(15, 'iamge2'),
	(16, 'image3'),
	(17, 'image1'),
	(18, 'iamge2'),
	(19, 'image3'),
	(20, 'image1'),
	(21, 'iamge2'),
	(22, 'image3'),
	(23, 'iamge1'),
	(24, 'image2'),
	(25, 'image1'),
	(26, 'image2');

-- Dumping structure for table cdweb.product_images
CREATE TABLE IF NOT EXISTS `product_images` (
  `product_id` bigint(20) NOT NULL,
  `images_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK3701am6d8us1lbn5v3j75yinr` (`images_id`),
  KEY `FKi8jnqq05sk5nkma3pfp3ylqrt` (`product_id`),
  CONSTRAINT `FKf0umfnj5k9xnadjjss6qced6h` FOREIGN KEY (`images_id`) REFERENCES `product_image` (`id`),
  CONSTRAINT `FKi8jnqq05sk5nkma3pfp3ylqrt` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_images: ~17 rows (approximately)
INSERT INTO `product_images` (`product_id`, `images_id`) VALUES
	(9, 8),
	(10, 9),
	(11, 10),
	(12, 11),
	(13, 12),
	(13, 13),
	(14, 14),
	(14, 15),
	(14, 16),
	(15, 17),
	(15, 18),
	(15, 19),
	(16, 20),
	(16, 21),
	(16, 22),
	(17, 23),
	(17, 24),
	(18, 25),
	(18, 26);

-- Dumping structure for table cdweb.product_import
CREATE TABLE IF NOT EXISTS `product_import` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `imported_at` datetime(6) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `product_color_id` bigint(20) DEFAULT NULL,
  `product_size_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKludugctmrtqoopcsb8jpqpsea` (`product_id`),
  KEY `FK9lavd38gy63bqs6tevdjl2oci` (`product_color_id`),
  KEY `FKhl0qtn6vae87fft8o2w35fn0a` (`product_size_id`),
  KEY `FKdhvthjfn2m3bgcy4l2lrk5d06` (`user_id`),
  CONSTRAINT `FK9lavd38gy63bqs6tevdjl2oci` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `FKdhvthjfn2m3bgcy4l2lrk5d06` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKhl0qtn6vae87fft8o2w35fn0a` FOREIGN KEY (`product_size_id`) REFERENCES `product_size` (`id`),
  CONSTRAINT `FKludugctmrtqoopcsb8jpqpsea` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_import: ~0 rows (approximately)

-- Dumping structure for table cdweb.product_review
CREATE TABLE IF NOT EXISTS `product_review` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `is_show` int(11) NOT NULL,
  `rating_score` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkaqmhakwt05p3n0px81b9pdya` (`product_id`),
  KEY `FK78cdr7qgrm9sp9igada7vk4xp` (`user_id`),
  CONSTRAINT `FK78cdr7qgrm9sp9igada7vk4xp` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkaqmhakwt05p3n0px81b9pdya` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_review: ~0 rows (approximately)

-- Dumping structure for table cdweb.product_size
CREATE TABLE IF NOT EXISTS `product_size` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `size` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_size: ~18 rows (approximately)
INSERT INTO `product_size` (`id`, `description`, `size`) VALUES
	(2, 'string', 'string'),
	(3, 'string', 'L'),
	(4, 'string', 'XL'),
	(5, 'string', 'XL'),
	(6, 'string', 'XL'),
	(7, 'string', 'XL'),
	(8, 'string', 'L'),
	(9, 'cho cân nặng từ 40-45kg', 'L'),
	(10, 'cho cân nặng từ 45-55kg', 'XL'),
	(11, 'cho cân nặng từ 55-70kg', 'XXL'),
	(12, 'cho cân nặng từ 40-45kg', 'L'),
	(13, 'cho cân nặng từ 45-55kg', 'XL'),
	(14, 'cho cân nặng từ 55-70kg', 'XXL'),
	(15, 'cho cân nặng từ 40-45kg', 'L'),
	(16, 'cho cân nặng từ 45-55kg', 'XL'),
	(17, 'cho cân nặng từ 55-70kg', 'XXL'),
	(18, 'Dành cho người từ 50-60kg', 'XL'),
	(19, 'Dành cho người từ 60-75kg', 'XXL'),
	(20, 'Dành cho nam nặng từ 45 - 55kg', 'L'),
	(21, 'Dành cho nam nặng từ 55- 70kg', 'XL');

-- Dumping structure for table cdweb.product_sizes
CREATE TABLE IF NOT EXISTS `product_sizes` (
  `product_id` bigint(20) NOT NULL,
  `sizes_id` bigint(20) NOT NULL,
  UNIQUE KEY `UK51qxp61oih4mjc27ob0ne93ej` (`sizes_id`),
  KEY `FK4w69qsh5hd062xv3hqkpgpdpu` (`product_id`),
  CONSTRAINT `FK4w69qsh5hd062xv3hqkpgpdpu` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKdkuro0gtnwv4onedldpu94esm` FOREIGN KEY (`sizes_id`) REFERENCES `product_size` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_sizes: ~17 rows (approximately)
INSERT INTO `product_sizes` (`product_id`, `sizes_id`) VALUES
	(9, 3),
	(9, 4),
	(10, 5),
	(11, 6),
	(13, 7),
	(13, 8),
	(14, 9),
	(14, 10),
	(14, 11),
	(15, 12),
	(15, 13),
	(15, 14),
	(16, 15),
	(16, 16),
	(16, 17),
	(17, 18),
	(17, 19),
	(18, 20),
	(18, 21);

-- Dumping structure for table cdweb.product_tag
CREATE TABLE IF NOT EXISTS `product_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `tag_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2rf7w3d88x20p7vuc2m9mvv91` (`product_id`),
  KEY `FKmkwenho1ceh0xlwoq9e5xdmhe` (`tag_name`),
  CONSTRAINT `FK2rf7w3d88x20p7vuc2m9mvv91` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKmkwenho1ceh0xlwoq9e5xdmhe` FOREIGN KEY (`tag_name`) REFERENCES `tag` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_tag: ~4 rows (approximately)
INSERT INTO `product_tag` (`id`, `product_id`, `tag_name`) VALUES
	(1, 17, 'Quần tây'),
	(2, 17, 'Quần'),
	(3, 17, 'Đồ nam'),
	(4, 17, 'Freeship'),
	(5, 18, 'Đồ nam'),
	(6, 18, 'Áo thun'),
	(7, 18, 'Thoáng mát');

-- Dumping structure for table cdweb.refresh_token
CREATE TABLE IF NOT EXISTS `refresh_token` (
  `refresh_token` varchar(255) NOT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `exprired_at` datetime(6) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.refresh_token: ~0 rows (approximately)

-- Dumping structure for table cdweb.tag
CREATE TABLE IF NOT EXISTS `tag` (
  `name` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.tag: ~4 rows (approximately)
INSERT INTO `tag` (`name`, `description`) VALUES
	('Áo thun', NULL),
	('Freeship', NULL),
	('Quần', NULL),
	('Quần tây', NULL),
	('Thoáng mát', NULL),
	('Đồ nam', NULL);

-- Dumping structure for table cdweb.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avt_path` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKko17hhca8tc1abp8so09fsygo` (`username`,`email`,`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.user: ~1 rows (approximately)
INSERT INTO `user` (`id`, `avt_path`, `created_at`, `date_of_birth`, `email`, `full_name`, `gender`, `password`, `phone_number`, `role`, `updated_at`, `username`) VALUES
	(1, 'https://i.imgur.com/W60xqJf.png', NULL, NULL, 'admin@min.ad', 'Ăi VÄƒn Min', 0, '$2a$10$Cgk.Cf8DXnZnHaIY9Ap4w.MTNrdQrMqGaaUo09Q66sCQOm0I7siSK', NULL, 'ADMIN', NULL, 'admin');

-- Dumping structure for table cdweb.voucher
CREATE TABLE IF NOT EXISTS `voucher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `end_at` datetime(6) DEFAULT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `max_decrease` int(11) NOT NULL,
  `min_price` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `percent_decrease` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `start_at` datetime(6) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.voucher: ~0 rows (approximately)

-- Dumping structure for table cdweb.wishlist_item
CREATE TABLE IF NOT EXISTS `wishlist_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5s5jxai41c8tqklyy111ngqh7` (`product_id`),
  KEY `FKbsjwaanb89g3yvyetvkn67u6m` (`user_id`),
  CONSTRAINT `FK5s5jxai41c8tqklyy111ngqh7` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FKbsjwaanb89g3yvyetvkn67u6m` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.wishlist_item: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
