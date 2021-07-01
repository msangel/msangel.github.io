---
title: Docker Compose.md
date: 2021-07-01 22:27:26 Z
---

Getting started with docker-compose
## Orchestration

When we split up our application into multiple different containers we get some new problems. How do we make the different parts talk to each other? On a single host? On multiple hosts?

Docker solves the problem of orchestration with on single host with links.

To simplify the linking of containers Docker provides a tool called `docker-compose`. It was previously called `fig` and was developed by another company which was recently acquired by Docker.

### docker-compose

[![fig](https://blog.jayway.com/wp-content/uploads/2015/03/fig-150x150.png)](http://blog.jayway.com/wp-content/uploads/2015/03/fig.png)

`docker-compose` declares the information for multiple containers in a single file, `docker-compose.yml`. Here is an example of a file that manages two containers, web and redis.

  1 web:
  2   build: .
  3   command: python app.py
  4   ports:
  5    - "5000:5000"
  6   volumes:
  7    - .:/code
  8   links:
  9    - redis
 10 redis:
 11   image: redis

To start the above containers, you can run the command `docker-compose up`.

  1 $ docker-compose up
  2 Pulling image orchardup/redis...
  3 Building web...
  4 Starting figtest_redis_1...
  5 Starting figtest_web_1...
  6 redis_1 | [8] 02 Jan 18:43:35.576 # Server
  7 started, Redis version 2.8.3
  8 web_1   |  * Running on http://0.0.0.0:5000/

It is also possible to start the containers in detached mode with `docker-compose up -d` and I can find out what containers are running with `docker-compose ps`.

  1 $ docker-compose up -d
  2 Starting figtest_redis_1...
  3 Starting figtest_web_1...
  4 $ docker-compose ps
  5 Name              Command                    State   Ports
  6 ------------------------------------------------------------
  7 figtest_redis_1   /usr/local/bin/run         Up
  8 figtest_web_1     /bin/sh -c python app.py   Up      5000->5000

It is possible to run commands that work with a single container or commands that work with all containers at once.

  1 # Get env variables for web container
  2 $ docker-compose run web env

  3 # Scale to multiple containers
  4 $ docker-compose scale web=3 redis=2

  5 # Get logs for all containers
  6 $ docker-compose logs

As you can see from the above commands, scaling is supported. The application must be written in a way that can handle multiple containers. Load-balancing is not supported out of the box.

##
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


> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE3NDU4MjQ3MThdfQ==
-->
