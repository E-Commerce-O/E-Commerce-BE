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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.category: ~0 rows (approximately)

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
  `user_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `delivery_method` varchar(255) DEFAULT NULL,
  `delivery_price` double DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `order_detail_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK29ccmfm0xbvm1u1j0htr1qbxd` (`order_detail_id`),
  KEY `FK_order_user` (`user_id`),
  CONSTRAINT `FK19p8j74c74j5717x1m1i8wsf3` FOREIGN KEY (`order_detail_id`) REFERENCES `order_detail` (`id`),
  CONSTRAINT `FK_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  KEY `FKworhp4pd7gwxpn8l493chbxe` (`address_id`),
  KEY `FKhs2ar2pk25769sfav1kdcnj0x` (`product_voucher_id`),
  KEY `FK9apsojf0und36k6muhwwcgadp` (`ship_voucher_id`),
  KEY `order_id` (`order_id`),
  CONSTRAINT `FK9apsojf0und36k6muhwwcgadp` FOREIGN KEY (`ship_voucher_id`) REFERENCES `voucher` (`id`),
  CONSTRAINT `FK_order_detail_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKhs2ar2pk25769sfav1kdcnj0x` FOREIGN KEY (`product_voucher_id`) REFERENCES `voucher` (`id`),
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
  KEY `FK551losx9j75ss5d6bfsqvijna` (`product_id`),
  KEY `FKbtxf5g8lvyw7pgwq713rtgagh` (`product_color_id`),
  KEY `FKhhtfmjxqj7awt1qmiydpp4n9e` (`product_size_id`),
  KEY `FK_order_item_order` (`order_id`),
  CONSTRAINT `FK551losx9j75ss5d6bfsqvijna` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKbtxf5g8lvyw7pgwq713rtgagh` FOREIGN KEY (`product_color_id`) REFERENCES `product_color` (`id`),
  CONSTRAINT `FKhhtfmjxqj7awt1qmiydpp4n9e` FOREIGN KEY (`product_size_id`) REFERENCES `product_size` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.order_item: ~0 rows (approximately)

-- Dumping structure for table cdweb.product
CREATE TABLE IF NOT EXISTS `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `discount` int(11) NOT NULL,
  `lug` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK1mtsbur82frn64de7balymq9s` (`category_id`),
  CONSTRAINT `FK1mtsbur82frn64de7balymq9s` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product: ~0 rows (approximately)

-- Dumping structure for table cdweb.product_color
CREATE TABLE IF NOT EXISTS `product_color` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `color` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKqb6lncpndi0w5po3rr5r9up5e` (`product_id`),
  CONSTRAINT `FKqb6lncpndi0w5po3rr5r9up5e` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_color: ~0 rows (approximately)

-- Dumping structure for table cdweb.product_image
CREATE TABLE IF NOT EXISTS `product_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_path` varchar(255) DEFAULT NULL,
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6oo0cvcdtb6qmwsga468uuukk` (`product_id`),
  CONSTRAINT `FK6oo0cvcdtb6qmwsga468uuukk` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_image: ~0 rows (approximately)

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
  `product_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK8i3jm2ctt0lsyeik2wt76yvv0` (`product_id`),
  CONSTRAINT `FK8i3jm2ctt0lsyeik2wt76yvv0` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_size: ~0 rows (approximately)

-- Dumping structure for table cdweb.product_tag
CREATE TABLE IF NOT EXISTS `product_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `tag_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2rf7w3d88x20p7vuc2m9mvv91` (`product_id`),
  KEY `FK3b3a7hu5g2kh24wf0cwv3lgsm` (`tag_name`) USING BTREE,
  CONSTRAINT `FK2rf7w3d88x20p7vuc2m9mvv91` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
  CONSTRAINT `FK_product_tag_tag` FOREIGN KEY (`tag_name`) REFERENCES `tag` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.product_tag: ~0 rows (approximately)

-- Dumping structure for table cdweb.tag
CREATE TABLE IF NOT EXISTS `tag` (
  `name` varchar(255) NOT NULL DEFAULT '""',
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `UK1wdpsed5kna2y38hnbgrnhi5b` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.tag: ~0 rows (approximately)

-- Dumping structure for table cdweb.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `avt_path` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `gender` int(11) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table cdweb.user: ~0 rows (approximately)

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
