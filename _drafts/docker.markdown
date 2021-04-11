---
title: docker intro
date: 2019-06-08 03:06:00 Z
---

# The "Docker" keyword
Docker is a company.
Docker is a software (Docker community edition (CE) and Docker enterprise edition (EE)).

# What it is, and why we need this. The difference between Virtual Servers and Dedicated servers.

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
 

