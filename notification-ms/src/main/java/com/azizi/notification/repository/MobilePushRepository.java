package com.azizi.notification.repository;

import com.azizi.notification.document.MobilePush;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MobilePushRepository extends MongoRepository<MobilePush, String> {

}
