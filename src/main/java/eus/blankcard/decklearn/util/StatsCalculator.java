package eus.blankcard.decklearn.util;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.models.card.CardResponseModel;

@Component
public class StatsCalculator {

    public Time getTotalStudyTime(TrainingSessionModel trainingSession) {
        List<ResultsModel> results = trainingSession.getResults();

        long totalMilis = 0;

        for(ResultsModel result : results) {
            for(CardResponseModel cardResponse : result.getCardResponses()) {
                LocalTime responseTime = cardResponse.getResponseTime().toLocalTime();

                long sec = responseTime.getSecond();
                long min = responseTime.getMinute();
    
                totalMilis += sec * 1000;
                totalMilis += (min * 60) * 1000;
            }
        }

        // - 1h bc Time adds an hour depending on your GTM
        totalMilis -= 3600000;

        Time totalTime = new Time(totalMilis);

        System.out.println("Total time is " + totalTime.toString());

        return totalTime;
    }

    public Time getAvgResponseTime(TrainingSessionModel trainingSession) {
        List<CardResponseModel> cardResponses = new ArrayList<>();
        trainingSession.getResults().forEach(r -> r.getCardResponses().forEach(cr -> cardResponses.add(cr)));

        long totalMilis = 0;

        for (CardResponseModel cardResponse : cardResponses) {
            LocalTime responseTime = cardResponse.getResponseTime().toLocalTime();

            long sec = responseTime.getSecond();
            long min = responseTime.getMinute();

            totalMilis += sec * 1000;
            totalMilis += (min * 60) * 1000;
        }

        long milisMean = totalMilis / cardResponses.size();

        // - 1h bc Time adds an hour depending on your GTM
        milisMean -= 3600000;

        Time time = new Time(milisMean);

        System.out.println("Avg time is " + time.toString());

        return time;
    }

    public int getPassRatio(TrainingSessionModel trainingSession) {
        List<ResultsModel> results = trainingSession.getResults();
        
        int passRatio = 0;
        int cardNum = results.size();
        int errorCount = 0;

        for(ResultsModel result : results) {
            if(result.getErrorCount() > 1) {
                errorCount ++;
            }            
        }

        if(errorCount >= cardNum) {
            passRatio = 0;
        } else  {
            int correctCards = cardNum - errorCount;
    
            passRatio = (correctCards * 100) / cardNum;
        }

        return passRatio;
    }

    public float getGradeChange(TrainingSessionModel trainingSession, int currentPassRatio) {
        List<TrainingSessionModel> traininSessions = trainingSession.getTraining().getTrainingSessions();
        float gradeChange = 0;

        if (traininSessions.size() <= 1) {
            gradeChange = currentPassRatio;
        } else {
            // Get the previous to the current one
            TrainingSessionModel prevTraining = traininSessions.get(traininSessions.size() - 2);
            System.out.println("Previous training id is " + prevTraining.getId());

            float prevPassRatio = getPassRatio(prevTraining);

            System.out.println("prevPassRatio = " + prevPassRatio);
            System.out.println("CUrrent pass ratio = " + currentPassRatio);

            gradeChange = ((currentPassRatio / prevPassRatio) - 1) * 100;

            System.out.println("Grade change today = " + gradeChange);
        }

        return gradeChange;
    }
}
