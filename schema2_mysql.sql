# delete from furnacedata_multipoint; delete from furnacedata_loadtc; delete from furnace_base; delete from iceboxdata_value; delete from icebox_base; delete from checktable;
# drop table furnace_base; drop table furnacedata_multipoint; drop table furnacedata_loadtc; drop table checktable; drop view shippinginput;
# select * from furnace_base; select * from furnacedata_multipoint; select * from furnacedata_loadtc; select * from iceboxdata_value; select * from icebox_base; select * from checktable;

CREATE TABLE `furnace_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts` datetime NOT NULL,
  `tc` varchar(128) NOT NULL DEFAULT '',
  `qt_water` varchar(128) NOT NULL DEFAULT '',
  `qt_35` varchar(128) NOT NULL DEFAULT '',
  `qt_16` varchar(128) NOT NULL DEFAULT '',
  `selected` tinyint(4) NOT NULL DEFAULT '0',
  `which` int(11) NOT NULL DEFAULT '0',
  INDEX `which` (`which`),
  PRIMARY KEY (`id`)
);

# replaces mp1, mp2
CREATE TABLE `furnacedata_multipoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` int(11) NOT NULL DEFAULT '0',
  `data_value` varchar(128) NOT NULL DEFAULT '',
  `data_order` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fbid` (`fbid`),
  INDEX `fbid_data_order` (`fbid`,`data_order`),
  FOREIGN KEY (`fbid`) REFERENCES furnace_base(`id`)
);

# replaces lc1, lc2, lc3
CREATE TABLE `furnacedata_loadtc` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` int(11) NOT NULL DEFAULT '0',
  `data_value` varchar(128) NOT NULL DEFAULT '',
  `data_order` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fbid` (`fbid`),
  INDEX `fbid_data_order` (`fbid`,`data_order`),
  FOREIGN KEY (`fbid`) REFERENCES furnace_base(`id`)
);

CREATE TABLE `icebox_base` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ts` datetime NOT NULL,
  `selected` tinyint(4) NOT NULL DEFAULT '0',
  `which` int(11) NOT NULL DEFAULT '0',
  INDEX `which` (`which`),
  PRIMARY KEY (`id`)
);

CREATE TABLE `iceboxdata_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fbid` int(11) NOT NULL DEFAULT '0',
  `data_value` varchar(128) NOT NULL DEFAULT '',
  `data_order` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `fbid` (`fbid`),
  INDEX `fbid_data_order` (`fbid`,`data_order`),
  FOREIGN KEY (`fbid`) REFERENCES icebox_base(`id`)
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

CREATE OR REPLACE VIEW pfs1 AS
SELECT fb.id, fb.ts, format(fb.tc,1), fb.qt_water, fb.qt_35, fb.qt_16, fb.selected, fb.which,
(SELECT format(fm.data_value,1) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 0) AS mp1,
(SELECT format(fm.data_value,1) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 1) AS mp2,
(SELECT format(fm.data_value,1) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 2) AS mp3,
(SELECT format(fl.data_value,1) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 0) AS lc1,
(SELECT format(fl.data_value,1) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 1) AS lc2,
(SELECT format(fl.data_value,1) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 2) AS lc3
FROM furnace_base fb;

CREATE VIEW icebox AS
SELECT ib.id, ib.ts, ib.selected, ib.which,
(SELECT format(ibd.data_value,1) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 0) AS ib1,
(SELECT format(ibd.data_value,1) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 1) AS ib2,
(SELECT format(ibd.data_value,1) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 2) AS ib3
FROM icebox_base ib;

