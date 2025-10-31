output "rds_endpoint" {
  description = "Endpoint del RDS"
  value       = aws_db_instance.this.address
}

output "rds_port" {
  description = "Puerto del RDS"
  value       = aws_db_instance.this.port
}

output "rds_db_name" {
  description = "Nombre de la base de datos"
  value       = aws_db_instance.this.db_name
}
