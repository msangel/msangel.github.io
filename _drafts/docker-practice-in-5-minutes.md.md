---
title: Docker Practice In 5 Minutes.md
date: 2021-07-01 11:01:04.148000000 Z
---

todo: revrite fully:

Getting started with docker
https://docs.docker.com/engine/reference/commandline/docker/
>docker

docker <Management Command> <Sub-Command <Opts/Args>

Management Commands:
docker container # manage containers
docker image # manage images
docker network # manage network
docker system # misc system info and commands


info commands
ps          List containers
ps -a
// too long to list

manage containers
  attach      Attach local standard input, output, and error streams to a running container
  exec        Run a command in a running container
  create      Create a new container
  kill        Kill one or more running containers
  rm          Remove one or more containers
  rename      Rename a container
  start       Start one or more stopped containers
  stop        Stop one or more running containers
  restart     Restart one or more containers
  pause       Pause all processes within one or more containers
  unpause     Unpause all processes within one or more containers
  run         Run a command in a new container
  wait        Block until one or more containers stop, then print their exit codes


fs commands
  cp          Copy files/folders between a container and the local filesystem
  export      Export a container's filesystem as a tar archive
  save        Save one or more images to a tar archive (streamed to STDOUT by default)
  diff        Inspect changes to files or directories on a container's filesystem
  import      Import the contents from a tarball to create a filesystem image 


Commands for interacting with images
$ docker images  # shows all images.
$ docker import  # creates an image from a tarball.
$ docker build   # creates image from Dockerfile.
$ docker commit  # creates image from a container.
$ docker rmi     # removes an image.
$ docker history # list changes of an image.
$ docker load # load an image from a tar archive or STDIN


Registry commands:
  login       Log in to a Docker registry
  search      Search the Docker Hub for images
  logout      Log out from a Docker registry
  pull        Pull an image or a repository from a registry
  push        Push an image or a repository to a registry


Getting started with docker-machine
https://docs.docker.com/machine/reference/
Docker machine is an orchestration tool that allows you to manage multiple docker hosts. It lets you provision multiple virtual docker hosts locally, or on the cloud, and manage them with docker-machine commands. You can start, restart, and inspect managed hosts. You can point docker client to one of the hosts and then manage daemon on that host directly

Getting started with docker-compose
https://docs.docker.com/compose/reference/overview/
Docker compose is also an orchestration tool for docker. It allows you to easily manage multiple containers dependent on each other within one docker host via docker-compose CLI. You use a YAML file to configure all the containers. With one command you can start all containers in the correct order and set up networking between them.
Very nice complex and working sample: [spring-petclinic-microservices](https://github.com/spring-petclinic/spring-petclinic-microservices)
just clone the repository and run:
```shell
docker-compose up
```

Getting started with swarm mode
https://docs.docker.com/engine/swarm/
Docker swarm is another orchestration tool aimed to manage a cluster of docker hosts. While docker-compose managers multiple Docker containers within one docker host, docker swarm manages multiple docker hosts managing multiple Docker containers. Unlike docker-compose and docker-machine, docker swarm is not a standalone orchestration software. Swarm mode is built in docker engine and is managed through Docker client.

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTI5MDYzNzg1MSw3NTE3Mjc0MDMsLTE2ND
QxMDM3MDBdfQ==
-->