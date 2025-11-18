package com.korutil.server.service;

import com.korutil.server.dto.user.EmailVerificationDto;
import com.korutil.server.handler.CustomRuntimeException;
import com.korutil.server.handler.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.from-custom-domain}")
    private String mailCustomDomain;
    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${spring.mail.host}")
    private String smtpServer;

    public void sendMail(EmailVerificationDto dto) {
        try {
            sendMailMessage( dto.getSubject(), dto.getEmail(), dto.getToken() );
        } catch (MailException e) {
            throw new CustomRuntimeException(ErrorCode.FAILED_EMAIL_SEND);
        }
    }

    private void sendMailMessage(String subject, String mailTo, String token ) throws MailException {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo( mailTo );
        simpleMailMessage.setSubject( subject );
        simpleMailMessage.setText( token );
//        simpleMailMessage.setFrom(mailFrom + "@" + getHost());
        simpleMailMessage.setFrom(mailCustomDomain);

        javaMailSender.send(simpleMailMessage);
    }

    private String getHost() {
        int lastDotIndex = smtpServer.lastIndexOf('.');
        int secondLastDotIndex = smtpServer.lastIndexOf('.', lastDotIndex - 1);
        return smtpServer.substring(secondLastDotIndex + 1);
    }
}
