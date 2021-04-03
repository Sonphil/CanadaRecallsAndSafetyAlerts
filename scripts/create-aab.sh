#!/usr/bin/env bash

set -euo pipefail

readonly working_directory="$(git rev-parse --show-toplevel)"

cd "$working_directory"

github::debug() {
    echo "::debug:: $@"
}

github::set_output() {
    echo "::set-output name=$1::$2"
}

./gradlew bundleRelease

readonly aab_path="$(find app/build/outputs -name '*.aab' | head -1)"

if [[ -f "$aab_path" ]]; then
    github::debug "found an aab file at $aab_path"
    github::set_output "aab_path" "$aab_path"
else
    github::debug "could not find an aab file"
fi

readonly mapping_file_path="$(find app/build/outputs -name 'mapping.txt' | head -1)"

if [[ -f "$mapping_file_path" ]]; then
    github::debug "found an mapping file at $mapping_file_path"
    github::set_output "mapping_file_path" "$mapping_file_path"
else
    github::debug "could not find an mapping file"
fi