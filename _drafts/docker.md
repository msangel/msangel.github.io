---
title: docker intro
date: 2019-06-08 03:06:00 Z
---

https://sites.google.com/site/modernskyangel/in-progress/docker-big

# The "Docker" keyword
Before diving into the docker world, lets clarify basics about what Docker is:
 1. Docker is a company
 2. Docker is a software. Actually two kind of it
	 1. Docker CE (community edition) 
	 2. Docker EE (enterprise edition)

# What it is, and why we need this

A very simple answer to the question of why we need it is: it allows us to run our applications in the same way and in the same environment whenever we have either this is Windows PC, Linux laptop, dedicated server or virtual hosting.  It's a common problem when developers create their software on their computer and so it can be run only on their computer. Docker creates an isolated environment that is the same everywhere.
There exists a known "Matrix of hell" that show a wide variety of different systems and their different behavior on different hardware:

![Matrix of hell](https://github.com/msangel/msangel.github.io/raw/master/_drafts/docker/the_matrix_of_hell.png "Matrix of hell")

Also, there exists another problem: developers want to use edge technologies like Node.js, Rust, Go, Microservices, Cassandra, Hadoop, etc.
But server operations want to use the same tooling as they used yesterday, what they used last year because it is proven, it works!

And docker allows to combine wishes of both sides, it's like an interaction contract.

Operations have to care about only one thing. They have to support deploying containers. So they are satisfied. Developers are happy too. They can develop with whatever the fad of the day is and then just stick it into a container and throw it over the wall to operations.

So docker allows to solve this matrix in a very straightforward way:
![Matrix solved](https://github.com/msangel/msangel.github.io/raw/master/_drafts/docker/the_matrix_of_hell_solved.png "Matrix solved")This is really cool approach. But technologies came to it not from the beginning. Hosting have long and complex history of that have the impact on the docker, and I pretty sure we must overview some basics of hosting history for understanding what requirements and problem exist prior docker.

## Hosting history

First sites and services were hosted on office computers. Well, you can even imagine how many problems there were with this approach. Staring from "I accidentally turned it off" to real power supply stability issues.
After, the dns-providers starts providing additional service for hosting, as add-on service. And it's a good point, as dns-servers anyway must work 24/7 with extreme stability, so the companies running that knew well how to achieve this stability for sites and services of their users.
Quite fast the specialization took the place and so appears entire data centers with dedicated computers (dedicated servers). Still, the prices for that were not flexible and because of usually low utilization of the server per user, the same computers start to sell to many people, of course with some isolation level between user's data. These isolation requirements cause appearing and evolution of virtual server software.

## About virtual servers
The virtual servers are software that allow to run another virtual emulated computer as any regular application. And as far as that cumputer is emulated, its environment also emulated, so running any programs in it will not harm you primary operation system(if you have one, as steping a bit forward, I can said that there exists virtual servers applications that run directly on hardware and don't requires OS to be installed).
The advantages of vitrual servers are:
1. managed environment (including choice of operating system)
2. the required amount of resources (cheaper where possible)
3. easy creation of backups and restoration from them
easy launch of new instances (for providers)
4. better use of resources (for providers) incl. electricity
The root place in virtual servers took hypervisor.

## Hypervisor
The hypervisor, also referred to as Virtual Machine Manager (VMM), is what enables virtualization (running several operating systems on one physical computer). It allows the host computer to share its resources between VMs([link](https://www.vmware.com/topics/glossary/content/hypervisor)).
There exists two types of hypervisor: native(or bare metal) and hosted.
![Hypervisor types](https://github.com/msangel/msangel.github.io/raw/master/_drafts/docker/hypervisor.png "Hypervisor types")

### Native hypervisor
In native type the hypervisor is installed right on top of the underlying machine’s hardware (so, in this case, there is no host OS, there are only guest OS’s). This is usually installed on a machine on which the whole purpose was to run many virtual machines. The hypervisor here have its own device drivers and interact with hardware directly. Such hypervisors are faster, simpler and hence more stable.

Examples:
[antsleOS](https://antsle.com/all/os-used-in-antsle/), [Xen](https://xenproject.org/), [XCP-ng](https://xcp-ng.org/), [Oracle VM Server](https://www.oracle.com/virtualization/vm-server-for-x86/), [Microsoft Hyper-V](https://docs.microsoft.com/en-us/windows-server/virtualization/hyper-v/hyper-v-technology-overview), Xbox One system software, and VMware ESX/ESXi

Type 2, also called “Hosted Hypervisor”
This is a program that is installed on top of the operating system. This type of hypervisor is something like a “translator” that translates the guest operating system’s system calls into the host operating system’s system calls.

Examples:
VMware Workstation, VMware Player, VirtualBox, Parallels Desktop for Mac and QEMU
 


Also take a look on [presentation](https://docs.google.com/presentation/d/e/2PACX-1vT9OVJT6Etyzd-FJEKitGW5g7t8zzEmPdykV9AoCGZSYoLSuCo1hUxrffhkzwaiwtQ7r3o4VJjp_e57/pub?start=false&loop=false&delayms=3000)
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTkwOTE5ODczOCwtODY5MTM4MTI4LC0yMT
EzNzc5Nzk2LC03OTY2NjgyNDMsMTM3NzIxMDE4NiwtMTIxMTI4
MDU1MywyMTA1NDA5NzU1LDQ5NjgxOTMzNywtMTUxMTgyNjkzMy
w1OTE2Njc4NTUsNjQ0OTkyNDU1LC0xNTczNjU5Mzg1LDE0MjE4
NjIwODcsNzExMDM0ODUyLC03NDI2ODQ4MTQsMTI0NDIxMDcyOS
wtMTU2NDE1NjI5NCwtODk5NzMxNDE0LDEyNjIyMjUwODEsMTU0
NTYyOTU0XX0=
-->