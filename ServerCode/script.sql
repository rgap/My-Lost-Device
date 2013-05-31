CREATE TABLE IF NOT EXISTS `DEVICE` (
  `devId` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) NOT NULL,
  `devType` char(200) DEFAULT NULL,
  `devLocation` char(30) DEFAULT NULL,
  `devState` int(11) DEFAULT NULL,
  `devCreated` datetime DEFAULT NULL,
  PRIMARY KEY (`devId`),
  KEY `Relationship1` (`userId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=48 ;


CREATE TABLE IF NOT EXISTS `DUSER` (
  `userId` int(11) NOT NULL AUTO_INCREMENT,
  `userName` char(30) NOT NULL,
  `userPass` char(30) NOT NULL,
  PRIMARY KEY (`userId`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=19 ;

