package org.damm.ideaforge.controller;

import java.util.List;

import javax.validation.Valid;

import org.damm.ideaforge.pojo.Team;
import org.damm.ideaforge.pojo.TeamMember;
import org.damm.ideaforge.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/team")
public class TeamController {

	@Autowired
	private TeamService teamService;

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView list() {
		List<Team> teams = teamService.findAll();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("teams", teams);
		modelAndView.setViewName("team/list");
		return modelAndView;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public ModelAndView newTeam() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("team", new Team());
		modelAndView.setViewName("team/new");
		return modelAndView;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView create(@Valid Team team, BindingResult bindingResult) {
		Team savedTeam = teamService.saveTeam(team);
		return editResponse(savedTeam);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public ModelAndView edit(@Valid Team team, BindingResult bindingResult) {
		Team updatedTeam = teamService.updateTeam(team);
		return editResponse(updatedTeam);
	}

	@RequestMapping(value = "/addmember", method = RequestMethod.POST)
	public ModelAndView addmember(@Valid TeamMember teamMember, BindingResult bindingResult) {
		teamService.addMember(teamMember.getTeamId(), teamMember.getEmail());
		Team team = teamService.find(teamMember.getTeamId());
		return editResponse(team);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView loadForEdit(@PathVariable("id") Long id) {
		Team team = teamService.find(id);
		return editResponse(team);
	}

	private ModelAndView editResponse(Team team) {
		TeamMember teamMember = new TeamMember();
		teamMember.setTeamId(team.getId());

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("team", team);
		modelAndView.addObject("member", teamMember);
		modelAndView.setViewName("team/edit");
		return modelAndView;
	}

}
