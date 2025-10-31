#!/bin/bash
set -e

echo "🚀 Desplegando módulo RDS en AWS..."

cd "$(dirname "$0")/../terraform"

terraform init -reconfigure
terraform validate

echo "📦 Creando base de datos RDS..."
terraform plan -target=module.rds
terraform apply -target=module.rds -auto-approve

echo "✅ Base de datos RDS creada exitosamente."
terraform output
