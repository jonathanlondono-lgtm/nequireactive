variable "vpc_name" {
  type        = string
  description = "Nombre de la VPC"
}

variable "vpc_cidr" {
  type        = string
  description = "Bloque CIDR para la VPC"
}

variable "public_subnets" {
  type        = list(string)
  description = "CIDRs de las subnets públicas"
}

variable "private_subnets" {
  type        = list(string)
  description = "CIDRs de las subnets privadas"
}

variable "azs" {
  type        = list(string)
  description = "Zonas de disponibilidad"
}
