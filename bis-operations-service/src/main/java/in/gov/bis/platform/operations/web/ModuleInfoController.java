package in.gov.bis.platform.operations.web;

import in.gov.bis.platform.common.model.ModuleDescriptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/module-info")
public class ModuleInfoController {

    @GetMapping
    public ModuleDescriptor moduleInfo() {
        return new ModuleDescriptor(
                "operations-service",
                "Operations (HR/Finance/Procurement)",
                List.of("6.18 HRD and Establishment", "6.19 Finance and Accounts", "6.20 Procurement and Inventory"),
                List.of("35", "36", "37", "55", "56", "80", "81"),
                List.of("Batch 1", "Batch 3", "Batch 5")
        );
    }
}
