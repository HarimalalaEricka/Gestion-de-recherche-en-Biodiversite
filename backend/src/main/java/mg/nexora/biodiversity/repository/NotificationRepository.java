package mg.nexora.biodiversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import mg.nexora.biodiversity.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
