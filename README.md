#### Preparation stuff
`docker run --name postgresdb -p 5432:5432 -e POSTGRES_PASSWORD=quattro -d postgres`

`mvn clean verify -Pclean-main-schema,migrate-main-schema`

#
#### Migrate testdata
`cd db && mvn install -Pmigrate-main-schema-testdata`

#
#### Run application
`mvn spring-boot:run`

#
#### Build script
Go to the `additional` directory and type:
#####Run build with basic required features `./rbs.sh` 

#####Run build with tests `./rbs.sh test`

#####Run build with testdata migration `./rbs.sh testdata`