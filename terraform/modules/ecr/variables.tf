variable "repository_name" {
  description = "Nombre del repositorio ECR"
  type        = string
}

variable "tags" {
  description = "Tags para el repositorio"
  type        = map(string)
  default     = {}
}