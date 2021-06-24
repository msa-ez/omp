package omp;

import omp.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class MyVoteViewHandler {


    @Autowired
    private MyVoteRepository myVoteRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenVoted_then_CREATE_1 (@Payload Voted voted) {
        try {

            if (!voted.validate()) return;

            // view 객체 생성
            MyVote myVote = new MyVote();
            // view 객체에 이벤트의 Value 를 set 함
            myVote.setVoteId(voted.getId());
            myVote.setCandidateId(voted.getCandidateId());
            myVote.setVoterId(voted.getVoterId());
            myVote.setVotingDate(voted.getVotingDate());
            // view 레파지 토리에 save
            myVoteRepository.save(myVote);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @StreamListener(KafkaProcessor.INPUT)
    public void whenCandidateDeleted_then_DELETE_1(@Payload CandidateDeleted candidateDeleted) {
        try {
            if (!candidateDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            myVoteRepository.deleteByCandidateId(candidateDeleted.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}