package in.gov.bis.platform.integration.web;

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
                "integration-service",
                "Integration and Notification",
                List.of("7.3.1 Integration and Transaction Management Framework", "7.3.14 Payment Gateway Integration", "7.3.17 Centralized Audit Logging and Monitoring"),
                List.of("13", "14", "18", "91", "94"),
                List.of("Cross-batch")
        );
    }
}
