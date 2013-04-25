CREATE DATABASE  IF NOT EXISTS `typdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `typdb`;
-- MySQL dump 10.13  Distrib 5.6.10, for Win64 (x86_64)
--
-- Host: localhost    Database: typdb
-- ------------------------------------------------------
-- Server version	5.6.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `typusers`
--

DROP TABLE IF EXISTS `typusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `typusers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ipaddr` varchar(20) NOT NULL,
  `pubkeyloc` varchar(55) DEFAULT NULL,
  `messloc1` varchar(55) DEFAULT NULL,
  `messloc2` varchar(55) DEFAULT NULL,
  `messloc3` varchar(55) DEFAULT NULL,
  `messloc4` varchar(55) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `typusers`
--

LOCK TABLES `typusers` WRITE;
/*!40000 ALTER TABLE `typusers` DISABLE KEYS */;
INSERT INTO `typusers` VALUES (2,'192.168.0.1','here.xml','from 127.0.0.1Sat Mar 02 13 08 35 GMT 2013.xml',NULL,NULL,NULL),(3,'192.168.0.2',NULL,'from 127.0.0.1Thu Feb 28 22 05 42 GMT 2013.xml',NULL,NULL,NULL),(4,'192.168.0.3','there.xml','from 127.0.0.1Mon Feb 25 13 05 57 GMT 2013.xml',NULL,NULL,NULL),(5,'192.168.0.13',NULL,'from 127.0.0.1Sun Feb 24 18 37 15 GMT 2013.xml',NULL,NULL,NULL),(6,'192.168.0.12',NULL,NULL,NULL,NULL,NULL),(8,'127.0.0.1','keys/user8pubkey.key','nomessagefile.xml','messloc2for8.txt','xxx.xml','xxx.xml'),(9,'192.168.1.4',NULL,'nomessagefile.xml',NULL,NULL,NULL),(10,'192.168.1.1',NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `typusers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-04-24 12:44:45
