package com.micro.salaryservice.service.impl;

import com.micro.commonlib.common.exception.ErrorMessages;
import com.micro.commonlib.common.exception.StandardException;
import com.micro.salaryservice.service.ExportService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ExportServiceFactory {
    private final Map<String, ExportService> serviceMap = new HashMap<>();

    public ExportServiceFactory (List<ExportService> exportServices) {
        for(ExportService service : exportServices) {
            serviceMap.put(service.getType(), service);
        }
    }

    public ExportService getCurrentService(String type) {
        ExportService exportService = serviceMap.get(type.toLowerCase());

        if(exportService == null) {
            throw new StandardException(ErrorMessages.INVALID_FORMAT, "Input type must be Excel or PDF");
        }

        return exportService;
    }
}
