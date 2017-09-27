package org.damm.ideaforge.service;

import java.util.List;

import org.damm.ideaforge.pojo.Event;
import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.User;
import org.damm.ideaforge.repository.EventRepository;
import org.damm.ideaforge.repository.TeamRepository;
import org.damm.ideaforge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class EventService {

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TeamRepository teamRepository;

	public Event saveEvent(Event event) {
		String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
		User currentUser = userRepository.findUserByEmail(currentUserEmail);
		eventRepository.insert(event, currentUser);
		return find(event.getId());
	}

	public Event updateEvent(Event event) {
		eventRepository.update(event);
		return find(event.getId());
	}

	public List<Event> findAll() {
		return eventRepository.findAll();
	}

	public Event find(Long id) {
		Event event = eventRepository.find(id);
		User user = userRepository.find(event.getUserId());
		List<Team> teams = eventRepository.teams(id);
		event.setCreatedBy(user);
		event.setTeams(teams);
		return event;
	}

	public Team addTeam(long eventId, String teamName) {
		Team team = teamRepository.findByName(teamName);
		if (team != null) {
			eventRepository.addTeam(team.getId(), eventId);
		}
		return team;
	}

}
