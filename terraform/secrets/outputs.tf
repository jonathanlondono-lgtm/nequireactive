output "secret_arn" {
  value       = aws_secretsmanager_secret.db_secret.arn
  description = "ARN del secreto creado"
  sensitive   = true
}
