package eus.blankcard.decklearn.util;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;

import eus.blankcard.decklearn.models.ResultsModel;
import eus.blankcard.decklearn.models.TrainingModel;
import eus.blankcard.decklearn.models.TrainingSessionModel;
import eus.blankcard.decklearn.models.card.CardResponseModel;
import eus.blankcard.decklearn.models.deck.DeckModel;
import eus.blankcard.decklearn.models.user.UserModel;

@Component
public class StatsCalculator {

    public Time getTotalStudyTime(TrainingSessionModel trainingSession) {
        List<ResultsModel> results = trainingSession.getResults();

        long totalMilis = 0;

        for (ResultsModel result : results) {
            for (CardResponseModel cardResponse : result.getCardResponses()) {
                LocalTime resTim = cardResponse.getResponseTime().toLocalTime();
                LocalDateTime responseTime = LocalDateTime.of(LocalDate.now(), resTim);

                long sec = responseTime.getSecond();
                long min = responseTime.getMinute();

                totalMilis += sec * 1000;
                totalMilis += (min * 60) * 1000;
            }
        }

        // - 1h bc Time adds an hour depending on your GTM
        // totalMilis -= 3600000;

        Time totalTime = new Time(totalMilis);

        return totalTime;
    }

    public Time getAvgResponseTime(TrainingSessionModel trainingSession) {
        List<CardResponseModel> cardResponses = new ArrayList<>();
        trainingSession.getResults().forEach(r -> r.getCardResponses().forEach(cr -> cardResponses.add(cr)));

        long totalMilis = 0;
        long milisMean = 0;

        for (CardResponseModel cardResponse : cardResponses) {
            LocalTime resTim = cardResponse.getResponseTime().toLocalTime();
            LocalDateTime responseTime = LocalDateTime.of(LocalDate.now(), resTim);

            long sec = responseTime.getSecond();
            long min = responseTime.getMinute();

            totalMilis += sec * 1000;
            totalMilis += (min * 60) * 1000;
        }

        if (cardResponses.size() != 0) {
            milisMean = totalMilis / cardResponses.size();

            // - 1h bc Time adds an hour depending on your GTM
            // milisMean -= 3600000;
        }

        Time time = new Time(milisMean);

        return time;
    }

    public int getPassRatio(TrainingSessionModel trainingSession) {
        List<ResultsModel> results = trainingSession.getResults();

        int passRatio = 0;
        int cardNum = results.size();
        int errorCount = 0;

        for (ResultsModel result : results) {
            if (result.getErrorCount() > 1) {
                errorCount++;
            }
        }

        if (errorCount >= cardNum) {
            passRatio = 0;
        } else {
            int correctCards = cardNum - errorCount;

            passRatio = (correctCards * 100) / cardNum;
        }

        return passRatio;
    }

    public int getAveragePassRatio(UserModel user) {
        int passRatio = 0;
        int sessionNumber = 0;

        for (TrainingModel training : user.getTrainings()) {
            for (TrainingSessionModel trainingsession : training.getTrainingSessions()) {
                passRatio += getPassRatio(trainingsession);
                sessionNumber++;
            }
        }

        if (sessionNumber != 0) {
            passRatio = passRatio / sessionNumber;
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

            float prevPassRatio = getPassRatio(prevTraining);

            if (prevPassRatio == 0) {
                gradeChange = currentPassRatio;
            } else {
                gradeChange = ((currentPassRatio / prevPassRatio) - 1) * 100;
            }
        }

        return gradeChange;
    }

    public Time getAvgResponseTime(UserModel user) {
        List<Time> avgResponseTimes = new ArrayList<>();
        long totalMilis = 0;
        long milisMean = 0;

        for (TrainingModel training : user.getTrainings()) {
            for (TrainingSessionModel trainingsession : training.getTrainingSessions()) {
                avgResponseTimes.add(getAvgResponseTime(trainingsession));
            }
        }

        for (Time time : avgResponseTimes) {
            LocalTime resTim = time.toLocalTime();
            LocalDateTime responseTime = LocalDateTime.of(LocalDate.now(), resTim);
            
            long sec = responseTime.getSecond();
            long min = responseTime.getMinute();

            totalMilis += sec * 1000;
            totalMilis += (min * 60) * 1000;
        }

        if (avgResponseTimes.size() != 0) {
            milisMean = totalMilis / avgResponseTimes.size();

            // - 1h bc Time adds an hour depending on your GTM
            // milisMean -= 3600000;
        }

        Time time = new Time(milisMean);

        return time;
    }

    public AtomicInteger getTotalStudies(UserModel user) {
        AtomicInteger totalStudies = new AtomicInteger(0);

        user.getTrainings().forEach(t -> totalStudies.getAndAdd(t.getTrainingSessions().size()));

        return totalStudies;
    }

    public AtomicInteger getMonthStudies(UserModel user) {
        AtomicInteger monthStudies = new AtomicInteger(0);
        LocalDate now = LocalDate.now();

        user.getTrainings()
                .forEach(t -> t.getTrainingSessions().stream()
                        .filter(ts -> ts.getDate().toLocalDateTime().getMonthValue() == now.getMonthValue())
                        .forEach(filteredTraining -> monthStudies.getAndIncrement()));

        return monthStudies;
    }

    public AtomicInteger getMonthStudies(DeckModel deck) {
        LocalDate now = LocalDate.now();
        List<TrainingModel> monthTraining = deck.getTrainings().stream()
                .filter(t -> t.getTrainingDate().toLocalDateTime().getMonthValue() == now.getMonthValue())
                .collect(Collectors.toCollection(ArrayList::new));

        return new AtomicInteger(monthTraining.size());
    }

    public Time getAvgResponseTime(DeckModel deck) {
        List<Time> avgResponseTimes = new ArrayList<>();
        long totalMilis = 0;
        long milisMean = 0;

        for (TrainingModel training : deck.getTrainings()) {
            for (TrainingSessionModel trainingsession : training.getTrainingSessions()) {
                avgResponseTimes.add(getAvgResponseTime(trainingsession));
            }
        }

        for (Time time : avgResponseTimes) {
            LocalTime resTim = time.toLocalTime();
            LocalDateTime responseTime = LocalDateTime.of(LocalDate.now(), resTim);

            long sec = responseTime.getSecond();
            long min = responseTime.getMinute();

            totalMilis += sec * 1000;
            totalMilis += (min * 60) * 1000;
        }

        if (avgResponseTimes.size() != 0) {
            milisMean = totalMilis / avgResponseTimes.size();

            // - 1h bc Time adds an hour depending on your GTM
            // milisMean -= 3600000;
        }

        Time time = new Time(milisMean);

        return time;
    }

    public int getAveragePassRatio(DeckModel deck) {
        int passRatio = 0;
        int sessionNumber = 0;

        for (TrainingModel training : deck.getTrainings()) {
            for (TrainingSessionModel trainingsession : training.getTrainingSessions()) {
                passRatio += getPassRatio(trainingsession);
                sessionNumber++;
            }
        }

        if (sessionNumber != 0) {
            passRatio = passRatio / sessionNumber;
        }

        return passRatio;
    }
}