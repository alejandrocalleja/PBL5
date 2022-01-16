package eus.blankcard.decklearn.controller;

import java.time.LocalDate;

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

        int totalStudies = 0;
        user.getTrainings().forEach(t -> totalStudies += t.getTrainingSessions().size());

        LocalDate now = LocalDate.now();

        int monthStudies = 0;
        user.getTrainings()
                .forEach(t -> t.getTrainingSessions().stream()
                        .filter(ts -> ts.getTrainingSessionDate().toLocalDate().getMonthValue() == now.getMonthValue())
                        .forEach(filteredTraining -> monthStudies++));

        req.setAttribute("user", user);
        req.setAttribute("totalStudies", totalStudies);
        req.setAttribute("studiesMonth", 0);
        req.setAttribute("avgTime", 0);
        req.setAttribute("total", 0);
        req.setAttribute("passRatio", 0);

        return "user/user_stats";
    }
}
