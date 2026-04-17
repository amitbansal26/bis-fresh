package in.gov.bis.platform.identity.web;

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
                "identity-service",
                "Identity and Access",
                List.of("6.2 User Management", "6.3 Common Requirements"),
                List.of("31", "32", "33"),
                List.of("Batch 1")
        );
    }
}
