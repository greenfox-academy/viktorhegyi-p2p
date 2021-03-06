package com.greenfox.chat.repository;

import com.greenfox.chat.model.NameOfUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Viktor on 2017-05-17.
 */
@Component
public interface UserRepo extends CrudRepository<NameOfUser, Long>{

}
