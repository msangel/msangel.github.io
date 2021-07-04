##   
Docker Registries

Docker Hub is the official repository for images. It supports public (free) and private (fee) repositories. Repositories can be tagged as _official_ and this means that they are curated by the maintainers of the project (or someone connected with it).

Docker Hub also supports automatic builds of projects hosted on Github and Bitbucket. If automatic build is enabled an image will automatically be built every time you push to your source code repository.

If you don't want to use automatic builds, you can also `docker push` directly to Docker Hub. `docker pull` will pull images. `docker run` with an image that does not exist locally will automatically initiate a `docker pull`.

It is also possible to host your images elsewhere. Docker maintains code for [docker-registry](https://github.com/docker/docker-registry) on Github. But, I have found it to be slow and buggy.

Quay, Tutum, and Google also provides hosting of private docker images.
<!--stackedit_data:
eyJoaXN0b3J5IjpbMTMzNTI5NDQwXX0=
-->