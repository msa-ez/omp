package omp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

 @RestController
 public class ElectionController {

    @Autowired
    ElectionRepository electionRepository;

    @RequestMapping(value = "elections/canVote",
        method = RequestMethod.GET,
        produces = "application/json;charset=UTF-8")
    public boolean canVote(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("##### /elections/canVote  called #####");

        Long electionId = Long.valueOf(request.getParameter("electionId"));
        Optional<Election> election = electionRepository.findById(electionId);
        if(election.isPresent()){
            Election electionValue = election.get();
            LocalDate now = LocalDate.now();
            LocalDate startDate = electionValue.getVotingDay().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = startDate.plusDays(electionValue.getVotingPeriod());
            if(now.isAfter(startDate) && now.isBefore(endDate)) return true;
            return false;
        }else{
            return false;
        }
    
    }
 }
