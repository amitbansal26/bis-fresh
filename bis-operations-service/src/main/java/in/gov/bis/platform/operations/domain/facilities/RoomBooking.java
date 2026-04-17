package in.gov.bis.platform.operations.domain.facilities;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "room_bookings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomBooking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String roomNo;
    private String roomType;
    private String bookedBy;
    private String officeCode;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String purpose;
    private String status;
    private Instant createdAt;
}
