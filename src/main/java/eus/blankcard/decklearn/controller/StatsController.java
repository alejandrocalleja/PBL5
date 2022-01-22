package eus.blankcard.decklearn.controller;

import java.time.LocalDate;
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

@Controller
public class StatsController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/stats")
    public String getStats(HttpServletRequest req, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        UserModel user = userRepository.findByUsername(currentPrincipalName);

        AtomicInteger totalStudies = new AtomicInteger(0);
        user.getTrainings().forEach(t -> totalStudies.getAndAdd(t.getTrainingSessions().size()));

        LocalDate now = LocalDate.now();

        AtomicInteger monthStudies = new AtomicInteger(0);
        user.getTrainings()
                .forEach(t -> t.getTrainingSessions().stream()
                        .filter(ts -> ts.getDate().toLocalDateTime().getMonthValue() == now.getMonthValue())
                        .forEach(filteredTraining -> monthStudies.getAndIncrement()));

                        // AtomicReference<Time> avgTime = new AtomicReference<>("");

        // user.getTrainings().forEach(t -> t.getTrainingSessions().forEach(ts -> ts.getResults().forEach(res -> {
        //     res.getAvgResTime().toLocalTime();
        // })));

        req.setAttribute("user", user);
        req.setAttribute("totalStudies", totalStudies);
        req.setAttribute("studiesMonth", monthStudies);
        req.setAttribute("saves", user.getSavedDecks().size());
        req.setAttribute("avgTime", 0);
        req.setAttribute("total", 0);
        req.setAttribute("passRatio", 0);
        req.setAttribute("stats", true);

        return "user/user_stats";
    }
}
