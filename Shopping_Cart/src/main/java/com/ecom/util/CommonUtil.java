package com.ecom.util;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.ecom.model.ProductOrder;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CommonUtil {

	@Autowired
	private JavaMailSender mailSender;

	public Boolean sendMail(String url, String recipientMail) throws UnsupportedEncodingException, MessagingException {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		String content = "<p>Hello, </p>" 
				+ "<p>You have requested to reset your password. </p>"
				+ "<p>Click the link below to change your password : </p>" 
				+ "<p><a href=\"" + url
				+ "\">Change my password</a></p>";

		helper.setFrom("herrypatel7290@gmail.com", "Shopping Cart");
		helper.setTo(recipientMail);
		helper.setSubject("Password Reset");
		helper.setText(content, true);
		mailSender.send(message);

		return true;
	}

	public static String generateUrl(HttpServletRequest request) {

		// http://localhost:8080/forgot-password
		String siteUrl = request.getRequestURL().toString();

		return siteUrl.replace(request.getServletPath(), "");
	}
	
	String orderContent = "<p>Thank you, your order has been placed successfully.</p>"
				  + "<p>Product Details :- </p>"
				  + "<p>Name : [[productName]] </p>"
				  + "<p>Category : [[category]] </p>"
				  + "<p>Quantity : [[quantity]] </p>"
				  + "<p>Price : [[price]] </p>"
				  + "<p>Payment Type : [[paymentType]] </p>";
			

	public Boolean sendMailForProductOrder(ProductOrder order) throws Exception, MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		orderContent = orderContent.replace("[[productName]]", order.getProduct().getTitle());
		orderContent = orderContent.replace("[[category]]", order.getProduct().getCategory());
		orderContent = orderContent.replace("[[quantity]]", Integer.toString(order.getQuantity()));
		orderContent = orderContent.replace("[[price]]", order.getPrice().toString());
		orderContent = orderContent.replace("[[paymentType]]", order.getPaymentType());

		helper.setFrom("herrypatel7290@gmail.com", "Shopping Cart");
		helper.setTo(order.getOrderAddress().getEmail());
		helper.setSubject("Product Order Status");
		helper.setText(orderContent, true);
		mailSender.send(message);

		return false;
	}
}
