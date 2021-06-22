package za.co.dearx.leave.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import za.co.dearx.leave.domain.PublicHoliday;

/**
 * Spring Data SQL repository for the PublicHoliday entity.
 */
@Repository
public interface PublicHolidayRepository extends JpaRepository<PublicHoliday, Long>, JpaSpecificationExecutor<PublicHoliday> {
    @Query("select publicHoliday from PublicHoliday publicHoliday where date BETWEEN :startDate AND :endDate")
    Optional<List<PublicHoliday>> findAllholidaysBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Modifying
    @Query("delete from PublicHoliday publicHoliday where date BETWEEN :startDate AND :endDate")
    void deleteAllHolidaysForYear(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
