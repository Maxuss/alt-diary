CREATE TABLE students (
                          id uuid PRIMARY KEY UNIQUE NOT NULL,
                          name varchar(127) NOT NULL,
                          surname varchar(128) NOT NULL,
                          email varchar(100) NOT NULL,
                          pass_hash varchar(64) NOT NULL
);

CREATE TABLE teachers (
                          id uuid PRIMARY KEY UNIQUE NOT NULL,
                          name varchar(127) NOT NULL,
                          surname varchar(127) NOT NULL,
                          patronymic varchar(127) NOT NULL,
                          email varchar(100) NOT NULL,
                          pass_hash varchar(64) NOT NULL
);
