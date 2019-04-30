package org.address.book.controller;

import static spark.Spark.*;

import org.address.book.exception.ContactException;
import org.address.book.model.ContactInfo;
import org.address.book.response.StandardResponse;
import org.address.book.response.StatusResponse;
import org.address.book.service.ContactService;
import org.address.book.service.ContactServiceImpl;

import com.google.gson.Gson;

/**
 * @author rishi
 *	This Class is main controller where each api request is executed.
 */
public class AddressBookController {

	public static void main(String[] args) {
		
		final ContactService contactService = new ContactServiceImpl();
		
		//this uri return contact info for unique name
		get("/contact/:name", (req, res) -> {
			res.type("application/json");
	
			return new Gson().toJson(new Gson().toJsonTree(contactService.getContact(req.params(":name"))));
		});

		//this uri return contact info based on query param, page size and page number
		get("/contact", (req, res) -> {
			res.type("application/json");
			
			  String pageSize = req.queryParams("pageSize"); 
			  String pageNumber =  req.queryParams("page");
			  String queryString = req.queryParams("query");
			 

			return new Gson().toJson(new Gson().toJsonTree(contactService.getContact(pageSize, pageNumber, queryString)));
		});

		// this uri saves contact information in elastic datastore.
		post("/contact", (request, response) -> {
			response.type("application/json");

			ContactInfo contact = new Gson().fromJson(request.body(), ContactInfo.class);
			contactService.addContact(contact);

			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
		});

		//this uri updates the existing record in elastic datastore.
		put("/contact/:name", (request, response) -> {
			response.type("application/json");

			ContactInfo contactInfo = new Gson().fromJson(request.body(), ContactInfo.class);
			contactService.editContact(contactInfo, request.params(":name"));
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS));
		});

		//this uri deletes the existing record in elastic datastore.
		delete("/contact/:name", (request, response) -> {
			response.type("application/json");

			contactService.deleteContact(request.params(":name"));
			return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "Contact Deleted"));
		});

		//this handles the custom exception thrown by class.
		get("/throwexception", (request, response) -> {
			throw new ContactException();
		});

		//this handles the custom exception thrown by class.
		exception(ContactException.class, (exception, request, response) -> {
			response.body(exception.getMessage());
		});

	}

}
