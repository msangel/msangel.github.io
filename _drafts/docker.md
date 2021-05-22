---
title: docker intro
date: 2019-06-08 03:06:00 Z
---

https://sites.google.com/site/modernskyangel/in-progress/docker-big

# The "Docker" keyword
Before diving into the docker world, lets clarify basics about what Docker is:

 1. Docker is a company
 2. Docker is a software. Actually two kinds of it
	 1. Docker CE (community edition) 
	 2. Docker EE (enterprise edition)

# What it is, and why we need this. The difference between Virtual Servers and Dedicated servers.

A very simple answer to the question of why we need it is: it allows us to run our applications in the same way and in the same environment whenever we have either this is Windows PC, Linux Laptop, dedicated server or virtual hosting.  It's a common problem when developers create their software on their computer and so it can be run only on their computer. Docker creates an isolated environment that is the same everywhere.
There exists a known "Matrix of hell" that show a wide variety of different systems and their different behavior on different hardware:

![Matrix of hell](https://github.com/msangel/msangel.github.io/raw/master/_drafts/docker/the_matrix_of_hell.png "Matrix of hell")

Also, there exists another problem: developers want to use edge technologies like Node.js, Rust, Go, Microservices, Cassandra, Hadoop, etc.
But server operations want to use the same as they used yesterday, what they used last year because it is proven, it works!

And docker allows combining both, it's like an interaction contract.

Operations are satisfied because they only have to care about one thing. They have to support deploying containers. Developers are also happy. They can develop with whatever the fad of the day is and then just stick it into a container and throw it over the wall to operations.

So docker allows to solve this matrix in a very straightforward way:
![Matrix solved](https://github.com/msangel/msangel.github.io/raw/master/_drafts/docker/the_matrix_of_hell_solved.png "Matrix solved")


## Hosting history
On computers in offices
As an additional service of dns-providers (addon-service)
Data centers and dedicated computers (Dedicated servers)
Virtual servers

## Virtual servers, because of...
Advantages
1. managed environment (including choice of operating system)
2. the required amount of resources (cheaper where possible)
3. easy creation of backups and restoration from them
easy launch of new instances (for providers)
4. better use of resources (for providers) incl. electricity

## Hypervisor
The hypervisor, also referred to as Virtual Machine Manager (VMM), is what enables virtualization (running several operating systems on one physical computer). It allows the host computer to share its resources between VMs.[5]

This software is installed right on top of the underlying machine’s hardware (so, in this case, there is no Host OS, there are only Guest OS’s). You would do this on a machine on which the whole purpose was to run many virtual machines.

Type 1, also called “Bare Metal Hypervisor”
Type 1 hypervisors have their own device drivers and interact with hardware directly unlike type 2 hypervisors. That’s what makes them faster, simpler and hence more stable.

Examples:
AntsleOs, Xen, XCP-ng, Oracle VM Server, Microsoft Hyper-V, Xbox One system software, and VMware ESX/ESXi

Type 2, also called “Hosted Hypervisor”
This is a program that is installed on top of the operating system. This type of hypervisor is something like a “translator” that translates the guest operating system’s system calls into the host operating system’s system calls.

Examples:
VMware Workstation, VMware Player, VirtualBox, Parallels Desktop for Mac and QEMU
 


Also take a look on [presentation](https://docs.google.com/presentation/d/e/2PACX-1vT9OVJT6Etyzd-FJEKitGW5g7t8zzEmPdykV9AoCGZSYoLSuCo1hUxrffhkzwaiwtQ7r3o4VJjp_e57/pub?start=false&loop=false&delayms=3000)
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE1NjQxNTYyOTQsLTg5OTczMTQxNCwxMj
YyMjI1MDgxLDE1NDU2Mjk1NCwxNjk4NjgyODA1LDY5OTM1NzA4
OSwyMDIzNTY4MTMsLTUwNTAzNzIyOCwxMjY3NDE0MjkwLDY2MD
k0MTg2NywtMTYxMzY3MjUzMV19
-->