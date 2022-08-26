Applications are silent till their developers will not provide them a way "to say" something. Usually developers have no resources and even needs to show nicely all what's happaned behind the scene of any application to final users. But still for tracking the processes and recording errors during the development, there a need somehow in cheap and fast way to track the application states. In era of GUI/WEB-applications the [standart application streams](https://en.wikipedia.org/wiki/Standard_streams) are not in use. So good and easy idea is to leave GUI/network sockets for user and use `stdout`/`stderr` to developers for logging application behavior. That seems easy win.

With time of application running, amount of processings and granularity of recording(logging) evrything to `stdout`/`stderr`  the size of output may groving dramatically. This will cause problem in navigating across them and tracking reqired information. Also, the production and development tracking configuration may be different different(in most cases it is). Also, eventually, each application may wonted to have few more intependent logging chanels(file output streams) in additional(or instead) to those from `stdout`/`stderr`.
So more specialized tools are needed. In general the  

The standard java library [does have](https://docs.oracle.com/javase/10/core/java-logging-overview.htm) log implementation inside it - the "java.util.logging" package (or "JUL"). Initially it was very poor, but now its somehow better. And because of this people need better tools. And so many opensource alternatives appear(top list):
 - Log4j(now the v2 is supported)
 - Commons-logging (wrapper around Log4j and JUL)
 - Logback(successor of Log4j v1)
 - SLF4J(wrapper around Logback, Commons-logging, Log4j and JUL).
As for now there is no usage statistic, but from my personal expirience all of them are in wide use. And Log4j is one of them.

### Log4j features


 https://habr.com/ru/search/?q=java%20logging&target_type=posts&order=relevance


About log4j
key feature(of interest)
architecture of common application server with shared log4j, package problem
sample
log4shell
fixing log4shell


notes:
https://logging.apache.org/log4j/2.x/security.html
https://stackoverflow.com/questions/26025727/separate-log4j2-files-for-web-applications-in-single-weblogic-container
https://logging.apache.org/log4j/2.x/manual/webapp.html
https://www.google.com/search?q=each+webapp+own++log4j2.xml
https://logging.apache.org/log4j/log4j-2.3.1/manual/configuration.html
https://logging.apache.org/log4j/2.x/log4j-core/apidocs/org/apache/logging/log4j/core/selector/JndiContextSelector.html
https://logging.apache.org/log4j/2.x/manual/configuration.html#PropertySubstitution
https://logging.apache.org/log4j/2.x/manual/lookups.html
https://www.google.com/search?q=log4j2.enableSysLookup








> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTAzMjYxOTgwMSwxMDIwODk4Nzg1LDEwNj
k4Mzc3NCwxMzM3Mzk1MDk2LDExNjk2NzAzNTIsLTg4MDAyNzA5
NywxNjc5MjI1OTA3LC0xMDk3OTI4ODg4LDYyNDkwNDczNV19
-->