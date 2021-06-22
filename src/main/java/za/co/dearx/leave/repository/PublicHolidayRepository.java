package za.co.dearx.leave.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.PublicHoliday;

/**
 * Spring Data SQL repository for the PublicHoliday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long>, JpaSpecificationExecutor<PublicHoliday> {
    List<PublicHoliday> findAllBetweenDates();
}
