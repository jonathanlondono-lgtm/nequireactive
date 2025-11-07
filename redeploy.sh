#!/bin/bash

# Script para reconstruir y redesplegar la aplicación en AWS ECS
set -e

echo "🔨 Construyendo la aplicación..."
./gradlew clean bootJar -x test

echo "📦 Copiando JAR al directorio de deployment..."
cp applications/app-service/build/libs/retoreactivo.jar deployment/

echo "🐳 Construyendo imagen Docker..."
cd deployment
docker build -t retroactivo-app:latest .

echo "🏷️  Etiquetando imagen para ECR..."
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text --profile terraform)
AWS_REGION="us-east-1"
ECR_REPO="${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com/retroactivo-app"

docker tag retroactivo-app:latest ${ECR_REPO}:latest

echo "🔐 Autenticando con ECR..."
aws ecr get-login-password --region ${AWS_REGION} --profile terraform | docker login --username AWS --password-stdin ${ECR_REPO}

echo "⬆️  Subiendo imagen a ECR..."
docker push ${ECR_REPO}:latest

echo "🔄 Forzando nuevo despliegue en ECS..."
aws ecs update-service \
    --cluster retroactivo-cluster \
    --service retroactivo-service \
    --force-new-deployment \
    --region ${AWS_REGION} \
    --profile terraform

echo "✅ ¡Despliegue iniciado! Monitorea el progreso en la consola de AWS ECS"
echo "📊 Puedes ver los logs en CloudWatch Logs"
