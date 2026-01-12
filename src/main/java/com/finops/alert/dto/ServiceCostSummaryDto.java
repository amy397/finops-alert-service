package com.finops.alert.dto;

import java.math.BigDecimal;

public record ServiceCostSummaryDto(
        String serviceName,
        BigDecimal totalCost,
        BigDecimal percentage
) {}
