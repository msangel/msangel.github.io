---
title: Example of usage Ansible with Amazon Lightsail
date: 2022-07-07 19:59:00 Z
lang: en
---
Ansible is a tool for managing a cluster of computers. It's idea is simple - working on top of remote ssh, it execute the same command on each registered node. In opposite to classical orchestration tools, where the controller shoult be accessible(online) to dependent nodes, ansible just requires nodes to be accessible to controller. In simple and popular case, controller is your computer. Even it has a [lot of additional features](https://www.redhat.com/en/technologies/management/ansible/what-is-ansible), in this sample it will be used for automation of java-application deployment to remote host, in this case to Amazon Lightsail node.

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE1ODc2NjcyNjUsLTE0ODIxMjA3MzcsNT
E2NjIwNDc3LDE4NTU5MTM0ODBdfQ==
-->