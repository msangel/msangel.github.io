Logging in java got some excessive complexity. This is caused by way of its development. Well, all of us can print all debug information to the system output using plain `System.out.println`. The number of such logs can become huge. This will cause problem in navigating across them and tracking reqired information. Also, production and development log configuation is different. So more specialized tool needed. The standart java library [do have](https://docs.oracle.com/javase/10/core/java-logging-overview.htm) log implementation inside with it. 


About log4j
spreading of log4j
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
eyJoaXN0b3J5IjpbODM2MDc2NDg2LDE2NzkyMjU5MDcsLTEwOT
c5Mjg4ODgsNjI0OTA0NzM1XX0=
-->