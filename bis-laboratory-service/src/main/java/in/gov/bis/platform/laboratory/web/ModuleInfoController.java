package in.gov.bis.platform.laboratory.web;

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
                "laboratory-service",
                "LIMS and LRS",
                List.of("6.16 LIMS", "6.17 LRS"),
                List.of("38", "39", "40", "41"),
                List.of("Batch 1")
        );
    }
}
