-- extension for uuid in Postgres
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE franchise (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE branch (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  franchise_id UUID NOT NULL REFERENCES franchise(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE product (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  branch_id UUID NOT NULL REFERENCES branch(id) ON DELETE CASCADE,
  name VARCHAR(255) NOT NULL,
  stock INTEGER NOT NULL CHECK (stock >= 0),
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE INDEX idx_branch_franchise ON branch(franchise_id);
CREATE INDEX idx_product_branch ON product(branch_id);
CREATE INDEX idx_product_name_branch ON product(branch_id, name);
