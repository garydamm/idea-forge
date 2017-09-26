package org.damm.ideaforge.controller;

import java.util.List;

import javax.validation.Valid;

import org.damm.ideaforge.pojo.Idea;
import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.TeamIdea;
import org.damm.ideaforge.service.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/idea")
public class IdeaController {

	@Autowired
	private IdeaService ideaService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		List<Idea> ideas = ideaService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("ideas", ideas);
		modelAndView.setViewName("idea/list");
		return modelAndView;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newIdea() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("idea", new Idea());
		modelAndView.setViewName("idea/new");
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid Idea idea, BindingResult bindingResult) {
		Idea savedIdea = ideaService.saveIdea(idea);
		return editResponse(savedIdea);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Idea idea, BindingResult bindingResult) {
		Idea updatedIdea = ideaService.updateIdea(idea);
		return editResponse(updatedIdea);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView loadForEdit(@PathVariable("id") Long id) {
		Idea idea = ideaService.find(id);
		return editResponse(idea);
	}

	@RequestMapping(value = "/addteam", method = RequestMethod.POST)
	public ModelAndView addmember(@Valid @ModelAttribute("teamIdea") TeamIdea teamIdea, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Team newTeamIdea = ideaService.addTeam(teamIdea.getIdeaId(), teamIdea.getTeamName());
		if (newTeamIdea == null) {
			bindingResult.rejectValue("teamName", "invalid.team.name", "invalid team name");
		}
		Idea idea = ideaService.find(teamIdea.getIdeaId());
		modelAndView.addObject("teamIdea", teamIdea);
		modelAndView.addObject("idea", idea);
		modelAndView.setViewName("idea/edit");
		return modelAndView;
	}

	private ModelAndView editResponse(Idea idea) {
		TeamIdea teamIdea = new TeamIdea();
		teamIdea.setIdeaId(idea.getId());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("idea", idea);
		modelAndView.addObject("teamIdea", teamIdea);
		modelAndView.setViewName("idea/edit");
		return modelAndView;
	}

}
