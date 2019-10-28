package com.tomasov.raidmanagement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index() {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/secure", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView secureIndex() {
		return new ModelAndView("secure/index");
	}
	
}
