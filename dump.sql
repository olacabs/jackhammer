-- MySQL dump 10.13  Distrib 5.7.22, for osx10.12 (x86_64)
--
-- Host: localhost    Database: jchauth
-- ------------------------------------------------------
-- Server version	5.7.22

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
-- Table structure for table `ActionsRoles`
--

DROP TABLE IF EXISTS `ActionsRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ActionsRoles` (
  `actionId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  `createdAt` datetime DEFAULT NULL,
  `updatedAt` datetime DEFAULT NULL,
  KEY `actionIdIndex` (`actionId`),
  KEY `roleIdIndex` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ActionsRoles`
--

LOCK TABLES `ActionsRoles` WRITE;
/*!40000 ALTER TABLE `ActionsRoles` DISABLE KEYS */;
/*!40000 ALTER TABLE `ActionsRoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `actions`
--

DROP TABLE IF EXISTS `actions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `iconClass` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isScanTypeModule` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actions`
--

LOCK TABLES `actions` WRITE;
/*!40000 ALTER TABLE `actions` DISABLE KEYS */;
INSERT INTO `actions` VALUES (1,'Dashboard','fa fa-dashboard','2018-05-17 08:19:20','2018-05-24 08:51:53',1),(2,'Scans','fa fa-search-plus','2018-05-17 08:19:20','2018-05-24 08:51:53',1),(3,'Applications','fa fa-tasks','2018-05-17 08:19:20','2018-05-24 08:51:53',1),(4,'Analytics','fa fa-line-chart','2018-05-17 08:19:20','2018-05-24 08:51:53',1),(5,'Filters','fa fa-filter','2018-05-17 08:19:20','2018-05-24 08:51:53',1),(6,'RBAC','fa fa-users','2018-05-17 08:19:20','2018-05-17 08:19:20',0),(7,'Settings','fa fa-cog','2018-05-17 08:19:20','2018-05-17 08:19:20',0);
/*!40000 ALTER TABLE `actions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `branches`
--

DROP TABLE IF EXISTS `branches`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `branches` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `repo_id` int(11) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `branches`
--

LOCK TABLES `branches` WRITE;
/*!40000 ALTER TABLE `branches` DISABLE KEYS */;
/*!40000 ALTER TABLE `branches` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comments`
--

DROP TABLE IF EXISTS `comments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `findingId` int(11) DEFAULT NULL,
  `name` text,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `findingIdIndex` (`findingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comments`
--

LOCK TABLES `comments` WRITE;
/*!40000 ALTER TABLE `comments` DISABLE KEYS */;
/*!40000 ALTER TABLE `comments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `defaultRole`
--

DROP TABLE IF EXISTS `defaultRole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `defaultRole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `roleId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `defaultRole`
--

LOCK TABLES `defaultRole` WRITE;
/*!40000 ALTER TABLE `defaultRole` DISABLE KEYS */;
INSERT INTO `defaultRole` VALUES (2,68,'2018-06-18 08:33:00','2018-08-29 11:29:34');
/*!40000 ALTER TABLE `defaultRole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `findings`
--

DROP TABLE IF EXISTS `findings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `findings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repoId` int(11) DEFAULT NULL,
  `branchId` int(11) DEFAULT NULL,
  `current` tinyint(1) DEFAULT NULL,
  `name` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `severity` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `fingerprint` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `toolName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `fileName` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `lineNumber` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `scanId` int(11) DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'New',
  `isFalsePositive` tinyint(1) DEFAULT '0',
  `externalLink` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `solution` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `cvssScore` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `userInput` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `scanerInstanceId` int(11) DEFAULT NULL,
  `closed_by` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `issue_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `closed_date` date DEFAULT NULL,
  `port` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `protocol` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `state` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `product` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `scripts` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `version` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `host` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `request` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `response` text CHARACTER SET utf8 COLLATE utf8_unicode_ci,
  `confidence` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `notExploitable` tinyint(1) DEFAULT '0',
  `port_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `host_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `tag_id` int(11) DEFAULT NULL,
  `advisory` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `cveCode` varchar(255) DEFAULT NULL,
  `cweCode` varchar(255) DEFAULT NULL,
  `scanTypeId` int(11) DEFAULT NULL,
  `ownerTypeId` int(11) DEFAULT NULL,
  `pushedToJira` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `repoIdIndex` (`repoId`),
  KEY `scanIdIndex` (`scanId`),
  KEY `isFalsePositiveIndex` (`isFalsePositive`),
  KEY `notExploitableIndex` (`notExploitable`),
  KEY `scanTypeIdIndex` (`scanTypeId`),
  KEY `ownerTypeIdIndex` (`ownerTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=1409 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `findings`
--

LOCK TABLES `findings` WRITE;
/*!40000 ALTER TABLE `findings` DISABLE KEYS */;
/*!40000 ALTER TABLE `findings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `findingsTags`
--

DROP TABLE IF EXISTS `findingsTags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `findingsTags` (
  `findingId` int(11) DEFAULT NULL,
  `tagId` int(11) DEFAULT NULL,
  KEY `index_findings_tags_on_finding_id_and_tag_id` (`findingId`,`tagId`),
  KEY `index_findings_tags_on_tag_id_and_finding_id` (`tagId`,`findingId`),
  KEY `findingIdIndex` (`findingId`),
  KEY `tagIdIndex` (`tagId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `findingsTags`
--

LOCK TABLES `findingsTags` WRITE;
/*!40000 ALTER TABLE `findingsTags` DISABLE KEYS */;
/*!40000 ALTER TABLE `findingsTags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `git`
--

DROP TABLE IF EXISTS `git`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `git` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL,
  `gitEndpoint` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `gitType` varchar(255) DEFAULT NULL,
  `apiAccessToken` varchar(255) DEFAULT NULL,
  `organizationName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `git`
--

LOCK TABLES `git` WRITE;
/*!40000 ALTER TABLE `git` DISABLE KEYS */;
/*!40000 ALTER TABLE `git` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groups`
--

DROP TABLE IF EXISTS `groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groups` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) DEFAULT NULL,
  `scanTypeId` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `name` (`name`),
  KEY `nameIndex` (`name`),
  KEY `scanTypeIdIndex` (`scanTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groups`
--

LOCK TABLES `groups` WRITE;
/*!40000 ALTER TABLE `groups` DISABLE KEYS */;
/*!40000 ALTER TABLE `groups` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupsRoles`
--

DROP TABLE IF EXISTS `groupsRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupsRoles` (
  `groupId` int(11) DEFAULT NULL,
  `roleId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `groupIdIndex` (`groupId`),
  KEY `roleIdIndex` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupsRoles`
--

LOCK TABLES `groupsRoles` WRITE;
/*!40000 ALTER TABLE `groupsRoles` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupsRoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupsUsers`
--

DROP TABLE IF EXISTS `groupsUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupsUsers` (
  `userId` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  KEY `userIdIndex` (`userId`),
  KEY `groupIdIndex` (`groupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupsUsers`
--

LOCK TABLES `groupsUsers` WRITE;
/*!40000 ALTER TABLE `groupsUsers` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupsUsers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hardcodeSecrets`
--

DROP TABLE IF EXISTS `hardcodeSecrets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hardcodeSecrets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commitsDepth` int(11) DEFAULT NULL,
  `commitsStartDate` date DEFAULT NULL,
  `regex` text,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hardcodeSecrets`
--

LOCK TABLES `hardcodeSecrets` WRITE;
/*!40000 ALTER TABLE `hardcodeSecrets` DISABLE KEYS */;
/*!40000 ALTER TABLE `hardcodeSecrets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jira`
--

DROP TABLE IF EXISTS `jira`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jira` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `host` varchar(255) DEFAULT NULL,
  `userName` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `defaultProject` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jira`
--

LOCK TABLES `jira` WRITE;
/*!40000 ALTER TABLE `jira` DISABLE KEYS */;
/*!40000 ALTER TABLE `jira` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `languages`
--

DROP TABLE IF EXISTS `languages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `languages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `fileExtension` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `languages`
--

LOCK TABLES `languages` WRITE;
/*!40000 ALTER TABLE `languages` DISABLE KEYS */;
INSERT INTO `languages` VALUES (1,'Ruby','rb','2017-12-13 13:19:29','2017-12-18 10:27:42'),(2,'JavaScript','js','2017-12-13 13:19:29','2017-12-18 10:27:50'),(3,'Java','java','2017-12-13 13:19:29','2017-12-18 10:28:08'),(4,'CoffeeScript','coffee','2017-12-13 13:19:30','2017-12-18 10:28:34');
/*!40000 ALTER TABLE `languages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `critical_count` int(11) DEFAULT NULL,
  `high_count` int(11) DEFAULT NULL,
  `medium_count` int(11) DEFAULT NULL,
  `low_count` int(11) DEFAULT NULL,
  `info_count` int(11) DEFAULT NULL,
  `critical_email` varchar(250) DEFAULT NULL,
  `high_email` varchar(250) DEFAULT NULL,
  `medium_email` varchar(250) DEFAULT NULL,
  `low_email` varchar(250) DEFAULT NULL,
  `info_email` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ownerTypes`
--

DROP TABLE IF EXISTS `ownerTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ownerTypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `isDefault` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ownerTypes`
--

LOCK TABLES `ownerTypes` WRITE;
/*!40000 ALTER TABLE `ownerTypes` DISABLE KEYS */;
INSERT INTO `ownerTypes` VALUES (1,'Corporate',1),(2,'Team',0),(3,'Personal',0);
/*!40000 ALTER TABLE `ownerTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissionsRoles`
--

DROP TABLE IF EXISTS `permissionsRoles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissionsRoles` (
  `roleId` int(11) DEFAULT NULL,
  `permissionId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissionsRoles`
--

LOCK TABLES `permissionsRoles` WRITE;
/*!40000 ALTER TABLE `permissionsRoles` DISABLE KEYS */;
/*!40000 ALTER TABLE `permissionsRoles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repos`
--

DROP TABLE IF EXISTS `repos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `target` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ownerTypeId` int(11) DEFAULT NULL,
  `scanTypeId` int(11) DEFAULT NULL,
  `branchId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1188 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repos`
--

LOCK TABLES `repos` WRITE;
/*!40000 ALTER TABLE `repos` DISABLE KEYS */;
/*!40000 ALTER TABLE `repos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `index_roles_on_name` (`name`),
  KEY `index_roles_on_name_and_resource_type_and_resource_id` (`name`),
  KEY `nameIndex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (60,'Admin','2018-05-21 11:55:50','2018-05-21 11:55:50'),(68,'Dev','2018-05-22 07:07:44','2018-05-22 07:07:44'),(70,'Security Team','2018-08-29 11:28:02','2018-08-29 11:28:02');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolesTasks`
--

DROP TABLE IF EXISTS `rolesTasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolesTasks` (
  `roleId` int(11) NOT NULL,
  `taskId` int(11) NOT NULL,
  KEY `roleIdIndex` (`roleId`),
  KEY `taskIdIndex` (`taskId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolesTasks`
--

LOCK TABLES `rolesTasks` WRITE;
/*!40000 ALTER TABLE `rolesTasks` DISABLE KEYS */;
INSERT INTO `rolesTasks` VALUES (70,41),(70,42),(70,43),(70,44),(70,45),(70,46),(70,47),(70,48),(70,49),(70,50),(70,51),(70,52),(70,53),(70,55),(70,56),(70,57),(70,58),(70,59),(70,60),(70,61),(70,118),(60,41),(60,42),(60,43),(60,44),(60,45),(60,46),(60,47),(60,48),(60,49),(60,50),(60,51),(60,52),(60,53),(60,55),(60,56),(60,57),(60,58),(60,59),(60,60),(60,61),(60,62),(60,63),(60,64),(60,65),(60,66),(60,67),(60,68),(60,69),(60,70),(60,71),(60,72),(60,73),(60,74),(60,75),(60,76),(60,77),(60,78),(60,79),(60,80),(60,83),(60,84),(60,85),(60,86),(60,87),(60,88),(60,93),(60,95),(60,98),(60,99),(60,118),(60,124),(60,125),(60,126),(60,127),(60,128),(60,129),(60,130),(60,131),(60,132),(60,133),(60,134),(60,136),(60,137),(60,138),(68,42),(68,43),(68,45),(68,46),(68,48),(68,49),(68,51),(68,52),(68,55),(68,57),(68,58),(68,60),(68,61),(68,124),(68,125),(68,132);
/*!40000 ALTER TABLE `rolesTasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolesTeams`
--

DROP TABLE IF EXISTS `rolesTeams`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolesTeams` (
  `roleId` int(11) NOT NULL,
  `teamId` int(11) NOT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`roleId`,`teamId`),
  KEY `roleIdIndex` (`roleId`),
  KEY `teamIdIndex` (`teamId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolesTeams`
--

LOCK TABLES `rolesTeams` WRITE;
/*!40000 ALTER TABLE `rolesTeams` DISABLE KEYS */;
/*!40000 ALTER TABLE `rolesTeams` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rolesUsers`
--

DROP TABLE IF EXISTS `rolesUsers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rolesUsers` (
  `roleId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  KEY `roleIdIndex` (`roleId`),
  KEY `userIdIndex` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rolesUsers`
--

LOCK TABLES `rolesUsers` WRITE;
/*!40000 ALTER TABLE `rolesUsers` DISABLE KEYS */;
INSERT INTO `rolesUsers` VALUES (60,118);
/*!40000 ALTER TABLE `rolesUsers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scanTools`
--

DROP TABLE IF EXISTS `scanTools`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scanTools` (
  `scanId` int(11) DEFAULT NULL,
  `toolId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `toolInstanceId` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT 'Queued',
  KEY `scanIdIndex` (`scanId`),
  KEY `toolIdIndex` (`toolId`),
  KEY `toolInstanceIdIndex` (`toolInstanceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scanTools`
--

LOCK TABLES `scanTools` WRITE;
/*!40000 ALTER TABLE `scanTools` DISABLE KEYS */;
/*!40000 ALTER TABLE `scanTools` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scanTypes`
--

DROP TABLE IF EXISTS `scanTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scanTypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isStatic` tinyint(1) DEFAULT '0',
  `isWeb` tinyint(1) DEFAULT '0',
  `isWordpress` tinyint(1) DEFAULT '0',
  `isMobile` tinyint(1) DEFAULT '0',
  `isNetwork` tinyint(1) DEFAULT '0',
  `isHardCodeSecret` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `nameIndex` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scanTypes`
--

LOCK TABLES `scanTypes` WRITE;
/*!40000 ALTER TABLE `scanTypes` DISABLE KEYS */;
INSERT INTO `scanTypes` VALUES (4,'Source Code','2018-06-13 09:24:57','2018-06-19 14:12:37',1,0,0,0,0,0),(5,'Web','2017-02-06 08:40:29','2018-06-01 10:38:03',0,1,0,0,0,0),(6,'Wordpress','2017-02-06 08:40:39','2018-06-01 10:37:14',0,0,1,0,0,0),(7,'Mobile','2017-02-06 08:40:48','2018-06-01 10:38:25',0,0,0,1,0,0),(8,'Network','2017-02-06 08:40:57','2018-06-01 10:38:14',0,0,0,0,1,0),(11,'Hardcoded Secret','2017-04-11 05:11:49','2018-06-01 10:38:40',0,0,0,0,0,1);
/*!40000 ALTER TABLE `scanTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scans`
--

DROP TABLE IF EXISTS `scans`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scans` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `target` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `repoId` int(11) DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `statusReason` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `groupId` int(11) DEFAULT NULL,
  `scanTypeId` int(11) DEFAULT NULL,
  `totalCount` int(11) DEFAULT '0',
  `criticalCount` int(11) DEFAULT '0',
  `highCount` int(11) DEFAULT '0',
  `mediumCount` int(11) DEFAULT '0',
  `lowCount` int(11) DEFAULT '0',
  `infoCount` int(11) DEFAULT '0',
  `ownerTypeId` int(11) DEFAULT NULL,
  `isTaggedTools` tinyint(1) DEFAULT '0',
  `endTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `startTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `branch` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `scheduleTypeId` int(11) DEFAULT NULL,
  `supported` tinyint(1) DEFAULT '0',
  `scanPlatforms` varchar(255) DEFAULT NULL,
  `cloneRequired` tinyint(1) DEFAULT '0',
  `apkTempFile` varchar(255) DEFAULT NULL,
  `lastRunDate` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `userIdIndex` (`userId`),
  KEY `repoIdIndex` (`repoId`),
  KEY `groupIdIndex` (`groupId`),
  KEY `ownerTypeIdIndex` (`ownerTypeId`),
  KEY `scheduleTypeIdIndex` (`scheduleTypeId`),
  KEY `scanTypeIdIndex` (`scanTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scans`
--

LOCK TABLES `scans` WRITE;
/*!40000 ALTER TABLE `scans` DISABLE KEYS */;
/*!40000 ALTER TABLE `scans` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduleTypes`
--

DROP TABLE IF EXISTS `scheduleTypes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduleTypes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `days` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduleTypes`
--

LOCK TABLES `scheduleTypes` WRITE;
/*!40000 ALTER TABLE `scheduleTypes` DISABLE KEYS */;
INSERT INTO `scheduleTypes` VALUES (1,'MONTHLY',30),(2,'WEEKLY',7),(3,'DAILY',1);
/*!40000 ALTER TABLE `scheduleTypes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `severityLevels`
--

DROP TABLE IF EXISTS `severityLevels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `severityLevels` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  `enabled` tinyint(1) DEFAULT '0',
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `mailIds` text,
  `threshHoldCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `severityLevels`
--

LOCK TABLES `severityLevels` WRITE;
/*!40000 ALTER TABLE `severityLevels` DISABLE KEYS */;
INSERT INTO `severityLevels` VALUES (1,'High',1,'2018-06-14 07:56:21','2018-08-29 11:26:24',NULL,NULL),(2,'Medium',0,'2018-06-14 07:56:21','2018-08-29 11:26:24',NULL,NULL),(3,'Low',1,'2018-06-14 07:56:21','2018-08-29 11:26:24',NULL,NULL),(4,'Info',1,'2018-06-14 07:56:22','2018-08-29 11:26:24',NULL,NULL),(5,'Critical',1,'2018-06-18 10:29:35','2018-08-29 11:26:24',NULL,NULL);
/*!40000 ALTER TABLE `severityLevels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `smtp`
--

DROP TABLE IF EXISTS `smtp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `smtp` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `applicationUrl` varchar(255) DEFAULT NULL,
  `smtpHost` varchar(255) DEFAULT NULL,
  `smtpUserName` varchar(255) DEFAULT NULL,
  `smtpPassword` varchar(255) DEFAULT NULL,
  `smtpPort` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `smtp`
--

LOCK TABLES `smtp` WRITE;
/*!40000 ALTER TABLE `smtp` DISABLE KEYS */;
/*!40000 ALTER TABLE `smtp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tags`
--

DROP TABLE IF EXISTS `tags`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_tags_on_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tags`
--

LOCK TABLES `tags` WRITE;
/*!40000 ALTER TABLE `tags` DISABLE KEYS */;
/*!40000 ALTER TABLE `tags` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `taskRoute` varchar(255) DEFAULT NULL,
  `actionId` int(11) NOT NULL,
  `parentId` int(11) DEFAULT NULL,
  `ownerTypeId` int(11) DEFAULT NULL,
  `displayName` varchar(255) DEFAULT NULL,
  `apiUrl` varchar(255) DEFAULT NULL,
  `method` varchar(60) DEFAULT NULL,
  `accessToAll` tinyint(1) DEFAULT '0',
  `hideFromUi` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `actionIdIndex` (`actionId`),
  KEY `parentIdIndex` (`parentId`),
  KEY `ownerTypeIdIndex` (`ownerTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

LOCK TABLES `tasks` WRITE;
/*!40000 ALTER TABLE `tasks` DISABLE KEYS */;
INSERT INTO `tasks` VALUES (1,'Corporate Dashboard','/dashboard/',1,NULL,1,NULL,NULL,NULL,0,0),(2,'Team Dashboard','/dashboard/',1,NULL,2,NULL,NULL,NULL,0,0),(3,'Personal Dashboard','/dashboard/',1,NULL,3,NULL,NULL,NULL,0,0),(4,'Corporate Scans','/scans/',2,NULL,1,NULL,NULL,NULL,0,0),(5,'Team Scans','/scans/',2,NULL,2,NULL,NULL,NULL,0,0),(6,'Personal Scans','/scans/',2,NULL,3,NULL,NULL,NULL,0,0),(7,'Corporate Applications','/applications/',3,NULL,1,NULL,NULL,NULL,0,0),(9,'Personal Applications','/repos/',3,NULL,3,NULL,NULL,NULL,0,0),(10,'Corporate Analytics','/analytics/',4,NULL,1,NULL,NULL,NULL,0,0),(11,'Team Analytics','/analytics/',4,NULL,2,NULL,NULL,NULL,0,0),(12,'Personal Analytics','/analytics/',4,NULL,3,NULL,NULL,NULL,0,0),(13,'Corporate Filters','/filters/',5,NULL,1,NULL,NULL,NULL,0,0),(14,'Team Filters','/filters/',5,NULL,2,NULL,NULL,NULL,0,0),(15,'Personal Filters','/filters/',5,NULL,3,NULL,NULL,NULL,0,0),(16,'Scan Types','/scan_types',7,NULL,0,NULL,NULL,NULL,0,0),(17,'Tools Configuration','/tools',7,NULL,0,NULL,NULL,NULL,0,0),(19,'Jira Configuration','/jira',7,NULL,0,NULL,NULL,NULL,0,0),(20,'Git Configuration','/git',7,NULL,0,NULL,NULL,NULL,0,0),(21,'Configure Mails','/configure_mail',7,NULL,0,NULL,NULL,NULL,0,0),(22,'SMTP Settings','/smtp_settings',7,NULL,0,NULL,NULL,NULL,0,0),(23,'Users','/users',6,NULL,0,NULL,NULL,NULL,0,0),(24,'Groups','/groups',6,NULL,0,NULL,NULL,NULL,0,0),(25,'Roles','/roles',6,NULL,0,NULL,NULL,NULL,0,0),(41,'Read',NULL,2,4,1,NULL,'/api/v1/app/scans/list','POST',0,0),(42,'Read',NULL,2,5,2,NULL,'/api/v1/app/scans/list','POST',0,0),(43,'Read',NULL,2,6,3,NULL,'/api/v1/app/scans/list','POST',0,0),(44,'Create','/add_scan/1/4',2,4,1,NULL,'/api/v1/app/scans','POST',0,0),(45,'Create','/add_scan/2/4',2,5,2,NULL,'/api/v1/app/scans','POST',0,0),(46,'Create','/add_scan/3/4',2,6,3,NULL,'/api/v1/app/scans','POST',0,0),(47,'Delete',NULL,2,4,1,NULL,'/api/v1/app/scans/{id}','DELETE',0,0),(48,'Delete',NULL,2,5,2,NULL,'/api/v1/app/scans/{id}','DELETE',0,0),(49,'Delete',NULL,2,6,3,NULL,'/api/v1/app/scans/{id}','DELETE',0,0),(50,'Read',NULL,1,1,1,NULL,'/api/v1/app/dashboard/view','POST',0,0),(51,'Read',NULL,1,2,2,NULL,'/api/v1/app/dashboard/view','POST',0,0),(52,'Read',NULL,1,3,3,NULL,'/api/v1/app/dashboard/view','POST',0,0),(53,'Read',NULL,3,7,1,NULL,'/api/v1/app/applications/list','POST',0,0),(55,'Read',NULL,3,9,3,NULL,'/api/v1/app/applications/list','POST',0,0),(56,'Read',NULL,4,10,1,NULL,'/api/v1/app/analytics/view','POST',0,0),(57,'Read',NULL,4,11,2,NULL,'/api/v1/app/analytics/view','POST',0,0),(58,'Read',NULL,4,12,3,NULL,'/api/v1/app/analytics/view','POST',0,0),(59,'Read',NULL,5,13,1,NULL,'/api/v1/app/filters/list','POST',0,0),(60,'Read',NULL,5,14,2,NULL,'/api/v1/app/filters/list','POST',0,0),(61,'Read',NULL,5,15,3,NULL,'/api/v1/app/filters/list','POST',0,0),(62,'Read',NULL,6,23,0,NULL,'/api/v1/app/users/list','POST',0,0),(63,'Delete',NULL,6,23,0,NULL,'/api/v1/app/users/{id}','DELETE',0,0),(64,'Update','/edit_user/',6,23,0,NULL,'/api/v1/app/users/{id}','PUT',0,0),(65,'Create','add_group',6,24,0,NULL,'/api/v1/app/groups','POST',0,0),(66,'Read',NULL,6,24,0,NULL,'/api/v1/app/groups/list','POST',1,0),(67,'Delete',NULL,6,24,0,NULL,'/api/v1/app/groups/{id}','DELETE',0,0),(68,'Update','/edit_group/',6,24,0,NULL,'/api/v1/app/groups/{id}','PUT',0,0),(69,'Create','add_role',6,25,0,NULL,'/api/v1/app/roles','POST',0,0),(70,'Read',NULL,6,25,0,NULL,'/api/v1/app/roles/list','POST',0,0),(71,'Delete',NULL,6,25,0,NULL,'/api/v1/app/roles/{id}','DELETE',0,0),(72,'Update','/edit_role/',6,25,0,NULL,'/api/v1/app/roles/{id}','PUT',0,0),(73,'Create','add_scan_type',7,16,0,NULL,'/api/v1/app/scan_types','POST',0,0),(74,'Read',NULL,7,16,0,NULL,'/api/v1/app/scan_types/list','POST',1,0),(75,'Delete',NULL,7,16,0,NULL,'/api/v1/app/scan_types/{id}','DELETE',0,0),(76,'Update','/edit_scan_type/',7,16,0,NULL,'/api/v1/app/scan_types/{id}','PUT',0,0),(77,'Create','add_tool',7,17,0,NULL,'/api/v1/app/tools','POST',0,0),(78,'Read',NULL,7,17,0,NULL,'/api/v1/app/tools/list','POST',0,0),(79,'Delete',NULL,7,17,0,NULL,'/api/v1/app/tools/{id}','DELETE',0,0),(80,'Update','/edit_tool/',7,17,0,NULL,'/api/v1/app/tools/{id}','PUT',0,0),(83,'Update',NULL,7,19,0,NULL,'/api/v1/app/git/{id}','PUT',0,0),(84,'Read',NULL,7,19,0,NULL,'/api/v1/app/git/{id}','GET',0,0),(85,'Update',NULL,7,20,0,NULL,'/api/v1/app/jira/{id}','PUT',0,0),(86,'Read',NULL,7,20,0,NULL,'/api/v1/app/jira/{id}','GET',0,0),(87,'Update',NULL,7,21,0,NULL,'/api/v1/app/smtp/{id}','PUT',0,0),(88,'Create',NULL,7,21,0,NULL,'/api/v1/app/smtp','POST',0,0),(91,'Tasks List',NULL,0,NULL,0,NULL,'/api/v1/app/tasks/list','POST',1,1),(92,'Signup Role Configuration','/signup_role_configuration',7,NULL,0,NULL,NULL,NULL,0,0),(93,'Update',NULL,7,92,0,NULL,'/api/v1/app/default_role/{id}','PUT',0,0),(94,'Hardcode Secrets Configuration','/hardcode_secrets_configuration',7,NULL,0,NULL,NULL,NULL,0,0),(95,'Create',NULL,7,94,0,NULL,'/api/v1/app/hardcode_secret','POST',0,0),(98,'Create',NULL,7,92,0,NULL,'/api/v1/app/default_role','POST',0,0),(99,'Update',NULL,7,94,0,NULL,'/api/v1/app/hardcode_secret/{id}','PUT',0,0),(101,'Actions List',NULL,0,NULL,0,NULL,'/api/v1/app/actions/list','POST',1,1),(102,'Findings',NULL,2,NULL,0,NULL,NULL,NULL,0,1),(104,'Tags',NULL,2,NULL,0,NULL,NULL,NULL,0,1),(105,'Comments',NULL,2,NULL,0,NULL,NULL,NULL,0,1),(107,'Uploads',NULL,2,NULL,0,NULL,NULL,NULL,0,1),(112,'Repos List',NULL,0,NULL,0,NULL,'/api/v1/app/repos/list','POST',1,1),(113,'Languages List',NULL,0,NULL,0,NULL,'/api/v1/app/languages/list','POST',1,1),(114,'Scheduled Tyles List',NULL,0,NULL,0,NULL,'/api/v1/app/schedule_types/list','POST',1,1),(117,'Executive Dashboard','/excutive_dashboard/',1,NULL,1,NULL,NULL,NULL,0,0),(118,'Read',NULL,1,117,1,NULL,NULL,NULL,0,0),(120,'Logout',NULL,0,NULL,0,NULL,'/api/v1/app/logout','DELETE',1,1),(121,'Change Password',NULL,0,NULL,0,NULL,'/api/v1/app/users/change_password','POST',1,1),(122,'Reset Password Token',NULL,0,NULL,0,NULL,'/api/v1/app/verify_reset_password_token','GET',1,1),(123,'Reset Password',NULL,0,NULL,0,NULL,'/api/v1/app/reset_password','POST',1,1),(124,'List',NULL,2,102,0,NULL,'/api/v1/app/findings/list','POST',0,0),(125,'Update',NULL,2,102,0,NULL,'/api/v1/app/findings/{id}','PUT',0,0),(126,'Create',NULL,2,105,0,NULL,'/api/v1/app/comments','POST',0,0),(127,'Read',NULL,2,105,0,NULL,'/api/v1/app/comments/list','POST',0,0),(128,'Read',NULL,2,107,0,NULL,'/api/v1/app/uploads/list','POST',0,0),(129,'Create',NULL,2,107,0,NULL,'/api/v1/app/uploads','DELETE',0,0),(130,'Read',NULL,2,104,0,NULL,'/api/v1/app/tags/list','POST',0,0),(131,'Create',NULL,2,104,0,NULL,'/api/v1/app/tags','POST',0,0),(132,'View',NULL,2,102,0,NULL,'/api/v1/app/findings/{id}','GET',0,0),(133,'Create',NULL,7,20,0,NULL,'/api/v1/app/git','POST',0,0),(134,'Create',NULL,7,19,0,NULL,'/api/v1/app/jira','POST',0,0),(135,'Create',NULL,7,94,0,NULL,'/api/v1/app/hardcode_secret','POST',0,0),(136,'Update',NULL,2,4,1,NULL,'/api/v1/app/scans/{id}','PUT',0,0),(137,'Update',NULL,2,5,2,NULL,'/api/v1/app/scans/{id}','PUT',0,0),(138,'Update',NULL,2,6,3,NULL,'/api/v1/app/scans/{id}','PUT',0,0);
/*!40000 ALTER TABLE `tasks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toolInstances`
--

DROP TABLE IF EXISTS `toolInstances`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `toolInstances` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `toolId` int(11) DEFAULT NULL,
  `sessionId` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `maxAllowedScans` int(11) DEFAULT '0',
  `currentRunningScans` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `toolIdIndex` (`toolId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toolInstances`
--

LOCK TABLES `toolInstances` WRITE;
/*!40000 ALTER TABLE `toolInstances` DISABLE KEYS */;
/*!40000 ALTER TABLE `toolInstances` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tools`
--

DROP TABLE IF EXISTS `tools`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tools` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `languageId` int(11) DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isEnabled` tinyint(1) DEFAULT '1',
  `scanTypeId` int(11) DEFAULT NULL,
  `manifestJson` text,
  `instanceCount` int(11) DEFAULT NULL,
  `status` varchar(65) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `languageIdIndex` (`languageId`),
  KEY `scanTypeIdIndex` (`scanTypeId`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tools`
--

LOCK TABLES `tools` WRITE;
/*!40000 ALTER TABLE `tools` DISABLE KEYS */;
/*!40000 ALTER TABLE `tools` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uploads`
--

DROP TABLE IF EXISTS `uploads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uploads` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `findingId` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `file_content_type` varchar(255) DEFAULT NULL,
  `file_file_size` int(11) DEFAULT NULL,
  `file_updated_at` datetime DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `userId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `findingIdIndex` (`findingId`),
  KEY `userIdIndex` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uploads`
--

LOCK TABLES `uploads` WRITE;
/*!40000 ALTER TABLE `uploads` DISABLE KEYS */;
/*!40000 ALTER TABLE `uploads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `resetPasswordToken` varchar(255) DEFAULT NULL,
  `resetPasswordSentAt` datetime DEFAULT NULL,
  `createdAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedAt` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `isDeleted` tinyint(1) DEFAULT '0',
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `emailIndex` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=133 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (118,'jackhammer@olacabs.com',NULL,NULL,'2018-05-22 10:13:24','2018-08-30 04:03:05','jackhammer',0,'$2a$10$ZNZBh9v4xvUGH2W3ST5qR..40OhOwVps4EeKVs50qSSepSUqfcMP6');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
alter table findings add column modifiedBy varchar(255);
INSERT INTO languages (name,fileExtension) VALUES ('Python','py');
insert into groups(name,scanTypeId) values('Web',(select id from scanTypes where name='Web'));
insert into groups(name,scanTypeId) values('Wordpress',(select id from scanTypes where name='Wordpress'));
insert into groups(name,scanTypeId) values('Mobile',(select id from scanTypes where name='Mobile'));
insert into groups(name,scanTypeId) values('Network',(select id from scanTypes where name='Network'));
-- Dump completed on 2018-09-19 11:35:46
