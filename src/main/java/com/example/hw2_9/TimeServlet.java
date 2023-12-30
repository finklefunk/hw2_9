package com.example.hw2_9;

import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@WebServlet(name = "time", value = "/time")
public class TimeServlet extends HttpServlet {

    private TemplateEngine engine;
    private ZonedDateTime message;

    public void init() {

       message = ZonedDateTime.now( ZoneId.of( "UTC" ) );

        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(this.getServletContext().getRealPath("/WEB-INF/templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        String timezoneVar = request.getParameter("timezone");

        if( timezoneVar != null) {
            timezoneVar = timezoneVar.replace(' ','+');
            response.addCookie(new Cookie("lastTimezone", timezoneVar));
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie != null){
                        if(cookie.getName() != null){
                            if (cookie.getName().equals("lastTimezone")) {
                                timezoneVar = cookie.getValue();
                                break;
                            }
                        }
                    }
                }
            }
            if( timezoneVar == null) {
                timezoneVar = "UTC";
            }
        }
        message = ZonedDateTime.now( ZoneId.of( timezoneVar ) );
        ;

        Context timeResponseContext = new Context(
                request.getLocale());
                timeResponseContext.setVariable("message", message);

        engine.process("timeRenderTemplate", timeResponseContext, response.getWriter());
        response.getWriter().close();
    }

    public void destroy() {
    }
}
