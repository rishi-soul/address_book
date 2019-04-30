package org.address.book.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;

import org.address.book.exception.ContactException;
import org.address.book.model.ContactInfo;
import org.address.book.service.ContactServiceImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author rishi
 *	Test Class to mock Elastic Search.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactServiceImplTest {

	private static ContactServiceImpl mockedBookDAL;
	private static ContactInfo contact1;
	private static ContactInfo contact2;

	@BeforeClass
	public static void setUp() throws ContactException {
		mockedBookDAL = mock(ContactServiceImpl.class);
		contact1 = new ContactInfo("John", "abc@gmail.com", "3452341234", "California");
		contact2 = new ContactInfo("Albert", "fgh@gmail.com", "0987654321", "Texas");

		when(mockedBookDAL.getContact("John")).thenReturn(contact1);
		when(mockedBookDAL.getContact("1", "2", "name:John"))
				.thenReturn((Collection<ContactInfo>) Arrays.asList(contact1));

	}

	@Test
	public void testGetContact() throws Exception {

		ContactInfo contactInfo = mockedBookDAL.getContact("John");
		assertEquals("John", contactInfo.getName());
		assertEquals("abc@gmail.com", contactInfo.getEmail());
		assertEquals("3452341234", contactInfo.getPhoneNumber());
		assertEquals("California", contactInfo.getAddress());

	}

	@Test
	public void testGetAllContact() throws Exception {

		Collection<ContactInfo> contactInfo = mockedBookDAL.getContact("1", "2", "name:John");
		assertEquals(1, contactInfo.size());

	}

	@Test
	public void testeditContact() throws Exception {
		mockedBookDAL.editContact(contact1, "John");
	}

	@Test
	public void testaddContact() throws Exception {
		mockedBookDAL.addContact(contact1);
	}

}