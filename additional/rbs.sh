#!/bin/bash

readonly SUCCESS_MESSAGE_COLOUR=$'\e[1;32m'
readonly ERROR_MESSAGE_COLOUR=$'\e[1;31m'
readonly MAIN_MESSAGE_COLOUR=$'\e[1;33m'
readonly STOCK_COLOUR=$'\e[0m'

readonly TESTDATA=testdata
readonly TEST=test

readonly MARKER=$1
readonly PROJECT_DIRECTORY=$(pwd)
readonly MVN=$PROJECT_DIRECTORY/./mvnw

function fail_check(){
  if [[ "$?" -ne 0 ]]; then
    message $ERROR_MESSAGE_COLOUR "ACHTUNG!"
    exit 1
  fi
}

function message() {
  printf "\n%s" $1
  echo "$2"
  printf "%s\n" $STOCK_COLOUR
}

function execute_command() {
  $@
  fail_check
}

function build_db() {
  clear
  cd $PROJECT_DIRECTORY/db
  message $MAIN_MESSAGE_COLOUR "Building main schema"
  execute_command $MVN clean install -Pclean-main-schema,migrate-main-schema
}

function migrate_testdata() {
  clear
  cd $PROJECT_DIRECTORY/db
  message $MAIN_MESSAGE_COLOUR "Migrating testdata in main schema"
  execute_command $MVN clean install -Pmigrate-main-schema-testdata
}

function build_app() {
  clear
  cd $PROJECT_DIRECTORY/app
  message $MAIN_MESSAGE_COLOUR "Building application"
  execute_command $MVN clean install -DskipTests
}

function build_with_tests() {
  clear
  cd $PROJECT_DIRECTORY/app
  message $MAIN_MESSAGE_COLOUR "Building application with tests"
  execute_command $MVN clean verify -Pclean-main-schema,migrate-main-schema
}

function main() {
  build_db
  build_app

  if [[ $TESTDATA = $MARKER ]]; then
    migrate_testdata
  fi

  if [[ $TEST = $MARKER ]]; then
    build_with_tests
  fi

  message $SUCCESS_MESSAGE_COLOUR "Project built successfully"
  exit 0
}

main