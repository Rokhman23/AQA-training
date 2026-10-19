# AQA Java homework project

Gradle project for the Basic Java, JUnit, and REST Assured homework.

- Production methods: `src/main/java/ru/aqa/BasicJavaTasks.java`
- JUnit 5 tests: `src/test/java/ru/aqa/BasicJavaTasksTest.java`
- `runAllTests` runs the project tests.
- `afterTests` runs them and then prints `Test run is over`.
- `apiTest` runs only REST Assured tests tagged `api` against the local study API at `http://localhost:8080`.

Start the study API from the supplied `Учебный стенд.zip` before running `apiTest`.
The API returns an empty product collection as `{"goods":[]}`, so the two GET-list
tests check that `goods` is empty.

Open this folder in IntelliJ IDEA and import it as a Gradle project.
