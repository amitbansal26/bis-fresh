package in.gov.bis.platform.operations.repository.facilities;

import in.gov.bis.platform.operations.domain.facilities.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Long> {
    List<LibraryBook> findByAvailable(boolean available);
}
