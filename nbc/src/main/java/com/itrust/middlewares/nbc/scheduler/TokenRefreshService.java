package com.itrust.middlewares.nbc.scheduler;

import com.itrust.middlewares.nbc.BaseService;
import com.itrust.middlewares.nbc.auth.services.AuthService;
import com.itrust.middlewares.nbc.exceptions.RestResponse;
import com.itrust.middlewares.nbc.logging.repository.LoggingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TokenRefreshService extends BaseService {

    @Value("${notification.email}")
    private String notificationEmail;

    private static final Logger logger = LoggerFactory.getLogger(TokenRefreshService.class);

    final JavaMailSender mailSender;

    final AuthService authService;

    public TokenRefreshService( JavaMailSender javaMailSender, AuthService authService) {
        this.mailSender = javaMailSender;
        this.authService = authService;
    }

    // Method to refresh the token
    public void refreshToken() {
        try {
            RestResponse response = authService.autoAuth();
            logger.info("started token refresh task");
            logSlack(response.getMessage());
        } catch (Exception e) {
            logger.error("Error during token refresh: ", e);
        }
    }

    // Send email
    private void sendEmail(String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(notificationEmail);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    // Schedule the token refresh every 10 hours
//    @Scheduled(fixedRate = 10 * 60 * 60 * 1000) // 10 hours in milliseconds
    public void scheduledTokenRefresh() {
        logger.info("Starting scheduled token refresh...");
        refreshToken();
    }
}

