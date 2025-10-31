resource "aws_db_subnet_group" "this" {
  name       = "${var.db_name}-subnet-group"
  subnet_ids = var.private_subnet_ids

  tags = {
    Name = "${var.db_name}-subnet-group"
  }
}

resource "aws_security_group" "rds_sg" {
  name        = "${var.db_name}-sg"
  description = "Permite acceso al RDS desde la app"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "${var.db_name}-sg"
  }
}

resource "aws_db_instance" "this" {
  identifier             = var.db_name
  engine                 = "postgres"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.this.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  skip_final_snapshot    = true
  publicly_accessible    = false
  deletion_protection    = false

  tags = {
    Name = var.db_name
  }
}
