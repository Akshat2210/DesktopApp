-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: gurukripa
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `accounts` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `date_` date DEFAULT NULL,
  `AMOUNT` decimal(10,2) NOT NULL,
  `medium` varchar(50) DEFAULT NULL,
  `REMARK` varchar(255) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `r_G` varchar(40) DEFAULT NULL,
  `bill_id` int DEFAULT NULL,
  `balance` float DEFAULT NULL,
  `InCash` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `NAME` (`name`),
  KEY `bill_id` (`bill_id`),
  CONSTRAINT `accounts_ibfk_1` FOREIGN KEY (`name`) REFERENCES `people` (`name`),
  CONSTRAINT `accounts_ibfk_2` FOREIGN KEY (`bill_id`) REFERENCES `bills` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=534 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `billnumber`
--

DROP TABLE IF EXISTS `billnumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `billnumber` (
  `SNO` int NOT NULL AUTO_INCREMENT,
  `customerName` varchar(50) DEFAULT NULL,
  `itemName` varchar(50) DEFAULT NULL,
  `K` int DEFAULT NULL,
  `L` int DEFAULT NULL,
  `C` int DEFAULT NULL,
  `CM` int DEFAULT NULL,
  `M` int DEFAULT NULL,
  `Gold` float(10,3) DEFAULT NULL,
  `GoldRate` int DEFAULT NULL,
  `Total` int DEFAULT NULL,
  `id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`SNO`),
  KEY `id` (`id`),
  KEY `customerName` (`customerName`),
  CONSTRAINT `billnumber_ibfk_2` FOREIGN KEY (`id`) REFERENCES `bills` (`id`),
  CONSTRAINT `billnumber_ibfk_3` FOREIGN KEY (`customerName`) REFERENCES `people` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bills`
--

DROP TABLE IF EXISTS `bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bills` (
  `id` int NOT NULL AUTO_INCREMENT,
  `CustName` varchar(255) NOT NULL,
  `ForwardAmount` int DEFAULT NULL,
  `CurAmount` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `CustName` (`CustName`),
  CONSTRAINT `bills_ibfk_1` FOREIGN KEY (`CustName`) REFERENCES `people` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `TYPE` varchar(35) NOT NULL,
  PRIMARY KEY (`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datewali`
--

DROP TABLE IF EXISTS `datewali`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datewali` (
  `datev` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `datewali1`
--

DROP TABLE IF EXISTS `datewali1`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `datewali1` (
  `datev` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `dummy`
--

DROP TABLE IF EXISTS `dummy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dummy` (
  `name` varchar(200) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `people`
--

DROP TABLE IF EXISTS `people`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `people` (
  `name` varchar(50) NOT NULL,
  `type` varchar(255) NOT NULL,
  `outstandings` int DEFAULT NULL,
  PRIMARY KEY (`name`),
  KEY `type` (`type`),
  CONSTRAINT `people_ibfk_1` FOREIGN KEY (`type`) REFERENCES `category` (`TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-10-10 12:33:38
