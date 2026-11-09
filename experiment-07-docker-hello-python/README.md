# Experiment 7 — Simple containerized application using Docker

**Aim:** Develop a simple containerized application using Docker.

**Tools:** Python 3.9, Docker

## Files

| File | Purpose |
|------|---------|
| `hello.py` | Prints `Hello World` |
| `Dockerfile` | Base image, copy script, set `CMD` |

## Steps

```bash
# 1. Build the image
docker build -t myimage .

# 2. Run the container
docker run --name mycontainer myimage

# 3. Verify the output
docker logs mycontainer
```

## Observed output

```console
$ docker build -t myimage .
[+] Building 3.4s (7/7) FINISHED
 => [1/3] FROM docker.io/library/python:3.9-slim
 => [2/3] WORKDIR /app/
 => [3/3] COPY hello.py /app/
 => exporting to image

$ docker run --name mycontainer myimage
Hello World

$ docker logs mycontainer
Hello World

$ docker ps -a --filter name=mycontainer
CONTAINER ID   IMAGE     COMMAND            CREATED          STATUS                     NAMES
7c2b1f0e9a44   myimage   "python hello.py"  10 seconds ago   Exited (0) 8 seconds ago   mycontainer
```

## Cleanup

```bash
docker rm mycontainer
docker rmi myimage
```

## Explanation

* `FROM python:3.9-slim` provides the interpreter without build tooling, keeping
  the image small.
* `COPY hello.py /app/` bakes the script into the image, so the container is
  self-contained and needs no host files.
* `CMD ["python", "hello.py"]` uses exec form, so Python is PID 1 and receives
  signals directly.
* The container exits with status 0 after printing, which is why `docker ps -a`
  shows it as `Exited (0)` and `docker logs` is used to read the output.

This image is pushed and deployed to Kubernetes in Experiment 8 and Experiment 9.
