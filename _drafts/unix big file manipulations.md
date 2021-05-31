---
title: Unix big file manipulations
date: 2021-05-31 11:48:11 Z
---sed flags:
* `p` - print
* `q` - quit processing

Show lines by number:
```bash
sed -n '10000000,10000020p; 10000021q' filename
sed -n '10000000,+20p'  filename
``` 
> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTg4MzYzNjU2MV19
-->