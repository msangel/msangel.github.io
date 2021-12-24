---
title: Unix big file manipulations
date: 2021-05-31 11:51:14 Z
---

Small recipes for manipulation with BIG files using *nix (linux and macOS) tooling. 

Some of these tools may be installed on Windows using a lot of various ways, the most easy probably will be:
* [MSYS2](https://www.msys2.org/) - just set of tools
* [cygwin](https://www.cygwin.com/)  - just set of tools
* [wls](https://docs.microsoft.com/en-us/windows/wsl/install) - entire linux subsystem bring to your Windows by Microsoft, because they know where the future is

### Count lines in file
Correct:
```bash
grep -c ^ file.txt
```
note: command `wc -l` that is first google result count new-line characters, and this is not working in this case:
```txt
1 This is the first line.\n
2 This is the second line.
```

### Show lines by number
#### Using sed
In this case we will use `sed` command. Logic behind this command is: it has two buffers. The first one is used for reading input file line by line, the second contains current line and the parametrized operation executed on that buffer. In default mode after each line the result of second buffer is printed to out before and after, but this can be disabled by `-n`(longer version `--quiet`), for example if we want to print only required lines.

Command is optional parameter, it can contain ReqExp or line number for identifying lines of interest. For this task we need two commands:
* `p` - print
* `q` - quit processing after that line

before the command we need to provide lines addressing. General rules are:
* number - for identifying line number where the command should be executed;
* first~step - command will be executed for the fist line number and then for all with given step;
* $ - last file line;
* /regular_expression/ - any string that match by ReqExp. Can have own flags, like: `I` - case-insensitive and so on;
* number, number - starting from string line no. 1 to string line no. 2;
* number, /regular_expression/ - starting from string line no. 1 to string matching pattern
* number, +amount -  amount of lines starting from string line no. 1
* number, ~number, starting from string line no. 1 to string line number mod number 2
* <anything>! - invert selection(remain file except matching lines)

Examples:
```
Show lines by number:
```bash
sed -n '10000000,10000020p; 10000021q' filename
sed -n '10000000,+20p'  filename
``` 

#### Using tail and head
There exists two very specialized tools in *nix the simply took either `head` or `tail` of the file. These works much faster then `sed` but the capabilities are more limited.
`head` parameters:
* `-n <number>` or `-<number>` took only given amount of **first** lines  
* `--lines=-<number>` took all first lines of file except given amount of latest(because of minus)

`tail` parameters:
* `-n <number>` or `-<number>` took only given amount of **last** lines
* `-n +<number>` or `+<number>` took all last lines of file except given amount of first(because of plus)

So, combining these, we can print lines of interest from file. Example:
```bash
# print 10 lines staring from 2nd:
tail +2 sample.txt | head -10
# print lines 10-12:
tail +10 sample.txt | head -2
# Copy from one file to another with skipping header
cat in.csv | tail -n +2  >> out.csv
```

Speed comparison:
```bash
> time tail +50000000 bigfile.csv | head -2
< real	0m4,291s
< user	0m1,560s
< sys	0m2,700s
> time sed -n '50000000,+2p;50000001q'  bigfile.csv
> real	0m7,080s
> user	0m4,468s
> sys	0m2,608s
```
So, according to this tail and head combination is as twice as faster.

### Delete lines by number
#### Using `sed`
For `sed` there two important option for this:
* `-i` parameter means inplace edition (otherwise instead of file edition this will print out without deleted line)
* `d` command means delete that line

Examples:
```bash
# delete line number 10
sed -i '10d' sample.txt
# delete first line
sed -i '1d' sample.txt
```
