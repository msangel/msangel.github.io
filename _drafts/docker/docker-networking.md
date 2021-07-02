---
title: Custom Dns In Docker.md
date: 2021-07-01 11:18:21 Z
---




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
Linking a container sets up networking from the linking container into the linked container. It does two things:

It updates the /etc/hosts with the link name given to the container, db in the example above. Making it possible to access the container by the name db. This is very good.
It creates environment variables for the EXPOSEd ports. This is practically useless since I can access the same port by using a hostname:port combination anyway.
The linked networking is not constrained by the ports EXPOSEd by the image. All ports are available to the linking container.

Kubernetes solution:

  

Host alias config:

doc:

[https://kubernetes.io/docs/concepts/services-networking/add-entries-to-pod-etc-hosts-with-host-aliases/](https://kubernetes.io/docs/concepts/services-networking/add-entries-to-pod-etc-hosts-with-host-aliases/)

  

[https://stackoverflow.com/questions/56390226/how-to-add-extra-hosts-entries-in-helm-charts](https://stackoverflow.com/questions/56390226/how-to-add-extra-hosts-entries-in-helm-charts)

  

Docker solution:

google: docker host file records

-   setting custom domains inside dockerfile: [https://stackoverflow.com/questions/38302867/how-to-update-etc-hosts-file-in-docker-image-during-docker-build](https://stackoverflow.com/questions/38302867/how-to-update-etc-hosts-file-in-docker-image-during-docker-build)
-   setting dns for container when running docker: [https://forums.docker.com/t/dns-resolution-not-working-in-containers/36246](https://forums.docker.com/t/dns-resolution-not-working-in-containers/36246)
<!--stackedit_data:
eyJoaXN0b3J5IjpbNjg4NjcwODFdfQ==
-->