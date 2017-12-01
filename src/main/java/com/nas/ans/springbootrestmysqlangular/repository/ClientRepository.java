package com.nas.ans.springbootrestmysqlangular.repository;

import org.springframework.data.repository.CrudRepository;

import com.nas.ans.springbootrestmysqlangular.entity.Client;


public interface ClientRepository extends CrudRepository<Client, Long> {
	Client findByName(String name);
}
