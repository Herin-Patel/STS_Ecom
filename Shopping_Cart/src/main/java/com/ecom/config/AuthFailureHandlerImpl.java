package com.ecom.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ecom.model.UserDtls;
import com.ecom.repository.UserRepository;
import com.ecom.service.UserService;
import com.ecom.util.AppConstant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {

	@Autowired
	private UserRepository userRepositoryObj;

	@Autowired
	private UserService userServiceObj;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {

		String email = request.getParameter("username");

		UserDtls userDtlsObj = userRepositoryObj.findByEmail(email);

		if (userDtlsObj != null) {
			
			if (userDtlsObj.getIsEnable()) {

				if (userDtlsObj.getAccountNonLocked()) {

					if (userDtlsObj.getFailedAttempt() < AppConstant.ATTEMPT_TIME) {
						userServiceObj.increaseFailedAttempt(userDtlsObj);
					} else {
						userServiceObj.userAccountLock(userDtlsObj);
						exception = new LockedException("Your account is locked ! After failed attempt 3 times");
					}
				} else {

					if (userServiceObj.unlockAccountTimeExpired(userDtlsObj)) {
						exception = new LockedException("Your account is Unlocked ! Please try to login");
					} else {
						exception = new LockedException("Your account is Locked ! Please try again after sometime");
					}
				}
			} else {
				exception = new LockedException("Your account is Inactive !");
			}
		} else {
			exception = new LockedException("Email or password is invalid !");
		}

		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}
}
