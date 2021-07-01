todo: revrite fully:



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
eyJoaXN0b3J5IjpbLTEwNzQ3NjE5MTAsNzUxNzI3NDAzLC0xNj
Q0MTAzNzAwXX0=
-->