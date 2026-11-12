package mg.nexora.biodiversity.config;

import mg.nexora.biodiversity.model.Utilisateur;
import mg.nexora.biodiversity.repository.UtilisateurRepository;
import mg.nexora.biodiversity.model.enums.Role;
import mg.nexora.biodiversity.model.enums.StatutUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    // Cree un utilisateur admin par défaut si la base de données est vide
    @Bean
    CommandLineRunner creerAdmin(UtilisateurRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.count() == 0) {
                Utilisateur admin = new Utilisateur();
                admin.setNom("Administrateur");
                admin.setEmail("admin@nexora.mg");
                admin.setMotDePasse(encoder.encode("Admin123!"));
                admin.setRole(Role.ADMIN);
                admin.setStatut(StatutUser.ACTIVE);
                admin.setDateCreation(java.time.LocalDateTime.now());
                repo.save(admin);
                System.out.println(">>> Admin créé : admin@nexora.mg / Admin123! (à changer)");
            }
        };
    }
}