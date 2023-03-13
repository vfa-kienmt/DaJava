package com.springmvc.DaJava.repositories;

import com.springmvc.DaJava.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    User findByFullName(String fullName);
}
