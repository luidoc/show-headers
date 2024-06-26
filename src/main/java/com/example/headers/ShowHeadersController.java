package com.example.headers;

import java.io.PrintWriter;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/headers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ShowHeadersController {

    private static final Logger LOG = LoggerFactory.getLogger(ShowHeadersController.class);

    @RequestMapping(value = { "", "/**" }, method = { RequestMethod.POST, RequestMethod.PATCH, RequestMethod.HEAD,
            RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.GET })
    public void headersProcess(final HttpServletRequest servletRequest, final HttpServletResponse servletResponse) {
        servletResponse.setContentType("text/html");
        try {
            PrintWriter pwriter = servletResponse.getWriter();
            Enumeration<String> headerNames = servletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = (String) headerNames.nextElement();
                String value = servletRequest.getHeader(key);
                pwriter.println("Header " + key + " -> " + value + "<br/>");
                LOG.info("Header " + key + " -> " + value);
            }

            StringBuilder sb = new StringBuilder();
            if (servletRequest.getHeader("x-forwarded-proto") != null) {
                sb.append(servletRequest.getHeader("x-forwarded-proto")).append("://");
            } else {
                sb.append(servletRequest.getScheme()).append("://");
            }
            if (servletRequest.getHeader("x-forwarded-host") != null) {
                sb.append(servletRequest.getHeader("x-forwarded-host"));
            } else {
                sb.append(servletRequest.getServerName());
            }
            if (servletRequest.getHeader("x-forwarded-proto") == null) {
                boolean standardPort = servletRequest.getServerPort() == 80 || servletRequest.getServerPort() == 443;
                if (!standardPort) {
                    sb.append(":").append(servletRequest.getServerPort());
                }
            }
            sb.append(servletRequest.getContextPath());

            pwriter.println("Return url: " + sb.toString() + "<br/>");
            LOG.info("Return url: " + sb.toString());
            pwriter.println("Return by getScheme: " + servletRequest.getScheme() + "<br/>");
            LOG.info("Return by getScheme: " + servletRequest.getScheme());
            pwriter.close();
            servletResponse.setStatus(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
