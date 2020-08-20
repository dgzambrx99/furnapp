CREATE TABLE `shippinginput` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts` datetime NOT NULL,
  `tc` varchar(6) NOT NULL DEFAULT '',
  `mp1` varchar(6) NOT NULL DEFAULT '',
  `mp2` varchar(6) NOT NULL DEFAULT '',
  `lc1` varchar(6) NOT NULL DEFAULT '',
  `lc2` varchar(6) NOT NULL DEFAULT '',
  `lc3` varchar(6) NOT NULL DEFAULT '',
  `selected` tinyint(4) NOT NULL DEFAULT '0',
  `which` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
);

CREATE TABLE `checktable` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) NOT NULL DEFAULT '',
  `filepath` varchar(255) NOT NULL DEFAULT '',
  `hash` varchar(64) NOT NULL DEFAULT '',
  `mtime` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `hash` (`hash`)
);

