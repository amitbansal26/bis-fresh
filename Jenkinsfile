pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
  }

  environment {
    DOCKER_REGISTRY = 'ghcr.io/YOUR_ORG'
    IMAGE_TAG = "${BUILD_NUMBER}"
    GITOPS_FILE = 'deploy/k8s/kustomization.yaml'
    SERVICES = 'bis-identity-service bis-certification-service bis-laboratory-service bis-operations-service bis-integration-service bis-master-data-service'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Unit Tests') {
      steps {
        sh 'mvn -B clean test'
      }
    }

    stage('Build & Push Images') {
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'docker-registry-creds',
            usernameVariable: 'DOCKER_USER',
            passwordVariable: 'DOCKER_PASS'
          )
        ]) {
          sh '''#!/bin/bash
            set -euxo pipefail

            for service in ${SERVICES}; do
              mvn -B -pl ${service} -am spring-boot:build-image \
                -DskipTests \
                -Dspring-boot.build-image.imageName=${DOCKER_REGISTRY}/${service}:${IMAGE_TAG} \
                -Dspring-boot.build-image.publish=true \
                -Ddocker.publishRegistry.username=${DOCKER_USER} \
                -Ddocker.publishRegistry.password=${DOCKER_PASS}
            done
          '''
        }
      }
    }

    stage('Update GitOps Manifests') {
      steps {
        withCredentials([
          usernamePassword(
            credentialsId: 'gitops-push-creds',
            usernameVariable: 'GIT_USER',
            passwordVariable: 'GIT_TOKEN'
          )
        ]) {
          sh '''#!/bin/bash
            set -euxo pipefail

            git config user.name "jenkins-bot"
            git config user.email "jenkins-bot@local"

            awk -v tag="${IMAGE_TAG}" '
              /^images:/ {in_images=1}
              in_images && $1=="newTag:" {$2=tag}
              {print}
            ' ${GITOPS_FILE} > ${GITOPS_FILE}.tmp && mv ${GITOPS_FILE}.tmp ${GITOPS_FILE}

            git add ${GITOPS_FILE}
            if git diff --cached --quiet; then
              echo "No GitOps manifest updates detected"
              exit 0
            fi

            git commit -m "ci: update Kubernetes image tags to ${IMAGE_TAG}"

            REMOTE_URL=$(git config --get remote.origin.url)
            if [[ "${REMOTE_URL}" =~ ^git@github.com:(.+)/(.+)\\.git$ ]]; then
              PUSH_URL="https://${GIT_USER}:${GIT_TOKEN}@github.com/${BASH_REMATCH[1]}/${BASH_REMATCH[2]}.git"
            elif [[ "${REMOTE_URL}" =~ ^https://github.com/(.+)/(.+)\\.git$ ]]; then
              PUSH_URL="https://${GIT_USER}:${GIT_TOKEN}@github.com/${BASH_REMATCH[1]}/${BASH_REMATCH[2]}.git"
            else
              echo "Unsupported remote URL format: ${REMOTE_URL}"
              exit 1
            fi
            git push "${PUSH_URL}" HEAD:${BRANCH_NAME}
          '''
        }
      }
    }
  }

  post {
    success {
      echo 'CI completed. ArgoCD will reconcile deploy/k8s and roll out the new image tags.'
    }
  }
}
