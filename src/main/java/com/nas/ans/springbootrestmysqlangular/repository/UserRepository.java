package com.nas.ans.springbootrestmysqlangular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nas.ans.springbootrestmysqlangular.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByName(String name);

}
