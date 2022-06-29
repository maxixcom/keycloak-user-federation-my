# My Keycloak User Federation

The project is intended to support users from my other system.

## Requirements

- Keycloak >= 18.0.0
- Postgresql

## Build

```shell
./gradlew build
```

## Install

Copy jar from `./build/libs` folder to `keycloak/providers` one.
Run in the root keycloak directory:

```shell
./bin/kc.sh build
```

After that you can run keycloak server.