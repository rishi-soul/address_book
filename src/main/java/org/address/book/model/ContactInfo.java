package org.address.book.model;

import java.io.Serializable;

/**
 * @author rishi
 *  pojo class of data model.
 */
public class ContactInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String contactId;
	private String name;
	private String email;
	private String phoneNumber;
	private String address;

	public ContactInfo() {}

	public ContactInfo(String contactId, String name, String email, String phoneNumber, String address) {

		this.contactId = contactId;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}
	
	public ContactInfo(String name, String email, String phoneNumber, String address) {

		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public ContactInfo(String contactId, String name, String email) {

		this.contactId = contactId;
		this.name = name;
		this.email = email;
	}

	public String getId() {
		return contactId;
	}

	public void setId(String id) {
		this.contactId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
