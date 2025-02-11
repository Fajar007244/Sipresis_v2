ALTER TABLE siswa ADD COLUMN is_aktif BOOLEAN DEFAULT 1;
UPDATE siswa SET is_aktif = 1;
