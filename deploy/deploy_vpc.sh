set -e

terraform init
terraform validate
terraform plan -target=module.vpc
terraform apply -target=module.vpc -auto-approve
