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
  private_subnet_ids = module.vpc.private_subnet_ids
}

module "ecr" {
  source = "./modules/ecr"

  repository_name = "${var.project_name}-app"

  tags = {
    Name        = "${var.project_name}-ecr"
    Environment = var.environment
    ManagedBy   = "Terraform"
  }
}

module "alb" {
  source = "./modules/alb"

  project_name       = var.project_name
  vpc_id             = module.vpc.vpc_id
  public_subnet_ids  = module.vpc.public_subnet_ids
}

module "iam" {
  source = "./modules/iam"

  project_name  = var.project_name
  db_secret_arn = module.db_secrets.secret_arn
}

module "cloudwatch" {
  source = "./modules/cloudwatch"

  project_name       = var.project_name
  environment        = var.environment
  log_retention_days = 7
}

module "ecs" {
  source = "./modules/ecs"

  project_name        = var.project_name
  environment         = var.environment
  vpc_id              = module.vpc.vpc_id
  private_subnet_ids  = module.vpc.private_subnet_ids
  alb_sg_id           = module.alb.alb_sg_id
  target_group_arn    = module.alb.target_group_arn
  alb_listener_arn    = module.alb.alb_listener_arn
  execution_role_arn  = module.iam.ecs_task_execution_role_arn
  task_role_arn       = module.iam.ecs_task_role_arn
  ecr_repository_url  = module.ecr.repository_url
  db_endpoint         = module.rds.rds_endpoint
  db_secret_arn       = module.db_secrets.secret_arn
  log_group_name      = module.cloudwatch.log_group_name
  aws_region          = "us-east-1"
  task_cpu            = "256"
  task_memory         = "512"
  desired_count       = 1

  depends_on = [module.alb, module.rds]
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
