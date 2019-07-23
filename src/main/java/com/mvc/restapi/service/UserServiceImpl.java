package com.mvc.restapi.service;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvc.restapi.entity.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> list() {
		Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery <User> cq = cb.createQuery(User.class);
        Root <User> root = cq.from(User.class);
        cq.select(root);
        Query query = session.createQuery(cq);
        return query.getResultList();
	}

	@Override
	@Transactional
	public User find(Integer id) {
		Session session = sessionFactory.getCurrentSession();
        return session.byId(User.class).load(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseEntity<Object> add(User user) {
		String status = "Failed to save User";
		Session session = sessionFactory.getCurrentSession();
		User oldUser = this.find(user.getId());
		if (oldUser != null) {
			status = "User id already exists";
			return new ResponseEntity<>(status, HttpStatus.CONFLICT);
		}
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery <User> cq = cb.createQuery(User.class);
		Root <User> root = cq.from(User.class);
		Path<Object> name = root.get("name");
		Path<Object> email = root.get("email");
		Predicate predicateName = cb.equal(name, user.getName());
		Predicate predicateEmail = cb.equal(email, user.getEmail());
		Predicate predicateUser = cb.or(predicateName, predicateEmail);
		
		cq.select(root).where(predicateUser);
		Query query = session.createQuery(cq);
		oldUser = (User) query.getResultList().stream().findFirst().orElse(null);
		
		if (oldUser != null && StringUtils.equalsIgnoreCase(oldUser.getEmail(), user.getEmail()) ||
			oldUser != null && StringUtils.equalsIgnoreCase(oldUser.getName(), user.getName())) {
			status = "User email/name already exists";
			return new ResponseEntity<>(status, HttpStatus.CONFLICT);
		}
		session.save(user);
		status = "Saved the user";
		return new ResponseEntity<>(status, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<Object> update(Integer id, User user) {
		String status = "Failed to find the User";
		Session session = sessionFactory.getCurrentSession();
		if (this.find(id) != null) {
			user.setId(id);
			session.merge(user);
			status = "User updated";
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}

	@Override
	@Transactional
	public ResponseEntity<Object> delete(Integer id) {
		String status = "Failed to find the User";
		Session session = sessionFactory.getCurrentSession();
		User user = this.find(id);
		if (user != null) {
			session.delete(user);
			status = "User Deleted";
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}

}
