package Weather.model.repository;

import Weather.model.entity.Subscriber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SubscriberRepository extends MongoRepository<Subscriber, String> {
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
}
