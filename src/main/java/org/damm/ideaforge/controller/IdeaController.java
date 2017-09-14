package org.damm.ideaforge.controller;

import java.util.List;

import javax.validation.Valid;

import org.damm.ideaforge.pojo.Idea;
import org.damm.ideaforge.service.IdeaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
		ideaService.updateIdea(idea);
		return editResponse(idea);
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView editFromID(@PathVariable("id") Long id) {
		Idea idea = ideaService.find(id);
		return editResponse(idea);
	}

	private ModelAndView editResponse(Idea idea) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("idea", idea);
		modelAndView.setViewName("idea/edit");
		return modelAndView;
	}

}
