resource "random_password" "db_password" {
  length           = var.password_length
  special          = true
  override_special = "_%@"
}

resource "aws_secretsmanager_secret" "db_secret" {
  name        = var.secret_name
  description = "Credenciales del usuario de base de datos RDS"
}

resource "aws_secretsmanager_secret_version" "db_secret_version" {
  secret_id = aws_secretsmanager_secret.db_secret.id
  secret_string = jsonencode({
    username = var.db_username
    password = random_password.db_password.result
  })
}
