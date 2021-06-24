
package omp.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

@FeignClient(name="election", url="http://${api.url.election}")
public interface ElectionService {

    @RequestMapping(method= RequestMethod.GET, path="/elections/canVote")
    public boolean canVote(@RequestParam Long electionId);

}