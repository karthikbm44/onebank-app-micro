package com.bank.oneBank.service;

import com.bank.oneBank.dto.EmailDetails;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmailAlerts(EmailDetails emailDetails);

}
