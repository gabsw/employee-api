package com.tdx.employee.api.employee.entity;

import com.tdx.employee.api.employee.model.EmployeeDTO;
import com.tdx.employee.api.employee.mapper.EmployeeMapper;
import com.vladmihalcea.hibernate.type.basic.PostgreSQLEnumType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "employee")
@TypeDef(
    name = "title",
    typeClass = PostgreSQLEnumType.class
)
@NamedQuery(name = Employee.FIND_BY_TITLE,
            query = "SELECT e " +
                    "FROM Employee e " +
                    "WHERE e.title = :title")
public class Employee {
    public static final String FIND_BY_TITLE = "findByTitle";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @Column(name = "start_date")
    @NotNull
    private LocalDate startDate;
    @NotNull
    private String team;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Type(type = "title")
    private Title title;

    public Employee toEmployee(final Employee employee) {
        return EmployeeMapper.INSTANCE.toEmployee(employee, this);
    }

    public EmployeeDTO toEmployeeDTO() {
        return EmployeeMapper.INSTANCE.toDto(this);
    }
}
