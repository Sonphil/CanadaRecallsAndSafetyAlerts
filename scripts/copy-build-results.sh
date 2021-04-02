#!/usr/bin/env bash

github::set_output() {
    echo "::set-output name=$1::$2"
}

github::error() {
    echo "::error::$@"
}

github::failure() {
    exit 1
}

path="build-reports"
mkdir -p $path

for dir in $(find . -type d -regex ".*/build/reports")
do
  module=$(echo $dir | cut -d "/" -f 2)
  echo "Copying build reports for the $module module"
  source="$module/build/reports"

  if [[ ! -d "$source" ]]; then
    github::error "Source directory does not exist: $source"
    github::failure
  fi

  mkdir -p "$path/$module" && cp -r "$source" $_
done

github::set_output "path" "$path"