package in.gov.bis.platform.common.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ModuleDescriptor(
        @NotBlank String serviceName,
        @NotBlank String domainCluster,
        @NotEmpty List<String> coveredRfpAreas,
        @NotEmpty List<String> roadmapPhases,
        @NotEmpty List<String> rolloutBatches
) {
}
