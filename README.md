# AQA Java homework project

Multi-module Gradle project for the course automation tests.

## Modules

- `common` — shared test configuration and Basic Java homework.
- `api-tests` — REST API builder, endpoint clients, API assertions, and endpoint tests.
- `ui-tests` — PageObjects, page assertions, Selenide listener, and UI tests.

All configurable URLs, endpoints, credentials, browser settings, product data, and messages are in `common/src/main/resources/test-config.properties`. System properties and matching environment variables override file values (for example `-Dapi.url=...` or `API_URL=...`). Credentials are excluded from printed configuration.

## Run

Start the study web application and API at the configured URLs, then run:

```shell
./gradlew smokeApi
./gradlew smokeUi
./gradlew smoke
./gradlew runAllTests
```

The smoke tasks run tests tagged `smoke` in their respective modules. UI tests use Chrome.

Generate and archive the Allure report after running the tests:

```shell
./gradlew allureReportArchive
```

The ZIP archive is written to `build/reports/allure-report.zip`.
