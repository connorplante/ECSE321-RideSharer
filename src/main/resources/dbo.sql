DROP DATABASE IF EXISTS dbo;

CREATE DATABASE dbo;

use dbo;

CREATE TABLE `Users` (
  `UserID` int(11) NOT NULL,
  `Username` varchar(45) DEFAULT NULL,
  `Email` varchar(45) DEFAULT NULL,
  `Password` varchar(45) DEFAULT NULL,
  `FirstName` varchar(45) DEFAULT NULL,
  `LastName` varchar(45) DEFAULT NULL,
  `Rating` int(11) DEFAULT NULL,
  `numRides` int(11) DEFAULT NULL,
  `Role` int(11) DEFAULT NULL,
  `Status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`UserID`),
  UNIQUE KEY `UserID_UNIQUE` (`UserID`),
  UNIQUE KEY `Username_UNIQUE` (`Username`)
);

CREATE TABLE `Cars` (
  `CarID` int(11) NOT NULL,
  `Make` varchar(45) DEFAULT NULL,
  `Model` varchar(45) DEFAULT NULL,
  `Year` int(11) DEFAULT NULL,
  `numSeats` int(11) DEFAULT NULL,
  `LicensePlate` varchar(45) DEFAULT NULL,
  `FK_UserID` int(11) DEFAULT NULL,
  PRIMARY KEY (`CarID`),
  UNIQUE KEY `CarID_UNIQUE` (`CarID`)
);

CREATE TABLE `Legs` (
  `LegID` int(11) NOT NULL,
  `Start` varchar(45) DEFAULT NULL,
  `End` varchar(45) DEFAULT NULL,
  `Price` int(11) DEFAULT NULL,
  `NumSeats` int(11) DEFAULT NULL,
  `FK_TripID` int(11) NOT NULL,
  PRIMARY KEY (`LegID`),
  UNIQUE KEY `LegID_UNIQUE` (`LegID`)
);


CREATE TABLE `PassengerTrips` (
  `PassengerTripID` int(11) NOT NULL AUTO_INCREMENT,
  `FK_TripID` int(11) DEFAULT NULL,
  `FK_UserID` int(11) DEFAULT NULL,
  `Price` int(11) DEFAULT NULL,
  PRIMARY KEY (`PassengerTripID`),
  UNIQUE KEY `PassengerTripID_UNIQUE` (`PassengerTripID`)
);


CREATE TABLE `Trips` (
  `TripID` int(11) NOT NULL AUTO_INCREMENT,
  `Date` datetime DEFAULT NULL,
  `Time` int(11) DEFAULT NULL,
  `Start` varchar(45) DEFAULT NULL,
  `End` varchar(45) DEFAULT NULL,
  `FK_UserID` int(11) DEFAULT NULL,
  `FK_CarID` int(11) DEFAULT NULL,
  PRIMARY KEY (`TripID`),
  UNIQUE KEY `TripID_UNIQUE` (`TripID`),
  KEY `FK_CarID_idx` (`FK_CarID`),
  KEY `FK_UserID_idx` (`FK_UserID`) 
);




