---
title: Update timezone database in JVM
date: 2024-04-07 20:21:00 Z
lang: en
---

### Update timezone database for JVM in Linux

#### Problem
I live in `Europe\Kyiv` timezone. My computer knows it and shows the correct time in the clock widget on the desktop. As well as it shows the correct time via command line. And it properly detects that I am in a DST(daylight saving time) period now.

BUT JVM doesn't know about it. Just because it reads `Europe\Kyiv` from the system and doesn't recognize it. As all it knows is `Europe\Kiev`. [K**yi**v is not K**ie**v.](https://en.wikipedia.org/wiki/KyivNotKiev) So it reads further and somehow detects that original time offset for me is `+2` and so it uses it, but without DST rules. 
This:
```java
TimeZone aDefault = TimeZone.getDefault();
System.out.println("default timezone: " + aDefault);
System.out.println("new date: " + new Date());
```
Print this:
```text
default timezone: sun.util.calendar.ZoneInfo[id="GMT+02:00",offset=7200000,dstSavings=0,useDaylight=false,transitions=0,lastRule=null]
new date: Sun Apr 07 22:44:10 GMT+02:00 2024
```
But in fact I am in `GMT+03:00` timezone and so the detected `now` should be `23:44:10` but not `22:44:10`. 

### Solution
Java keeps information about all known timezones in its own internal storage. And that's updatable. The storage format differs from version to version, but you don't need to know it. As the Oracle provides a tool for updating timezone information in installed java. Its called `TZUpdater`. 

And I'm not going to use it since I found no easy readme, the download anything from Oracle website is a pain, and I have strong concerns it works with mine AdoptOpenJDK installed. You may [try your luck with it](https://www.oracle.com/java/technologies/javase/tzupdater-readme.html), but since there exists opensource alternative [ZIUpdater](https://www.azul.com/products/components/ziupdater-time-zone-tool/), I'm going to use it instead.

Work with `ZIUpdater` is simple: download, extract, run. You can download it from the [website](https://www.azul.com/products/components/ziupdater-time-zone-tool/): ![img.png](/resources/jvm-tz-update/img.png){: pretty}. 

For updating timezone information, you also need this information to get somewhere. IANA is a good and reliable source. Just download the latest file from there: https://data.iana.org/time-zones/tzdata-latest.tar.gz 

After run the tool with path to this file as parameter:
```bash
java -jar ziupdater-1.1.1.1.jar  -l  file:///home/msangel/Desktop/tzupdater/ziupdater1.1.1.1-jse8+7-any_jvm/tzdata-latest.tar.gz
```
That's it!

### Checking
```java
TimeZone aDefault = TimeZone.getDefault();
System.out.println("default timezone: " + aDefault);
System.out.println("new date: " + new Date());
```
Gives proper:
```text
default timezone: sun.util.calendar.ZoneInfo[id="Europe/Kyiv",offset=7200000,dstSavings=3600000,useDaylight=true,transitions=121,lastRule=java.util.SimpleTimeZone[id=Europe/Kyiv,offset=7200000,dstSavings=3600000,useDaylight=true,startYear=0,startMode=2,startMonth=2,startDay=-1,startDayOfWeek=1,startTime=3600000,startTimeMode=2,endMode=2,endMonth=9,endDay=-1,endDayOfWeek=1,endTime=3600000,endTimeMode=2]]
new date: Mon Apr 08 00:25:07 EEST 2024
```

Done.
