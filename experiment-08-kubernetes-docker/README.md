# Experiment 8 — Integrate Kubernetes and Docker

**Aim:** Build an image with Docker and deploy/manage it with Kubernetes.

**Tools:** Docker, Minikube, kubectl

## Files

| File | Purpose |
|------|---------|
| `deployment.yaml` | 2 replicas of the Flask registration app |
| `service.yaml` | ClusterIP service on port 80 → container port 5000 |

## Steps

### 1. Build the image with Docker

```bash
# Start the cluster once
minikube start --driver=docker
kubectl get nodes

# Build the image inside Minikube's Docker daemon so the cluster can see it
eval $(minikube docker-env)             # PowerShell: & minikube -p minikube docker-env | Invoke-Expression
cd ../experiment-01-flask-docker
docker build -t myapp:1.0 .
cd ../experiment-08-kubernetes-docker
```

Alternatively push the image to a registry (`docker tag` + `docker push`) and set
`image:` in `deployment.yaml` to `docker.io/<user>/myapp:1.0`.

### 2. Deploy to Kubernetes

```bash
kubectl apply -f deployment.yaml
kubectl apply -f service.yaml

kubectl get deployments
kubectl get pods -l app=myapp
kubectl get services
kubectl describe deployment myapp
```

### 3. Reach the application

```bash
kubectl port-forward service/myapp-service 8080:80
# open http://localhost:8080/register

# or, with Minikube
minikube service myapp-service --url
```

### 4. Manage the containers

```bash
kubectl scale deployment myapp --replicas=4        # scaling
kubectl set image deployment/myapp myapp=myapp:1.1 # rolling update
kubectl rollout status deployment/myapp
kubectl rollout history deployment/myapp
kubectl rollout undo deployment/myapp              # rollback
kubectl logs -l app=myapp --tail=20
kubectl delete -f service.yaml -f deployment.yaml  # cleanup
```

## Observed result

```console
$ kubectl get pods -l app=myapp
NAME                     READY   STATUS    RESTARTS   AGE
myapp-6d9c8b7f5c-4xk2n   1/1     Running   0          45s
myapp-6d9c8b7f5c-q8m7p   1/1     Running   0          45s

$ kubectl get svc myapp-service
NAME            TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)   AGE
myapp-service   ClusterIP   10.96.142.211   <none>        80/TCP    50s
```

## Note on the manual's example

The manual deploys `image: myimage` (the Experiment 7 container) with
`containerPort: 80`. That container prints `Hello World` and exits immediately,
so a Deployment would restart it forever (`CrashLoopBackOff`) and it never
listens on a port. This folder therefore deploys the long-running Flask
registration app from Experiment 1, whose container listens on 5000; the service
still exposes it on port 80 exactly as the manual describes. The one-shot
Experiment 7 image is run as a Kubernetes **Job** in Experiment 9, which is the
correct resource for a program that terminates.

## Explanation

* Docker packages the application and its dependencies; Kubernetes schedules and
  manages the resulting containers across nodes.
* A **Deployment** keeps the desired number of replicas running and performs
  rolling updates; a **Service** gives the pods a stable virtual IP and DNS name.
* `imagePullPolicy: IfNotPresent` lets Minikube use the locally built image.
* `kubectl port-forward` and `minikube service` expose a ClusterIP service for
  testing without a cloud load balancer.
