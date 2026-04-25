USE prediction_db;

-- clear data
DELETE FROM result_numbers;
DELETE FROM result;

-- =========================
-- CONFIG
-- =========================
SET @days = 200;
SET @startDate = CURDATE(); -- 🔥 hôm nay
SET @id = 0;

-- =========================
-- GENERATE RESULT TABLE
-- =========================
INSERT INTO result (id, date, single_number)
SELECT 
    @id := @id + 1,
    DATE_SUB(@startDate, INTERVAL t.n DAY), -- 🔥 lùi về quá khứ
    CASE
        WHEN t.n BETWEEN 0 AND 5 THEN 55
        WHEN t.n BETWEEN 6 AND 15 THEN 22
        WHEN t.n BETWEEN 16 AND 30 THEN 33
        WHEN t.n BETWEEN 31 AND 45 THEN 77
        WHEN t.n BETWEEN 46 AND 60 THEN 11
        WHEN t.n BETWEEN 61 AND 80 THEN 44
        WHEN t.n BETWEEN 81 AND 100 THEN 88
        WHEN t.n BETWEEN 101 AND 130 THEN 66
        WHEN t.n BETWEEN 131 AND 160 THEN 99
        ELSE FLOOR(RAND()*100)
    END
FROM (
    SELECT @row := @row + 1 AS n
    FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) a,
         (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) b,
         (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4) c,
         (SELECT @row := -1) init
    LIMIT 200
) t;