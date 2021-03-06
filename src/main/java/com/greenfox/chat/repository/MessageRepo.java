package com.greenfox.chat.repository;

import com.greenfox.chat.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Viktor on 2017-05-18.
 */
@Component
public interface MessageRepo extends CrudRepository<Message, Long> {

  Iterable<Message> findAllByOrderByTimestampDesc();
}
