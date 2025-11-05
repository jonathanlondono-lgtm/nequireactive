variable "db_username" {
  description = "Usuario de la base"
  type        = string
  default     = "dummy"

}

variable "project_name" {
  description = "Nequi"
  type        = string
  default     = "retroactivo"
}

variable "environment" {
  description = "Ambiente de despliegue "
  type        = string
  default     = "dev"
}