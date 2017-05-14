-- phpMyAdmin SQL Dump
-- version 2.11.9.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Erstellungszeit: 08. Mai 2017 um 08:10
-- Server Version: 5.0.67
-- PHP-Version: 5.2.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `mmbbs_trader`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ereignis`
--

DROP TABLE IF EXISTS `ereignis`;
CREATE TABLE IF NOT EXISTS `ereignis` (
  `id`           INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `beschreibung` VARCHAR(255)     NOT NULL,
  `statement`    VARCHAR(255)     NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 4;

--
-- Daten für Tabelle `ereignis`
--

INSERT INTO `ereignis` (`id`, `beschreibung`, `statement`) VALUES
  (1, 'Brand im Lagerhaus, Verlust von 50 % der Nahrungsmittel in Kairo', 'Update ort_has_ware SET ...'),
  (2, 'wareion', ''),
  (3, 'wareion', '');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ort`
--

DROP TABLE IF EXISTS `ort`;
CREATE TABLE IF NOT EXISTS `ort` (
  `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(25)               DEFAULT NULL,
  `kapazitaet` INT(10) UNSIGNED          DEFAULT NULL,
  `x`          INT(11)                   DEFAULT NULL,
  `y`          INT(11)                   DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 7;

--
-- Daten für Tabelle `ort`
--

INSERT INTO `ort` (`id`, `name`, `kapazitaet`, `x`, `y`) VALUES
  (1, 'Alexandria', 300, 1600, 200),
  (2, 'Tunis', 500, 450, 300),
  (3, 'Rom', 500, 850, 800),
  (4, 'Venedig', 800, 900, 950),
  (5, 'Istanbul', 800, 1650, 750),
  (6, 'Barcelona', 300, 300, 750);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ort_has_ware`
--

DROP TABLE IF EXISTS `ort_has_ware`;
CREATE TABLE IF NOT EXISTS `ort_has_ware` (
  `ware_id`    INT(10) UNSIGNED NOT NULL,
  `ort_id`     INT(10) UNSIGNED NOT NULL,
  `menge`      DECIMAL(10, 0) UNSIGNED DEFAULT NULL,
  `kapazitaet` INT(11)          NOT NULL,
  `preis`      DECIMAL(10, 0)          DEFAULT NULL,
  `produktion` DOUBLE           NOT NULL,
  `verbrauch`  DOUBLE           NOT NULL,
  KEY `ort_has_ware_FKIndex1` (`ort_id`),
  KEY `ort_has_ware_FKIndex2` (`ware_id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1;

--
-- Daten für Tabelle `ort_has_ware`
--

INSERT INTO `ort_has_ware` (`ware_id`, `ort_id`, `menge`, `kapazitaet`, `preis`, `produktion`, `verbrauch`) VALUES
  (1, 1, 62, 300, 61, 0.607012017264042, 0.973718779137096),
  (2, 1, 110, 300, 115, 0.0475578746269996, 0.316633066457355),
  (3, 1, 134, 300, 27, 0.440491739139419, 0.252559527058675),
  (4, 1, 129, 300, 20, 0.941322448608766, 0.948933692601448),
  (5, 1, 142, 300, 74, 0.920701147914586, 0.756704692502231),
  (6, 1, 49, 300, 253, 0.0214200495010429, 0.836986200732166),
  (1, 2, 184, 500, 49, 0.120670885891347, 0.092395857993882),
  (2, 2, 165, 500, 106, 0.0999666630290138, 0.222645771897068),
  (3, 2, 192, 500, 25, 0.813328901131944, 0.398707220702159),
  (4, 2, 138, 500, 20, 0.553549430848611, 0.571625522870222),
  (5, 2, 171, 500, 83, 0.19747935253892, 0.272520224817582),
  (6, 2, 272, 500, 110, 0.770163097204792, 0.033254842304862),
  (1, 3, 335, 500, 37, 0.855784950643578, 0.179160257036947),
  (2, 3, 278, 500, 90, 0.328446463987647, 0.104751579561039),
  (3, 3, 270, 500, 28, 0.538418536575901, 0.377837974930031),
  (4, 3, 155, 500, 20, 0.273934295656098, 0.236157584224043),
  (5, 3, 284, 500, 70, 0.358985064885565, 0.0864526024893416),
  (6, 3, 230, 500, 163, 0.355307848523658, 0.517181465883908),
  (1, 4, 494, 800, 40, 0.519983814582214, 0.048374705992988),
  (2, 4, 483, 800, 83, 0.681922116951944, 0.264486497514403),
  (3, 4, 337, 800, 36, 0.276666167449827, 0.589871375439569),
  (4, 4, 258, 800, 31, 0.119358405582009, 0.827177932324985),
  (5, 4, 474, 800, 68, 0.777814475612542, 0.407538611821401),
  (6, 4, 481, 800, 125, 0.704249663003022, 0.298632510284551),
  (1, 5, 475, 800, 42, 0.380413593147335, 0.00617046561666808),
  (2, 5, 492, 800, 81, 0.88961157937498, 0.42954653075854),
  (3, 5, 475, 800, 25, 0.478897946401404, 0.105850170465047),
  (4, 5, 389, 800, 21, 0.092557043854666, 0.145234738611835),
  (5, 5, 328, 800, 98, 0.448502592228821, 0.806808776042246),
  (6, 5, 533, 800, 113, 0.688536134258412, 0.0222543738989666),
  (1, 6, 141, 300, 53, 0.0456634974532421, 0.161554396302956),
  (2, 6, 135, 300, 111, 0.670781519888697, 0.869244441268262),
  (3, 6, 170, 300, 26, 0.333877677408864, 0.0616550939731813),
  (4, 6, 147, 300, 20, 0.306642468372958, 0.348247090678892),
  (5, 6, 207, 300, 58, 0.821308079009231, 0.0617991537431247),
  (6, 6, 210, 300, 107, 0.845071562421575, 0.0399603816121448);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `schiff`
--

DROP TABLE IF EXISTS `schiff`;
CREATE TABLE IF NOT EXISTS `schiff` (
  `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `ort_id`     INT(10) UNSIGNED NOT NULL,
  `trader_id`  INT(10) UNSIGNED NOT NULL,
  `name`       VARCHAR(25)               DEFAULT NULL,
  `tonnage`    INT(10) UNSIGNED          DEFAULT NULL,
  `wert`       DOUBLE                    DEFAULT NULL,
  `fahrkosten` FLOAT                     DEFAULT NULL,
  `blocked`    TINYINT(1)       NOT NULL,
  PRIMARY KEY (`id`),
  KEY `schiff_FKIndex1` (`trader_id`),
  KEY `schiff_FKIndex2` (`ort_id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 19;

--
-- Daten für Tabelle `schiff`
--

INSERT INTO `schiff` (`id`, `ort_id`, `trader_id`, `name`, `tonnage`, `wert`, `fahrkosten`, `blocked`) VALUES
  (1, 1, 1, 'galeere', 1000, 500, 1, 0),
  (2, 3, 2, 'euler_schiff', 1000, 700, 1, 0),
  (3, 3, 3, 'mega imba supercrafter', 1000, 10000, 1, 0),
  (4, 4, 4, 'alex sein rosarotes flasc', 1000, 1000, 1, 0),
  (5, 3, 5, 'Patricks Enterprise', 1000, 500, 1, 1),
  (18, 2, 18, 'Unsinkable', 1000, 1000, 10, 1);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `schiff_has_ware`
--

DROP TABLE IF EXISTS `schiff_has_ware`;
CREATE TABLE IF NOT EXISTS `schiff_has_ware` (
  `ware_id`   INT(10) UNSIGNED NOT NULL,
  `schiff_id` INT(10) UNSIGNED NOT NULL,
  `menge`     INT(10) UNSIGNED DEFAULT NULL,
  PRIMARY KEY (`ware_id`, `schiff_id`),
  KEY `schiff_has_ware_FKIndex1` (`ware_id`),
  KEY `schiff_has_ware_FKIndex2` (`schiff_id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1;

--
-- Daten für Tabelle `schiff_has_ware`
--

INSERT INTO `schiff_has_ware` (`ware_id`, `schiff_id`, `menge`) VALUES
  (1, 1, 10),
  (2, 1, 0),
  (3, 1, 10),
  (4, 1, 0),
  (5, 1, 0),
  (6, 1, 0),
  (1, 2, 0),
  (2, 2, 0),
  (3, 2, 0),
  (4, 2, 0),
  (5, 2, 0),
  (6, 2, 0),
  (1, 3, 0),
  (2, 3, 0),
  (3, 3, 0),
  (4, 3, 0),
  (5, 3, 0),
  (6, 3, 0),
  (1, 4, 0),
  (2, 4, 0),
  (3, 4, 0),
  (4, 4, 0),
  (5, 4, 0),
  (6, 4, 0),
  (1, 5, 0),
  (2, 5, 0),
  (3, 5, 40),
  (4, 5, 40),
  (5, 5, 0),
  (6, 5, 0),
  (6, 18, 20),
  (5, 18, 0),
  (4, 18, 110),
  (3, 18, 20),
  (2, 18, 40),
  (1, 18, 10);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `trader`
--

DROP TABLE IF EXISTS `trader`;
CREATE TABLE IF NOT EXISTS `trader` (
  `id`     INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`   VARCHAR(25)               DEFAULT NULL,
  `pass`   VARCHAR(32)               DEFAULT NULL,
  `geld`   DOUBLE                    DEFAULT NULL,
  `master` VARCHAR(1)       NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 20;

--
-- Daten für Tabelle `trader`
--

INSERT INTO `trader` (`id`, `name`, `pass`, `geld`, `master`) VALUES
  (1, 'nero', '770bfedcd56afdbb406a93355add8070', 9120, '0'),
  (2, 'cleopatra', 'e2f9f842fd8e1ae90dc428d39cab7167', 10000, '0'),
  (3, 'Konstantin', '9115b9aab6fd005f51e429a39e9e9618', 10000, '0'),
  (4, 'Freibeuter James', 'd52e32f3a96a64786814ae9b5279fbe5', 10000, '0'),
  (5, 'otto', 'e5645dd85deb100fd1d71d0e8d671091', 8050, '0'),
  (18, 'x', '9dd4e461268c8034f5c8564e155c67a6', 6820, '1'),
  (19, 'y', '415290769594460e2e485922904f345d', 10000, '0');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `ware`
--

DROP TABLE IF EXISTS `ware`;
CREATE TABLE IF NOT EXISTS `ware` (
  `id`         INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(25)               DEFAULT NULL,
  `basispreis` DOUBLE(10, 0)    NOT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = latin1
  AUTO_INCREMENT = 7;

--
-- Daten für Tabelle `ware`
--

INSERT INTO `ware` (`id`, `name`, `basispreis`) VALUES
  (1, 'Gewürze', 50),
  (2, 'Seide', 100),
  (3, 'Wolle', 30),
  (4, 'Nahrung', 20),
  (5, 'Waffen', 80),
  (6, 'Sklaven', 150);
