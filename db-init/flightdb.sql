-- -------------------------------------------------------------
-- TablePlus 6.8.0(654)
--
-- https://tableplus.com/
--
-- Database: flightdb
-- Generation Time: 2026-01-30 17:47:08.3730
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


DROP TABLE IF EXISTS `airports`;
CREATE TABLE `airports` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `city` varchar(100) DEFAULT NULL,
  `country` varchar(100) DEFAULT NULL,
  `iata_code` varchar(3) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKrck10qn096aw10ds8rjqf35ah` (`iata_code`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `bookings`;
CREATE TABLE `bookings` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `booking_date` datetime(6) DEFAULT NULL,
  `booking_reference` varchar(255) NOT NULL,
  `status` enum('CANCELLED','CHECKED_IN','CONFIRMED','PENDING') DEFAULT NULL,
  `flight_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKe92mgyq35mdeo8gc1un2o6uk0` (`booking_reference`),
  KEY `FKidcytqkgq0ve4x1elcnbmdy8a` (`flight_id`),
  KEY `FKeyog2oic85xg7hsu2je2lx3s6` (`user_id`),
  CONSTRAINT `FKeyog2oic85xg7hsu2je2lx3s6` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKidcytqkgq0ve4x1elcnbmdy8a` FOREIGN KEY (`flight_id`) REFERENCES `flights` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `email_notifications`;
