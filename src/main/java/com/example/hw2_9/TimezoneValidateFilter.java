package com.example.hw2_9;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String timezoneVar = servletRequest.getParameter("timezone");

        try{
            if( timezoneVar != null ){
                ZoneId timezoneId = ZoneId.of(timezoneVar.replace(' ','+'));
            }

        }
        catch (DateTimeException e) {
            ((HttpServletResponse)servletResponse).setStatus(400);

            servletResponse.setContentType("text/html");
            servletResponse.getWriter().write("Invalid timezone");
            servletResponse.getWriter().close();

        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
