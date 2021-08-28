package za.co.dearx.leave.config;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import za.co.dearx.leave.domain.LeaveApplication;
import za.co.dearx.leave.service.LeaveApplicationQueryService;
import za.co.dearx.leave.service.LeaveApplicationService;
import za.co.dearx.leave.service.criteria.LeaveApplicationCriteria;
import za.co.dearx.leave.service.dto.LeaveApplicationDTO;
import za.co.dearx.leave.service.exception.NotFoundException;	

@Configuration
@EnableScheduling
public class LeaveScheduler {
	
	 private final Logger log = LoggerFactory.getLogger(LeaveScheduler.class);
	 
	 @Autowired
	 private LeaveApplicationQueryService leaveAppQueryService;
	
	 @Autowired
	 private LeaveApplicationService leaveApplicationService;
	 
	 @Scheduled(cron = "0 0 1 * * ?")
	    public void scheduleTaskUsingCronExpression() {
		 //TODO; Correction & Optimization [Entity instead of DTO, Criteria utilizing Leave Status]
	    	log.info("[Scheduled Task running to update Leave Statues]");
	        LocalDate currentDate = LocalDate.now();

	        List<LeaveApplicationDTO> leaveAppList = Collections.emptyList();
	        Pageable page = PageRequest.of(0, 20);
	        Page<LeaveApplicationDTO> allLeaveApps = this.leaveAppQueryService.findByCriteria(new LeaveApplicationCriteria(), page);
	        leaveAppList = allLeaveApps.getContent();

	        for (LeaveApplicationDTO leaveApp: leaveAppList) {
	            if (leaveApp.getStartDate().isBefore(currentDate) && leaveApp.getLeaveStatus().getName().contains("Approved"))
	            {
	            	log.info("[Scheduled Task running to update Leave Status for: {}]", leaveApp.toString());
	            	try {
						LeaveApplication updatedLeaveApp = this.leaveApplicationService.updateStatus(leaveApp.getId(), "Taken");
						log.info("[Scheduled Task updated Leave Status for: {}]", updatedLeaveApp.toString());
					} catch (NotFoundException e) {
						e.printStackTrace();
					}
	            } else {
	            	continue;
	            }
	        }
	        log.info("[Scheduled Task Complete]");
	    }

}
