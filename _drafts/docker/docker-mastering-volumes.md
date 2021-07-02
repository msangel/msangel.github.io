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


> Written with [StackEdit](https://stackedit.io/).
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTM2NDQ0NDc5Nl19
-->