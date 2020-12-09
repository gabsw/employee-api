package com.tdx.employee.api.employee.repository;

import com.tdx.employee.api.employee.entity.Employee;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class EmployeeRepository implements PanacheRepository<Employee> {

    public List<Employee> findBetweenDates(final LocalDate fromDate, final LocalDate toDate) {
        return find("start_date BETWEEN :fromDate AND :toDate",
                    Parameters.with("fromData", fromDate).and("toDate", toDate))
            .list();
    }

    public Optional<Employee> update(final Long id, final Employee employee) {
        return findByIdOptional(id).map(emp -> emp.toEmployee(employee));
    }

    public Optional<Employee> create(final Employee employee) {
        persist(employee);
        return Optional.of(employee);
    }

    public Optional<Employee> delete(final Long id) {
        return findByIdOptional(id).map(employee -> {
            delete(employee);
            return employee;
        });
    }
}

