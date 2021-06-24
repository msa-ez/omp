package omp;

import omp.config.kafka.KafkaProcessor;

import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    @Autowired CampaignRepository campaignRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCandidateDeleted_DeleteCanditate(@Payload CandidateDeleted candidateDeleted){

        if(!candidateDeleted.validate()) return;

        System.out.println("\n\n##### listener DeleteCanditate : " + candidateDeleted.toJson() + "\n\n");

        List<Campaign> campaigns = campaignRepository.findByCanditateId(candidateDeleted.getId());
        campaigns.forEach(campaign -> {
            campaignRepository.delete(campaign);
        });
            
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){}


}
