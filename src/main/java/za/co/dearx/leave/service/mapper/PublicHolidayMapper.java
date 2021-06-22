package za.co.dearx.leave.service.mapper;

import org.mapstruct.*;
import za.co.dearx.leave.domain.*;
import za.co.dearx.leave.service.dto.PublicHolidayDTO;

/**
 * Mapper for the entity {@link PublicHoliday} and its DTO {@link PublicHolidayDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PublicHolidayMapper extends EntityMapper<PublicHolidayDTO, PublicHoliday> {}
