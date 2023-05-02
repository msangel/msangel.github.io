Alter InputStream by adding some chagacters before, inside and after depending on stream content. 

Example use case - transform 1-column CSV by adding header IF the first row is a number (so the CSV is without header). Consumer agreement is - file is always with header.  Assuming:
* stream processing (file can be large, up to gigabyte)
* empty stream should return empty stream
* underlying error in source stream should be prapagated to resulting InputStream

Expected interfaces:
```java
InputStream is = InputStreamMapper.ofCodepointMapping(InputStream is, Function<Integer, String> codepointMapper);
InputStream is = InputStreamMapper.ofCodepointStringMapping(InputStream is, Function<String, String> codepointStringMapper);
InputStream is = InputStreamMapper.ofCharMapping(InputStream is, Function<Character, String> charMapper);
InputStream is = InputStreamMapper.ofLineMapping(InputStream is, Function<String, String> lineMapper);
```
2 physical implenetation:
* based on common large buffer using ByteArrayOutputStream and ByteArrayInputStream
* based on pipe(small buffer with blocking) using PipedOutputStream and PipedInputStream with filling in dedicated thread (parametrized of built-in `CompletableFuture.supplyAsync` executor )
* more?

Also requirements to row mapper:
allow rows to be added to stream based on stream content in the beginning/middle/in the end
allow rows to be removed from stream based on stream content in the beginning/middle/in the end


