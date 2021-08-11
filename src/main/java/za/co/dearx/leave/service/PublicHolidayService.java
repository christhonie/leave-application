package za.co.dearx.leave.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import za.co.dearx.leave.client.calendarific.dto.GetHolidaysResponseDTO;
import za.co.dearx.leave.client.calendarific.dto.Holiday;
import za.co.dearx.leave.config.ApplicationProperties;
import za.co.dearx.leave.config.CalendarificProperties;
import za.co.dearx.leave.domain.PublicHoliday;
import za.co.dearx.leave.repository.PublicHolidayRepository;
import za.co.dearx.leave.service.dto.PublicHolidayDTO;
import za.co.dearx.leave.service.exception.UpdateException;
import za.co.dearx.leave.service.mapper.PublicHolidayMapper;

/**
 * Service Implementation for managing {@link PublicHoliday}.
 */
@Service
@Transactional
public class PublicHolidayService {

    private static final String PUBLIC_HOLIDAY = "Public Holiday";

    private final Logger log = LoggerFactory.getLogger(PublicHolidayService.class);

    private final PublicHolidayRepository publicHolidayRepository;

    private final PublicHolidayMapper publicHolidayMapper;

    @Autowired
    private CalendarificProperties calendarificProps;

    @Autowired
    private ApplicationProperties appProps;

    private static final String OBSERVED_KEYWORD = "observed";

    public PublicHolidayService(PublicHolidayRepository publicHolidayRepository, PublicHolidayMapper publicHolidayMapper) {
        this.publicHolidayRepository = publicHolidayRepository;
        this.publicHolidayMapper = publicHolidayMapper;
    }

    /**
     * Save a publicHoliday.
     *
     * @param publicHolidayDTO the entity to save.
     * @return the persisted entity.
     */
    public PublicHolidayDTO save(PublicHolidayDTO publicHolidayDTO) {
        log.debug("Request to save PublicHoliday : {}", publicHolidayDTO);
        PublicHoliday publicHoliday = publicHolidayMapper.toEntity(publicHolidayDTO);
        publicHoliday = publicHolidayRepository.save(publicHoliday);
        return publicHolidayMapper.toDto(publicHoliday);
    }

    /**
     * Partially update a publicHoliday.
     *
     * @param publicHolidayDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublicHolidayDTO> partialUpdate(PublicHolidayDTO publicHolidayDTO) {
        log.debug("Request to partially update PublicHoliday : {}", publicHolidayDTO);

        return publicHolidayRepository
            .findById(publicHolidayDTO.getId())
            .map(
                existingPublicHoliday -> {
                    publicHolidayMapper.partialUpdate(existingPublicHoliday, publicHolidayDTO);
                    return existingPublicHoliday;
                }
            )
            .map(publicHolidayRepository::save)
            .map(publicHolidayMapper::toDto);
    }

    /**
     * Get all the publicHolidays.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PublicHolidayDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PublicHolidays");
        return publicHolidayRepository.findAll(pageable).map(publicHolidayMapper::toDto);
    }

    /**
     * Get one publicHoliday by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublicHolidayDTO> findOne(Long id) {
        log.debug("Request to get PublicHoliday : {}", id);
        return publicHolidayRepository.findById(id).map(publicHolidayMapper::toDto);
    }

    /**
     * Delete the publicHoliday by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PublicHoliday : {}", id);
        publicHolidayRepository.deleteById(id);
    }

    public void reloadData(int year) throws UpdateException {
        RestTemplate client = new RestTemplate();

        StringBuffer sb = new StringBuffer();
        sb
            .append(calendarificProps.baseUrl)
            .append("/api/v2/holidays?api_key=")
            .append(calendarificProps.apiKey)
            .append("&country=za&year=")
            .append(year)
            .append("&type=national");

        URI url;
        try {
            url = new URI(sb.toString());
        } catch (URISyntaxException e) {
            log.error("Calendarific URL could not be built", e);
            throw new UpdateException(
                PUBLIC_HOLIDAY,
                "URL for the Public Holiday service was invalid. Contact system administrator to review application configuration."
            );
        }

        ResponseEntity<GetHolidaysResponseDTO> response;
        try {
            response = client.getForEntity(url, GetHolidaysResponseDTO.class);
        } catch (RestClientException e) {
            log.error("Calendarific service API call failed", e);
            throw new UpdateException(
                PUBLIC_HOLIDAY,
                "Could not retrieve public holidays. Try again later or contact the system administrator if it persists."
            );
        }

        List<PublicHoliday> dirtyList = new ArrayList<PublicHoliday>();
        List<PublicHoliday> publicHolidays = new ArrayList<PublicHoliday>();
        List<String> observedDayStrings = new ArrayList<String>();

        for (Holiday holiday : response.getBody().response.holidays) {
            PublicHoliday newRecord = new PublicHoliday();
            String holidayNameString = holiday.name;

            // Add observed days to a list so we can remove the "unobserved" ones later
            if (holidayNameString.contains(OBSERVED_KEYWORD)) {
                observedDayStrings.add(holidayNameString.replace(OBSERVED_KEYWORD, "").stripTrailing());
            }

            newRecord.setName(holiday.name);
            LocalDate date = LocalDate.of(holiday.date.datetime.year, holiday.date.datetime.month, holiday.date.datetime.day);
            newRecord.setDate(date);
            dirtyList.add(newRecord);
        }

        for (PublicHoliday holiday : dirtyList) {
            if (!observedDayStrings.contains(holiday.getName())) {
                publicHolidays.add(holiday);
            }
        }

        //Get the last day of the year
        final YearMonth yearMonth = YearMonth.of(year, 12);
        final LocalDate yearEndDate = yearMonth.atEndOfMonth();
        //Delete all
        publicHolidayRepository.deleteAllHolidaysForYear(LocalDate.ofYearDay(year, 1), yearEndDate);

        publicHolidayRepository.saveAll(publicHolidays);
    }

    public void reloadSurrounding5Years() throws UpdateException {
        int currentYearLong = LocalDate.now().getYear();

        for (int i = currentYearLong - appProps.publicHolidayWindow; i <= currentYearLong + appProps.publicHolidayWindow; i++) {
            reloadData(i);
        }
    }

    public List<LocalDate> getHolidaysBetween(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> datesBetween = new ArrayList<LocalDate>();
        List<PublicHoliday> publicHolidaysBetween = publicHolidayRepository.findAllholidaysBetween(startDate, endDate).get();

        for (PublicHoliday publicHoliday : publicHolidaysBetween) {
            datesBetween.add(publicHoliday.getDate());
        }

        return datesBetween;
    }

    public Integer calculateWorkDays(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> holidaysBetweenDates = getHolidaysBetween(startDate, endDate);
        int days = 0;
        while (!startDate.isAfter(endDate)) {
            if (
                !startDate.getDayOfWeek().equals(DayOfWeek.SUNDAY) &&
                !startDate.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                !holidaysBetweenDates.contains(startDate)
            ) {
                days++;
            }
            startDate = startDate.plusDays(1);
        }
        return days;
    }
}
