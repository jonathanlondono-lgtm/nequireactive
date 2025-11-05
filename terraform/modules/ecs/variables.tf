variable "project_name" {
  type        = string
  description = "Nombre del proyecto"
}

variable "environment" {
  type        = string
  description = "Ambiente de despliegue"
}

variable "vpc_id" {
  type        = string
  description = "ID de la VPC"
}

variable "private_subnet_ids" {
  type        = list(string)
  description = "IDs de las subnets privadas"
}

variable "alb_sg_id" {
  type        = string
  description = "ID del security group del ALB"
}

variable "target_group_arn" {
  type        = string
  description = "ARN del target group del ALB"
}

variable "alb_listener_arn" {
  type        = string
  description = "ARN del listener del ALB"
}

variable "execution_role_arn" {
  type        = string
  description = "ARN del rol de ejecución de ECS"
}

variable "task_role_arn" {
  type        = string
  description = "ARN del rol de tarea de ECS"
}

variable "ecr_repository_url" {
  type        = string
  description = "URL del repositorio ECR"
}

variable "db_endpoint" {
  type        = string
  description = "Endpoint de la base de datos"
}

variable "db_secret_arn" {
  type        = string
  description = "ARN del secreto de la base de datos"
}

variable "log_group_name" {
  type        = string
  description = "Nombre del log group de CloudWatch"
}

variable "aws_region" {
  type        = string
  description = "Región de AWS"
  default     = "us-east-1"
}

variable "task_cpu" {
  type        = string
  description = "CPU para la tarea de Fargate"
  default     = "256"
}

variable "task_memory" {
  type        = string
  description = "Memoria para la tarea de Fargate"
  default     = "512"
}

variable "desired_count" {
  type        = number
  description = "Número deseado de tareas"
  default     = 1
}

