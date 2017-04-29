package kr.ac.hansung.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;
import kr.ac.hansung.model.User;
import kr.ac.hansung.service.UserService;

@RestController		// @Controller + @ResponseBody
@RequestMapping("/api")
public class RestAPIController {
	@Autowired
	UserService userService;
	
	// ----------------------------- Retrieve All Users -----------------------------
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public ResponseEntity<List<User>> listAllUsers(){
		// ResponseEntity = header, body(json), HTTP.status �������� �����ؼ� �ѱ� �� ����.
		// JSON format ���� �ٲپ �Ѱ��� ( jackson.databind )
		List<User> users = userService.findAllUsers();
		if(users.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users,HttpStatus.OK);
	}
	
	// ----------------------------- Retrieve Single User --------------------------
	
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
	public ResponseEntity<User> getUser(@PathVariable("id") long id){
		User user = userService.findById(id);
		if(user == null ){
			// to do : custom Exception
			throw new UserNotFoundException(id);
		}
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	// ----------------------------- Create a User -----------------------------------
	
		@RequestMapping(value="/users", method=RequestMethod.POST)	// Request body(json)�� ������� ������ json �������� �Ѿ��
		public ResponseEntity<Void> createUser(@RequestBody User user, 
				UriComponentsBuilder ucBuilder) {
			// Body �κ��� ���ٴ� �ǹ̷� <Void> �� �־���

			if(userService.isUserExist(user)){
				// to do Exception , User�� �̹� ������ ��� ����
				throw new UserDuplicatedException(user.getName());
			}
			userService.saveUser(user);
			
			// header �� ������� uri �� �Ѱ���
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/api/users/{id}").
					buildAndExpand(user.getId()).toUri());
			return new ResponseEntity<Void>(headers,HttpStatus.CREATED);
		}
		

	// ----------------------------- Update a User --------------------------
		
		@RequestMapping(value="/users/{id}", method=RequestMethod.PUT)
		public ResponseEntity<User> udpateUser(@PathVariable("id") long id, @RequestBody User user){
			User currentUser = userService.findById(id);
			if(currentUser == null ){
				// to do Exception 
				throw new UserNotFoundException(id);
			}
			currentUser.setName(user.getName());
			currentUser.setAge(user.getAge());
			currentUser.setSalary(user.getSalary());
			
			userService.updateUser(currentUser);
			return new ResponseEntity<User>(currentUser,HttpStatus.OK);
		}
		
	// ----------------------------- Delete a User --------------------------
		
		@RequestMapping(value="/users/{id}", method=RequestMethod.DELETE)
		public ResponseEntity<User> deleteUser(@PathVariable("id") long id){
			
			User user = userService.findById(id);
			if(user == null ){
				// to do Exception 
				throw new UserNotFoundException(id);
			}
			userService.deleteUserById(id);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
		
	// ----------------------------- Delete All User --------------------------
		
		@RequestMapping(value="/users", method=RequestMethod.DELETE)
		public ResponseEntity<User> deleteAllUsers(){
			
			userService.deleteAllUsers();
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
	
}
