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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "employee")
@TypeDef(
    name = "title",
    typeClass = PostgreSQLEnumType.class
)
public class Employee {
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

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Employee)) {
            return false;
        }
        final Employee other = (Employee) obj;

        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, team, title); //TODO: return number?
    }

    public Employee toEmployee(final Employee employee) {
        return EmployeeMapper.INSTANCE.toEmployee(employee, this);
    }

    public EmployeeDTO toEmployeeDTO() {
        return EmployeeMapper.INSTANCE.toDto(this);
    }
}
