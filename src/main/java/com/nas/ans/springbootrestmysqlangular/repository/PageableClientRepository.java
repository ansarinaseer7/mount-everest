package com.nas.ans.springbootrestmysqlangular.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.nas.ans.springbootrestmysqlangular.entity.Client;

public interface PageableClientRepository extends PagingAndSortingRepository<Client, Long> {
	
}
