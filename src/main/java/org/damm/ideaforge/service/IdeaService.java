package org.damm.ideaforge.service;

import java.util.List;

import org.damm.ideaforge.pojo.Idea;
import org.damm.ideaforge.repository.IdeaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdeaService {

	@Autowired
	private IdeaRepository ideaRepository;

	public Idea saveIdea(Idea idea) {
		return ideaRepository.insert(idea);
	}

	public void updateIdea(Idea idea) {
		ideaRepository.update(idea);
	}

	public List<Idea> findAll() {
		return ideaRepository.findAll();
	}
}
