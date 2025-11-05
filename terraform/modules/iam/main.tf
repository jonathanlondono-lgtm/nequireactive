# IAM Role para ECS Task Execution
resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.project_name}-ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "${var.project_name}-ecs-task-execution-role"
  }
}

# Adjuntar política para ECS Task Execution
resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Política adicional para Secrets Manager
resource "aws_iam_role_policy" "ecs_task_execution_secrets_policy" {
  name = "${var.project_name}-ecs-secrets-policy"
  role = aws_iam_role.ecs_task_execution_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ]
        Resource = var.db_secret_arn
      }
    ]
  })
}

# IAM Role para ECS Task (runtime)
resource "aws_iam_role" "ecs_task_role" {
  name = "${var.project_name}-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })

  tags = {
    Name = "${var.project_name}-ecs-task-role"
  }
}

# Política para permitir logs en CloudWatch
resource "aws_iam_role_policy" "ecs_task_cloudwatch_policy" {
  name = "${var.project_name}-ecs-cloudwatch-policy"
  role = aws_iam_role.ecs_task_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ]
        Resource = "*"
      }
    ]
  })
}

# ============================================
# Usuario IAM para GitHub Actions Deployer
# ============================================

resource "aws_iam_user" "github_actions_deployer" {
  name = "github-actions-deployer"
  path = "/"

  tags = {
    Name        = "github-actions-deployer"
    Description = "Usuario para despliegues automatizados desde GitHub Actions"
  }
}

# Política para GitHub Actions Deployer
resource "aws_iam_user_policy" "github_actions_deployer_policy" {
  name = "github-actions-deployer-policy"
  user = aws_iam_user.github_actions_deployer.name

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      # Permisos para Secrets Manager
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue",
          "secretsmanager:DescribeSecret"
        ]
        Resource = var.db_secret_arn
      },
      # Permisos para CloudWatch Logs
      {
        Effect = "Allow"
        Action = [
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents",
          "logs:DescribeLogStreams"
        ]
        Resource = [
          "arn:aws:logs:*:*:log-group:/ecs/${var.project_name}-app",
          "arn:aws:logs:*:*:log-group:/ecs/${var.project_name}-app:*"
        ]
      },
      # Permisos para ECR
      {
        Effect = "Allow"
        Action = [
          "ecr:GetAuthorizationToken",
          "ecr:BatchCheckLayerAvailability",
          "ecr:GetDownloadUrlForLayer",
          "ecr:BatchGetImage",
          "ecr:PutImage",
          "ecr:InitiateLayerUpload",
          "ecr:UploadLayerPart",
          "ecr:CompleteLayerUpload"
        ]
        Resource = "*"
      },
      # Permisos para ECS - Lectura de task definitions y servicios
      {
        Effect = "Allow"
        Action = [
          "ecs:DescribeTaskDefinition",
          "ecs:DescribeTasks",
          "ecs:DescribeServices",
          "ecs:DescribeClusters",
          "ecs:ListTasks",
          "ecs:ListServices",
          "ecs:ListTaskDefinitions"
        ]
        Resource = "*"
      },
      # Permisos para ECS - Registro y actualización de task definitions
      {
        Effect = "Allow"
        Action = [
          "ecs:RegisterTaskDefinition",
          "ecs:DeregisterTaskDefinition"
        ]
        Resource = "*"
      },
      # Permisos para ECS - Actualización de servicios
      {
        Effect = "Allow"
        Action = [
          "ecs:UpdateService",
          "ecs:UpdateServicePrimaryTaskSet"
        ]
        Resource = "arn:aws:ecs:*:*:service/${var.project_name}-cluster/*"
      },
      # Permisos para IAM - Pasar roles a ECS
      {
        Effect = "Allow"
        Action = [
          "iam:PassRole"
        ]
        Resource = [
          aws_iam_role.ecs_task_execution_role.arn,
          aws_iam_role.ecs_task_role.arn
        ]
        Condition = {
          StringEquals = {
            "iam:PassedToService" = "ecs-tasks.amazonaws.com"
          }
        }
      }
    ]
  })
}

