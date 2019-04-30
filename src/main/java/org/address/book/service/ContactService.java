package org.address.book.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.address.book.exception.ContactException;
import org.address.book.model.ContactInfo;

/**
 * @author rishi
 * 
 * Interface layer abstract of all the api service that needs to be overridden.
 *
 */
public interface ContactService {
	public void addContact(ContactInfo ContactInfo) throws ContactException;

    public Collection<ContactInfo> getContact(String pageSize, String pageNumber, String stringQuery);

    public ContactInfo getContact(String name) throws ContactException;

    public void editContact(ContactInfo contactInfo, String name) throws ContactException;

    public void deleteContact(String name) throws ContactException;

}
