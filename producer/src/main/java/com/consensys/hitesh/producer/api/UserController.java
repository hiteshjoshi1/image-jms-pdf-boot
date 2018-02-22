package com.consensys.hitesh.producer.api;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.consensys.hitesh.producer.model.User;
import com.consensys.hitesh.producer.security.validator.UserValidator;
import com.consensys.hitesh.producer.services.SecurityService;
import com.consensys.hitesh.producer.services.UserSignUpService;

import io.swagger.annotations.Api;

/**
 * 
 * @author hitjoshi
 *
 */
@Controller
@Api(value = " UserAPI", description = "Api for User Security")
public class UserController {

	protected Logger logger = Logger.getLogger(UserController.class.getName());

	@Autowired
	private UserSignUpService userSignUpService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private UserValidator userValidator;

	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("userForm", new User());

		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			model.addAttribute("userForm", new User());
			return "registration";
		}

		userSignUpService.save(userForm);

		securityService.autologin(userForm.getUsername(), userForm.getPasswordConfirm());

		return "redirect:/welcome";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}

	@RequestMapping(value = { "/welcome","/" }, method = RequestMethod.GET)
	public String welcome(Model model) {
		return "welcome";
	}
}
