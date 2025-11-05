variable "project_name" {
  type        = string
  description = "Nombre del proyecto"
}

variable "environment" {
  type        = string
  description = "Ambiente de despliegue"
  default     = "dev"
}

variable "log_retention_days" {
  type        = number
  description = "Días de retención de logs"
  default     = 7
}

