package com.example.web.controllers;


import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.web.service.UserService;

@Controller
public class TestComponent {

	@Autowired
	private UserService userService;
	
	@RequestMapping(path = "/call/mapping", method = RequestMethod.GET)
	public void testSql() {
		System.out.println("Inside testSql method");
		userService.insertUser();
	}
	
	@ResponseBody
	@RequestMapping(path = "/search", method = RequestMethod.POST)
	public ResponseEntity<Object> search(@RequestParam("firstName") String firstName, 
										@RequestParam("lastName") String lastName){
		System.out.println("FirstName : " + firstName);
		return new ResponseEntity<Object>(userService.getResult(firstName, lastName), HttpStatus.OK);
	}
	
	@ResponseBody
	@RequestMapping(path="/afterLogin", method = RequestMethod.GET)
	public ResponseEntity<Object> loginSuccess(@RequestParam(name = "token", required = false) String token){
		System.out.println("Token : "+ token);
		return new ResponseEntity<Object>("token-received", HttpStatus.OK);
	}
	
	static final String dirPath = "d:\\test-files\\pics\\";
	@ResponseBody
	@RequestMapping(path = "/uploadfile", method = RequestMethod.POST)
	public void uploadFile(@RequestParam("file") MultipartFile file) {
		System.out.println("File name: "+ file.getOriginalFilename());
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		
		try {
			file.transferTo(new File(dirPath+file.getOriginalFilename()));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
