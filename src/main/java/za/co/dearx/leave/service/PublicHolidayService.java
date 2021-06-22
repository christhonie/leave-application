package za.co.dearx.leave.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import za.co.dearx.leave.calendarific.dto.GetHolidaysResponseDTO;
import za.co.dearx.leave.calendarific.dto.Holiday;
import za.co.dearx.leave.domain.PublicHoliday;
import za.co.dearx.leave.repository.PublicHolidayRepository;
import za.co.dearx.leave.service.dto.PublicHolidayDTO;
import za.co.dearx.leave.service.mapper.PublicHolidayMapper;

/**
 * Service Implementation for managing {@link PublicHoliday}.
 */
@Service
@Transactional
public class PublicHolidayService {

    private final Logger log = LoggerFactory.getLogger(PublicHolidayService.class);

    private final PublicHolidayRepository publicHolidayRepository;

    private final PublicHolidayMapper publicHolidayMapper;

    @Value("${leaveApplication.calendarific.baseUrl}")
    private String calendarificBaseUrl;

    @Value("${leaveApplication.calendarific.apiKey}")
    private String calendarificApiKey;

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

    public void reloadData(long year) throws RestClientException, URISyntaxException {
        RestTemplate client = new RestTemplate();

        StringBuffer sb = new StringBuffer();
        sb
            .append(calendarificBaseUrl)
            .append("/api/v2/holidays?api_key=")
            .append(calendarificApiKey)
            .append("&country=za&year=")
            .append(year)
            .append("&type=national");

        ResponseEntity<GetHolidaysResponseDTO> response = client.getForEntity(new URI(sb.toString()), GetHolidaysResponseDTO.class);

        for (Holiday holiday : response.getBody().response.holidays) {
            PublicHoliday newRecord = new PublicHoliday();
            newRecord.setName(holiday.name);
            LocalDate date = LocalDate.of(holiday.date.datetime.year, holiday.date.datetime.month, holiday.date.datetime.day);
            newRecord.setDate(date);
            publicHolidayRepository.save(newRecord);
        }
    }

    public void calculateWorkingDays() {
        // TODO Theunis' area
        // Get a range of dates between the passed in dates
        // Exclude weekends
        // Use repository to find all holidays between two dates
        // exclude holidays
        // return number of working days
    }
}
