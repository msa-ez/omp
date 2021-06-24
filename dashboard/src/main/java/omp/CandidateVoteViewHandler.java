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
public class CandidateVoteViewHandler {


    @Autowired
    private CandidateVoteRepository candidateVoteRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCandidateCreated_then_CREATE_1 (@Payload CandidateCreated candidateCreated) {
        try {

            if (!candidateCreated.validate()) return;

            // view 객체 생성
            CandidateVote candidateVote = new CandidateVote();
            // view 객체에 이벤트의 Value 를 set 함
            candidateVote.setElectionId(candidateCreated.getElctionId());
            candidateVote.setNo(candidateCreated.getNo());
            candidateVote.setParty(candidateCreated.getParty());
            candidateVote.setName(candidateCreated.getName());
            candidateVote.setCampaigns(candidateCreated.getCampaigns());
            candidateVote.setCandidateId(candidateCreated.getId());
            // view 레파지 토리에 save
            candidateVoteRepository.save(candidateVote);
        
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenVoted_then_UPDATE_1(@Payload Voted voted) {
        try {
            if (!voted.validate()) return;
                // view 객체 조회
            Optional<CandidateVote> candidateVoteOptional = candidateVoteRepository.findById(voted.getCandidateId());
            if( candidateVoteOptional.isPresent()) {
                CandidateVote candidateVote = candidateVoteOptional.get();
                // view 객체에 이벤트의 eventDirectValue 를 set 함
                    int voteCount = candidateVote.getVotes() == null ? 0 : candidateVote.getVotes();
                    candidateVote.setVotes(voteCount + 1);
                // view 레파지 토리에 save
                candidateVoteRepository.save(candidateVote);
            }
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenCandidateDeleted_then_DELETE_1(@Payload CandidateDeleted candidateDeleted) {
        try {
            if (!candidateDeleted.validate()) return;
            // view 레파지 토리에 삭제 쿼리
            candidateVoteRepository.deleteById(candidateDeleted.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}