#### Preparation stuff
`docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=quattro -d postgres`

`mvn clean verify -Pclean-main-schema,migrate-main-schema`

#
#### Run application
`mvn spring-boot:run`
