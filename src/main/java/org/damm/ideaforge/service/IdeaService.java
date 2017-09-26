package org.damm.ideaforge.service;

import java.util.List;

import org.damm.ideaforge.pojo.Idea;
import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.User;
import org.damm.ideaforge.repository.IdeaRepository;
import org.damm.ideaforge.repository.TeamRepository;
import org.damm.ideaforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class IdeaService {

	@Autowired
	private IdeaRepository ideaRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	public Idea saveIdea(Idea idea) {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userRepository.findUserByEmail(currentUserEmail);
		ideaRepository.insert(idea, currentUser);
		return find(idea.getId());
	}

	public Idea updateIdea(Idea idea) {
		ideaRepository.update(idea);
		return find(idea.getId());
	}

	public List<Idea> findAll() {
		return ideaRepository.findAll();
	}

	public Team addTeam(long ideaId, String teamName) {
		Team team = teamRepository.findByName(teamName);
		if (team != null) {
			ideaRepository.addTeam(team.getId(), ideaId);
		}
		return team;
	}

	public Idea find(Long id) {
		Idea idea = ideaRepository.find(id);
		User user = userRepository.find(idea.getUserId());
		List<Team> teams = ideaRepository.teams(id);
		idea.setCreatedBy(user);
		idea.setTeams(teams);
		return idea;
	}
}
