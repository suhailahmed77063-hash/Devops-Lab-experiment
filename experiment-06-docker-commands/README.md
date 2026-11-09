# Experiment 6 — Docker commands for content management

**Aim:** Explore Docker commands used to manage containers, images and content.

**Tool:** Docker Engine / Docker Desktop 24+

## Container lifecycle

```bash
docker run --name mycontainer -it ubuntu:16.04 /bin/bash   # create + start + attach
docker run -d --name web -p 8080:80 nginx:alpine           # detached, port published
docker start mycontainer                                   # start a stopped container
docker stop mycontainer                                    # graceful stop (SIGTERM)
docker restart mycontainer
docker kill mycontainer                                    # immediate stop (SIGKILL)
docker rm mycontainer                                      # remove a stopped container
docker rm -f mycontainer                                   # force remove a running one
docker rename mycontainer ubuntu-shell                     # rename
```

`docker run --name mycontainer -it ubuntu:16.04 /bin/bash` opens an interactive
shell inside the container; `exit` returns to the host and stops the container.

## Inspecting containers

```bash
docker ps                    # running containers
docker ps -a                 # all containers, including stopped
docker ps -q                 # ids only
docker inspect mycontainer   # full JSON: IP, mounts, env, state
docker logs mycontainer      # stdout/stderr (add -f to follow)
docker stats                 # live CPU / memory / network usage
docker top mycontainer       # processes inside the container
docker port web              # published port mapping
docker diff mycontainer      # filesystem changes since start
```

## Images

```bash
docker images                          # local images
docker pull ubuntu:16.04               # download from Docker Hub
docker build -t myimage:1.0 .          # build from ./Dockerfile
docker tag myimage:1.0 myuser/myimage:1.0
docker push myuser/myimage:1.0         # publish to a registry
docker history myimage                 # layers and their sizes
docker rmi myimage:1.0                 # delete an image
docker image prune -a                  # remove unused images
```

## Content in and out of a container

```bash
docker cp mycontainer:/etc/hostname ./hostname.txt   # container → host
docker cp ./app.py mycontainer:/app/app.py           # host → container
docker exec -it mycontainer /bin/bash                # shell in a running container
docker exec mycontainer cat /etc/os-release          # one-off command
docker commit mycontainer myimage:snapshot           # container → image
```

## Volumes and networks (persistent content)

```bash
docker volume create mydata
docker run -d --name db -v mydata:/var/lib/postgresql/data postgres:16
docker volume ls
docker volume inspect mydata
docker network create mynet
docker run -d --name app --network mynet nginx:alpine
docker network ls
docker network inspect mynet
```

## Housekeeping

```bash
docker system df          # disk usage by images / containers / volumes
docker system prune       # remove stopped containers, unused networks, dangling images
docker system prune -a    # also remove unused images
```

## Observed results

```console
$ docker run --name mycontainer -it ubuntu:16.04 /bin/bash
root@5f1c9a2b7d31:/# cat /etc/os-release | head -2
NAME="Ubuntu"
VERSION="16.04.7 LTS (Xenial Xerus)"

$ docker ps -a
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS                     NAMES
5f1c9a2b7d31   ubuntu:16.04   "/bin/bash"   2 minutes ago   Exited (0) 10 seconds ago  mycontainer

$ docker start mycontainer
mycontainer

$ docker ps
CONTAINER ID   IMAGE          COMMAND       CREATED         STATUS         PORTS   NAMES
5f1c9a2b7d31   ubuntu:16.04   "/bin/bash"   2 minutes ago   Up 3 seconds           mycontainer

$ docker stop mycontainer
mycontainer

$ docker rm mycontainer
mycontainer
```

## Viva notes

* A **container** is a running instance of an image; an **image** is the read-only
  template built from `Dockerfile` layers.
* Containers are ephemeral: deleting a container deletes its writable layer, so
  persistent data belongs in a **volume** or a bind mount.
* `docker run -p host:container` publishes a port; `-v host:container` mounts data.
* `docker rm` only works on stopped containers unless `-f` is used.
