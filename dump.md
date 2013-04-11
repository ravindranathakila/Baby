
Just a huge dump of stuff we need from time to time. No specific order or correlation.

```

  //MAKE SURE LOCATIONID IS PRIMARY KEY, MODIFY FOLLOWING SCRIPT
        CREATE TABLE location(LOCATIONID BIGINT,CLEARANCE BIGINT,LOCATIONGEO1 VARCHAR(128),LOCATIONGEO2 VARCHAR(128),LOCATIONINFO VARCHAR(128),LOCATIONNAME VARCHAR(128), LOCATIONSUPERSET_LOCATION BIGINT, PRIMARY KEY (LOCATIONID) )
        CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE('APP','LOCATION','location.dat','    ',null,null,0);
        import --connect jdbc:derby://www.ilikeplaces.com/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp --table location --hbase-create-table
        ./sqoop import --connect jdbc:derby://localhost:1527/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp -P  --table APP.LOCATION --hbase-create-table --split-by locationid
        ./sqoop import --connect jdbc:derby://localhost:1527/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp -P  --table APP.LOCATION --hbase-create-table --split-by locationid

        CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE('ILP','LOCATION','locationtab.dat','','"','UTF-8');


        hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=HBASE_ROW_KEY,locationid,locationId:clearance,locationId:locationGeo1,locationId:locationGeo2,locationId:locationInfo,locationId:locationName,locationId:locationSuperSet location locationtab.dat

        public Long locationId;
        public Long clearance = 0L;
        public String locationName;
        public String locationInfo;
        public Location locationSuperSet;
        public String locationGeo1;
        public String locationGeo2;


        hdfs dfs -put locationtab.dat

        hdfs dfs -ls


        //MAKE SURE LOCATIONID IS PRIMARY KEY, MODIFY FOLLOWING SCRIPT
        CREATE TABLE location(LOCATIONID BIGINT,CLEARANCE BIGINT,LOCATIONGEO1 VARCHAR(128),LOCATIONGEO2 VARCHAR(128),LOCATIONINFO VARCHAR(128),LOCATIONNAME VARCHAR(128), LOCATIONSUPERSET_LOCATION BIGINT, PRIMARY KEY (LOCATIONID) )
        CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE('APP','LOCATION','location.dat','    ',null,null,0);
        import --connect jdbc:derby://www.ilikeplaces.com/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp --table location --hbase-create-table
        ./sqoop import --connect jdbc:derby://localhost:1527/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp -P  --table APP.LOCATION --hbase-create-table --split-by locationid
        ./sqoop import --connect jdbc:derby://localhost:1527/ilp --driver org.apache.derby.jdbc.ClientDriver --username ilp -P  --table APP.LOCATION --hbase-create-table --split-by locationid

        CALL SYSCS_UTIL.SYSCS_EXPORT_TABLE('ILP','LOCATION','locationtab.dat','','"','UTF-8');


        hbase org.apache.hadoop.hbase.mapreduce.ImportTsv -Dimporttsv.columns=HBASE_ROW_KEY,locationid,locationId:clearance,locationId:locationGeo1,locationId:locationGeo2,locationId:locationInfo,locationId:locationName,locationId:locationSuperSet location locationtab.dat

        public Long locationId;
        public Long clearance = 0L;
        public String locationName;
        public String locationInfo;
        public Location locationSuperSet;
        public String locationGeo1;
        public String locationGeo2;


        hdfs dfs -put locationtab.dat

        hdfs dfs -ls



        query = "SELECT DISTINCT humansNetPeople FROM HumansNetPeople humansNetPeople inner join humansNetPeople.humansNetPeoples friend WHERE friend.humanId = :humanId"

javax.persistence.PersistenceException: Human
Caused by: javax.persistence.PersistenceException: Human
Caused by: org.apache.hadoop.hbase.TableNotFoundException: Human

Happens when datanuclues is run against an empty HBase installation. i.e. No tables.


Good to have bash_profile values for a java server.

alias k='kill -9'
alias pt='ps -ef|grep tomcat'
alias ph='ps -ef|grep hbase'
alias t='tail -f'
alias ll='ls -la'





enable 'Album'
enable 'Human'
enable 'HumansAlbum'
enable 'HumansAuthentication'
enable 'HumansAuthorization'
enable 'HumansIdentity'
enable 'HumansNet'
enable 'HumansNetPeople'
enable 'HumansPrivateEvent'
enable 'HumansPrivateLocation'
enable 'HumansPrivatePhoto'
enable 'HumansPublicPhoto'
enable 'HumansTribe'
enable 'HumansUnseen'
enable 'HumansWall'
enable 'Location'
enable 'LongMsg'
enable 'Map'
enable 'Msg'
enable 'Mute'
enable 'PrivateEvent'
enable 'PrivateLocation'
enable 'PrivatePhoto'
enable 'PublicPhoto'
enable 'Tribe'
enable 'Url'
enable 'Wall'
enable 'albumsalbumPhotos'
enable 'privateEventInvitesprivateEventsInvited'
enable 'privateEventOwnersprivateEventsOwned'
enable 'privateEventRejectsprivateEventsRejected'
enable 'privateEventViewersprivateEventsViewed'
enable 'privateLocationOwnersprivateLocationsOwned'
enable 'privateLocationViewersprivateLocationsViewed'
enable 'tribeMemberstribes'



disable 'Human'
disable 'HumansAlbum'
disable 'HumansAuthentication'
disable 'HumansAuthorization'
disable 'HumansIdentity'
disable 'HumansNet'
disable 'HumansNetPeople'
disable 'HumansPrivateEvent'
disable 'HumansPrivateLocation'
disable 'HumansPrivatePhoto'
disable 'HumansPublicPhoto'
disable 'HumansTribe'
disable 'HumansUnseen'
disable 'HumansWall'


alter 'Human',{NAME => 'humanId'}
alter 'HumansAlbum',{NAME => 'humanId'}
alter 'HumansAuthentication',{NAME => 'humanId'}
alter 'HumansAuthorization',{NAME => 'humanId'}
alter 'HumansIdentity',{NAME => 'humanId'}
alter 'HumansNet',{NAME => 'humanId'}
alter 'HumansNetPeople',{NAME => 'humanId'}
alter 'HumansPrivateEvent',{NAME => 'humanId'}
alter 'HumansPrivateLocation',{NAME => 'humanId'}
alter 'HumansPrivatePhoto',{NAME => 'humanId'}
alter 'HumansPublicPhoto',{NAME => 'humanId'}
alter 'HumansTribe',{NAME => 'humanId'}
alter 'HumansUnseen',{NAME => 'humanId'}
alter 'HumansWall',{NAME => 'humanId'}

enable 'Human'
enable 'HumansAlbum'
enable 'HumansAuthentication'
enable 'HumansAuthorization'
enable 'HumansIdentity'
enable 'HumansNet'
enable 'HumansNetPeople'
enable 'HumansPrivateEvent'
enable 'HumansPrivateLocation'
enable 'HumansPrivatePhoto'
enable 'HumansPublicPhoto'
enable 'HumansTribe'
enable 'HumansUnseen'
enable 'HumansWall'


```
