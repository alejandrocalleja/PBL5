package eus.blankcard.decklearn.controller;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import eus.blankcard.decklearn.models.user.UserModel;
import eus.blankcard.decklearn.repository.user.UserRepository;
import eus.blankcard.decklearn.util.StatsCalculator;

@Controller
public class StatsController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StatsCalculator statsCalculator;

    @GetMapping("/stats")
    public String getStats(HttpServletRequest req, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        
        UserModel user = userRepository.findByUsername(currentPrincipalName);

        AtomicInteger totalStudies = statsCalculator.getTotalStudies(user);
        AtomicInteger monthStudies = statsCalculator.getMonthStudies(user);
        int savedDecks = user.getSavedDecks().size();
        Time avgTime = statsCalculator.getAvgResponseTime(user);
        String timeFormat = new SimpleDateFormat("mm:ss").format(avgTime);

        int avgPass = statsCalculator.getAveragePassRatio(user);

        req.setAttribute("user", user);
        req.setAttribute("totalStudies", totalStudies);
        req.setAttribute("studiesMonth", monthStudies);
        req.setAttribute("saves", savedDecks);
        req.setAttribute("avgTime", timeFormat);
        req.setAttribute("averagePass", avgPass);
        req.setAttribute("stats", true);

        return "user/user_stats";
    }
}
