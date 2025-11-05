output "cluster_id" {
  value       = aws_ecs_cluster.main.id
  description = "ID del cluster ECS"
}

output "cluster_name" {
  value       = aws_ecs_cluster.main.name
  description = "Nombre del cluster ECS"
}

output "service_name" {
  value       = aws_ecs_service.app.name
  description = "Nombre del servicio ECS"
}

output "task_definition_arn" {
  value       = aws_ecs_task_definition.app.arn
  description = "ARN de la task definition"
}

output "ecs_tasks_sg_id" {
  value       = aws_security_group.ecs_tasks_sg.id
  description = "ID del security group de las tareas ECS"
}
