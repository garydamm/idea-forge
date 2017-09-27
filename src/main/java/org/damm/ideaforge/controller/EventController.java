package org.damm.ideaforge.controller;

import java.util.List;

import javax.validation.Valid;

import org.damm.ideaforge.pojo.Event;
import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.TeamEvent;
import org.damm.ideaforge.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/event")
public class EventController {

	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		List<Event> events = eventService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("events", events);
		modelAndView.setViewName("event/list");
		return modelAndView;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newEvent() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("event", new Event());
		modelAndView.setViewName("event/new");
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid Event event, BindingResult bindingResult) {
		Event savedEvent = eventService.saveEvent(event);
		return editResponse(savedEvent);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Event event, BindingResult bindingResult) {
		Event updatedEvent = eventService.updateEvent(event);
		return editResponse(updatedEvent);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView loadForEdit(@PathVariable("id") Long id) {
		Event event = eventService.find(id);
		return editResponse(event);
	}

	@RequestMapping(value = "/addteam", method = RequestMethod.POST)
	public ModelAndView addmember(@Valid @ModelAttribute("teamEvent") TeamEvent teamEvent,
	        BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Team newTeamEvent = eventService.addTeam(teamEvent.getEventId(), teamEvent.getTeamName());
		if (newTeamEvent == null) {
			bindingResult.rejectValue("teamName", "invalid.team.name", "invalid team name");
		}
		Event event = eventService.find(teamEvent.getEventId());
		modelAndView.addObject("teamEvent", teamEvent);
		modelAndView.addObject("event", event);
		modelAndView.setViewName("event/edit");
		return modelAndView;
	}

	private ModelAndView editResponse(Event event) {
		TeamEvent teamEvent = new TeamEvent();
		teamEvent.setEventId(event.getId());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("event", event);
		modelAndView.addObject("teamEvent", teamEvent);
		modelAndView.setViewName("event/edit");
		return modelAndView;
	}

}
