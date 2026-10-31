package mg.nexora.biodiversity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerLienReinitialisation(String destinataire, String lien) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinataire);
            message.setSubject("Nexora : réinitialisation de votre mot de passe");
            message.setText(
                "Bonjour,\n\n"
            + "Vous avez demandé à réinitialiser votre mot de passe.\n"
            + "Cliquez sur le lien ci-dessous (valable 1 heure) :\n\n"
            + lien + "\n\n"
            + "Si vous n'êtes pas à l'origine de cette demande, ignorez ce message.");
            mailSender.send(message);
            log.info("E-mail de réinitialisation envoyé à {}", destinataire);
        } catch (Exception e) {
            log.warn("Envoi d'e-mail impossible ({}). Lien de réinitialisation : {}", e.getMessage(), lien);
        }
    }
}