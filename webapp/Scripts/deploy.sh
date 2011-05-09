cd /var/www/DreamSpell
sudo tar zxvf /tmp/DreamSpell.tar.gz
sudo chown -R www-data:www-data /var/www/DreamSpell
sudo chmod -R g+w /var/www/DreamSpell
pgrep mono | sudo xargs kill -9
sudo /etc/init.d/apache2 force-reload
sudo rm /tmp/DreamSpell.tar.gz
sudo rm /tmp/deploy.sh
