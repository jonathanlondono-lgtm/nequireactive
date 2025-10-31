variable "secret_name" {
  description = "AWS Secrets Manager"
  type        = string
}

variable "db_username" {
  description = "Usuario de la base"
  type        = string
}

variable "password_length" {
  description = "Longitud de la contraseña generada automáticamente"
  type        = number
  default     = 16
}
