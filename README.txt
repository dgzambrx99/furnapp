1. Create a new database

2. Run schema2.sql in the database.

3. In the directory with pom.xml, enter at the command line:
cd ext
mvn install:install-file -Dfile=SX.jar -DgroupId=com.smartxls -DartifactId=smartxls -Dversion=4.0.2 -Dpackaging=jar

4. Edit src/main/resources/app.properties:
Set furnaceapp.db_username and furnaceapp.db_password to the database user and password
In furnaceapp.db_connection_url, replace "xlstodb2" with the name of the database
In furnaceapp.db_connection_url, replace "localhost" with the host of the database
Set furnaceapp.delay_between_loops to the number of milliseconds to delay
Set furnaceapp.base_dir to the full path of the folder that will be monitored

5. In the directory with pom.xml, enter at the command line:
mvn clean compile exec:java

6. Store xlsx files in subdirectories of the folder indicated by "furnaceapp.base_dir".
The 'which' column of the furnace_base table is set from an integer of the parent's parent
directory of the xlsx file. If that isn't an integer, than the parent directory name is used.
If neither are integers, -1 is used.

7. In the database, run this to see the data:
select * from furnace_base; select * from furnacedata_multipoint; select * from furnacedata_loadtc; select * from checktable;
