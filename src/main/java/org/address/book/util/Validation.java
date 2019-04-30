package org.address.book.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.address.book.exception.ContactException;
import org.address.book.model.ContactInfo;

/**
 * @author rishi
 *	Validation utility class.
 */
public class Validation {

	/**
	 * 
	 * Validate data sent in request.
	 * @param data
	 * @return  data
	 * @throws ContactException
	 */
	public ContactInfo validateContact(ContactInfo data) throws ContactException {
		try {
			
			if (data == null)
				throw new ContactException("User information not found");

			if (!validatePhoneNumber(data.getPhoneNumber())) {
				throw new ContactException("Phone number not valid. Please enter valid phone number.");
			}

			if (!validateEmail(data.getEmail())) {
				throw new ContactException("Email id is not valid");
			}

			return data;
		} catch (Exception ex) {
			throw new ContactException(ex.getMessage());
		}
	}

	/**
	 * 
	 * Validate Phone Number sent in contact.
	 * @param phoneNo
	 * @return
	 */
	private boolean validatePhoneNumber(String phoneNo) {
		// validate phone numbers of format "1234567890"
		if (phoneNo.matches("\\d{10}"))
			return true;
		// validating phone number with -, . or spaces
		else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
			return true;
		// validating phone number with extension length from 3 to 5
		else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}"))
			return true;
		// validating phone number where area code is in braces ()
		else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}"))
			return true;
		// return false if nothing matches the input
		else
			return false;

	}

	/**
	 * Validate email sent in contact
	 * @param email
	 * @return
	 */
	private boolean validateEmail(String email) {
		String regex = "^(.+)@(.+)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);

		return matcher.matches();
	}
}
