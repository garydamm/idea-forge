package org.damm.ideaforge.service;

import org.damm.ideaforge.pojo.User;
import org.damm.ideaforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public void saveUser(User user) {
		userRepository.insert(user);
	}

}
