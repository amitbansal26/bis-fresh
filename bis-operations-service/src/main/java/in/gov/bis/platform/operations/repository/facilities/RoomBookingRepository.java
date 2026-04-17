package in.gov.bis.platform.operations.repository.facilities;

import in.gov.bis.platform.operations.domain.facilities.RoomBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RoomBookingRepository extends JpaRepository<RoomBooking, Long> {
    List<RoomBooking> findByOfficeCode(String officeCode);
    List<RoomBooking> findByStatus(String status);
}
