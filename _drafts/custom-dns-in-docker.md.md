---
title: Custom Dns In Docker.md
date: 2021-07-01 11:18:21.582000000 Z
---

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
eyJoaXN0b3J5IjpbMTgyMTU2NDc2OV19
-->