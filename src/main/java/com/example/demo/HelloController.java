package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.TxToken.Token;

@Controller
public class HelloController {
	final TxToken txToken;

	public HelloController(TxToken txToken) {
		this.txToken = txToken;
	}

	@ModelAttribute("token")
	Token token() {
		return txToken.create();
	}

	@GetMapping(path = "/")
	String form(Model model) {
		return "form";
	}

	@PostMapping(path = "/", params = "next")
	String next(Model model, @RequestParam Token token) {
		return "next";
	}

	@PostMapping(path = "/", params = "confirm")
	String confirm(Model model, @RequestParam Token token) {
		if (!txToken.check(token)) {
			return "error";
		}
		return "confirm";
	}

	@PostMapping(path = "/")
	String submit(Model model, @RequestParam Token token) {
		if (!txToken.check(token)) {
			return "error";
		}
		return "redirect:/?complete";
	}

	@GetMapping(path = "/", params = "complete")
	String complete(Model model) {
		return "complete";
	}
}
