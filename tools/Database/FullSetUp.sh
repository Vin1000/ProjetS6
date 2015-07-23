# Script to install Postgres and create the DB

# Install postgreSQL and starts the server
# uncomment 3 next lines if install is required
sudo apt-get update;
sudo apt-get install postgresql;
sudo service postgresql start;

sudo echo "Creating Database";

#Change the default password of Postgres user
sudo -u postgres psql -U postgres -d postgres -c "\password postgres";

#Create the Database
sudo -u postgres createdb Opus;

#Runs the script
sudo -upostgres psql -f CreateDbStructure.sql Opus;

#To connect on bd execute the following
#sudo -u postgres psql postgres;
#\c Opus;