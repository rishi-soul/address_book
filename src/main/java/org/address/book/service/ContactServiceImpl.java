package org.address.book.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.address.book.exception.ContactException;
import org.address.book.model.ContactInfo;
import org.address.book.util.Validation;
import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import com.google.gson.Gson;

/**
 * @author rishi
 *	This is an implementation of the service layer where business logic is written.
 */
public class ContactServiceImpl implements ContactService {
	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final String SCHEME = "http";

	private static Gson gson = new Gson();

	private static final String INDEX = "contactdata";
	private static final String TYPE = "doc";
	private Validation validation = new Validation();
	
	RestHighLevelClient restHighLevelClient;
	

	public ContactServiceImpl() {
		restHighLevelClient = new RestHighLevelClient(
				RestClient.builder(new HttpHost(HOST, PORT_ONE, SCHEME)));	
		
	}

	/* (non-Javadoc)
	 * @see org.address.book.service.ContactService#addContact(org.address.book.model.ContactInfo)
	 * takes ContactInfo parameter that will persist in datastore.
	 */
	@Override
	public void addContact(ContactInfo contactInfo) throws ContactException {

		ContactInfo contact = validation.validateContact(contactInfo);

		contactInfo.setId(UUID.randomUUID().toString());

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("contactId", contactInfo.getId());
		dataMap.put("name", contactInfo.getName());
		dataMap.put("email", contactInfo.getEmail());
		dataMap.put("address", contactInfo.getAddress());
		dataMap.put("phoneNumber", contactInfo.getPhoneNumber());

		IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, contactInfo.getId()).source(dataMap);
		try {
			IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
			

		} catch (ElasticsearchException e) {
			e.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

	}

	/* (non-Javadoc)
	 * @see org.address.book.service.ContactService#getContact(java.lang.String)
	 * take unique name parameter and get the contact information of that unique name
	 */
	@Override
	public ContactInfo getContact(String name) throws ContactException {

		SearchRequest searchRequest = new SearchRequest();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery("name", name));

		searchRequest.source(searchSourceBuilder);

		List<ContactInfo> listInfo = new ArrayList<>();
		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			SearchHit[] results = searchResponse.getHits().getHits();

			for (SearchHit hit : results) {

				String sourceAsString = hit.getSourceAsString();
				if (sourceAsString != null) {
					ContactInfo contactInfo = gson.fromJson(sourceAsString, ContactInfo.class);
					listInfo.add(contactInfo);
				}
			}
		} catch (ElasticsearchException ex) {
			ex.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

		if (listInfo.size() < 1) {
			throw new ContactException("User not found");
		}
		return listInfo.get(0);
	}

	/* (non-Javadoc)
	 * @see org.address.book.service.ContactService#editContact(org.address.book.model.ContactInfo, java.lang.String)
	 * take unique name parameter and contactInfo that needs to be updated
	 */
	@Override
	public void editContact(ContactInfo contactInfo, String name) throws ContactException {
		ContactInfo info = getContact(name);
		validation.validateContact(contactInfo);
		contactInfo.setId(info.getId());

		UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, info.getId()).fetchSource(true);
		try {
			String infoString = gson.toJson(contactInfo);

			updateRequest.doc(infoString, XContentType.JSON);
			UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
			

		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}

	}

	/* (non-Javadoc)
	 * @see org.address.book.service.ContactService#deleteContact(java.lang.String)
	 * take unique name paramter to delete existing record.
	 */
	@Override
	public void deleteContact(String name) throws ContactException {
		ContactInfo contactInfo = getContact(name);

		if (contactInfo == null) {
			throw new ContactException("User not present to delete");
		}

		DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, contactInfo.getId());
		try {
			DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
			System.out.println(deleteResponse);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
	}

	/* (non-Javadoc)
	 * @see org.address.book.service.ContactService#getContact(java.lang.String, java.lang.String, java.lang.String)
	 * take various pagesixe , page number and query string and will return all the contact data based on those parameter.
	 */
	@Override
	public Collection<ContactInfo> getContact(String pageSize, String pageNumber, String queryString) {
		SearchRequest searchRequest = new SearchRequest();

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		if(pageNumber != null)
			searchSourceBuilder.from((Integer.parseInt(pageNumber)-1) * Integer.parseInt(pageSize));
		if(pageSize != null)
			searchSourceBuilder.size(Integer.parseInt(pageSize));
		
		if(queryString !=null) {		
			searchSourceBuilder.query(QueryBuilders.queryStringQuery(queryString));
		}

		searchRequest.source(searchSourceBuilder);

		List<ContactInfo> listInfo = new ArrayList<>();
		
		searchRequest.source(searchSourceBuilder);

		
		try {
			SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
			SearchHit[] results = searchResponse.getHits().getHits();

			for (SearchHit hit : results) {

				String sourceAsString = hit.getSourceAsString();
				if (sourceAsString != null) {
					ContactInfo contactInfo = gson.fromJson(sourceAsString, ContactInfo.class);
					listInfo.add(contactInfo);
				}
			}
		} catch (ElasticsearchException ex) {
			ex.getDetailedMessage();
		} catch (java.io.IOException ex) {
			ex.getLocalizedMessage();
		}

		
		return listInfo;
	}

}
