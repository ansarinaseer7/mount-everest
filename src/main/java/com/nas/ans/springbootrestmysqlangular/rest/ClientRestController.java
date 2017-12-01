package com.nas.ans.springbootrestmysqlangular.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.nas.ans.springbootrestmysqlangular.common.exception.RestApiException;
import com.nas.ans.springbootrestmysqlangular.entity.Client;
import com.nas.ans.springbootrestmysqlangular.repository.ClientRepository;
import com.nas.ans.springbootrestmysqlangular.repository.PageableClientRepository;

@RestController
@RequestMapping("/client")
@CrossOrigin  //used to configure CORS i.e. Cross-origion-resource-sharing removes the security threat by cross origion like in our case rest api is running on port 8080 and front end code is running on port 4200
public class ClientRestController {
	public static final Logger logger = LoggerFactory.getLogger(ClientRestController.class);
	
	@Autowired
	ClientRepository clientRepository;
	
	@Autowired
	PageableClientRepository pageableClientRepository;
	
	/*@RequestMapping(path="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Client> getClients(){
		return new ResponseEntity<>(new Client(), HttpStatus.OK);
	}*/
	
	@RequestMapping(path="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Page<Client> getAllClients(@PageableDefault(size = 10) Pageable  pageable){
		Page<Client> clientList = pageableClientRepository.findAll(new PageRequest(0, Integer.MAX_VALUE));
		
		return clientList;
	}
	
	
	
	/*@RequestMapping(path="", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE,	consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Client> createClient(@RequestBody	Client client){
	
		return new ResponseEntity<>(new Client(), HttpStatus.CREATED);
	}*/
	
	@RequestMapping(path="", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<?> createClient(@RequestBody	Client client, UriComponentsBuilder ucBuilder) {
		if (doesClientExist(client)) {
			logger.error("Unable to create a client with name " + client.getName());
			return new ResponseEntity<Object>(new RestApiException(client.getName() + " already exist."),HttpStatus.CONFLICT);
		}
		clientRepository.save(client);
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/client/{id}").buildAndExpand(client.getId()).toUri());
		
		return new ResponseEntity<Client>(client, HttpStatus.CREATED);
	}
	
	/*
	@RequestMapping(path="", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Client> updateClient(@RequestBody	Client clientToBeCreated){
		
		return new ResponseEntity<>(new Client(), HttpStatus.OK);
	}*/
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(path = "/{clientId}", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Client> updateClient(@PathVariable long clientId, @RequestBody	Client client){
		Client currentClient = clientRepository.findOne(clientId);
		if (currentClient == null) {
			logger.error("Unable to update the client with id {}", clientId);
			
			return new ResponseEntity(new RestApiException("Unable to upate this client with id: " + clientId ), HttpStatus.NOT_FOUND);
		}
		currentClient.setName(client.getName());
		currentClient.setAddress(client.getAddress());
		currentClient.setDate(client.getDate());

		clientRepository.save(currentClient);
		
		return new ResponseEntity<Client>(currentClient, HttpStatus.OK);
	}
	
	/*@RequestMapping(path = "/{clientId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteClient(@PathVariable long clientId){
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}*/
    // To replace cllient entirely 
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(path = "/{clientId}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteClient(@PathVariable long clientId){
		logger.info("Fetching & Deleting a client with id {}", clientId);
		Client currentClient = clientRepository.findOne(clientId);
		if (currentClient == null) {
			logger.error("Unable to delete the client with id {}", clientId);
			return new ResponseEntity(new RestApiException("Unable to delete this client with id: " + clientId ), HttpStatus.NOT_FOUND);
		}
		clientRepository.delete(clientId);
	
		return new ResponseEntity<Client>(HttpStatus.NO_CONTENT);	
	}
	//implementing PATCH http method to do the partial update
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PatchMapping("/patch/{id}")
	public ResponseEntity<?> partialUpdateAddress(
	  @RequestBody String address, @PathVariable long id) {
	    
		Client currentClient = clientRepository.findOne(id);
		if (currentClient == null) {
			logger.error("Unable to update the client with id {}", id);
			
			return new ResponseEntity(new RestApiException("Unable to upate this client with id: " + id ), HttpStatus.NOT_FOUND);
		}
		currentClient.setAddress(address);
		clientRepository.save(currentClient);
		
		return ResponseEntity.ok("resource address updated");
	}
	
	//checking if client exists
	public boolean doesClientExist(Client client) {
		return clientRepository.findByName(client.getName()) != null;
	}
	
	@RequestMapping(path = "/{clientId}", method=RequestMethod.GET)
	public Client getClientById(@PathVariable long clientId){
		return this.clientRepository.findOne(clientId);
	}
}
