provider "aws" {
  region  = "us-east-1"
  profile = "terraform"
}

module "db_secrets" {
  source          = "./secrets"
  secret_name     = "mi-db-secret-terraform"
  db_username     = var.db_username
  password_length = 16
}

module "vpc" {
  source          = "./modules/vpc"
  vpc_name        = "retroactivo-vpc"
  vpc_cidr        = "10.0.0.0/16"
  public_subnets  = ["10.0.1.0/24", "10.0.2.0/24"]
  private_subnets = ["10.0.3.0/24", "10.0.4.0/24"]
  azs             = ["us-east-1a", "us-east-1b"]
}
module "rds" {
  source             = "./modules/rds"
  db_name            = "retroactivodb"
  db_username        = local.db_username
  db_password        = local.db_password
  vpc_id             = module.vpc.vpc_id
  private_subnet_ids = module.vpc.private_subnets_ids
}

data "aws_secretsmanager_secret" "db_secret" {
  name = "mi-db-secret-terraform"
}

data "aws_secretsmanager_secret_version" "db_secret_version" {
  secret_id = data.aws_secretsmanager_secret.db_secret.id
}

locals {
  # Decodifica el JSON almacenado en SecretString
  db_credentials = jsondecode(data.aws_secretsmanager_secret_version.db_secret_version.secret_string)
  db_username    = local.db_credentials["username"]
  db_password    = local.db_credentials["password"]
}
