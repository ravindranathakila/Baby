### Help!!! We need help :-)

#### What we do,

We make people happy by showing them what is interesting around them. Be it a movie or a theatrical show, be it a grand game or night party, we want people to know, participate in them and have fun.

#### How do we do it,

We have set up a TGIF mail, to which anybody can subscribe to events in any area she likes. Every Friday she will receive an email saying Thank God It Is Friday. This email will contain interesting events to attend over the weekend until next Friday.

Well, this is definitely not the only way to do it. That's where you are important to us. Help us do a more meaningful job. Give us ideas. Implement some or give your own spin to it. Make things happen!

#### What is it all about?

Make happy people. We want to make more and more happy people. If you can do a better job than us, well then come join us, or beat us to it (we love a challenge!). 


___



Attribution

 Eye - Thank you http://findicons.com/pack/2448/wpzoom_developer 
 
 Denise(Font) - Thank you Denise! http://budeni.com/  http://www.dafont.com/denise-b.d2982?l[]=10 
 
 Image on home page - http://www.flickr.com/photos/widerbergs/5859028739/sizes/l/in/photostream/ 
 
 Wikipedia (Lock Image) - Thank you https://commons.wikimedia.org/wiki/File:Ambox_padlock_red.svg 


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
