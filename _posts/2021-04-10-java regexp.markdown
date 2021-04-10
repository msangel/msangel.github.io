---
title: java regexp
date: 2021-04-10 01:57:00 Z
layout: base
---

{:.no_toc}

* TOC
{:toc}

### Theory and history.

Regular expressions (regexp) is a formal language for searching and manipulating with substrings in text. Regular expressions is based on usage if meta-symbols (wildcard characters). This is, actually, a string pattern, which formalizing a search rule ([wiki](https://en.wikipedia.org/wiki/Regular_expression){:target="_blank"}) .

There exists two kinds of regular expressions:

* POSIX (older one, basics)

* PCRE (perl-compatible regular expressions, more extended)

Regular expressions have a huge flexibility but are not guaranteed optimal, because they works based of exhaustive  search. So badly written regular expression can kill your program.  So far, there are techniques for optimizing them, for example build search pattern based on search input with [Aho–Corasick algorithm](https://en.wikipedia.org/wiki/Aho%E2%80%93Corasick_algorithm) (keywords for more deep search: NFA and minimal DFA, prefix tree). Here is just example implementation: [PreSuf](http://search.cpan.org/\~jhi/Regex-PreSuf-1.17/PreSuf.pm){:target="_blank"}.

Also you need to understand, that there is no so easy to work with regexps on huge files due their nature. Also regular expressions are possible to use with streaming data, but that's another hard moment that needs more research.

### RegExp in Java

There are many regexp implementations in Java. Some a bit outdated list with performance comparisons you can find here: [http://www.tusker.org/regex/regex_benchmark.html](http://www.tusker.org/regex/regex_benchmark.html){:target="_blank"}.

In scope of this article we are going to review regular expressions implementation from Java standard library.  This implementation is represented by three classes:

* PatternSyntaxException: Unchecked exception thrown to indicate a syntax error in a regular-expression pattern.

* Matcher: object that keep processed string inside, remember processing state and used pattern.

* Pattern - actually pattern object.

What we can do with all of this?

#### Test string for pattern matching

Method 1:

    Pattern.compile("a*b",Pattern.CASE_INSENSITIVE).matcher("aaab").matches();// true  

ATTENTION: only full matching - from string beginning to its end, so the next example:

    Pattern.compile("a*b",Pattern.CASE_INSENSITIVE & Pattern.MULTILINE).matcher("aaabaaab").matches();// false  

will return  false , as far as whole string is not match the pattern.
The solution:

    Pattern.compile(".*a*b.*",Pattern.CASE_INSENSITIVE).matcher("aaabaaab").matches() /* full maching */: true  

Method 2:

    "dreedisgood".matches("(?i).\*IS.\*"); // where (?i) - flags  

#### Flags

Flags are calculating as bit mask. List of them are in documentation about [Pattern](https://docs.oracle.com/javase/tutorial/essential/regex/pattern.html){:target="_blank"} class.

#### Split string with delimiter

    String[] strings = Pattern.compile("a").split("asdasdasd");
    for (String word : strings) {  
        System.out.println("splitted: " + word);
    }  

OUT:

    splitted:  
    splitted: sd  
    splitted: sd  
    splitted: sd  

If you look carefully, you can find that before the first "a" letter there is one empty line with 0 length.

Another  example:

    Pattern p3 = Pattern.compile("\\d+\\s?");  
    String[] words =  p3.split("java5tiger 77 java6mustang");  
    for (String word : words){  
        System.out.println("splitted: "+word);
    }  

OUT:

    splitted: java  
    splitted: tiger  
    splitted: java  
    splitted: mustang  

And one more sample, where we gonna split only into 2 groups: the first one and the rest:

    String ssstr = "";  
    int c = 0;
    String[] strings = Pattern.compile("\\d").split("va1sda1sda3sd",2);
    for (String s: strings) {  
        ssstr+="s\["+(c++)+"\]:"+s+"  ";
    }  
    System.out.println("split with arg: "+ssstr);  

OUT:

    split with arg: s[0]:va  s[1]:sda1sda3sd  

#### Check prefix(if string starting with another string)

This will return true

    Pattern.compile("va.*b").matcher("va1sda1sda 3sbd").lookingAt(); //  true  

And this will return false

    Pattern.compile("a.*b").matcher("va1sda1sda 3sbd").lookingAt(); //  false  

#### Changing pattern/data in existing matcher

    Pattern p3 = Pattern.compile("va.+?b"); // base pattern
    Matcher m3 = p3.matcher("va1sda1bda 3sbd"); // base mather
    System.out.println("find smth? "+m3.find());  
    System.out.println("what? "+m3.group());  
    System.out.println(m3.toString());//gives the information about used pattern and matcher's state  
    m3.usePattern(Pattern.compile("\\d")); // set new pattern  
    System.out.println(m3.toString());// info after reset
    m3.reset(); // matcher reset state
    System.out.println("can we search again?: "+ m3.find());  
    System.out.println("Whats there? "+m3.group());  

OUT:

    find smth? true
    what? va1sda1b  
    java.util.regex.Matcher[pattern=va.+?b region=0,15 lastmatch=va1sda1b]  
    java.util.regex.Matcher[pattern=\d region=0,15 lastmatch=]  
    can we search again?: true
    Whats there? 1

If we will take this line from code above:

    m3.reset();  

then the new pattern will continue to search from the last place and the last line would be

    Whats there? 3

Also the method `reset()` can be parameterized  with new string for matcher, so replacing `m3.reset()` for `m3.reset("5sdf")` in the code above will gives us:

    Whats there? 5  

#### Substring search

    Matcher m3 = Pattern.compile("\\d(.{1})").matcher("va1sda1dda 3jhbd");  
    while (m3.find()) {
        // group() is equal to group(0), where 0 mean the full found substring 
        System.out.println("new group after find: "+ m3.group()); 
        // and here 1 mean the first selectet with parentheses group in substring
        System.out.println("new subgroup in found: "+ m3.group(1));
    }  

OUT:

    new group after find: 1s  
    new subgroup in found: s  
    new group after find: 1d  
    new subgroup in found: d  
    new group after find: 3j  
    new subgroup in found: j  

Also there is parameterized variant of the `find` method:

    Matcher.find(int offset) 

which is looking by offset.

Another example:

     String regex = "(\\w+)@(\\w+\\.)(\\w+)(\\.\\w+)*";
     String s ="emeil adresses:mymail@tut.by and rom@bsu.by";
     Pattern p2 = Pattern.compile(regex);
     Matcher m2 = p2.matcher(s);
     if(m2.find()) {
        for(int i=0; i<m2.groupCount(); i++) { 
             System.out.println("all groupes: "+m2.group(i)); 
        }
    }
    m2.reset();
    while (m2.find()) {
        System.out.println("e-mail: "+  m2.group());
    }

OUT:

    all groupes: mymail@tut.by  
    all groupes: mymail  
    all groupes: tut.  
    all groupes: by  
    e-mail: mymail@tut.by  
    e-mail: rom@bsu.by  

#### Replace by pattern

##### Method 1

Simple replace letter "i" for "1"

    CharSequence inputStr =  "this is simple tex thaw i want to chancE ";  
    System.out.println(inputStr);
    String patternStr = "i";
    String replacementStr = "1";
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(inputStr);
    String output = matcher.replaceAll(replacementStr);
    System.out.println(output);

OUT:

    this is simple tex thaw i want to chancE  
    th1s 1s s1mple tex thaw 1 want to chancE  

##### Method 2

This example of more complex replace with usage of groups. Access to groups are made via $x, where х - group number. This is replacing order of worlds with their spaces :

    ...
    String patternStr = "(\\s?is)(\\s?+simple)(\\s+tex)";
    String replacementStr = "$3$1$2";
    ...

OUT:

    this is simple tex thaw i want to chancE
    this tex is simple thaw i want to chancE

##### Method 3

Next sample looking for world which starts with  letter and ends with digit. Found result is wrapping with text in the beginning and in the end:

    CharSequence inputStr = "ab12 cd efg34 asdf 123";
    System.out.println(inputStr);
    String patternStr = "([a-zA-Z]+[0-9]+)";  
    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(inputStr);  
    StringBuffer buf = new StringBuffer();
    while (matcher.find()) {
        String replaceStr = matcher.group();
        matcher.appendReplacement(buf, "found<" + replaceStr + ">");
    }
    matcher.appendTail(buf);
    System.out.println(buf.toString());

OUT:

    ab12 cd efg34 asdf 123  
    found<ab12> cd found<efg34> asdf 123  

##### Method 4

One more good sample. Replacing values in string by key.

    Map<String, String> props = new HashMap<String, String>();  
    props.put("key1", "fox");
    props.put("key2", "dog");
    String input = "The quick brown ${key1} jumps over the lazy ${key2}."; System.out.println(input); 
    Pattern p = Pattern.compile("\\$\\{([^}]+)\\}");
    Matcher m = p.matcher(input);
    StringBuffer sb = new StringBuffer();
    while (m.find()) { 
        m.appendReplacement(sb, "");
        sb.append(props.get(m.group(1)));
    }
    m.appendTail(sb);
    System.out.println(sb.toString());

OUT:

    The quick brown ${key1} jumps over the lazy ${key2}.  
    The quick brown fox jumps over the lazy dog.  

#### Greedy match

First go to wikipedia, or to good answer on [stackoverflow](https://stackoverflow.com/questions/2301285/what-do-lazy-and-greedy-mean-in-the-context-of-regular-expressions){:target="_blank"} for getting familiar with conception.
By default the pattern took longest possible string. But this is changeable. Here's samples with manipulation of these settings.

    String input = "abdcxyz";
    
    // in this case to the first group belongs all possible symbols, 
    // but the minimal amoung for the second group is preserved
    myMatches("([a-z]*)([a-z]+)", input);  // 1st: abdcxy 2nd: z
    
    // this case the first group is greedy so the matching string 
    // is shortest possible, all the rest goes to 2nd
    myMatches("([a-z]?)([a-z]+)", input); // 1st: a 2nd: bdcxyz
    
    // this case the 1st will took all string and 
    // the 2nd will be empty as far as * mean zero or more
    myMatches("([a-z]+)([a-z]*)", input); // 1st: abdcxyz 2nd:
    
    // and this case we will not match the string as it looks 
    // for minimal in both
    myMatches("([a-z]?)([a-z]?)", input); // nothing
    
    public static void myMatches(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
            System.out.print("1st: "+ matcher.group(1)+"     ");
            System.out.println("2nd: "+ matcher.group(2)); 
        } else {
            System.out.println("nothing");
        }  
    }

#### Regions

Quite simple conception - we can limit search area in string by creating "region" on it with defining start and end positions:

    String text = "012 456 890 234";
    Pattern ddd = Pattern.compile("\\d{3}");
    Matcher m = ddd.matcher(text).region(3, 12);
    System.out.println("region start here: "+m.regionStart()); System.out.println("region end here: "+m.regionEnd());
    while (m.find()) {
        System.out.printf("[%s] [%d,%d)%n", m.group(), m.start(), m.end()); 
    }

OUT:

    region start here: 3  
    region end here: 12  
    [456] [4,7)  
    [890] [8,11)  

Creating of region affect special symbols of start and end of string in pattern(^ and $). But this also might be changed with command `Matcher#useAnchoringBounds(true)`.

    String text = "012 456 890 234";
    Pattern ddd = Pattern.compile("^.*$");
    Matcher m = ddd.matcher(text).region(3, 12);
    System.out.println("region start here: "+m.regionStart()); System.out.println("region end here: "+m.regionEnd());
    if(m.find()){
        System.out.println("find matcher text with AnchoringBounds "+m.group());
        System.out.println("check AnchoringBounds: "+ m.hasAnchoringBounds());
    }
    m.reset();
    m.useAnchoringBounds(false);
    if(m.find()){
        System.out.println("find matcher text WITHOUT  AnchoringBounds "+m.group());
        System.out.println("check AnchoringBounds: "+ m.hasAnchoringBounds());
    }
    m.reset();  
    System.out.println("region start here: "+m.regionStart()); System.out.println("region end here: "+m.regionEnd());

OUT:

    region start here: 3  
    region end here: 12  
    find matcher text with AnchoringBounds  456 890  
    check AnchoringBounds: true  
    find matcher text WITHOUT  AnchoringBounds 012 456 890 234  
    check AnchoringBounds: false  
    region start here: 0  
    region end here: 15  

#### Regexp escaping

As we saw before - inside of string that we have passed for search or replacement there might be symbols with special meaning. For getting rid of this effect where its not needed these strings needs to be escaping in some way. I do not know how to do this, but that is not a problem - library developers have added this method into Pattern class:

    public static String quote(String s);

This one escape string, making it safe for use with regexp.

### Tutorials and articles

Quick references:

* 10 most popular regexps: http://www.mkyong.com/regular-expressions/10-java-regular-expression-examples-you-should-know/

* samples: http://www.java2s.com/Code/Java/Regular-Expressions/CatalogRegular-Expressions.htm

* working with streams: http://jexp.ru/index.php/Java/Regular_Expressions/Serialization

Official tutorial: http://docs.oracle.com/javase/tutorial/essential/regex/

Testing online:
http://myregexp.com/
<script src="{{ base.url | prepend: site.url }}/assets/links.js"></script>