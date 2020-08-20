
SELECT fb.id, fb.ts, TRY_CAST(fb.tc AS int) as tc, fb.qt_water, fb.qt_35, fb.qt_16, fb.selected, fb.which,
(SELECT TRY_CAST(fm.data_value AS int (10,1)) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 0) AS mp1,
(SELECT TRY_CAST(fm.data_value AS int (10,1)) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 1) AS mp2,
(SELECT TRY_CAST(fm.data_value AS int (10,1)) FROM furnacedata_multipoint fm WHERE fm.fbid = fb.id AND data_order = 2) AS mp3,
(SELECT TRY_CAST(fl.data_value AS int (10,1)) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 0) AS lc1,
(SELECT CONVERT(DECIMAL(10,1),fl.data_value) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 1) AS lc2,
(SELECT CONVERT(DECIMAL(10,1),fl.data_value) FROM furnacedata_loadtc fl WHERE fl.fbid = fb.id AND data_order = 2) AS lc3
FROM furnace_base fb;



use furnapp2
SELECT TRY_CAST (data_value as INT) from furnacedata_loadtc