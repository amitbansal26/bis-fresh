package in.gov.bis.platform.common.workflow;

public record WorkflowTransition(
        ApplicationStatus from,
        WorkflowAction action,
        ApplicationStatus to,
        String description
) {}
