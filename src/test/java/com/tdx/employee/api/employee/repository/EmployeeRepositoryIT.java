package com.tdx.employee.api.employee.repository;

import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.entity.Title;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@QuarkusTest
public class EmployeeRepositoryIT {
    @Inject
    EmployeeRepository repository;

    @Inject
    EntityManager entityManager;

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
    @Transactional
    public void setup() {
        entityManager.createNativeQuery("TRUNCATE employee").executeUpdate();
        entityManager.createNativeQuery("ALTER SEQUENCE employee_id_seq RESTART WITH 1").executeUpdate();
        entityManager.persist(persistedAgent2020);
        entityManager.persist(persistedSupervisor2019);
        entityManager.persist(persistedSupervisor2020);
        entityManager.flush();
    }

    @Test
    @Transactional
    void create() {
        final Employee toCreate =
            new Employee("Tom",
                            LocalDate.of(2019, 2, 9),
                            "Brown",
                            Title.ADMIN);

        final Employee created =
            new Employee(4L,
                         "Tom",
                         LocalDate.of(2019, 2, 9),
                         "Brown",
                         Title.ADMIN);

        Assertions.assertEquals(created, repository.create(toCreate).get());
    }

    @Test
    @Transactional
    void delete() {
        Optional<Employee> deleted = repository.delete(persistedAgent2020.getId());
        Assertions.assertEquals(persistedAgent2020, deleted.get());
    }

    @Test
    @Transactional
    void update() {
        final Employee toUpdate =
            new Employee(1L,
                         "John",
                         LocalDate.of(2020, 2, 9),
                         "Blue",
                         Title.ADMIN);

        Optional<Employee> updated = repository.update(1L, toUpdate);
        Assertions.assertEquals(toUpdate, updated.get());
    }

    @Test
    void givenTitle_afterRead_returnEmployeeList() {
        List<Employee> agents = Stream.of(persistedAgent2020)
                                      .collect(Collectors.toList());

        Assertions.assertEquals(agents, repository.findByTitleAndStartDate(Title.AGENT,
                                                                           null,
                                                                           null));
    }

    @Test
    void givenDates_afterRead_returnEmployeeList() {
        List<Employee> startedAt2020 = Stream.of(persistedAgent2020, persistedSupervisor2020)
                                             .collect(Collectors.toList());

        Assertions.assertEquals(startedAt2020, repository.findByTitleAndStartDate(null,
                                                                                  LocalDate.of(2020, 1, 1),
                                                                                  LocalDate.of(2020, 11, 21)));
    }

    @Test
    void givenNoFilterParams_afterRead_returnListWithAllEmployees() {
        List<Employee> all = Stream.of(persistedAgent2020, persistedSupervisor2019, persistedSupervisor2020)
                                   .collect(Collectors.toList());

        Assertions.assertEquals(all, repository.findByTitleAndStartDate(null,
                                                                           null,
                                                                           null));
    }

    @Test
    void givenNoMatchingFilterParams_afterRead_returnEmptyEmployeesList() {
        List<Employee> empty = Collections.emptyList();

        Assertions.assertEquals(empty, repository.findByTitleAndStartDate(Title.SUPERVISOR,
                                                                                  LocalDate.of(2030, 1, 1),
                                                                                  LocalDate.of(2040, 11, 21)));
    }


}
