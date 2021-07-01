---
title: Docker basis
date: 2019-06-08 03:06:00 Z
---

# {{ page.title }}
{: .title }
https://sites.google.com/site/modernskyangel/in-progress/docker-in-review

<!-- excerpt-start -->This article is about docker and the technologies it is built on. Mostly theory here.<!-- excerpt-start -->
For more practical things read that.

## The "Docker" keyword
Before diving into the docker world, lets clarify basics about what Docker is:
1. Docker is a company
2. Docker is a software. Actually two kind of it
    1. Docker CE (community edition)
    2. Docker EE (enterprise edition)
       The company develops the software and also build the infrastructure that people need around it.

## What it is, and why we need this

Best description what is docker is: lightweight virtual machine.
And we need it because allows us to run our applications in the same way and in the same environment whenever we have. Either this is Windows PC, Linux laptop, dedicated server or virtual hosting.  It's a common problem when developers create their software on their computer and so it can be run only on their computer. Docker creates an isolated environment that is the same everywhere.
There exists a known "Matrix of hell" that show a wide variety of different systems and their different behavior on different hardware:

![Matrix of hell](https://k.co.ua/resources/docker/the_matrix_of_hell.png "Matrix of hell"){: pretty}

Also, there exists another problem: developers want to use edge technologies like Node.js, Rust, Go, Microservices, Cassandra, Hadoop, etc.
But server engineers and security want to use the same tooling as they used before, something that is proven, works, and don't require additional work to configure/secure.

And so docker allows to combine wishes of both sides, it's like an interaction contract.

Server operations have to care about only one thing. They have to support deploying containers. So they are satisfied. Developers are happy too. They can develop with whatever the fad of the day is and then just stick it into a container and throw it over the wall to operations.

So docker allows to solve this matrix in a very straightforward way:

![Matrix solved](https://k.co.ua/resources/docker/the_matrix_of_hell_solved.png "Matrix solved"){: pretty}

This is really cool approach. But technologies were like that not from the beginning. Hosting has a long and very complex history. And that has an impact on the docker. I'm pretty sure we must overview some basics of hosting history for understanding what requirements and problems exist prior to docker.

## Hosting history

First sites and services were hosted on office computers. Well, you can even imagine how many problems there were with this approach. Staring from "I accidentally turned it off" to real power supply stability issues.
After, the DNS-providers starts providing additional service for hosting, as add-on service. And it's a good point, as DNS-servers anyway must work 24/7 with extreme stability, so the companies running that knew well how to achieve this stability for sites and services of their users.
Quite fast the specialization took the place and so appears entire data centers with dedicated computers (dedicated servers). Still, the prices for that were not flexible and because of usually low utilization of the server per user, the same computers start to sell to many people, of course with some isolation level between user's data. These isolation requirements cause appearing and evolution of virtual server software.
Another possible scenario thise days was to buy phisical server for own usage and put it to data-center. But that was way to expensive.

## About virtual servers
The virtual servers are software that allows running another virtual emulated computer as any regular application. And as far as that computer is emulated, its environment also emulated, so running any programs in it will not harm your primary operation system(if you have one, as stepping a bit forward, I can say that there exist virtual servers that run directly on hardware and don't require OS to be installed).
The advantages of vitrual servers are:
1. managed environment (including choice of operating system)
2. the required amount of resources (cheaper where possible)
3. easy creation of backups and restoration from them
4. easy launch of new instances (for providers)
5. better use of resources (for providers) incl. electricity
   The root place in virtual servers took hypervisor.

### Hypervisor
The hypervisor, also referred to as Virtual Machine Manager (VMM), is what enables virtualization (running several operating systems on one physical computer). It allows the host computer to share its resources between VMs([link](https://www.vmware.com/topics/glossary/content/hypervisor)).
There exists two types of hypervisor: native(or bare metal) and hosted.

![Hypervisor types](https://k.co.ua/resources/docker/hypervisor.png "Hypervisor types"){: pretty}

### Native hypervisor
The native hypervisor is installed right on top of the underlying machine’s hardware (so, in this case, there is no host OS, there are only guest OS’s). This is usually installed on a machine on which the whole purpose was to run many virtual machines. The hypervisor here has its own device drivers and interact with hardware directly. Such hypervisors are faster, simpler, and hence more stable.

Examples:
[antsleOS](https://antsle.com/all/os-used-in-antsle/), [Xen](https://xenproject.org/), [XCP-ng](https://xcp-ng.org/), [Oracle VM Server](https://www.oracle.com/virtualization/vm-server-for-x86/), [Microsoft Hyper-V](https://docs.microsoft.com/en-us/windows-server/virtualization/hyper-v/hyper-v-technology-overview), [Xbox One system software](https://direct.playstation.com/en-us/ps5), [VMware ESX/ESXi](https://www.vmware.com/products/esxi-and-esx.html) and many more

### Hosted hypervisor
This is a program(regular executable file) that is running on top of the operating system. This type of hypervisor is something like a “translator” that translates the guest operating system’s system calls into the host operating system’s system calls.

Examples:
[VirtualBox](https://www.virtualbox.org/), [Parallels Desktop for Mac](https://www.parallels.com/), [QEMU](https://www.qemu.org/), [VMware Workstation](https://www.vmware.com/products/workstation), [VMware Player](https://www.vmware.com/products/player) and many more

### Pros and Cons of virtual servers
Pros:
- Controlled environment, as each VM has own;
- Resources on demand, as this is simply hypervisor's option, that can be changed even without guests OS restart;
- Easy backups and restoring, as each VM's files are kept in one place. The drive storage of VM often is also emulated and persists as regular file;
- Easy new instances start, just click a button;
- Less power consumption, for providers, as allow to host many VM of one physical computer.

Cons:
- Low computational performance, as the calls to system are translated via intermediate layer;
- Huge memory usage, as guests OSs tends to fill all available RAM (as any regular OS), but we have many of them;
- Problems with hardware (USB, printers, etc.), as the access to those devices is not direct;
- Complex management;

## Containerization
Containerization is a lightweight alternative to full machine virtualization that involves encapsulating an application in a container with its own operating environment. For easy understanding we can say, that container is just a native process that running in own environment, so its execution is not affecting host environment in any way.

![VM vs Container](https://k.co.ua/resources/docker/vm_vs_container.png "VM vs Container"){: pretty}
The image describes the difference between a VM and Docker. Instead of a _hypervisor_ with Guest OSes on top, Docker uses a _Docker engine_ and containers on top. Does this really tell us anything? What is the difference between a "hypervisor" and the "Docker engine"? A nice way of illustrating this difference is through listing the running processes on the Host.

The following simplified process trees illustrates the difference.

On the Host running the VM there is only _one_ process running on the Host even though there are many processes running in the VM.

Running processes on Host for a VM
$ pstree VM

-+= /VirtualBox.app
|--= coreos-vagrant

On the Host running the Docker Engine all the processes running are visible. _The contained processes are running on the Host!_ They can be inspected and manipulated with normal commands like, `ps`, and `kill`.

Running processes on Host for a Docker Engine
$ pstree docker
-+= /docker
|--= /bin/sh
|--= node server.js
|--= go run app
|--= ruby server.rb
...
|--= /bin/bash

The size of a small virtual machine image with Core OS is about 1.2 GB. The size of a small container with busybox is 2.5 MB.

The startup time of a fast virtual machine is measured in minutes. The startup time of a container is often less than a second.

Integrating virtual machines running on the same host must be done by setting up the networking properly. Integrating containers is supported by Docker out of the box.

So, containers are lightweight, fast and easily integrated, but that is not all.


### Chroot - first attempts in processes isolation
<div class="row" markdown="1">
![chroot](https://k.co.ua/resources/docker/chroot.png "Chroot")
{: .pull-left .col-xs-12 .col-md-6 }

A chroot on Unix operating systems is an operation that changes the apparent root directory for the current running process and its children. A program that is
run in such a modified environment cannot name (and therefore normally cannot access) files outside the designated directory tree.
The chroot system call was introduced during development of Version 7 Unix in 1979, and added to BSD by Bill Joy on 18 March 1982 – in order to test installation and build system of 4.2BSD. An early use of the term "jail" as applied to chroot comes from Bill Cheswick creating a honeypot to monitor a cracker in 1991.
First known breaking out of chroot jail was published in 1999.
</div>

### Chroot on steroids
Docker's technology is based on LXC(Linux Containers) for <1.8 and libcontainer (now opencontainers/runc). All containers on a given host run under the same kernel, with other resources isolated per container.
Docker allows isolating a process at multiple levels through namespaces and utilities:
- **cgroups** for cpu and memory limits, reduced capabilities, controlling what you can use;
- **mnt** namespace provides a root filesystem, this one can be compared to chroot
- **pid** namespace so the process only sees itself and its children
- **network** namespace which allows the container to have its dedicated network stack
- **user** namespace which allows a non-root user on a host to be mapped with the root user within the container
- **uts** provides dedicated hostname per process tree (bootstrap from system values)
- **ipc** (inter-process communication)provides dedicated shared memory
- **seccomp** (secure computing mode with instructions checkings)
- **selinux**/**apparmor** (Security-Enhanced Linux / "Application Armor") — linux kernel security modules
- **ulimits** (number of open file descriptors per process)
- **Union File Systems** allows representing the result of multiple layers of filesystems as a single filesystem, where usually only the latest layer is for writing.

Using this capabilities same time, docker provide these  advantages:
- **Flexible**: Even the most complex applications can be containerized. Tread your container as a separated environment(actually it is), that runs natively on your hardware. Do what you want there. Install any software of any versions, organize your files your way there, and many more.
- **Lightweight**: besides of own environment, processes in the container as the same native processes that run on the host kernel directly.
- **Interchangeable**: you can deploy updates and upgrades on the fly. turning the container off is simple the same as shutting down all its processes. You can work with the container's file system even if the container stopped. After applying changes you can start them back, start from a saved image or even simply start a new version instead.
- **Portable**: anything you build locally can run anywhere, for example, to be deployed to the cloud.
- **Scalable**: you can increase and automatically distribute container replicas.

Still there exists some disadvatages.
First is **security**. Containers share the same hooks into the kernel, and that’s a problem because if there are any vulnerabilities in the kernel, then someone can get into other containers. Containers have not yet demonstrated that they can deliver the same secure boundaries that a VM still has.
The next one is **flexibility in OS choosing**. There is no way to use windows apps under linux and visa versa. Also there even no way to use another kernel, rather than existed one. Yes, we can install an emulator of desired system in a container(like [wine](https://www.winehq.org/) in linux and [wsl](https://docs.microsoft.com/en-us/windows/wsl/) in windows), but that's will be another intermediate layer of abstraction.

## Docker architecture
Docker is implemented as a client-server system.  Docker software consists of 2 separate programs, that is docker engine, also known as docker daemon (because it is, in fact, a daemon, running in the background ) and docker client.

Docker engine is responsible for running processes in isolated environments. For each process, it generates a new Linux container, allocates a new filesystem for it, allocates a network interface, sets an IP for it, sets NAT for it and then runs processes inside of it. It also manages such things as creating, removing images, fetching images from the registry of choice, creating, restarting, removing containers and many other things. Docker engine exposes the rest API which can be used to control the daemon.

Docker client is usually CLI app. Daemon is accessible via socket connection(TCP or UNIX) and simply provide controlling API. The client may, but does not have to, be on the same machine as the daemon.

Terminology:
-   **host**(also **docker server**) — the machine that is running the containers.
-   **image** — a hierarchy of files, with meta-data for how to run a container.  They are read-only. This give:
    -   many instances of the same
    -   you don't afraid of crashes
-   **container** — a contained running process, started from an image.
-   **registry** — a repository of images. It's a service that enables users to push images to it, make them public or private, and pull different images, all using the docker client CLI. Docker hub is a docker image registry provided by Docker, Inc itself. Alternatively, you can host your own docker registry.
-   **volume** — storage outside the container.
-   **Dockerfile** — a script for creating images.

![Docker use](https://k.co.ua/resources/docker/docker_use.png "Docker use"){: pretty}

The mentined above "Union File sysytem" built on top of   "Copy-On-Write" principle and allows hight reuse of file systems.

![Union File System](https://k.co.ua/resources/docker/copy_on_write_fs.png "Union File System"){: pretty}

For example: if you build most of your applications as PHP site driven by Apache Http Server, the single layer with Apache server will be shared between all of your applications. And the files of each of those applications every time will be put on top of that base at runtime time.

## Conclusion
A docker is a powerful tool that is created to solve many common developer's problems and tasks. But before using it, nice to have some insights on how we came to it and how it is working inside, actually, this article about that.
You may also want to look at the more practical part of using docker in my docker cookbook.

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTAzMDk3NTQ2MSwtMjEyMzk4NDAyNyw1Mz
k1ODg5OTEsLTMwMjQxNzEwMiwyMDI3MTAwOTQwLDkxNTkwMDYw
NCwtOTM1NDY2NTU3LDE1MzM1ODE1ODAsLTE1NDg5MDcxNyw5NT
czMjYwNTgsLTE5MzIxMzMzNTIsNTAzMDI0NDIxLC0zMjIwNDg4
MjgsLTE4NzkxOTAzNzAsMTcxMzg4OTA4NCwtMTI0ODk5NDg3OC
wtMTMzNDc2MDk4OCw3OTAwMzgxMTIsLTk4MjE0NzcyNSwtODk0
MzI3Mjc1XX0=
-->
