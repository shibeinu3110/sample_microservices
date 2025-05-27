package com.micro.employeeservice.mapper;

import com.micro.employeeservice.dto.request.EmployeeRequestDTO;
import com.micro.employeeservice.dto.response.EmployeeResponseDTO;
import com.micro.employeeservice.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toEmployee(EmployeeRequestDTO employeeRequestDTO);

    @Mapping(target = "fullName", expression = "java(employee.getFirstName() + \" \" + employee.getLastName())")
    EmployeeResponseDTO toEmployeeResponseDTO(Employee employee);

    List<EmployeeResponseDTO> toEmployeeResponseDTOList(List<Employee> employees);

    EmployeeRequestDTO toEmployeeRequestDTO(Employee employee);


}