CREATE TABLE `email_notifications` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body` longtext,
  `is_html` bit(1) NOT NULL,
  `recipient_email` varchar(255) NOT NULL,
  `sent_at` datetime(6) DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `booking_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK95wx3fxynpsi6u5scobhhggkt` (`booking_id`),
  CONSTRAINT `FK95wx3fxynpsi6u5scobhhggkt` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `flights`;
CREATE TABLE `flights` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `arrival_time` datetime(6) DEFAULT NULL,
  `base_price` decimal(38,2) DEFAULT NULL,
  `departure_time` datetime(6) DEFAULT NULL,
  `flight_number` varchar(255) NOT NULL,
  `status` enum('ARRIVED','CANCELLED','DELAYED','DEPARTED','SCHEDULED') DEFAULT NULL,
  `arrival_airport_id` bigint DEFAULT NULL,
  `assigned_pilot_id` bigint DEFAULT NULL,
  `departure_airport_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6bx3i9v6ikjiy0ru5ybor8t7` (`flight_number`),
  KEY `FKr90ujcvdphv3co3ry7aiel6l4` (`arrival_airport_id`),
  KEY `FK6iupidffxxbc4s41teqjxe3qc` (`assigned_pilot_id`),
  KEY `FK27lt4nklvbrwsw7x32dw0d05q` (`departure_airport_id`),
  CONSTRAINT `FK27lt4nklvbrwsw7x32dw0d05q` FOREIGN KEY (`departure_airport_id`) REFERENCES `airports` (`id`),
  CONSTRAINT `FK6iupidffxxbc4s41teqjxe3qc` FOREIGN KEY (`assigned_pilot_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKr90ujcvdphv3co3ry7aiel6l4` FOREIGN KEY (`arrival_airport_id`) REFERENCES `airports` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `passengers`;
CREATE TABLE `passengers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `passport_number` varchar(255) DEFAULT NULL,
  `seat_number` varchar(255) DEFAULT NULL,
  `special_request` varchar(255) DEFAULT NULL,
  `type` enum('ADULT','CHILD','INFANT') DEFAULT NULL,
  `booking_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKgc7vcfrut3vamougerwse2m2u` (`booking_id`),
  CONSTRAINT `FKgc7vcfrut3vamougerwse2m2u` FOREIGN KEY (`booking_id`) REFERENCES `bookings` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `create_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `email_verified` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `provider` enum('FACEBOOK','GOOGLE','LOCAL') NOT NULL,
  `provider_id` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `users_roles`;
CREATE TABLE `users_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`),
  KEY `FK2o0jvgh89lemvvo17cbqvdxaa` (`user_id`),
  CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `airports` (`id`, `city`, `country`, `iata_code`, `name`) VALUES
(1, 'LONDON', 'UK', 'LON', 'London International Airport'),
(2, 'MIAMI', 'USA', 'MIA', 'Miami International Airport'),
(3, 'Tokyo', 'Japan', 'HND', 'Haneda Airport'),
(4, 'Tokyo', 'Japan', 'NRT', 'Narita International Airport'),
(5, 'New York', 'USA', 'JFK', 'John F. Kennedy International Airport'),
(6, 'London', 'UK', 'LHR', 'Heathrow Airport'),
(7, 'Paris', 'France', 'CDG', 'Charles de Gaulle Airport'),
(8, 'Shanghai', 'China', 'PVG', 'Shanghai Pudong International Airport'),
(9, 'Beijing', 'China', 'PEK', 'Beijing Capital International Airport'),
(10, 'Dubai', 'UAE', 'DXB', 'Dubai International Airport'),
(11, 'Los Angeles', 'USA', 'LAX', 'Los Angeles International Airport'),
(12, 'Singapore', 'Singapore', 'SIN', 'Singapore Changi Airport'),
(13, 'Sydney', 'Australia', 'SYD', 'Sydney Kingsford Smith Airport'),
(14, 'Seoul', 'South Korea', 'ICN', 'Incheon International Airport'),
(15, 'Frankfurt', 'Germany', 'FRA', 'Frankfurt Airport'),
(16, 'Hong Kong', 'Hong Kong', 'HKG', 'Hong Kong International Airport'),
(17, 'Amsterdam', 'Netherlands', 'AMS', 'Amsterdam Airport Schiphol'),
(18, 'Bangkok', 'Thailand', 'BKK', 'Suvarnabhumi Airport'),
(19, 'Istanbul', 'Turkey', 'IST', 'Istanbul Airport'),
(20, 'Toronto', 'Canada', 'YYZ', 'Toronto Pearson International Airport'),
(21, 'Mumbai', 'India', 'BOM', 'Chhatrapati Shivaji Maharaj International Airport'),
(22, 'San Francisco', 'USA', 'SFO', 'San Francisco International Airport'),
(23, 'Doha', 'Qatar', 'DOH', 'Hamad International Airport'),
(24, 'Munich', 'Germany', 'MUC', 'Munich Airport'),
(25, 'Zurich', 'Switzerland', 'ZRH', 'Zurich Airport'),
(26, 'Madrid', 'Spain', 'MAD', 'Adolfo Suárez Madrid–Barajas Airport'),
(27, 'Rome', 'Italy', 'FCO', 'Leonardo da Vinci–Fiumicino Airport'),
(28, 'Chicago', 'USA', 'ORD', 'O\'Hare International Airport'),
(29, 'Atlanta', 'USA', 'ATL', 'Hartsfield–Jackson Atlanta International Airport'),
(30, 'Vancouver', 'Canada', 'YVR', 'Vancouver International Airport'),
(31, 'Melbourne', 'Australia', 'MEL', 'Melbourne Airport'),
(32, 'Auckland', 'New Zealand', 'AKL', 'Auckland Airport'),
(33, 'Taipei', 'Taiwan', 'TPE', 'Taoyuan International Airport'),
(34, 'Kuala Lumpur', 'Malaysia', 'KUL', 'Kuala Lumpur International Airport'),
(35, 'Delhi', 'India', 'DEL', 'Indira Gandhi International Airport'),
(36, 'Sao Paulo', 'Brazil', 'GRU', 'São Paulo/Guarulhos International Airport'),
(37, 'Mexico City', 'Mexico', 'MEX', 'Mexico City International Airport');

INSERT INTO `bookings` (`id`, `booking_date`, `booking_reference`, `status`, `flight_id`, `user_id`) VALUES
(1, '2025-11-09 11:26:39.192450', '0F435A0F', 'CHECKED_IN', 8, 1),
(2, '2025-11-09 11:32:03.066639', 'F0A8837B', 'CONFIRMED', 7, 1),
(3, '2025-11-09 11:41:42.501978', 'B4E673E4', 'CONFIRMED', 7, 1),
(4, '2025-11-09 11:45:18.777606', '091BD877', 'CONFIRMED', 7, 1),
(5, '2025-12-12 14:48:55.369239', '503E5075', 'CONFIRMED', 7, 2);

INSERT INTO `flights` (`id`, `arrival_time`, `base_price`, `departure_time`, `flight_number`, `status`, `arrival_airport_id`, `assigned_pilot_id`, `departure_airport_id`) VALUES
(6, '2025-07-27 20:00:00.000000', 1200.00, '2025-07-27 09:00:00.000000', 'CC228', 'SCHEDULED', 1, 1, 2),
(7, '2025-07-27 18:00:00.000000', 1200.00, '2025-07-27 05:00:00.000000', 'CA128', 'SCHEDULED', 1, 1, 2),
(8, '2025-07-27 18:00:00.000000', 1200.00, '2025-07-27 10:00:00.000000', 'AB110', 'SCHEDULED', 2, 1, 1),
(9, '2026-01-10 18:00:00.000000', 1200.00, '2026-01-01 10:00:00.000000', 'AB170', 'SCHEDULED', 2, 1, 1),
(10, '2026-01-12 18:00:00.000000', 1200.00, '2026-01-02 10:00:00.000000', 'AB180', 'SCHEDULED', 2, 1, 1),
(11, '2025-12-31 09:56:00.000000', 1300.00, '2025-12-30 12:55:00.000000', 'AE1230', 'SCHEDULED', 2, 1, 1),
(12, '2026-05-01 16:00:00.000000', 1200.00, '2026-05-01 10:00:00.000000', 'NH106', 'SCHEDULED', 11, 1, 3),
(13, '2026-05-03 06:30:00.000000', 850.50, '2026-05-02 18:30:00.000000', 'BA112', 'SCHEDULED', 6, 1, 5),
(14, '2026-05-05 23:45:00.000000', 920.00, '2026-05-05 15:00:00.000000', 'EK074', 'SCHEDULED', 10, 1, 7),
(15, '2026-05-10 16:50:00.000000', 1100.00, '2026-05-10 20:10:00.000000', 'UA858', 'SCHEDULED', 22, 1, 8),
(16, '2026-05-12 19:50:00.000000', 780.00, '2026-05-12 09:00:00.000000', 'SQ221', 'SCHEDULED', 13, 1, 12),
(17, '2026-05-15 16:15:00.000000', 540.00, '2026-05-15 11:45:00.000000', 'TG677', 'SCHEDULED', 18, 1, 4),
(18, '2026-05-19 05:00:00.000000', 1050.00, '2026-05-18 13:00:00.000000', 'LH720', 'SCHEDULED', 9, 1, 15),
(19, '2026-05-20 11:00:00.000000', 860.00, '2026-05-20 08:00:00.000000', 'BA113', 'SCHEDULED', 5, 1, 6),
(20, '2026-05-23 06:00:00.000000', 950.00, '2026-05-22 17:00:00.000000', 'KL692', 'SCHEDULED', 17, 1, 20),
(21, '2026-05-26 05:30:00.000000', 1300.00, '2026-05-25 23:30:00.000000', 'TK091', 'SCHEDULED', 19, 1, 14),
(22, '2026-05-28 20:30:00.000000', 980.00, '2026-05-28 14:35:00.000000', 'CX253', 'SCHEDULED', 6, 1, 16),
(23, '2026-05-29 19:20:00.000000', 750.50, '2026-05-29 17:10:00.000000', 'KL641', 'SCHEDULED', 5, 1, 17),
(24, '2026-06-01 22:30:00.000000', 1450.00, '2026-06-01 02:00:00.000000', 'EK414', 'SCHEDULED', 13, 1, 10),
(25, '2026-06-03 16:15:00.000000', 1120.00, '2026-06-02 21:55:00.000000', 'LH778', 'SCHEDULED', 12, 1, 15),
(26, '2026-06-04 19:30:00.000000', 890.00, '2026-06-04 01:50:00.000000', 'TK052', 'SCHEDULED', 4, 1, 19),
(27, '2026-06-05 12:50:00.000000', 450.00, '2026-06-05 08:30:00.000000', 'CA181', 'SCHEDULED', 3, 1, 9),
(28, '2026-06-08 06:10:00.000000', 1080.00, '2026-06-06 22:30:00.000000', 'UA839', 'SCHEDULED', 13, 1, 11),
(29, '2026-06-09 16:30:00.000000', 1250.00, '2026-06-08 11:30:00.000000', 'KE024', 'SCHEDULED', 14, 1, 22),
(30, '2026-06-11 06:50:00.000000', 820.00, '2026-06-10 18:45:00.000000', 'AC856', 'SCHEDULED', 6, 1, 20),
(31, '2026-06-12 18:15:00.000000', 320.00, '2026-06-12 16:15:00.000000', 'AI910', 'SCHEDULED', 10, 1, 21),
(32, '2026-06-14 07:10:00.000000', 940.00, '2026-06-14 00:05:00.000000', 'TG930', 'SCHEDULED', 7, 1, 18),
(33, '2026-06-15 11:20:00.000000', 410.00, '2026-06-15 07:35:00.000000', 'SQ890', 'SCHEDULED', 16, 1, 12),
(34, '2026-06-16 06:00:00.000000', 1150.00, '2026-06-16 09:30:00.000000', 'QF011', 'SCHEDULED', 11, 1, 13),
(35, '2026-06-19 00:15:00.000000', 880.00, '2026-06-18 10:15:00.000000', 'BA139', 'SCHEDULED', 21, 1, 6),
(36, '2026-06-21 09:10:00.000000', 790.00, '2026-06-20 19:30:00.000000', 'DL262', 'SCHEDULED', 7, 1, 5),
(37, '2026-06-22 17:15:00.000000', 670.00, '2026-06-22 11:05:00.000000', 'NH841', 'SCHEDULED', 12, 1, 3),
(38, '2026-06-25 10:45:00.000000', 1220.00, '2026-06-25 17:05:00.000000', 'JL062', 'SCHEDULED', 11, 1, 4),
(39, '2026-06-29 16:20:00.000000', 1030.00, '2026-06-28 23:25:00.000000', 'AF198', 'SCHEDULED', 8, 1, 7),
(40, '2026-06-30 14:25:00.000000', 1350.00, '2026-06-30 08:30:00.000000', 'EK201', 'SCHEDULED', 5, 1, 10),
(41, '2026-07-01 16:10:00.000000', 420.00, '2026-07-01 11:40:00.000000', 'KL1613', 'SCHEDULED', 19, 1, 17),
(42, '2026-07-02 13:15:00.000000', 950.00, '2026-07-02 08:00:00.000000', 'QR007', 'SCHEDULED', 6, 1, 23),
(43, '2026-07-03 18:20:00.000000', 880.00, '2026-07-03 15:50:00.000000', 'LH434', 'SCHEDULED', 28, 1, 24),
(44, '2026-07-04 16:00:00.000000', 920.00, '2026-07-04 13:10:00.000000', 'LX014', 'SCHEDULED', 5, 1, 25),
(45, '2026-07-06 05:40:00.000000', 1100.00, '2026-07-05 23:55:00.000000', 'IB6827', 'SCHEDULED', 36, 1, 26),
(46, '2026-07-06 16:05:00.000000', 1050.00, '2026-07-06 11:20:00.000000', 'DL067', 'SCHEDULED', 29, 1, 27),
(47, '2026-07-09 18:30:00.000000', 890.00, '2026-07-08 14:00:00.000000', 'CX837', 'SCHEDULED', 16, 1, 30),
(48, '2026-07-09 16:25:00.000000', 680.00, '2026-07-09 10:35:00.000000', 'SQ238', 'SCHEDULED', 12, 1, 31),
(49, '2026-07-10 10:40:00.000000', 350.00, '2026-07-10 09:00:00.000000', 'NZ103', 'SCHEDULED', 13, 1, 32),
(50, '2026-07-12 16:50:00.000000', 1020.00, '2026-07-12 19:20:00.000000', 'BR012', 'SCHEDULED', 11, 1, 33),
(51, '2026-07-13 16:35:00.000000', 850.00, '2026-07-13 09:50:00.000000', 'MH004', 'SCHEDULED', 6, 1, 34),
(52, '2026-07-15 07:40:00.000000', 780.00, '2026-07-15 02:50:00.000000', 'LH761', 'SCHEDULED', 15, 1, 35),
(53, '2026-07-17 13:40:00.000000', 940.00, '2026-07-16 19:45:00.000000', 'AM001', 'SCHEDULED', 26, 1, 37),
(54, '2026-07-19 08:15:00.000000', 960.00, '2026-07-18 17:55:00.000000', 'DL082', 'SCHEDULED', 7, 1, 29),
(55, '2026-07-21 20:30:00.000000', 1150.00, '2026-07-20 17:30:00.000000', 'NH111', 'SCHEDULED', 3, 1, 28),
(56, '2026-07-22 23:00:00.000000', 1300.00, '2026-07-22 03:00:00.000000', 'QR774', 'SCHEDULED', 23, 1, 36),
(57, '2026-07-24 07:45:00.000000', 1080.00, '2026-07-24 09:30:00.000000', 'AC034', 'SCHEDULED', 30, 1, 13),
(58, '2026-07-25 22:50:00.000000', 720.00, '2026-07-25 09:45:00.000000', 'AI162', 'SCHEDULED', 35, 1, 6),
(59, '2026-07-26 22:25:00.000000', 890.00, '2026-07-26 08:40:00.000000', 'NZ281', 'SCHEDULED', 32, 1, 12),
(60, '2026-07-28 14:40:00.000000', 1400.00, '2026-07-28 16:50:00.000000', 'NH180', 'SCHEDULED', 37, 1, 4),
(61, '2026-07-31 07:30:00.000000', 1050.00, '2026-07-30 23:10:00.000000', 'BR071', 'SCHEDULED', 24, 1, 33),
(62, '2026-08-01 13:40:00.000000', 1380.00, '2026-08-01 08:30:00.000000', 'EK215', 'SCHEDULED', 11, 1, 10),
(63, '2026-08-03 16:30:00.000000', 960.00, '2026-08-02 22:40:00.000000', 'LX138', 'SCHEDULED', 16, 1, 25),
(64, '2026-08-04 13:45:00.000000', 780.00, '2026-08-04 10:10:00.000000', 'AZ608', 'SCHEDULED', 5, 1, 27),
(65, '2026-08-05 19:50:00.000000', 450.00, '2026-08-05 11:45:00.000000', 'SQ421', 'SCHEDULED', 12, 1, 21),
(66, '2026-08-06 15:00:00.000000', 820.00, '2026-08-06 01:25:00.000000', 'TK068', 'SCHEDULED', 18, 1, 19),
(67, '2026-08-09 06:00:00.000000', 1150.00, '2026-08-08 23:20:00.000000', 'AF454', 'SCHEDULED', 36, 1, 7),
(68, '2026-08-10 14:55:00.000000', 930.00, '2026-08-09 20:50:00.000000', 'KL809', 'SCHEDULED', 34, 1, 17),
(69, '2026-08-10 14:20:00.000000', 760.00, '2026-08-10 12:10:00.000000', 'IB6251', 'SCHEDULED', 5, 1, 26),
(70, '2026-08-12 06:25:00.000000', 1190.00, '2026-08-12 09:00:00.000000', 'QF093', 'SCHEDULED', 11, 1, 31),
(71, '2026-08-16 04:30:00.000000', 1600.00, '2026-08-15 03:00:00.000000', 'QR920', 'SCHEDULED', 32, 1, 23);

INSERT INTO `passengers` (`id`, `first_name`, `last_name`, `passport_number`, `seat_number`, `special_request`, `type`, `booking_id`) VALUES
(1, 'Mary', 'White', NULL, '30A', NULL, 'ADULT', 1),
(2, 'Join', 'Black', NULL, '4A', NULL, 'ADULT', 2),
(3, 'Jackon', 'White', NULL, '3A', NULL, 'ADULT', 2),
(4, 'Join', 'Black', 'E78422512', '4A', NULL, 'ADULT', 3),
(5, 'Jackon', 'White', 'E88422512', '3A', NULL, 'ADULT', 3),
(6, 'Join', 'Black', 'E78422512', '4A', 'water only', 'ADULT', 4),
(7, 'Jackon', 'White', 'E88422512', '3A', '', 'ADULT', 4),
(8, 'Peng', 'Huang', 'E78422522', '1C', '', 'ADULT', 5);

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ADMIN'),
(2, 'PILOT'),
(3, 'CUSTOMER');

INSERT INTO `users` (`id`, `active`, `create_at`, `email`, `email_verified`, `name`, `password`, `phone_number`, `provider`, `provider_id`, `updated_at`, `created_at`) VALUES
(1, b'1', NULL, 'huangkouhou@gmail.com', b'0', 'PengHuang', '$2a$10$cVPsP3ykE2WUzg6xw0.y0uKAHuE5CAJPxf9VR9MStEypd13DNeBBW', '08084100182', 'LOCAL', NULL, '2025-10-30 16:12:59.325371', '2025-10-29 11:51:12.319907'),
(2, b'1', NULL, 'huangpengsleeping@gmail.com', b'0', 'PENG HUANG', '$2a$10$eb5amxBj/bPZgV48Qk31PezkuW/WGGLsG4dUwsY7Ei/xFY4woBkMO', '08084100184', 'LOCAL', NULL, '2025-12-07 20:58:16.851677', '2025-11-19 13:19:13.864454'),
(3, b'1', NULL, 'admin@email.com', b'0', 'KiKi', '$2a$10$i60vs0SANKDbmZbaHpUkbeht.zblDVeUzARvNQew1n9rED5Yja7wS', '08084100184', 'LOCAL', NULL, '2026-01-01 21:41:02.283854', '2026-01-01 21:41:02.283837'),
(4, b'1', NULL, 'huangkouhou@yahoo.com', b'0', 'ZhaoJinmai', '$2a$10$iMcKRaQV0FFnYDLPmlxyYOGMo33luo0iqufRLf6jbiVheHuli22RO', '123456', 'LOCAL', NULL, '2026-01-01 21:48:26.152024', '2026-01-01 21:48:26.152013');

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(2, 3),
(3, 1),
(4, 2);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;