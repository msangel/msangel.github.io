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

Copy from one file to another by 

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTIyOTk2NzMxNCwtMjAwMjMwOTI2MCwtMT
g5ODk0MDAyMywxODYxMTI5Mjc5LDQ5NTM4MzgwMl19
-->