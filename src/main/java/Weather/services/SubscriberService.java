package Weather.services;

import Weather.model.entity.Subscriber;
import Weather.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {
    private SubscriberRepository subscriberRepository;
    @Autowired
    public SubscriberService(SubscriberRepository subscriberRepository){
        this.subscriberRepository = subscriberRepository;
    }


    public int subscribersCount(){
        return subscriberRepository.findAll().size();
    }

    public boolean saveDeleteSubscriber(String email){
        if(subscriberRepository.existsByEmail(email)){
            subscriberRepository.deleteByEmail(email);
            return true;
        }else{
            subscriberRepository.save(new Subscriber(email));
            return false;
        }
    }

    public List<Subscriber> getSubscribers() {
        return subscriberRepository.findAll();
    }

    public int amountOfSubscribers(){
        return subscriberRepository.findAll().size();
    }
}
