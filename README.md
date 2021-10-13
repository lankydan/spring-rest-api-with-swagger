# Spring REST API documented with `springdoc-openapi`

The accompanying blog post for this repo can be found here - [https://lankydan.dev/documenting-a-spring-rest-api-following-the-openapi-specification](https://lankydan.dev/documenting-a-spring-rest-api-following-the-openapi-specification).

To run the application:

- Create a Postgres container:

    ```shell
    docker run --name postgres -e POSTGRES_PASSWORD=admin -e POSTGRES_USER=admin -p 5432:5432 -d postgres
    ```

- Run the application, from the project's root directory:

    ```shell
    mvn spring-boot:run
    ```

This project is using Java 17 so if you see errors trying to run the application due to Java versions, you can either:

- Upgrade your maven installation.
- Change the Java version of this project to one that matches your Java version. You should be ok to downgrade this project to 11 and possibly 8 without issues.