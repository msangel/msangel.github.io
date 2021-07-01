---
title: Docker Practice In 5 Minutes.md
date: 2021-07-01 11:01:04 Z
---

todo: revrite fully:

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

docker run -it --rm ubuntu
--interactive (-i) – send stdin to the process.
-tty (-t) – tell the process that a terminal(TeleTYpe) is present. This affects how the process outputs data and how it treats signals such as (Ctrl-C).
--rm – remove the container on exit.

docker run -d hadoop
--detached (-d) – Run in detached mode, you can attach again with docker
attach

Run a named container and pass it some environment variables
$ docker run \
  --name mydb \
  --env MYSQL_USER=db-user \
  -e MYSQL_PASSWORD=secret \
  --env-file ./mysql.env \
  mysql

publish port:
Publish container port 80 on a random port on the Host
$ docker run -p 80 nginx
 
Publish container port 80 on port 8080 on the Host
$ docker run -p 8080:80 nginx
 
Publish container port 80 on port 8080 on the localhost interface on the Host
$ docker run -p 127.0.0.1:8080:80 nginx
 
Publish all EXPOSEd ports from the container on random ports on the Host
$ docker run -P nginx

limits:
docker run --cpus=1.5 -cpuset-cpus=0,1 --cpu-shares 512 -m 256m -u=www nginx
 Run a shell inside the container with id 6f2c42c0
$ docker exec -it 6f2c42c0 sh
Networking in docker
create:
docker network create <some name>
list:
docker network list
run container with a network:
docker run -d --net <networkName>

linking:
Start a postgres container, named mydb
$ docker run --name mydb postgres

Link mydb as db into myqpp
$ docker run --link mydb:db myapp



Keeping state. Volumes.
map folders:
docker run -d -v /folder-on-host-machine/data/db:/data/db — net=myTestNetwork mongo

mount volumes from another container
Start a db container
$ docker run -v /var/lib/postgresql/data --name mydb postgres

Start a backup container with the volumes taken from the mydb container
$ docker run --volumes-from mydb backup

Docker registry (add image here)

security:
Use trusted images from your private repositories.
Don't run containers as root, if possible.
Treat root in a container as root outside a container

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



Getting started with docker-machine
https://docs.docker.com/machine/reference/
Docker machine is an orchestration tool that allows you to manage multiple docker hosts. It lets you provision multiple virtual docker hosts locally, or on the cloud, and manage them with docker-machine commands. You can start, restart, and inspect managed hosts. You can point docker client to one of the hosts and then manage daemon on that host directly
`docker-machine ip`
`docker-machine env msangel-host`

Getting started with docker-compose
https://docs.docker.com/compose/reference/overview/
Docker compose is also an orchestration tool for docker. It allows you to easily manage multiple containers dependent on each other within one docker host via docker-compose CLI. You use a YAML file to configure all the containers. With one command you can start all containers in the correct order and set up networking between them.
Very nice complex and working sample: [spring-petclinic-microservices](https://github.com/spring-petclinic/spring-petclinic-microservices)
just clone the repository and run:
```shell
docker-compose up
```
docker-compose
docker-compose.yml
web:
  build: .
  command: python app.py
  ports:
    - "5000:5000"
  volumes:
    - .:/code
  links:
    - redis
redis:
  image: redis

>docker-compose up
>docker-compose up -d
>docker-compose ps
>docker-compose scale

Getting started with swarm mode
https://docs.docker.com/engine/swarm/
Docker swarm is another orchestration tool aimed to manage a cluster of docker hosts. While docker-compose managers multiple Docker containers within one docker host, docker swarm manages multiple docker hosts managing multiple Docker containers. Unlike docker-compose and docker-machine, docker swarm is not a standalone orchestration software. Swarm mode is built in docker engine and is managed through Docker client.


https://engineering.bitnami.com/articles/best-practices-writing-a-dockerfile.html
https://github.com/jwilder/dockerize
https://www.kevinkuszyk.com/2016/11/28/connect-your-docker-client-to-a-remote-docker-host/
https://www.freecodecamp.org/news/a-beginners-guide-to-docker-how-to-create-your-first-docker-application-cc03de9b639f/
https://spring.io/guides/gs/spring-boot-docker/
https://github.com/spring-guides/gs-spring-boot-docker/blob/main/complete/pom.xml
https://hostadvice.com/how-to/how-to-limit-a-docker-containers-resources-on-ubuntu-18-04/
https://docs.docker.com/config/daemon/
https://www.surevine.com/building-docker-images-with-maven/
https://gitlab.com/msangel/webhook/-/blob/brbrbr/Dockerfile
https://codefresh.io/howtos/using-docker-maven-maven-docker/
https://www.mojohaus.org/exec-maven-plugin/usage.html
https://stackoverflow.com/questions/3491937/i-want-to-execute-shell-commands-from-mavens-pom-xml

> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTE5NjU2MDk5MSw4NjQ0NjAxODQsMzgzND
Q4MDgsLTM2MDk0MTk3OSwtNDUyMzkyOTMwLC0xODQ1MDU4NTU0
LDc1MTcyNzQwMywtMTY0NDEwMzcwMF19
-->