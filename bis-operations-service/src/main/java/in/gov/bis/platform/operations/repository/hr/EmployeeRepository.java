package in.gov.bis.platform.operations.repository.hr;

import in.gov.bis.platform.operations.domain.hr.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByOfficeCode(String officeCode);
    List<Employee> findByStatus(String status);
    Optional<Employee> findByEmployeeNo(String employeeNo);
}
