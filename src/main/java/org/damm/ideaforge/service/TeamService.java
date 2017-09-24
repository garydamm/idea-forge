package org.damm.ideaforge.service;

import java.util.List;

import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.User;
import org.damm.ideaforge.repository.TeamRepository;
import org.damm.ideaforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Autowired
	private UserRepository userRepository;

	public Team saveTeam(Team team) {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userRepository.findUserByEmail(currentUserEmail);
		teamRepository.insert(team, currentUser);
		return find(team.getId());
	}

	public Team updateTeam(Team team) {
		teamRepository.update(team);
		return find(team.getId());
	}

	public List<Team> findAll() {
		return teamRepository.findAll();
	}

	public void addMember(long teamId, String email) {
		User user = userRepository.findUserByEmail(email);
		teamRepository.addMember(teamId, user.getId());
	}

	public Team find(Long id) {
		Team team = teamRepository.find(id);
		User user = userRepository.find(team.getUserId());
		List<User> members = teamRepository.members(id);
		team.setCreatedBy(user);
		team.setMembers(members);
		return team;
	}

}
