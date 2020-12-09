package com.tdx.employee.api.employee.mapper;

import com.tdx.employee.api.employee.entity.Employee;
import com.tdx.employee.api.employee.model.EmployeeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "id", ignore = true)
    EmployeeDTO toDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeDTO dto);

    @Mapping(target = "id", ignore = true)
    Employee toEmployee(Employee source, @MappingTarget Employee target);
}
