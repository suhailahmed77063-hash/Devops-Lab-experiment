# Experiment 9 — Automate the containerized application using Kubernetes

**Aim:** Automate running the containerized application from Experiment 7 with
Kubernetes.

**Tools:** Docker, Minikube, kubectl

## Files

| File | Purpose |
|------|---------|
| `job.yaml` | Runs the Experiment 7 `myimage` once — prints `Hello World` and completes |
| `deployment.yaml` | 3 replicas of the web application, rolling updates |
| `service.yaml` | NodePort service, port 80 → 5000, node port 30080 |
| `hpa.yaml` | HorizontalPodAutoscaler: 3–6 pods at 70 % CPU |

The Experiment 7 program prints one line and exits, so Kubernetes runs it as a
**Job** (a completed pod), while the long-running web application from
Experiment 1 is run as a **Deployment**. Both are applied in the same namespace,
which is how one manifest set automates a whole lab environment.

## Steps

### 1. Create the cluster

```bash
minikube start --driver=docker
kubectl cluster-info
kubectl get nodes
```

Or with a cloud cluster (GKE / EKS) point `kubectl` at it with
`gcloud container clusters get-credentials` / `aws eks update-kubeconfig`.

### 2. Build and make the images available

```bash
eval $(minikube docker-env)                       # use Minikube's daemon

cd ../experiment-07-docker-hello-python
docker build -t myimage .

cd ../experiment-01-flask-docker
docker build -t myapp:1.0 .

cd ../experiment-09-kubernetes-automation
```

With a cloud cluster, push instead:
`docker tag myimage <registry>/myimage && docker push <registry>/myimage`, then
update the `image:` fields.

### 3. Apply the manifests

```bash
kubectl apply -f job.yaml
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml
```

```bash
kubectl get jobs
kubectl logs job/hello-world-job          # → Hello World
kubectl get pods -l app=myapp
kubectl get svc myapp-service
```

### 4. Verify the deployment

```bash
kubectl rollout status deployment/myapp
kubectl describe deployment myapp

minikube service myapp-service --url      # prints the reachable URL
curl -s "$(minikube service myapp-service --url)/register" | head -5
```

### 5. Autoscaling (optional)

```bash
minikube addons enable metrics-server
kubectl apply -f hpa.yaml
kubectl get hpa myapp-hpa
```

### 6. Manage and update

```bash
kubectl scale deployment myapp --replicas=5
kubectl set image deployment/myapp myapp=myapp:1.1
kubectl rollout status deployment/myapp
kubectl rollout undo deployment/myapp
kubectl delete job hello-world-job
kubectl delete -f service.yaml -f deployment.yaml -f hpa.yaml
```

## Observed result

```console
$ kubectl get jobs
NAME              COMPLETIONS   DURATION   AGE
hello-world-job   1/1           4s         30s

$ kubectl logs job/hello-world-job
Hello World

$ kubectl get pods -l app=myapp
NAME                     READY   STATUS    RESTARTS   AGE
myapp-7f5b9c6d4b-2rj9k   1/1     Running   0          40s
myapp-7f5b9c6d4b-8m4vp   1/1     Running   0          40s
myapp-7f5b9c6d4b-x6lsn   1/1     Running   0          40s

$ kubectl get svc myapp-service
NAME            TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)        AGE
myapp-service   NodePort   10.101.83.14   <none>        80:30080/TCP   45s
```

## Automating it in a pipeline

The same commands are what a CI/CD stage runs after the image is pushed:

```bash
docker build -t myapp:$GIT_COMMIT .
docker push <registry>/myapp:$GIT_COMMIT
kubectl set image deployment/myapp myapp=<registry>/myapp:$GIT_COMMIT
kubectl rollout status deployment/myapp --timeout=120s
kubectl rollout undo deployment/myapp    # automatic rollback on failure
```

Jenkins can run this from the Experiment 5 pipeline by replacing the `Deploy`
stage with the four commands above.

## Viva notes

* **Docker** builds and runs a single container; **Kubernetes** orchestrates
  many containers: scheduling, scaling, self-healing and rolling updates.
* A **Job** runs pods to completion (batch work); a **Deployment** keeps pods
  running continuously.
* A **Service** provides stable networking in front of ephemeral pods.
* `kubectl rollout` is what makes updates safe: it replaces pods gradually and
  can roll back to the previous ReplicaSet.
