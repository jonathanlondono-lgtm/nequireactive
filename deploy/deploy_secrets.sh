#!/bin/bash

# 🚀 Script para desplegar secretos de base de datos
# Buenas prácticas: sin -target y con variables dinámicas

read -p "Ingresa el usuario de la base de datos: " DB_USER

# Inicializa el entorno
terraform init -input=false

# Valida la configuración
terraform validate

# Planea los cambios
terraform plan -var="db_username=$DB_USER" -out=tfplan

# Aplica los cambios
terraform apply "tfplan"
