CREATE VIEW icebox AS
SELECT ib.id, ib.ts, ib.selected, ib.which,
(SELECT CONVERT(DECIMAL(10,1),ibd.data_value) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 0) AS ib1,
(SELECT CONVERT(DECIMAL(10,1),ibd.data_value) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 1) AS ib2,
(SELECT CONVERT(DECIMAL(10,1),ibd.data_value) FROM iceboxdata_value ibd WHERE ibd.fbid = ib.id AND data_order = 2) AS ib3
FROM icebox_base ib;