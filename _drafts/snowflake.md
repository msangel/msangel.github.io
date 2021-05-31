---
title: Snowflake
date: 2021-05-31 03:08:38 Z
---

Maven config:

```xml
<dependency>  
 <groupId>net.snowflake</groupId>  
 <artifactId>snowflake-jdbc</artifactId>  
 <version>3.13.2</version>  
</dependency>  
<dependency>  
 <groupId>net.snowflake</groupId>  
 <artifactId>snowflake-ingest-sdk</artifactId>  
 <version>0.10.2</version>  
</dependency>  
<dependency>  
 <groupId>org.bouncycastle</groupId>  
 <artifactId>bcprov-jdk15on</artifactId>  
 <version>1.68</version>  
</dependency>  
<dependency>  
 <groupId>org.bouncycastle</groupId>  
 <artifactId>bcpkix-jdk15on</artifactId>  
 <version>1.68</version>  
</dependency>
```

Sql instrustion with links:
```sql
-- all documentation starts here:  
-- https://docs.snowflake.com/en/user-guide-data-load.html  
-- best starting article: https://medium.com/snowflake/invoking-the-snowpipe-rest-api-from-postman-141070a55337  
-- list all the files in the stage for the current user  
ls @~;  
  
-- https://docs.snowflake.com/en/sql-reference/sql/create-stage.html  
CREATE OR REPLACE STAGE my_sample_stage;  
  
SHOW STAGES;  
-- auth:  
-- https://docs.snowflake.com/en/user-guide/key-pair-auth.html#configuring-key-pair-authentication  
DESCRIBE USER msangel;  
  
-- Only security administrators (i.e. users with the SECURITYADMIN role) or higher can alter a user  
-- own expirience: for setting current (root) user I have to be in ACCOUNTADMIN role;  
  
use role ACCOUNTADMIN;  
alter user msangel set rsa_public_key='MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3qiEP1WsjerRBuIsSnAVJA8DB5OJyC9I9+gmPvFG+fFIYuq+fPo3mugWNLyx1VfJ1yTnY7RNSB/5g5GhoYisCRt7C7g8aeEpF7bIb056+iQdigvyQG0yEOCMvSVHs+cjaPtmOotHK4//WiJu6w7GSSoNvSBBhEIDQ/GM3t8V+STxUK7VBFaTkmh133HykO76pjWHoFiSt4vb8uKvKdvVQuIVXeZzfbk28zFF/PHONVBWe44Iuv3ilDoLfu7y1ozLIHrMwYdTdTZR6mkKPilWnN78GHV0gmghfJn9DgLK5AwbHThOorccBvbeGzBqD3PbIllDzOEcb8BZC4Td5YBNUwIDAQAB';  
  
-- example on creating user and role for database for security  
-- https://www.alooma.com/docs/store-your-data/snowflake/granting-snowflake-access  
  
  
-- add files to stage  
-- https://docs.snowflake.com/en/sql-reference/sql/put.html  
-- note: there is no REST API for uploading files: https://stackoverflow.com/q/65738808  
  
-- copy data from stage to table 2 approaches:  
-- 1. create pipe  
-- but according to logic and https://stackoverflow.com/a/67477334  
-- the pipe is used for continuous data loading from a stream, so option 2 is better  
-- also nice scenario for pipe: https://www.just-bi.nl/continuously-loading-data-using-snowpipe/  
-- also file format there -- another scenario: https://towardsdatascience.com/streaming-from-s3-buckets-to-snowflake-db-for-data-engineers-8e4827b81b16  
-- some sdk: --     https://github.com/DataDanimal/snowflake-pipe-api  
--     https://github.com/snowflakedb/snowflake-ingest-java  
-- 2. use "copy into..." command  
-- https://docs.snowflake.com/en/user-guide/data-load-bulk.html  
-- https://docs.snowflake.com/en/sql-reference/sql/copy-into-table.html  
  
  
-- 2  
copy into mytable from @~/staged file_format = (format_name = 'my_csv_format');
```




> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMjA3Njk2MjIxXX0=
-->