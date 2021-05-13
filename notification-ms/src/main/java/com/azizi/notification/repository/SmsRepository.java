package com.azizi.notification.repository;

import com.azizi.notification.document.Sms;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SmsRepository extends MongoRepository<Sms, String> {

}
