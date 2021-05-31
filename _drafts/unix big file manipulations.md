---
title: Unix big file manipulations
date: 2021-05-31 11:51:14 Z
---

sed flags:
* `p` - print
* `q` - quit processing after that line
* 
Count lines in file:
```bash
# show tab with filename
wc -l /dir/file.txt
3272485 /dir/file.txt
# show just numbers
wc -l < /dir/file.txt

```
Show lines by number:
```bash
sed -n '10000000,10000020p; 10000021q' filename
sed -n '10000000,+20p'  filename
``` 

Delete lines by number:
```bash
sed -i '$d' <file>
```

Copy from one file to another with skipping header
```bash
# where +2 is "starting fromm line 2
cat in.csv | tail -n +2  >> out.csv
```

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTM4NjU3NzM5NCwxMjI5OTY3MzE0LC0yMD
AyMzA5MjYwLC0xODk4OTQwMDIzLDE4NjExMjkyNzksNDk1Mzgz
ODAyXX0=
-->