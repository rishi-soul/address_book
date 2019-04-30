# address_book
Address Book API
Installation:
Clone the git repository / download and extract the .zip file.  Download and install elasticsearch from https://www.elastic.co/downloads/elasticsearch

After downloading Elastic Search execute
>{PathToElasticSearchFolder}/bin/elasticsearch.bat

To Generate Jar
>mvn install

To Execute
>java -cp address_book-0.0.1-SNAPSHOT.jar org.address.book.controller.AddressBookController

GET /contact?pageSize={}&page={}&query={}

This endpoint will providing a listing of all contacts.
Example: 

URI: http://localhost:4567/contact?pageSize=1&page=1&query=name:john

Response: 
{
"contactId":"0b50ab9f-509d-42c9-8cc4-84ccd7b0cf30",
"name":"john",
"email":"user@domain.com",
"phoneNumber":"1234567890",
"address":"texas"
}

POST /contact

This endpoint will create the contact. 
Example:

URI: http://localhost:4567/contact
body: 
{
	"name": "john",
	"email": "user@domain.com",
	"phoneNumber": "1234567890",
	"address": "texas"
}

GET /contact/{name}

This endpoint will return the contact by a unique name. This name should be specified by the person entering the data.  
Example: 

URI: http://localhost:4567/contact/john

Response: 
{"contactId":"0b50ab9f-509d-42c9-8cc4-84ccd7b0cf30",
"name":"john",
"email":"user@domain.com",
"phoneNumber":"1234567890",
"address":"texas"
}


PUT /contact/{name}

This endpoint should update the contact by a unique name (and will give error if not found)
Example: 

URI: http://localhost:4567/contact/john

body: 
{
	"name": "john",
	"email": "user@domain.com",
	"phoneNumber": "1234567890",
	"address": "California"
}

DELETE /contact/{name}

This endpoint should delete the contact by a unique name (and should error if not found)
Example: 

URI: http://localhost:4567/contact/john

Response:
{
    "status": "SUCCESS"
}



