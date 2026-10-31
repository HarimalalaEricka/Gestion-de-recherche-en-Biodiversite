package mg.nexora.biodiversity.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import mg.nexora.biodiversity.model.enums.TypeToken;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void envoyerLien(String destinataire, String lien, TypeToken type) {
        String sujet;
        String texte;

        switch (type) {
            case INVITATION -> {
                sujet = "Nexora : vous êtes invité à rejoindre la plateforme";
                texte = "Bonjour,\n\n"
                    + "Un compte Nexora a été créé pour vous.\n"
                    + "Cliquez sur le lien ci-dessous pour choisir votre mot de passe :\n\n"
                    + lien + "\n\n"
                    + "Ce lien est à usage unique et expire automatiquement.";
            }
            case RESET_PASSWORD -> {
                sujet = "Nexora : réinitialisation de votre mot de passe";
                texte = "Bonjour,\n\n"
                    + "Vous avez demandé à réinitialiser votre mot de passe.\n"
                    + "Cliquez sur le lien ci-dessous :\n\n"
                    + lien + "\n\n"
                    + "Ce lien est à usage unique et expire automatiquement.\n"
                    + "Si vous n'êtes pas à l'origine de cette demande, ignorez ce message.";
            }
            default -> throw new IllegalArgumentException("Type inconnu : " + type);
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(destinataire);
            message.setSubject(sujet);
            message.setText(texte);
            mailSender.send(message);
            log.info("E-mail {} envoyé à {}", type, destinataire);
        } catch (Exception e) {
            log.warn("Envoi d'e-mail impossible ({}). Lien : {}", e.getMessage(), lien);
        }
    }
}