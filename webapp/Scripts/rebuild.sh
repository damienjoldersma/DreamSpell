mysqladmin drop DreamSpell -u root -p
mysqladmin create DreamSpell -u root -p
mysql -u root -p DreamSpell < seed.sql
