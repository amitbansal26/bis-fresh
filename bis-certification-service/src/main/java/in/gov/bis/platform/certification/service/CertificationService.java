package in.gov.bis.platform.certification.service;
import in.gov.bis.platform.certification.domain.*;
import in.gov.bis.platform.certification.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
public class CertificationService {
    private final CertificationApplicationRepository appRepo;
    private final WorkflowHistoryRepository historyRepo;
    private static final AtomicLong SEQ = new AtomicLong(1000);

    public CertificationService(CertificationApplicationRepository appRepo, WorkflowHistoryRepository historyRepo) {
        this.appRepo = appRepo; this.historyRepo = historyRepo;
    }

    public CertificationApplication submit(String schemeCode, String applicantName, String companyName,
            String productName, String productCategory, String standardNo,
            String factoryAddress, String factoryState, String factoryPinCode) {
        String appNo = "APP-" + schemeCode + "-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + SEQ.incrementAndGet();
        CertificationApplication app = CertificationApplication.builder()
            .applicationNo(appNo).schemeCode(schemeCode).applicantName(applicantName)
            .companyName(companyName).productName(productName).productCategory(productCategory)
            .standardNo(standardNo).factoryAddress(factoryAddress).factoryState(factoryState)
            .factoryPinCode(factoryPinCode).status(ApplicationStatus.SUBMITTED)
            .submittedAt(Instant.now()).createdAt(Instant.now()).updatedAt(Instant.now()).build();
        app = appRepo.save(app);
        historyRepo.save(WorkflowHistory.builder().applicationId(app.getId())
            .fromStatus("NEW").toStatus("SUBMITTED").action("SUBMIT")
            .performedAt(Instant.now()).build());
        return app;
    }

    public List<CertificationApplication> listByScheme(String schemeCode) {
        return schemeCode != null ? appRepo.findBySchemeCode(schemeCode) : appRepo.findAll();
    }

    public CertificationApplication getById(UUID id) {
        return appRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found: " + id));
    }

    public List<WorkflowHistory> getHistory(UUID applicationId) {
        return historyRepo.findByApplicationIdOrderByPerformedAtDesc(applicationId);
    }
}
