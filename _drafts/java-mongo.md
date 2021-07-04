Why mongo:
-   simple to learn and use
-   hight perfomance  

### Configuring local dev server

### Securing service

### Selecting library

### Sample app


### Solutions overview
There are many ways to use MongoDB in Java. There are three leaders(2nd and 3rd build on top of 1st) and Hibernate ogm: 

 - [MongoDB Java Driver](https://mongodb.github.io/mongo-java-driver/)
 - [Morphia](https://mongodb.github.io/morphia/)
 - [Spring data](http://projects.spring.io/spring-data-mongodb/)
 - [Hibernate ogm](http://hibernate.org/ogm/)

I have used all of them in projects of different size. Lets discuss their strong and weak parts.

#### MongoDB Java Driver
This one gives as native interface as it could be. Being developed by MongoDB team and providing low-level control this library provide most flexibility. This library even have async connector, for most progressive developers. Opposite side of using this library is that many more code has to be written(POJO converters, etc.). That might be simplified by [mongo2gson](https://code.google.com/p/mongo2gson/) and [json(gson) to mongo](http://stackoverflow.com/a/5711200/449553) helpers.

#### Morphia
This is the most simple and in the same time most feature-rich MongoDB ORM library, that build on top of MongoDB Java Driver by the same MongoDB team. Minimal boilerplate, flexible configuration, and strong api with all power of generics. The weak part came from strong - sticking to strong typing we are losing advantages of document-based database where any document can have arbitrary structure.  
 
#### Spring data
If I would need to pick java MongoDB library now, I would use Morphia in most cases except the one - if the application would be built on top of spring infrastructure. In that case I would choose [spring-data-mongodb](http://projects.spring.io/spring-data-mongodb/) library, a mongodb extension for [spring-data](http://projects.spring.io/spring-data/) project. It use all the features of spring-data, including my favorite one: generating DAO just based on marked interface's methods names. No need to write any implementation. Spring will generate it for you. All the bad parts came also from strong. The library cannot be used without spring and it have hard curve of learning.
  
#### Hibernate ogm mongodb
I have minimal experience with this project and I was not satisfied. Existence of this library show the laziness of people.  Instead of learning something new, they do use a good-known for them wrapper that hide all the power of underlying database. Good sides: if you have worked with Hibernate before, you don't need to learn anything new. Bad sides: trying to force MongoDB in scale of relational databases a lot of functionality dropping.  [Here's](https://www.thoughts-on-java.org/use-jpa-next-project/) a good article with discussions about the question: do you really need this library. 
 
#### Others
On the githab there are a bunch small drivers and ORM libraries for work with MongoDB. If none of above fit your needs, you can try any of these(or find your):

 - https//github.com/suguru/mongo-java-async-driver
 - https://github.com/JPDSousa/mongo-obj-framework
 - https://github.com/wangym/koubei-mongo
 - https://github.com/jmingo-projects/jmingo
 - https://github.com/allanbank/mongodb-async-driver
 - https://github.com/mongodb/mongo-java-driver-rx
 - https://github.com/allanbank/mongodb-async-driver


### Securing service

#### Create db admin

Securing working service:

[http://docs.mongodb.org/manual/tutorial/add-user-administrator/](http://docs.mongodb.org/manual/tutorial/add-user-administrator/)

>mongo

**\> show databases**

\> use mydb5

\> db.system.users.find()

**// пусто**

\> use admin

switched to db admin

>db.addUser( { user: "<username>", pwd: "<password>", roles: \[ "userAdminAnyDatabase", "userAdmin" \] } )

{

"user" : "<username>",

"pwd" : "<password_hash>",

"roles" : \[

"userAdminAnyDatabase",

"userAdmin"

\],

"_id" : ObjectId("530c14468d296d217b9804c2")

}

  

#### Enable auth

For disable local bypass, add option to mongodb:

mongod --setParameter enableLocalhostAuthBypass=0

or in config file([manual](http://docs.mongodb.org/manual/reference/parameters/#param.enableLocalhostAuthBypass)):

setParameter=enableLocalhostAuthBypass=0

  

actually, need:  

remove service

add this to service config:

auth=true
setParameter = enableLocalhostAuthBypass=0

install service

in case of problem with installing/reinstalling service:

[http://stackoverflow.com/questions/4661670/cannot-start-mongodb-as-a-service](http://stackoverflow.com/questions/4661670/cannot-start-mongodb-as-a-service)

[http://www.webiyo.com/2011/02/install-mongodb-service-on-windows-7.html](http://www.webiyo.com/2011/02/install-mongodb-service-on-windows-7.html)

  

now, after setting options

>mongo

MongoDB shell version: 2.4.1

connecting to: test

\> use admin

switched to db admin

\> db.system.users.find()

error: { "$err" : "not authorized for query on admin.system.users", "code" : 16550 }

\> db.auth('<username>','<password>');

1

\> db.system.users.find()

{ "\_id" : ObjectId("530c14468d296d217b9804c2"), "user" : "<username>", "pwd" : "<password\_hash>", "roles" : \[ "userAdminAnyDatabase", "userAdmin" \] }

>

  

#### Add user to db

[http://docs.mongodb.org/manual/tutorial/add-user-to-database/](http://docs.mongodb.org/manual/tutorial/add-user-to-database/)

  

use mydb5

db.addUser( { user: "<username>", pwd: "<password>", roles: \[ "readWrite", "dbAdmin", "userAdmin" \] } )

more about access rights: [http://docs.mongodb.org/manual/reference/user-privileges/](http://docs.mongodb.org/manual/reference/user-privileges/)

  

#### Configure libraries for work with auth

Spring mongo data  
  

<bean id="mongoTemplate" class="org.springframework.data.mongodb.core.MongoTemplate">
    <constructor-arg name="mongoDbFactory" ref="mongoDbFactory"/>
    <constructor-arg name="mongoConverter" ref="mappingConverter"/>
</bean>

<mongo:db-factory dbname="mydb5" id="mongoDbFactory" username="username" password="password"/>

  

#### Configure remote access

TODO:

[http://blog.aeonmedia.eu/2011/04/mongodb-setup-config-to-connect-by-remote-hosts-debian/](http://blog.aeonmedia.eu/2011/04/mongodb-setup-config-to-connect-by-remote-hosts-debian/)

[http://stackoverflow.com/questions/7159737/](http://stackoverflow.com/questions/7159737/)

  

  

#### Dumping and restore

Create dump:

\# timestamp=$(date +_%s) # timestamp  
timestamp=$(date +_%y.%m.%d_%H.%M)

\# mongodump --username <username> --password <password> -db mydb5 --out mydbdump${timestamp}

mongodump -db mydb5 --out mydbdump${timestamp}

tar -zcvf dump${timestamp}.tar.gz mydbdump${timestamp}

rm -rf mydbdump${timestamp}

  

Restore dump:

tar -xzvf dump\_14.03.04\_16.23.tar.gz

\# mongorestore --drop --username <username> --password <password> -db mydb5 dump\_14.03.04\_16.23\\mydb5

#mongorestore --host mongodb1.example.net --port 3017 --drop --username user --password pass /opt/backup/mongodump-2012-10-24

  

  

  

TODO: restore from log(if logging is enabled)  
[http://docs.mongodb.org/manual/tutorial/recover-data-following-unexpected-shutdown/](http://docs.mongodb.org/manual/tutorial/recover-data-following-unexpected-shutdown/)

[https://docs.mongodb.com/manual/core/authentication/](https://docs.mongodb.com/manual/core/authentication/)

[https://docs.mongodb.com/manual/tutorial/manage-users-and-roles/](https://docs.mongodb.com/manual/tutorial/manage-users-and-roles/)

[https://docs.mongodb.com/manual/tutorial/backup-and-restore-tools/](https://docs.mongodb.com/manual/tutorial/backup-and-restore-tools/)

### Documentation
Getting started: http://docs.mongodb.org/manual/tutorial/getting-started/
Fast overview: 
http://www.slideshare.net/tobiastrelle/j-16761910
https://blog.sourced-bvba.be/article/2014/12/01/mongodb-java-comparison/

<!--stackedit_data:
eyJoaXN0b3J5IjpbNjIyODM4ODg3XX0=
-->