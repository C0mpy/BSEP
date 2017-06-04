CREATE TABLE IF NOT EXISTS bezbednost.users (
  email VARCHAR(125) NOT NULL,
  password VARCHAR(1000) NOT NULL,
  salt VARCHAR(1000) NULL,
  role VARCHAR(45) NULL,
  PRIMARY KEY (email, password),
  UNIQUE INDEX email_UNIQUE (email ASC),
  UNIQUE INDEX password_UNIQUE (password ASC)
) ENGINE=InnoDB