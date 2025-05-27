package com.micro.employeeservice.service;

import com.micro.employeeservice.dto.request.EmployeeRequestDTO;
import com.micro.employeeservice.dto.response.EmployeeResponseDTO;
import com.micro.employeeservice.mapper.EmployeeMapper;
import com.micro.employeeservice.model.Employee;
import com.micro.employeeservice.repository.EmployeeRepository;
import com.micro.employeeservice.service.impl.EmployeeServiceImpl;
import com.micro.employeeservice.validator.EmployeeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    EmployeeServiceImpl employeeService;
    @Mock
    EmployeeValidator employeeValidator;
    @Mock
    EmployeeRepository employeeRepository;
    @Mock
    EmployeeMapper employeeMapper;
    @Mock
    MongoTemplate mongoTemplate;

    @Test
    void testGetEmployee() {
        // Given (chuẩn bị dữ liệu)
        Long employeeId = 100L;
        Employee employee = new Employee(employeeId, "Hung", "Dao", 20, "123", "huyhungshiba@gmail.com", "0375812092", 100L);
        EmployeeResponseDTO expectedDto = new EmployeeResponseDTO();
        expectedDto.setFullName("Hung Dao");

        // When (mock hành vi của dependency)
        doNothing().when(employeeValidator).checkEmployeeId(employeeId);
        when(employeeRepository.findByEmployeeId(employeeId)).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(employee)).thenReturn(expectedDto);

        // Then (gọi hàm thật và kiểm tra)
        EmployeeResponseDTO actualDto = employeeService.getEmployeeById(employeeId);

        // Assert
        assertNotNull(actualDto);
        assertEquals("Hung Dao", actualDto.getFullName());

        // Verify các dependency được gọi đúng
        verify(employeeValidator).checkEmployeeId(employeeId);
        verify(employeeRepository).findByEmployeeId(employeeId);
        verify(employeeMapper).toEmployeeResponseDTO(employee);
    }

    @Test
    void testCreateEmployee () {
        Long employeeId = 100L;
        Employee employee = new Employee(employeeId, "Hung", "Dao", 20, "123", "huyhungshiba@gmail.com", "0375812092", 100L);
        EmployeeResponseDTO expectedDto = new EmployeeResponseDTO();
        expectedDto.setFullName("Hung Dao");

        EmployeeRequestDTO requestDTO = new EmployeeRequestDTO();
// set các trường tương ứng giống employee
        requestDTO.setEmployeeId(employeeId);
        requestDTO.setFirstName("Hung");
        requestDTO.setLastName("Dao");
        requestDTO.setAge(20);
        requestDTO.setEmail("huyhungshiba@gmail.com");
        requestDTO.setPhone("0375812092");


        doNothing().when(employeeValidator).checkEmployee(any());
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeMapper.toEmployeeResponseDTO(any())).thenReturn(expectedDto);
        when(employeeMapper.toEmployeeRequestDTO(any())).thenReturn(requestDTO);

        EmployeeResponseDTO employeeResponseDTO = employeeService.saveEmployee(employeeMapper.toEmployeeRequestDTO(employee));


        assertNotNull(employeeResponseDTO);
        assertEquals(employeeResponseDTO.getFullName(), expectedDto.getFullName());
    }
}
