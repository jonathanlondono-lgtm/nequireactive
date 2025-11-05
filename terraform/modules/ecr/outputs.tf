output "repository_url" {
  description = "URL del repositorio ECR"
  value       = aws_ecr_repository.app.repository_url
}

output "repository_arn" {
  description = "ARN del repositorio ECR"
  value       = aws_ecr_repository.app.arn
}

output "repository_name" {
  description = "Nombre del repositorio ECR"
  value       = aws_ecr_repository.app.name
}

output "registry_id" {
  description = "Registry ID"
  value       = aws_ecr_repository.app.registry_id
}