CREATE TABLE furnace_base (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  ts datetime NOT NULL,
  tc varchar(128) NOT NULL DEFAULT '',
  qt_water varchar(128) NOT NULL DEFAULT '',
  qt_35 varchar(128) NOT NULL DEFAULT '',
  qt_16 varchar(128) NOT NULL DEFAULT '',
  selected tinyint NOT NULL DEFAULT '0',
  which INT NOT NULL DEFAULT '0'
);

CREATE NONCLUSTERED INDEX ix_furnace_base_which ON furnace_base (which);

-- replaces mp1, mp2
CREATE TABLE furnacedata_multipoint (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  fbid INT NOT NULL DEFAULT '0' constraint fk_furnacedata_multipoint_fbid foreign key references furnace_base(id),
  data_value varchar(128) NOT NULL DEFAULT '',
  data_order INT NOT NULL DEFAULT '0'
);

CREATE NONCLUSTERED INDEX ix_furnacedata_multipoint_fbid ON furnacedata_multipoint (fbid);
CREATE NONCLUSTERED INDEX ix_furnacedata_multipoint_data_order ON furnacedata_multipoint (fbid,data_order);

-- replaces lc1, lc2, lc3
CREATE TABLE furnacedata_loadtc (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  fbid INT NOT NULL DEFAULT '0' constraint fk_furnacedata_loadtc_fbid foreign key references furnace_base(id),
  data_value varchar(128) NOT NULL DEFAULT '',
  data_order INT NOT NULL DEFAULT '0'
);

CREATE NONCLUSTERED INDEX ix_furnacedata_loadtc_fbid ON furnacedata_loadtc (fbid);
CREATE NONCLUSTERED INDEX ix_furnacedata_loadtc_data_order ON furnacedata_loadtc (fbid,data_order);

CREATE TABLE icebox_base (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  ts datetime NOT NULL,
  selected tinyint NOT NULL DEFAULT '0',
  which INT NOT NULL DEFAULT '0'
);

CREATE NONCLUSTERED INDEX ix_icebox_base_which ON icebox_base (which);

CREATE TABLE iceboxdata_value (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  fbid INT NOT NULL DEFAULT '0' constraint fk_iceboxdata_value_fbid foreign key references icebox_base(id),
  data_value varchar(128) NOT NULL DEFAULT '',
  data_order INT NOT NULL DEFAULT '0'
);

CREATE NONCLUSTERED INDEX ix_iceboxdata_value_fbid ON iceboxdata_value (fbid);
CREATE NONCLUSTERED INDEX ix_iceboxdata_value_data_order ON iceboxdata_value (fbid,data_order);

CREATE TABLE checktable (
  id INT NOT NULL IDENTITY(1,1) PRIMARY KEY,
  filename varchar(255) NOT NULL DEFAULT '',
  filepath varchar(255) NOT NULL DEFAULT '',
  hash varchar(64) NOT NULL DEFAULT '',
  mtime INT NOT NULL DEFAULT '0',
  CONSTRAINT ix_hash UNIQUE (hash)
);


