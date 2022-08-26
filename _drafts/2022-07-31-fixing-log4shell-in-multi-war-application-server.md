### What is logging library?
Applications are silent till their developers will not provide them a way "to say" something. Usually, developers have no resources and even needs to show all that's happened behind the scene of any application to final users. But still, for tracking the processes and recording errors during the development, there is a need somehow in a cheap and fast way to track the application states. In the era of GUI/WEB applications the [standard application streams](https://en.wikipedia.org/wiki/Standard_streams) are no longer in use. So good and easy idea is to leave GUI/network sockets for users and use `stdout`/`stderr` to developers for logging application behavior. That seems an easy win.

With the time of application running, amount of processing and granularity of recording(logging) everything to `stdout`/`stderr`  the size of output may grow dramatically. This will cause problems in navigating across them and tracking required information. The options what to log and what not should be moved out of the application to some settings, so for changing logging properties will be no need to rebuild the application - changes will be applied on restart or even immediately, Also, the production and development tracking configuration may be different (in most cases it is). Even more, each application may wanted to have a few more independent logging channels(file output streams) in addition(or instead) to those from `stdout`/`stderr`.
So, more specialized tools are needed with the required capabilities. In general, the group of libraries for configurable logging of application states are called... logging libraries. 

### Logging libraries in java
Java have a list of logging solutions. 
First of all, the standard java library [does have](https://docs.oracle.com/javase/10/core/java-logging-overview.htm) log implementation inside it - the `java.util.logging` package (or "JUL"). Initially it was very poor. And because of this people needed better tools. And therefore many opensource alternatives appears, like:
 - [Log4j](https://logging.apache.org/log4j)(now the v2 is supported)
 - [Commons-logging](https://commons.apache.org/proper/commons-logging/) (wrapper around Log4j and JUL)
 - [Logback](https://logback.qos.ch/)(successor of Log4j v1)
 - [SLF4J](https://www.slf4j.org/)(wrapper around Logback, Commons-logging, Log4j and JUL).
As for now, there is no usage statistic, but from my personal experience, all of them are in wide use. If [java ran dozens of billions of devices](https://www.oracle.com/java/moved-by-java/timeline/), it means all of the libraries in the list above are used at least on hundreds of millions of ones. That's a lot!

### Log4j features
Log4j have a lot of features, like:
- is thread-safe
- have control of logging levels in hierarchical way
- can configure multiple output targets (appenders)
- logging configuration can be applied at runtime
- configurable output message format (layouts)
- injecting contextual information into log messages

All seems good and useful. But developers pay a lot of development effort on things that are used rarely but can be a source of vulnerabilities. Let's explore the last listed feature, as it was actually a source of probably the biggest java 0-day exploit in history so far.

### Log4Shell




  

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
eyJoaXN0b3J5IjpbLTc0NDY0NzEyMiwxOTM0Mjg4OTAsLTIwND
E5NDQzMSwxMDIwODk4Nzg1LDEwNjk4Mzc3NCwxMzM3Mzk1MDk2
LDExNjk2NzAzNTIsLTg4MDAyNzA5NywxNjc5MjI1OTA3LC0xMD
k3OTI4ODg4LDYyNDkwNDczNV19
-->