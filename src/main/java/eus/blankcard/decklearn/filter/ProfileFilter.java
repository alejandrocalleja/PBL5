package eus.blankcard.decklearn.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import eus.blankcard.decklearn.repository.user.UserRepository;

public class ProfileFilter implements Filter {

    @Autowired
    UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("User Filter");

        HttpServletRequest req = (HttpServletRequest) request;
        request.setCharacterEncoding("UTF-8");
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
        
        // Usar un helper que te devuelva si hay solo un parametro en la /. En caso de que haya uno, buscas si está en la db como username, si no devuelves un 404. Si hay mas de uno, devuelves un 404.
        // Si no hay ninguno, devuelves un 404.
    }
}