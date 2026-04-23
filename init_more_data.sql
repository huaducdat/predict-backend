-- =========================
-- AUTO GENERATE MORE DATA
-- =========================

SET @id = 7;

-- từ ngày 2026-04-08 -> 2026-05-25
-- tạo ~48 ngày (có skip ngày để test missing)

-- LOOP giả lập (viết tay cho MySQL)

-- =========================
-- DAY 8
-- =========================
INSERT INTO `result` VALUES (@id, '2026-04-08', 55);
INSERT INTO `result_numbers` VALUES
(@id,55),(@id,55),(@id,66),(@id,77),(@id,88),(@id,99),
(@id,1),(@id,2),(@id,3),(@id,4),(@id,5),(@id,6),(@id,7),(@id,8),(@id,9),
(@id,10),(@id,11),(@id,12),(@id,13),(@id,14),(@id,15),(@id,16),(@id,17),
(@id,18),(@id,19),(@id,20);
SET @id = @id + 1;

-- =========================
-- DAY 9
-- =========================
INSERT INTO `result` VALUES (@id, '2026-04-09', 55);
INSERT INTO `result_numbers` VALUES
(@id,55),(@id,55),(@id,55),(@id,66),(@id,77),(@id,88),(@id,99),
(@id,21),(@id,22),(@id,23),(@id,24),(@id,25),(@id,26),(@id,27),(@id,28),
(@id,29),(@id,30),(@id,31),(@id,32),(@id,33),(@id,34),(@id,35),
(@id,36),(@id,37),(@id,38),(@id,39);
SET @id = @id + 1;

-- =========================
-- DAY 10 (reset -> số khác)
-- =========================
INSERT INTO `result` VALUES (@id, '2026-04-10', 22);
INSERT INTO `result_numbers` VALUES
(@id,22),(@id,22),(@id,22),(@id,22),
(@id,33),(@id,44),(@id,55),(@id,66),
(@id,40),(@id,41),(@id,42),(@id,43),(@id,45),(@id,46),(@id,47),(@id,48),
(@id,49),(@id,50),(@id,51),(@id,52),(@id,53),(@id,54),(@id,56),
(@id,57),(@id,58),(@id,59);
SET @id = @id + 1;

-- =========================
-- DAY 11 (continue streak 22)
-- =========================
INSERT INTO `result` VALUES (@id, '2026-04-11', 22);
INSERT INTO `result_numbers` VALUES
(@id,22),(@id,22),(@id,22),
(@id,33),(@id,44),(@id,55),
(@id,60),(@id,61),(@id,62),(@id,63),(@id,64),(@id,65),(@id,67),(@id,68),
(@id,69),(@id,70),(@id,71),(@id,72),(@id,73),(@id,74),(@id,75),
(@id,76),(@id,77),(@id,78),(@id,79);
SET @id = @id + 1;

-- =========================
-- DAY 12 (skip DAY 12 -> missing)
-- =========================

-- =========================
-- DAY 13 (reset toàn bộ do missing)
-- =========================
INSERT INTO `result` VALUES (@id, '2026-04-13', 88);
INSERT INTO `result_numbers` VALUES
(@id,88),(@id,88),(@id,88),
(@id,77),(@id,66),(@id,55),
(@id,80),(@id,81),(@id,82),(@id,83),(@id,84),(@id,85),(@id,86),(@id,87),
(@id,89),(@id,90),(@id,91),(@id,92),(@id,93),(@id,94),(@id,95),
(@id,96),(@id,97),(@id,98),(@id,99);
SET @id = @id + 1;

-- =========================
-- AUTO CONTINUE (pattern)
-- =========================

-- Tạo thêm nhiều ngày random nhẹ
INSERT INTO `result` VALUES
(@id, '2026-04-14', 88),
(@id+1, '2026-04-15', 33),
(@id+2, '2026-04-16', 33),
(@id+3, '2026-04-17', 33),
(@id+4, '2026-04-18', 77),
(@id+5, '2026-04-20', 77), -- skip 19 -> missing
(@id+6, '2026-04-21', 11),
(@id+7, '2026-04-22', 11),
(@id+8, '2026-04-23', 11),
(@id+9, '2026-04-24', 44),
(@id+10, '2026-04-25', 44);

-- mỗi ngày add numbers đơn giản (cho nhẹ)
INSERT INTO `result_numbers`
SELECT r.id, FLOOR(RAND()*100)
FROM `result` r
JOIN (
  SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5
  UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9 UNION SELECT 10
  UNION SELECT 11 UNION SELECT 12 UNION SELECT 13 UNION SELECT 14 UNION SELECT 15
  UNION SELECT 16 UNION SELECT 17 UNION SELECT 18 UNION SELECT 19 UNION SELECT 20
  UNION SELECT 21 UNION SELECT 22 UNION SELECT 23 UNION SELECT 24 UNION SELECT 25
  UNION SELECT 26 UNION SELECT 27
) t;