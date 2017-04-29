package kr.ac.hansung.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import kr.ac.hansung.exception.ErrorResponse;
import kr.ac.hansung.exception.UserDuplicatedException;
import kr.ac.hansung.exception.UserNotFoundException;


// application 전체의 Exception 을 처리 하기 위해서 사용하는 Annotation
@ControllerAdvice
public class GlobalExceptionController {

	// ----------------------------- Exception Handler  --------------------------
		
		@ExceptionHandler(UserNotFoundException.class)
		public ResponseEntity<ErrorResponse> 
		handleUserNotFoundException(HttpServletRequest req, UserNotFoundException ex) {
			
			ErrorResponse errorResponse = new ErrorResponse();
			
			String requestURL = req.getRequestURL().toString();
			errorResponse.setRequestURL(requestURL);
			errorResponse.setErrorCode("user.notfound.exception");
			errorResponse.setErrorMsg("User with id " + ex.getUserId() + " not found");
			
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
			
		}
		
	// ----------------------------- Exception Handler  --------------------------
		
		@ExceptionHandler(UserDuplicatedException.class)
		public ResponseEntity<ErrorResponse> 
		handleUserDuplicatedException(HttpServletRequest req, UserDuplicatedException ex) {
			
			ErrorResponse errorResponse = new ErrorResponse();
			
			String requestURL = req.getRequestURL().toString();
			errorResponse.setRequestURL(requestURL);
			errorResponse.setErrorCode("user.duplicated.exception");
			errorResponse.setErrorMsg("Unable to create. A user with name " + ex.getUsername() + 
					" already exist");
			
			return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
			
		}
}
