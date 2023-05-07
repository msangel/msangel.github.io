Here's a trick:
InputStream operates with byte stream. And we woant to alter character in it. Firts - converting a bytes to char are something the JDK does itself. Second - char is not a codepoint. Third - one symbol may be crafted from multiple codepoints (my hero here is Wales flag emoji which is one symbol but 7 codepoints and so 14 chars: "🏴󠁧󠁢󠁷󠁬󠁳󠁿"󠁢󠁷󠁬󠁳󠁿 ). So before changing something you need to know WHAT you are about to change.


```groovy
@Grab(group='com.ibm.icu', module='icu4j', version='73.1')
import com.ibm.icu.text.BreakIterator;

String str = "a\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC77\uDB40\uDC6C\uDB40\uDC73\uDB40\uDC7Fb";



BreakIterator iterator = BreakIterator.getCharacterInstance();
iterator.setText(str);
int start = 0;
for (int end = iterator.next(); end != BreakIterator.DONE; ) {
    String possibleWord = str.substring(start, end);
    System.out.println("char: " + possibleWord);
    start = end;
    end = iterator.next()
}
```
___

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
 * org.apache.commons.io.output.ByteArrayOutputStream?
* based on pipe(small buffer with blocking) using PipedOutputStream and PipedInputStream with filling in dedicated thread (parametrized of built-in `CompletableFuture.supplyAsync` executor )
* more?

Also requirements to row mapper:
* allow rows to be added to stream based on stream content in the beginning/middle/in the end
* allow rows to be removed from stream based on stream content in the beginning/middle/in the end

Links:
https://stackoverflow.com/questions/55709937/how-to-obtain-inputstream-from-java-8-streams
https://stackoverflow.com/questions/54398003/process-next-character-full-unicode-code-point-streaming-from-java-input-strea
https://stackoverflow.com/questions/30336257/convert-inputstream-into-streamstring-given-a-charset


java.io.FilterInputStream and org.apache.commons.io.input.ProxyInputStream
org.apache.commons.io.input.TeeInputStream
