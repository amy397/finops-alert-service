package com.finops.alert.dto;

import java.math.BigDecimal;
import java.util.List;

public record CostSummaryDto(
        BigDecimal currentMonthCost,
        BigDecimal previousMonthCost,
        BigDecimal changePercent,
        BigDecimal todayCost,
        BigDecimal yesterdayCost,
        List<ServiceCostSummaryDto> topServices
) {}
