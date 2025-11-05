output "log_group_name" {
  value       = aws_cloudwatch_log_group.app_logs.name
  description = "Nombre del log group de CloudWatch"
}

output "log_group_arn" {
  value       = aws_cloudwatch_log_group.app_logs.arn
  description = "ARN del log group de CloudWatch"
}
# CloudWatch Log Group para la aplicación
resource "aws_cloudwatch_log_group" "app_logs" {
  name              = "/ecs/${var.project_name}-app"
  retention_in_days = var.log_retention_days

  tags = {
    Name        = "${var.project_name}-app-logs"
    Environment = var.environment
  }
}

# CloudWatch Log Stream (opcional, ECS lo crea automáticamente)
resource "aws_cloudwatch_log_stream" "app_log_stream" {
  name           = "${var.project_name}-log-stream"
  log_group_name = aws_cloudwatch_log_group.app_logs.name
}

