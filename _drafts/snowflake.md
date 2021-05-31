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

Java runner:
```java
  
import net.snowflake.ingest.SimpleIngestManager;  
import net.snowflake.ingest.connection.HistoryRangeResponse;  
import net.snowflake.ingest.connection.HistoryResponse;  
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;  
import org.bouncycastle.jce.provider.BouncyCastleProvider;  
import org.bouncycastle.openssl.PEMParser;  
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;  
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;  
import org.bouncycastle.operator.InputDecryptorProvider;  
import org.bouncycastle.operator.OperatorCreationException;  
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;  
import org.bouncycastle.pkcs.PKCSException;  
  
import java.io.FileReader;  
import java.io.IOException;  
import java.nio.file.Paths;  
import java.security.PrivateKey;  
import java.security.Security;  
import java.time.Instant;  
import java.util.Set;  
import java.util.TreeSet;  
import java.util.concurrent.Callable;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.Future;  
import java.util.concurrent.TimeUnit;  
import javax.crypto.spec.PBEKeySpec;  
  
/***  
 * Sample runner that works with snowflake staged files. * Inspired by: * https://github.com/DataDanimal/snowflake-pipe-api/blob/master/src/com/snowflake/pipe/SnowflakePipeWrapper.java * * Some samples can be found in the library repo (also for some reason maven version of it has to transit dependencies): * https://github.com/snowflakedb/snowflake-ingest-java * https://github.com/DataDanimal/snowflake-pipe-api * */  
public class SDKTest {  
  
    // Path to the private key file that you generated earlier.  
  private static final String PRIVATE_KEY_FILE = "src/main/resources/.snowflake_keys/rsa_key.p8";  
  
//    public static class PrivateKeyReader {  
//  
//        private static PrivateKey get(String keyFilePath)  
//  
//                throws IOException, OperatorCreationException, PKCSException, Exception {  
//  
//            String password = myProps.getProperty("passphrase");  
//            PEMParser pemParser = new PEMParser(new FileReader(Paths.get(keyFilePath).toFile()));  
//            PKCS8EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo) pemParser.readObject();  
//            pemParser.close();  
//            Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME);  
//            Security.insertProviderAt(new BouncyCastleProvider(), 1);  
//            InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(password.toCharArray());  
//            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);  
//            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());  
//            PrivateKeyInfo decryptedPrivateKeyInfo = encryptedPrivateKeyInfo.decryptPrivateKeyInfo(pkcs8Prov);  
//            PrivateKey privateKey = converter.getPrivateKey(decryptedPrivateKeyInfo);  
//            return privateKey;  
//  
//        }  
//    }  
  
  public static class PrivateKeyReader {  
  
        // If you generated an encrypted private key, implement this method to return  
 // the passphrase for decrypting your private key.  private static String getPrivateKeyPassphrase() {  
            return "<private_key_passphrase>";  
  }  
  
        public static PrivateKey get(String filename)  
                throws Exception {  
            PrivateKeyInfo privateKeyInfo = null;  
  Security.addProvider(new BouncyCastleProvider());  
  // Read an object from the private key file.  
  PEMParser pemParser = new PEMParser(new FileReader(Paths.get(filename).toFile()));  
  Object pemObject = pemParser.readObject();  
 if (pemObject instanceof PKCS8EncryptedPrivateKeyInfo) {  
                // Handle the case where the private key is encrypted.  
  PKCS8EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = (PKCS8EncryptedPrivateKeyInfo) pemObject;  
  String passphrase = getPrivateKeyPassphrase();  
  InputDecryptorProvider pkcs8Prov = new JceOpenSSLPKCS8DecryptorProviderBuilder().build(passphrase.toCharArray());  
  privateKeyInfo = encryptedPrivateKeyInfo.decryptPrivateKeyInfo(pkcs8Prov);  
  } else if (pemObject instanceof PrivateKeyInfo) {  
                // Handle the case where the private key is unencrypted.  
  privateKeyInfo = (PrivateKeyInfo) pemObject;  
  }  
            pemParser.close();  
  JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(BouncyCastleProvider.PROVIDER_NAME);  
 return converter.getPrivateKey(privateKeyInfo);  
  }  
    }  
  
    static class GetHistory implements Callable<HistoryResponse> {  
  
        private final SimpleIngestManager manager;  
  
 private final Set<String> filesWatchList;  
  
  GetHistory(SimpleIngestManager manager, Set<String> files) {  
            this.manager = manager;  
 this.filesWatchList = files;  
  }  
  
        String beginMark = null;  
  
 public HistoryResponse call()  
                throws Exception {  
            HistoryResponse filesHistory = null;  
 while (true) {  
                Thread.sleep(500);  
  HistoryResponse response = manager.getHistory(null, null, beginMark);  
 if (response.getNextBeginMark() != null) {  
                    beginMark = response.getNextBeginMark();  
  }  
                if (response != null && response.files != null) {  
                    for (HistoryResponse.FileEntry entry : response.files) {  
                        //if we have a complete file that we've  
 // loaded with the same name..  String filename = entry.getPath();  
 if (entry.getPath() != null && entry.isComplete() &&  
                                filesWatchList.contains(filename)) {  
                            if (filesHistory == null) {  
                                filesHistory = new HistoryResponse();  
  filesHistory.setPipe(response.getPipe());  
  }  
                            filesHistory.files.add(entry);  
  filesWatchList.remove(filename);  
  //we can return true!  
  if (filesWatchList.isEmpty()) {  
                                return filesHistory;  
  }  
                        }  
                    }  
                }  
            }  
        }  
    }  
      
    private static HistoryResponse waitForFilesHistory(SimpleIngestManager manager, Set<String> files) throws Exception {  
        ExecutorService service = Executors.newSingleThreadExecutor();  
  
    
  GetHistory historyCaller = new GetHistory(manager, files);  
  //fork off waiting for a load to the service  
  Future<HistoryResponse> result = service.submit(historyCaller);  
  
  HistoryResponse response = result.get(2, TimeUnit.MINUTES);  
 return response;  
  }  
  
    public static void main(String[] args) {  
//        final String host = "<account_name>.<region_id>.snowflakecomputing.com";  
  final String host = "pl18490.europe-west4.gcp.snowflakecomputing.com";  
 final String user = "msangel";  
 final String pipe = "<db_name>.<schema_name>.<pipe_name>";  
 try {  
            final long oneHourMillis = 1000 * 3600L;  
  String startTime = Instant  
                    .ofEpochMilli(System.currentTimeMillis() - 4 * oneHourMillis).toString();  
 final PrivateKey privateKey = PrivateKeyReader.get(PRIVATE_KEY_FILE);  
  SimpleIngestManager manager = new SimpleIngestManager(host.split("\\.")[0], user, pipe, privateKey, "https", host, 443);  
  Set<String> files = new TreeSet<>();  
  // Add the paths to the files that you want to load.  
 // Use paths that are relative to the stage where the files are located // (the stage that is specified in the pipe definition)..  files.add("<path>/<filename>");  
  files.add("<path>/<filemame>");  
  manager.ingestFiles(manager.wrapFilepaths(files), null);  
  HistoryResponse history = waitForFilesHistory(manager, files);  
  System.out.println("Received history response: " + history.toString());  
  String endTime = Instant  
                    .ofEpochMilli(System.currentTimeMillis()).toString();  
  
  HistoryRangeResponse historyRangeResponse =  
                    manager.getHistoryRange(null,  
  startTime,  
  endTime);  
  System.out.println("Received history range response: " +  
                    historyRangeResponse.toString());  
  
  } catch (Exception e) {  
            e.printStackTrace();  
  }  
  
    }  
}
```


> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTgwNzYxODk3XX0=
-->