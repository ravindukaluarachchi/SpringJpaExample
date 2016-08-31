package com.example1;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class Demo1Application {

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}
	
	
}

@RestController
class HelloController{
	@Autowired
	CustomerRepository repository;	
	
	@RequestMapping(value="/")
	@Transactional(readOnly = false)
	public String index(ModelAndView model){
		long count = repository.count();
		Customer customer = new Customer("ravindu" + count ,25);
		
		Card card1 = new Card("Gold" + count);
		Card card2 = new Card("Silver");
		
		customer.addCard(card1);
		customer.addCard(card2);
		
		repository.save(customer);
		
		Card card3 = new Card("Platinum");
		customer.addCard(card3);
		repository.save(customer);
		
		List<Customer> customers = repository.findByNameLike("%ravindu%");
		
		//String s = customers.stream().map(c -> c.getId() + " " + c.getName()).reduce(",",String::concat);
		
		String s = customers.stream().map(c -> {
			String cards = "";
			if(c.getCards() != null && !c.getCards().isEmpty()){
				cards = c.getCards().stream().map(cd -> cd.getName()).collect(Collectors.joining(",", "[", "]"));
			}
			return c.getId() + " " + c.getName() + " " + cards;
		}).collect(Collectors.joining("<br/>"));
		return s ;
	}
}