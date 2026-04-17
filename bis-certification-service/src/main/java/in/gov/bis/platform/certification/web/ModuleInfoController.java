package in.gov.bis.platform.certification.web;

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
                "certification-service",
                "Certification Core",
                List.of("6.5 Scheme I", "6.8 CoC", "6.11 OTR", "6.12 FMCS"),
                List.of("43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53"),
                List.of("Batch 2")
        );
    }
}
