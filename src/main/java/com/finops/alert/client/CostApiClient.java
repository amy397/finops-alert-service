package com.finops.alert.client;

import com.finops.alert.dto.CostSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cost-api", url = "${feign.client.cost-api.url}")
public interface CostApiClient {

    @GetMapping("/api/dashboard/summary")
    CostSummaryDto getCostSummary();
}
