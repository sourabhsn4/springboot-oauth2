package com.example.web.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Repository;

import com.example.web.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

@Repository
@Transactional
public class UserService {

	//PersistenceContext gives the entityManager for current transaction. 
	//It can be used only with @Transactional and @Repository
	@PersistenceContext
	private EntityManager entityManager;
	
	public void insertUser() {		
		User u = new User();
			u.setFirstName("Aakaash")
				.setLastName("Mandwal");
		entityManager.persist(u);
	}
	
	public ArrayNode getResult(String firstName, String lastName) {
		 ObjectMapper objMapper = new ObjectMapper();
		 
		 ArrayNode arrayNode = objMapper.createArrayNode();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> userRoot = cq.from(User.class);
		
		ParameterExpression<String> pFirstName = cb.parameter(String.class);
		ParameterExpression<String> pLastName = cb.parameter(String.class);
		cq.select(userRoot).where(cb.equal(userRoot.get("firstName"), pFirstName), 
									cb.equal(userRoot.get("lastName"), pLastName));
		List<User> userList = entityManager.createQuery(cq)
			.setParameter(pFirstName, firstName)
			.setParameter(pLastName, lastName)
			.getResultList();
			
		System.out.println("User list size: "+ userList.size());
		userList.stream()
				.forEach((u) -> {
					System.out.println(u.toString());
					arrayNode.add(
							objMapper.createObjectNode()
							   .put("first_name", u.getFirstName())
							);
				});		
		return arrayNode;
	}
}
