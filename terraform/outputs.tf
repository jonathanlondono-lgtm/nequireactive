output "ecr_repository_url" {
  description = "URL del repositorio ECR"
  value       = module.ecr.repository_url
}

output "ecr_repository_name" {
  description = "Nombre del repositorio ECR"
  value       = module.ecr.repository_name
}

output "alb_dns_name" {
  description = "DNS del Application Load Balancer"
  value       = module.alb.alb_dns_name
}

output "ecs_cluster_name" {
  description = "Nombre del cluster ECS"
  value       = module.ecs.cluster_name
}

output "ecs_service_name" {
  description = "Nombre del servicio ECS"
  value       = module.ecs.service_name
}

output "cloudwatch_log_group" {
  description = "Nombre del log group en CloudWatch"
  value       = module.cloudwatch.log_group_name
}

output "rds_endpoint" {
  description = "Endpoint de la base de datos RDS"
  value       = module.rds.rds_endpoint
}
