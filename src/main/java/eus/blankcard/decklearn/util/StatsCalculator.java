package eus.blankcard.decklearn.util;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.CardResponseModel;
import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;

@Component
public class StatsCalculator {

    public Time getTotalStudyTime(TrainingSessionModel trainingSession) {
        Time totalTime = null;

        List<Time> allTimes = new ArrayList<>();

        trainingSession.getResults().forEach(r -> r.getCardResponses().forEach(cr -> allTimes.add(cr.getResponseTime())));

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
            errorCount += result.getErrorCount();
        }
        
        int correctCards = cardNum - errorCount;

        passRatio = (correctCards * 100) / cardNum;

        return passRatio;
    }
}
