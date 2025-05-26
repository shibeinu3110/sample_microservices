package com.micro.salaryservice.mapper;

import com.micro.salaryservice.dto.request.SalaryRequestDTO;
import com.micro.salaryservice.dto.response.SalaryResponseDTO;
import com.micro.salaryservice.model.SalaryIncrement;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryIncrementMapper {
    SalaryIncrement toSalaryIncrement(SalaryRequestDTO requestDTO);
    SalaryResponseDTO toSalaryResponseDTO(SalaryIncrement salaryIncrement);
    List<SalaryResponseDTO> toSalaryResponseDTOList(List<SalaryIncrement> salaryIncrements);
    SalaryIncrement toSalaryIncrement(SalaryResponseDTO responseDTO);
}
