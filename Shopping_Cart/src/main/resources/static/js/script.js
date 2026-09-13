/*alert("Hello\nHow are you?");*/


$(function() {
	var $userRegister = $("#userRegister");

	$userRegister.validate({

		rules: {
			name: {
				required: true,
				lettersonly: true
			},
			email: {
				required: true,
				space: true,
				email: true
			},
			mobileNumber: {
				required: true,
				space: true,
				numericOnly: true,
				minLength: 10,
				maxLength: 12
			},
			password: {
				required: true,
				space: true
			},
			confirmPassword: {
				required: true,
				space: true,
				equalTo: '#pass'
			},
			address: {
				required: true,

			},
			city: {
				required: true,
				space: true,
				lettersonly: true
			},
			state: {
				required: true,
				space: true,
				lettersonly: true
			},
			pincode: {
				required: true,
				space: true,
				numericOnly: true
			},
			img: {
				required: true
			}
		},
		messages: {
			name: {
				required: 'Name is required',
				lettersonly: 'Invalid name'
			},
			email: {
				required: 'Email address is required',
				space: 'Space is not allowed in Email',
				email: 'Invalid email'
			},
			mobileNumber: {
				required: 'Mobile number is required',
				space: 'Space not allowed in mobile number',
				numericOnly: 'Invalid mobile number',
				minLength: 'Minimum 10 digits required',
				maxLenght: 'Maximum 12 digits required'
			},
			password: {
				required: 'Password is required',
				space: 'Space not allowed in Password'
			},
			confirmPassword: {
				required: 'Confirm password is required',
				space: 'Space not allowed in Confirm Password',
				equalTo: 'Password mismatch'
			},
			address: {
				required: 'Address is required',
				all: 'Invalid Address'
			},
			city: {
				required: 'City name is required',
				space: 'Space is not allowed in City',
				lettersonly: 'Invalid city'
			},
			state: {
				required: 'State name is required',
				space: 'Space is not allowed in State',
				lettersonly: 'Invalid state'
			},
			pincode: {
				required: 'Pincode is required',
				space: 'Space is not allowed in Pincode',
				numericOnly: 'Invalid pincode'
			},
			img: {
				required: 'Image is required'
			}
		}
	})
})


jQuery.validator.addMethod('lettersonly', function(value, element) {
	return /^[^-\s][a-zA-Z_\s-]+$/.test(value);
})


jQuery.validator.addMethod('space', function(value, element) {
	return /^[^-\s]+$/.test(value);
})


jQuery.validator.addMethod('all', function(value, element) {
	return /^[^-\s][a-zA-Z0-9_,.\s-]+$/.test(value);
})


jQuery.validator.addMethod('numericOnly', function(value, element) {
	return /^[0-9]+$/.test(value);
}) 