package in.gov.bis.platform.common.workflow;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class WorkflowEngine {

    private static final List<WorkflowTransition> TRANSITIONS = List.of(
            // Normal progression
            new WorkflowTransition(ApplicationStatus.DRAFT, WorkflowAction.SUBMIT,
                    ApplicationStatus.SUBMITTED, "Applicant submits the application"),
            new WorkflowTransition(ApplicationStatus.SUBMITTED, WorkflowAction.SCRUTINIZE,
                    ApplicationStatus.UNDER_SCRUTINY, "Officer begins scrutiny"),
            new WorkflowTransition(ApplicationStatus.UNDER_SCRUTINY, WorkflowAction.REQUEST_DOCUMENTS,
                    ApplicationStatus.PENDING_DOCUMENTS, "Additional documents requested"),
            new WorkflowTransition(ApplicationStatus.PENDING_DOCUMENTS, WorkflowAction.UPLOAD_DOCUMENTS,
                    ApplicationStatus.UNDER_SCRUTINY, "Applicant uploads requested documents"),
            new WorkflowTransition(ApplicationStatus.UNDER_SCRUTINY, WorkflowAction.ASSIGN_INSPECTOR,
                    ApplicationStatus.INSPECTION_SCHEDULED, "Inspector assigned and inspection scheduled"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_SCHEDULED, WorkflowAction.COMPLETE_INSPECTION,
                    ApplicationStatus.INSPECTION_COMPLETED, "Inspector completes the inspection"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_COMPLETED, WorkflowAction.APPROVE,
                    ApplicationStatus.APPROVED, "Application approved"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_COMPLETED, WorkflowAction.REJECT,
                    ApplicationStatus.REJECTED, "Application rejected"),

            // On-hold / resume
            new WorkflowTransition(ApplicationStatus.UNDER_SCRUTINY, WorkflowAction.PUT_ON_HOLD,
                    ApplicationStatus.ON_HOLD, "Application placed on hold"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_SCHEDULED, WorkflowAction.PUT_ON_HOLD,
                    ApplicationStatus.ON_HOLD, "Application placed on hold"),
            new WorkflowTransition(ApplicationStatus.ON_HOLD, WorkflowAction.RESUME,
                    ApplicationStatus.UNDER_SCRUTINY, "Application resumed from hold"),

            // Revoke an approved licence
            new WorkflowTransition(ApplicationStatus.APPROVED, WorkflowAction.REVOKE,
                    ApplicationStatus.REVOKED, "Licence revoked"),

            // Reopen a rejected application
            new WorkflowTransition(ApplicationStatus.REJECTED, WorkflowAction.REOPEN,
                    ApplicationStatus.DRAFT, "Rejected application reopened as draft"),

            // Withdraw — any active state
            new WorkflowTransition(ApplicationStatus.DRAFT, WorkflowAction.WITHDRAW,
                    ApplicationStatus.WITHDRAWN, "Application withdrawn"),
            new WorkflowTransition(ApplicationStatus.SUBMITTED, WorkflowAction.WITHDRAW,
                    ApplicationStatus.WITHDRAWN, "Application withdrawn"),
            new WorkflowTransition(ApplicationStatus.UNDER_SCRUTINY, WorkflowAction.WITHDRAW,
                    ApplicationStatus.WITHDRAWN, "Application withdrawn"),
            new WorkflowTransition(ApplicationStatus.PENDING_DOCUMENTS, WorkflowAction.WITHDRAW,
                    ApplicationStatus.WITHDRAWN, "Application withdrawn"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_SCHEDULED, WorkflowAction.WITHDRAW,
                    ApplicationStatus.WITHDRAWN, "Application withdrawn"),

            // Cancel — any active state
            new WorkflowTransition(ApplicationStatus.DRAFT, WorkflowAction.CANCEL,
                    ApplicationStatus.CANCELLED, "Application cancelled"),
            new WorkflowTransition(ApplicationStatus.SUBMITTED, WorkflowAction.CANCEL,
                    ApplicationStatus.CANCELLED, "Application cancelled"),
            new WorkflowTransition(ApplicationStatus.UNDER_SCRUTINY, WorkflowAction.CANCEL,
                    ApplicationStatus.CANCELLED, "Application cancelled"),
            new WorkflowTransition(ApplicationStatus.PENDING_DOCUMENTS, WorkflowAction.CANCEL,
                    ApplicationStatus.CANCELLED, "Application cancelled"),
            new WorkflowTransition(ApplicationStatus.INSPECTION_SCHEDULED, WorkflowAction.CANCEL,
                    ApplicationStatus.CANCELLED, "Application cancelled")
    );

    /**
     * Applies the given action to the current status and returns the resulting status.
     *
     * @throws IllegalStateException if the transition is not allowed
     */
    public ApplicationStatus apply(ApplicationStatus current, WorkflowAction action) {
        return TRANSITIONS.stream()
                .filter(t -> t.from() == current && t.action() == action)
                .map(WorkflowTransition::to)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "Action " + action + " is not allowed in status " + current));
    }

    /** Returns all actions that are valid from the given status. */
    public List<WorkflowAction> getAllowedActions(ApplicationStatus current) {
        return TRANSITIONS.stream()
                .filter(t -> t.from() == current)
                .map(WorkflowTransition::action)
                .toList();
    }

    /** Returns true if the action can be applied to the current status. */
    public boolean canTransition(ApplicationStatus current, WorkflowAction action) {
        return TRANSITIONS.stream()
                .anyMatch(t -> t.from() == current && t.action() == action);
    }
}
