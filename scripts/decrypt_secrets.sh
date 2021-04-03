#!/usr/bin/env bash

github::error() {
    echo "::error::$@"
}

github::failure() {
    exit 1
}

if [[ -z "${GOOGLE_SERVICES_ENC_PW:-}" ]]; then
    github::error "GOOGLE_SERVICES_ENC_PW is required"
    github::failure
fi

if [[ -z "${KEYSTORE_ENC_PW:-}" ]]; then
    github::error "KEYSTORE_ENC_PW is required"
    github::failure
fi

if [[ -z "${GOOGLE_PLAY_SERVICE_ACCOUNT_ENC_PW:-}" ]]; then
    github::error "GOOGLE_PLAY_SERVICE_ACCOUNT_ENC_PW is required"
    github::failure
fi

readonly working_directory="$(git rev-parse --show-toplevel)"

cd "$working_directory"

pwd

openssl version
openssl aes-256-cbc -salt -a -d -k "$GOOGLE_SERVICES_ENC_PW" -in ./encrypted/google-services.json.enc -out ./app/google-services.json
openssl aes-256-cbc -salt -a -d -k "$KEYSTORE_ENC_PW" -in ./encrypted/sonphil_keystore.jks.enc -out ./sonphil_keystore.jks
openssl aes-256-cbc -salt -a -d -k "$KEYSTORE_ENC_PW" -in ./encrypted/keystore.properties.enc -out ./keystore.properties
openssl enc -d -aes-256-cbc -md sha512 -pbkdf2 -iter 100000 -salt -k "$GOOGLE_PLAY_SERVICE_ACCOUNT_ENC_PW" -in ./encrypted/google-play-service-account.json.enc -out ./google-play-service-account.json