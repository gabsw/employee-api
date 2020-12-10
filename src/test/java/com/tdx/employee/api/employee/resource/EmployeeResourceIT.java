package com.tdx.employee.api.employee.resource;


import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.entity.Title;
import com.tdx.employee.api.employee.model.EmployeeDTO;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.tdx.employee.api.employee.resource.EmployeeResource.RESOURCE_PATH;
import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

@QuarkusTest
@Transactional
public class EmployeeResourceIT {
    @Inject
    Jsonb jsonb;

    @Inject
    EntityManager entityManager;

    private final long existingId = 1L;
    private final long unknownId = 1000L;

    private final Employee persistedAgent2020 =
        new Employee("John",
                     LocalDate.of(2020, 2, 9),
                     "Blue",
                     Title.AGENT);

    private final Employee persistedSupervisor2019 =
        new Employee("Tim",
                     LocalDate.of(2019, 2, 9),
                     "Red",
                     Title.SUPERVISOR);

    private final Employee persistedSupervisor2020 =
        new Employee("Tim",
                     LocalDate.of(2020, 2, 9),
                     "Red",
                     Title.SUPERVISOR);

    @BeforeEach
    public void setup() {
        entityManager.createNativeQuery("TRUNCATE employee").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE employee_id_seq RESTART WITH 1").executeUpdate();
        entityManager.persist(persistedAgent2020);
        entityManager.persist(persistedSupervisor2019);
        entityManager.persist(persistedSupervisor2020);
        entityManager.flush();
    }

    @Test
    void givenExistingId_afterRead_returnCorrespondingEmployee() {
        final String path = RESOURCE_PATH + "/{id}";

        final Employee read =
            new Employee(existingId,
                         "John",
                         LocalDate.of(2020, 2, 9),
                         "Blue",
                         Title.AGENT);

        Employee result = given()
            .when()
            .get(path, existingId)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .as(Employee.class);
        Assertions.assertEquals(read, result);
    }

    @Test
    void givenUnknownId_afterRead_returnStatusNotFound() {
        final String path = RESOURCE_PATH + "/{id}";
        given()
            .when()
            .get(path, unknownId)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void givenExistingId_afterDelete_returnNoContent() {
        final String path = RESOURCE_PATH + "/{id}";
        given()
            .when()
            .delete(path, existingId)
            .then()
            .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    void givenUnknownId_afterDelete_returnStatusNotFound() {
        final String path = RESOURCE_PATH + "/{id}";
        given()
            .when()
            .delete(path, unknownId)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void givenUnknownId_afterUpdate_returnStatusNotFound() {
        final String path = RESOURCE_PATH + "/{id}";

        final EmployeeDTO toUpdate =
            new EmployeeDTO("John",
                            LocalDate.of(2020, 2, 9),
                            "Blue",
                            Title.ADMIN);

        given()
            .when()
            .contentType("application/json")
            .body(jsonb.toJson(toUpdate))
            .put(path, unknownId)
            .then()
            .statusCode(NOT_FOUND.getStatusCode());
    }

    @Test
    void givenValidEmployeeDTO_afterUpdate_returnUpdatedEmployee() {
        final String path = RESOURCE_PATH + "/{id}";

        final EmployeeDTO toUpdate =
            new EmployeeDTO("John",
                            LocalDate.of(2020, 2, 9),
                            "Blue",
                            Title.ADMIN);

        final Employee updated =
            new Employee(existingId,
                         "John",
                         LocalDate.of(2020, 2, 9),
                         "Blue",
                         Title.ADMIN);

        Employee result = given()
            .when()
            .contentType("application/json")
            .body(jsonb.toJson(toUpdate))
            .put(path, existingId)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .as(Employee.class, ObjectMapperType.JSONB);

        Assertions.assertEquals(updated, result);
    }

    @Test
    void givenValidEmployeeDTO_afterCreate_returnCreatedEmployee() {
        final String postPath = RESOURCE_PATH + "/";
        final String getPath = RESOURCE_PATH + "/{id}";

        final EmployeeDTO toCreate =
            new EmployeeDTO("Tom",
                            LocalDate.of(2019, 2, 9),
                            "Brown",
                            Title.ADMIN);

        final Employee created =
            new Employee(4L,
                         "Tom",
                         LocalDate.of(2019, 2, 9),
                         "Brown",
                         Title.ADMIN);

        Response response = given()
            .when()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(jsonb.toJson(toCreate))
            .post(postPath)
            .then()
            .statusCode(CREATED.getStatusCode())
            .extract()
            .response();

        final String locationHeader = response.getHeader("location");
        final String id = locationHeader.substring(locationHeader.indexOf("=") + 1);

        Employee result = given()
            .when()
            .get(getPath, Integer.valueOf(id))
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .as(Employee.class, ObjectMapperType.JSONB);

        Assertions.assertEquals(created, result);
    }

    @Test
    void givenTitle_afterRead_returnEmployeeList() {
        final String path = RESOURCE_PATH + "/";

        List<Employee> agents = Stream.of(persistedAgent2020)
                                    .collect(Collectors.toList());

        List<Employee> result = given()
            .queryParam("title", "AGENT")
            .when()
            .get(path)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .jsonPath().getList(".", Employee.class);

        Assertions.assertEquals(agents, result);
    }

    @Test
    void givenDates_afterRead_returnEmployeeList() {
        final String path = RESOURCE_PATH + "/";

        List<Employee> startedAt2020 = Stream.of(persistedAgent2020, persistedSupervisor2020)
                                      .collect(Collectors.toList());

        List<Employee> result = given()
            .queryParam("from_date", "2020-01-01")
            .queryParam("to_date", "2020-12-31")
            .when()
            .get(path)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .jsonPath().getList(".", Employee.class);

        Assertions.assertEquals(startedAt2020, result);
    }

    @Test
    void givenNoFilterParams_afterRead_returnListWithAllEmployees() {
        final String path = RESOURCE_PATH + "/";

        List<Employee> all = Stream.of(persistedAgent2020, persistedSupervisor2019, persistedSupervisor2020)
                                             .collect(Collectors.toList());

        List<Employee> result = given()
            .when()
            .get(path)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .jsonPath().getList(".", Employee.class);

        Assertions.assertEquals(all, result);
    }

    @Test
    void givenNoMatchingFilterParams_afterRead_returnEmptyEmployeesList() {
        final String path = RESOURCE_PATH + "/";

        List<Employee> empty = Collections.emptyList();

        List<Employee> result = given()
            .queryParam("title", "AGENT")
            .queryParam("from_date", "2030-01-01")
            .queryParam("to_date", "2040-12-31")
            .when()
            .get(path)
            .then()
            .statusCode(OK.getStatusCode())
            .extract()
            .body()
            .jsonPath().getList(".", Employee.class);

        Assertions.assertEquals(empty, result);
    }
}
