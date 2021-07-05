---
title: Docker Practice In 5 Minutes.md
date: 2021-07-01 11:01:04 Z
---

## Installing


First run

Second run
complex sample with volume, enviroupment values, args, redefined entrypoint, expotred ports, and defining user to run (-u=www)

Concepts

Create image

base: alpine/ubuntu flavours

by commiting chages
why its bad

by Dockerfile

Managing images
Run and run options
Managing containers

Volumes


Security?
sample for debian-based
sample for alpine
_____


docker version

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
http://blog.jayway.com/wp-content/uploads/2015/03/docker-image.png

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
  
commonly used images:
scratch – this is the ultimate base image and it has 0 files and 0 size.
busybox – a minimal Unix weighing in at 2.5 MB.
debian:buster -  the latest Debian is at 108MB
ubuntu:latest - latest LTS (19.04 disco)
alpinelinux: https://alpinelinux.org/



By commit changes
$ docker run -i -t debian:jessie bash
root@e6c7d21960:/# apt-get update
root@e6c7d21960:/# apt-get install postgresql
root@e6c7d21960:/# apt-get install node
root@e6c7d21960:/# node --version
root@e6c7d21960:/# curl https://iojs.org/dist/v1.2.0/iojs-v1.2.0-
linux-x64.tar.gz -o iojs.tgz
root@e6c7d21960:/# tar xzf iojs.tgz
root@e6c7d21960:/# ls
root@e6c7d21960:/# cd iojs-v1.2.0-linux-x64/
root@e6c7d21960:/# ls
root@e6c7d21960:/# cp -r * /usr/local/
root@e6c7d21960:/# iojs --version
1.2.0
root@e6c7d21960:/# exit
$ docker ps -l -q
e6c7d21960
$ docker commit e6c7d21960 postgres-iojs
daeb0b76283eac2e0c7f7504bdde2d49c721a1b03a50f750ea9982464cfccb1e

By writing script

Create a special text file with name Dockerfile and 
docker build <path of the folder with Dockerfile in it>.

FROM openjdk:8-jdk-alpine
WORKDIR /app # define current working directory (user.dir env for java)
ADD . /app # add local content as new layer
RUN some-script.sh # defaults to /bin/sh
EXPOSE # ports, documentary instruction
CMD # run this in the container on its creating, the container bound to this process



By docjerfile
FROM debian:jessie
Dockerfile for postgres-iojs

