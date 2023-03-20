Writting microservice in java is easy.
Writting microservices in java to work in a cluster in more interesting - all the serbers must share some data to work properly. The basics (that is not always met) is to have shared http session. 
Another question is shared resources and concurrent access to them. Most cases this is database. But the transactioning should be tracked carefully. Eventually tables in db should be locked. The locking can be made using built-in db capabilities OR using external libraries like:
- https://github.com/alturkovic/distributed-lock
- https://www.baeldung.com/shedlock-spring
- etc

There alos a need to orchestrate jobs acros cluster and the quartz is a good solution so far, as this is job scheduler that does support cluster mode aout of box.

Regarding clustering readind/updating properties there a good solution [spring-cloud-config](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/), that does support easy integarion with diferent tadasources (git repository as default) and also automatical config reloading(so yes, those configs are reloadable).
Alternative to that is [Spring Cloud Bus](https://cloud.spring.io/spring-cloud-bus/reference/html/).
