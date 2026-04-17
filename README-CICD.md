# CI/CD Deployment Guide (Jenkins + ArgoCD + Kubernetes)

This repository now includes a GitOps-style CI/CD flow:

1. **Jenkins** runs tests, builds container images, pushes them to a registry, and updates image tags in GitOps manifests.
2. **ArgoCD** watches this repository and syncs Kubernetes resources to the cluster.

## Files added

- `Jenkinsfile`
- `deploy/argocd/bis-platform-application.yaml`
- `deploy/k8s/kustomization.yaml`
- `deploy/k8s/namespace.yaml`
- `deploy/k8s/configmap.yaml`
- `deploy/k8s/secret.yaml`
- `deploy/k8s/services.yaml`

## Prerequisites

- Kubernetes cluster
- ArgoCD installed in cluster
- Jenkins with:
  - Container build runtime for `spring-boot:build-image` (host Docker socket, DinD, or a Kubernetes pod template with Docker/Buildpacks support)
  - Maven + JDK 17
  - SCM access to this repository
- Container registry access (example uses GHCR)

## Configure placeholders

Before using the pipeline, update these placeholders:

- In Jenkins build parameters: set `DOCKER_REGISTRY` (example `ghcr.io/my-org`)
- In `deploy/k8s/kustomization.yaml`: all `newName: ghcr.io/YOUR_ORG/...`
- In `deploy/argocd/bis-platform-application.yaml`:
  - `repoURL`
  - `targetRevision` (branch used for GitOps)

Also replace placeholder credentials in `deploy/k8s/secret.yaml`.
For production, replace this Secret with a secure solution (for example Sealed Secrets, External Secrets Operator, or Vault integration).

## Jenkins credentials required

Create these Jenkins credentials:

1. `docker-registry-creds` (username/password)
   - Used to publish images.
2. `gitops-push-creds` (username/password or PAT)
   - Used by Jenkins to commit and push `deploy/k8s/kustomization.yaml` image tag updates.

## Deployment flow

### 1) Register Jenkins job

- Create a Multibranch Pipeline or Pipeline job pointing to this repo.
- Ensure it reads the root `Jenkinsfile`.

### 2) Create ArgoCD application

Apply:

```bash
kubectl apply -f deploy/argocd/bis-platform-application.yaml
```

ArgoCD will watch `deploy/k8s` and sync resources into namespace `bis-platform`.

### 3) Trigger CI/CD

- Push a commit to the tracked branch.
- Jenkins pipeline will:
  - Run `mvn clean test`
  - Validate that required placeholders are replaced (`DOCKER_REGISTRY`, `YOUR_ORG`, secret placeholders)
  - Build/push images for all backend services
  - Update all `newTag` values in `deploy/k8s/kustomization.yaml` to `${BUILD_NUMBER}`
  - Push the updated manifest commit
- ArgoCD detects the manifest update and deploys the new image tags.

## Verify deployment

```bash
kubectl get pods -n bis-platform
kubectl get svc -n bis-platform
argocd app get bis-platform
```

## Notes

- This setup deploys backend services only (`bis-*service` modules in the Maven reactor).
- Base Deployment manifests intentionally use `INVALID_TAG`; Kustomize image overrides must supply real tags.
- Database, Keycloak, and Kafka endpoints are configured via ConfigMap/Secret and should be adjusted for your environment.