RUN apt-get update
RUN apt-get install -y postgresql
RUN curl https://iojs.org/dist/iojs-v1.2.0.tgz -o iojs.tgz
RUN tar xzf iojs.tgz
RUN cp -r iojs-v1.2.0-linux-x64/* /usr/local

build it:
docker build -tag postgres-iojs .

Since every command in the Dockerfile creates a new layer it is often better to run similar commands together. Group the commands with and and split them over several lines for readability.
```
FROM debian:jessie
# Dockerfile for postgres-iojs

RUN apt-get update && \
  apt-get install postgresql && \
  curl https://iojs.org/dist/iojs-v1.2.0.tgz -o iojs.tgz && \
  tar xzf iojs.tgz && \
  cp -r iojs-v1.2.0-linux-x64/* /usr/local
```
The ordering of the lines in the Dockerfile is important as Docker caches the intermediate images, in order to speed up image building. Order your Dockerfile by putting the lines that change more often at the bottom of the file.

The Dockerfile supports 13 commands. Some of the commands are used when you build the image and some are used when you run a container from the image. Here is a list of the commands and when they are used.

BUILD Commands:
FROM – The image the new image will be based on.
MAINTAINER – Name and email of the maintainer of this image.
COPY – Copy a file or a directory into the image.
ADD – Same as COPY, but handle URL:s and unpack tarballs automatically.
RUN – Run a command inside the container, such as apt-get install.
ONBUILD – Run commands when building an inherited Dockerfile.
.dockerignore – Not a command, but it controls what files are added to the
build context. Should include .git and other files not needed when building
the image.

RUN Commands
CMD – Default command to run when running the container. Can be overridden
with command line parameters.
ENV – Set environment variable in the container.
EXPOSE – Expose ports from the container. Must be explicitly exposed by the
run command to the Host with -p or -P.
VOLUME – Specify that a directory should be stored outside the union file
system. If is not set with docker run -v it will be created in
/var/lib/docker/volumes
ENTRYPOINT – Specify a command that is not overridden by giving a new
command with docker run image cmd. It is mostly used to give a default
executable and use commands as parameters to it.
Both BUILD and RUN Commands
USER – Set the user for RUN, CMD and ENTRYPOINT.
WORKDIR – Sets the working directory for RUN, CMD, ENTRYPOINT, ADD and
COPY.

Commands for interacting with images
$ docker images  # shows all images.
$ docker import  # creates an image from a tarball.
$ docker build   # creates image from Dockerfile.
$ docker commit  # creates image from a container.
$ docker rmi     # removes an image.
$ docker history # list changes of an image.

Running containers

When a container is started, the process gets a new writable layer in the union file system where it can execute. It is also possible to make this layer read-only, forcing us to use volumes for all file output such as logging, and temp-files.


Commands for interacting with containers
$ docker create  # creates a container but does not start it.
$ docker run     # creates and starts a container.
$ docker stop    # stops it.
$ docker start   # will start it again.
$ docker restart # restarts a container.
$ docker rm      # deletes a container.
$ docker kill    # sends a SIGKILL to a container.
$ docker attach  # will connect to a running container.
$ docker wait    # blocks until container stops.
$ docker exec    # executes a command in a running container.

docker run -it --rm ubuntu
--interactive (-i) – send stdin to the process.
-tty (-t) – tell the process that a terminal(TeleTYpe) is present. This affects how the process outputs data and how it treats signals such as (Ctrl-C).
--rm – remove the container on exit.

docker run -d hadoop
--detached (-d) – Run in detached mode, you can attach again with docker
attach

Run a named container and pass it some environment variables with –env
```
$ docker run \
  --name mydb \
  --env MYSQL_USER=db-user \
  -e MYSQL_PASSWORD=secret \
  --env-file ./mysql.env \
  mysql
```

publish port:
Publish container port 80 on a random port on the Host
$ docker run -p 80 nginx
 
Publish container port 80 on port 8080 on the Host
$ docker run -p 8080:80 nginx
 
Publish container port 80 on port 8080 on the localhost interface on the Host
$ docker run -p 127.0.0.1:8080:80 nginx
 
Publish all EXPOSEd ports from the container on random ports on the Host
$ docker run -P nginx
The nginx image, for example, exposes port 80 and 443.

```Dockerfile
FROM debian:wheezy
MAINTAINER NGINX "docker-maint@nginx.com"
EXPOSE 80 443
```




It is also possible to limit how much access the container has to the Host's resources.

docker exec allows us to run commands inside already running containers. This is very good for debugging among other things.

```
# Run a shell inside the container with id 6f2c42c0
$ docker exec -it 6f2c42c0 sh
```






security(hacker photo here):
todo: default docker user?
Since docker is complex software with many coponents, the security issues are appears there eventually, but they got fixed.
known issues:
-   Image signatures are not properly verified.
-   If you have `root` in a container you can, potentially, get root on the entire box.

Advices:
- Use trusted images from your private repositories.
- Don't run containers as root, if possible.
- Treat root in a container as root outside a container

sec on user sample for alpine
https://wiki.alpinelinux.org/wiki/Setting_up_a_new_user
```Dockerfile
RUN addgroup -S app && adduser -S app -G app
RUN mkdir -p /var/opt/app/logs
RUN mkdir -p /opt/app
RUN chown app:app -R /opt/app
RUN chown app:app -R /var/opt/app/logs
USER app:app
WORKDIR /opt/app
```

##   
Inspecting Containers

A lot of commands are available for inspecting containers:

$ docker ps      # shows running containers.
$ docker inspect # info on a container (incl. IP address).
$ docker logs    # gets logs from container.
$ docker events  # gets events from container.
$ docker port    # shows public facing port of container.
$ docker top     # shows running processes in container.
$ docker diff    # shows changed files in container's FS.
$ docker stats   # shows metrics, memory, cpu, filsystem

I will only elaborate on `docker ps` and `docker inspect` since they are the most important ones.

List all containers, (--all means including stopped)
$ docker ps --all
CONTAINER ID   IMAGE            COMMAND    NAMES
9923ad197b65   busybox:latest   "sh"       romantic_fermat
fe7f682cf546   debian:jessie    "bash"     silly_bartik
09c707e2ec07   scratch:latest   "ls"       suspicious_perlman
b15c5c553202   mongo:2.6.7      "/entrypo  some-mongo
fbe1f24d7df8   busybox:latest   "true"     db_data


Inspect the container named silly_bartik
Output is shortened for brevity.
$ docker inspect silly_bartik
    1 [{
    2     "Args": [
    3         "-c",
    4         "/usr/local/bin/confd-watch.sh"
    5     ],
    6     "Config": {
   10         "Hostname": "3c012df7bab9",
   11         "Image": "andersjanmyr/nginx-confd:development",
   12     },
   13     "Id": "3c012df7bab977a194199f1",
   14     "Image": "d3bd1f07cae1bd624e2e",
   15     "NetworkSettings": {
   16         "IPAddress": "",
   18         "Ports": null
   19     },
   20     "Volumes": {},
   22 }]

Tips and Tricks

To get the id of a container is useful for scripting.

Get the id (-q) of the last (-l) run container
$ docker ps -l -q
c8044ab1a3d0

`docker inspect` can take a format string, a Go template, and it allows you to be more specific about what data you are interested in. Again, useful for scripting.

$ docker inspect -f '{{ .NetworkSettings.IPAddress }}' 6f2c42c05500

172.17.0.11

Use `docker exec` to interact with a running container.

Get the environment variables of a running container.
$ docker exec -it 6f2c42c05500 env

PATH=/usr/local/sbin:/usr...
HOSTNAME=6f2c42c05500
REDIS_1_PORT=tcp://172.17.0.9:6379
REDIS_1_PORT_6379_TCP=tcp://172.17.0.9:6379
...

Use volumes to avoid having to rebuild an image every time you run it. Every time the below Dockerfile is built it copies the current directory into the container.

  1 FROM dockerfile/nodejs:latest
  2
  3 MAINTAINER Anders Janmyr "anders@janmyr.com"
  4 RUN apt-get update && \
  5   apt-get install zlib1g-dev && \
  6   npm install -g pm2 && \
  7   mkdir -p /srv/app
  8
  9 WORKDIR /srv/app
 10 COPY . /srv/app
 11
 12 CMD pm2 start app.js -x -i 1 && pm2 logs
 13

Build and run the image
$ docker build -t myapp .
$ docker run -it --rm myapp

To avoid the rebuild, build the image once and then mount the local directory when you run it.

$ docker run -it --rm -v $(PWD):/srv/app myapp




Getting started with swarm mode
https://docs.docker.com/engine/swarm/
Docker swarm is another orchestration tool aimed to manage a cluster of docker hosts. While docker-compose managers multiple Docker containers within one docker host, docker swarm manages multiple docker hosts managing multiple Docker containers. Unlike docker-compose and docker-machine, docker swarm is not a standalone orchestration software. Swarm mode is built in docker engine and is managed through Docker client.

Aditional reading:
 - limiting your docker container resources is [builtin](https://docs.docker.com/config/containers/resource_constraints/), see [sample](https://hostadvice.com/how-to/how-to-limit-a-docker-containers-resources-on-ubuntu-18-04/)
 - Automate building your images by:
	 - maven with spotify plugin([sample](https://www.surevine.com/building-docker-images-with-maven/))
	 - maven with fabric8 plugin([website](https://dmp.fabric8.io/))
	 - maven [exec plugin](https://www.mojohaus.org/exec-maven-plugin/usage.html)
	 - [spring-boot:build-image](https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1)
 - Manage remote docker with [docker-machine](https://docs.docker.com/machine/)

resources:

 - https://blog.jayway.com/2015/03/21/a-not-very-short-introduction-to-docker/
 - https://engineering.bitnami.com/articles/best-practices-writing-a-dockerfile.html

___
to review: 
 - https://github.com/jwilder/dockerize

___




https://gitlab.com/msangel/webhook/-/blob/brbrbr/Dockerfile


where to use (docker hosting):
???

Summary
Docker is here to stay.
It fixes dependency hell.
Containers are fast!
Cluster solutions exists, but don't expect them to be seamless, yet!


volumes:
add image here
Volumes provide persistent storage outside the container. That means the data will not be saved if you commit the new image.

Start a new nginx container with /var/log as a volume
$ docker run  -v /var/log nginx

Since the directory of the host is not given, the volume is created in
/var/lib/docker/volumes/ec3c543bc..535.

The exact name of the directory can be found by running docker inspect container-id.

Start a new nginx container with /var/log as a volume mapped to /tmp on Host
$ docker run -v /tmp:/var/log nginx

It is also possible to mount volumes from another container with --volumes-from.

Start a db container
$ docker run -v /var/lib/postgresql/data --name mydb postgres

Start a backup container with the volumes taken from the mydb container
$ docker run --volumes-from mydb backup



Keeping state. Volumes.
map folders:
docker run -d -v /folder-on-host-machine/data/db:/data/db — net=myTestNetwork mongo

mount volumes from another container
Start a db container
$ docker run -v /var/lib/postgresql/data --name mydb postgres

Start a backup container with the volumes taken from the mydb container
$ docker run --volumes-from mydb backup
> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTI5Mjc5OTAzOSwtMzA1NTEyNDE3LDk3Nj
c3NDM3MSwtNzcyOTI4OTQzLC0xNTE1NjA0OTIxLC0xNzg2MDE4
Mzc4LDY2MDMyNzIwMCwtMjk4NTAxNjY0LDY2MjcxMDA1MSwxMT
g3OTc0MDU4LDUyMjA2NTI2MywxMjY5NDU4NTIzLDIxMjEzNDk5
MjgsLTEyNDM4OTg4NDQsODQ1MzQ4MDg2LDE3ODU5NTMzMzgsMj
A2MDI0MTU3LC0yMDQ1ODEzMDgwLDExOTY1NjA5OTEsODY0NDYw
MTg0XX0=
-->