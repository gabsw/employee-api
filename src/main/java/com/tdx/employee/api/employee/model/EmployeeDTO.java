package com.tdx.employee.api.employee.model;

import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.entity.Title;
import com.tdx.employee.api.employee.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.json.bind.annotation.JsonbProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    @NotBlank
    @Size(max = 100)
    private String name;
    @JsonbProperty("start_date")
    private LocalDate startDate;
    @NotBlank
    @Size(max = 20)
    private String team;
    private Title title;

    public Employee toEmployee() {
        return EmployeeMapper.INSTANCE.toEntity(this);
    }


}
