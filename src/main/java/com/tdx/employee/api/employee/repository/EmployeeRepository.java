package com.tdx.employee.api.employee.repository;

import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.entity.Title;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.tdx.employee.api.employee.entity.Employee.FIND_BY_TITLE;
import static javax.transaction.Transactional.TxType.REQUIRED;

@ApplicationScoped
@Transactional(REQUIRED)
public class EmployeeRepository implements PanacheRepository<Employee> {

    @Inject
    EntityManager em;

    public List<Employee> findByTitleUsingNamedQuery(final Title title) {
        return em.createNamedQuery(FIND_BY_TITLE, Employee.class)
                 .setParameter("title", title)
                 .getResultList();
    }

    // Alternatives using Panache methods
    public List<Employee> findByTitle(Title title) {
        return find("title", title).list();
    }

    public List<Employee> findBetweenDates(final LocalDate fromDate, final LocalDate toDate) {
        return find("start_date BETWEEN :fromDate AND :toDate",
                    Parameters.with("fromDate", fromDate).and("toDate", toDate))
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

