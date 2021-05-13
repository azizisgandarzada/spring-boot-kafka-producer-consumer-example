package com.azizi.notification.repository;

import com.azizi.notification.document.Email;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailRepository extends MongoRepository<Email, String> {

}
