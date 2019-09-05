package net.digibi.mailerlite;

import org.json.JSONObject;

import kong.unirest.*;

/**
 * MailerLiteClient
 */
public class MailerLiteClient {

    private static final String API_END_POINT = "https://api.mailerlite.com/api/v2/";
    private final String API_KEY;

    /**
     *
     * @param apiKey Mailerlite apiKey required for mailerlite API
     */
    public MailerLiteClient(String apiKey) {
        API_KEY = apiKey;
    }

    /**
     * Moves user provided in userBody to specified groupId
     * @param groupId
     * @param userBody
     * @return
     */
    public boolean moveSubscriberToGroup(long groupId, String userBody) {
        HttpResponse<String> response = Unirest.post(API_END_POINT + "groups/" + groupId + "/subscribers").header("content-type", "application/json").header("x-mailerlite-apikey", API_KEY)
                .body(userBody).asString();
        return response.isSuccess();
    }

    /**
     * Moves user(identified by email, name) to specified groupId and set type of subscription
     * @param groupId
     * @param email
     * @param name
     * @param type allowed values: unsubscribed, active, unconfirmed
     * @return
     */
    public boolean moveSubscriberToGroup(long groupId, String email, String name, String type) {
        return moveSubscriberToGroup(groupId, createUserBodyToSend(email, name, null, type));
    }

    /**
     * Moves user(identified by email, name) to specified groupId, set fields & type of subscription
     * @param groupId
     * @param email
     * @param name
     * @param fields fields to set
     * @param type allowed values: unsubscribed, active, unconfirmed
     * @return
     */
    public boolean moveSubscriberToGroup(long groupId, String email, String name, String fields, String type) {
        return moveSubscriberToGroup(groupId, createUserBodyToSend(email, name, fields, type));
    }

    /**
     * Method to construct String in JSON format
     * 
     * @param email
     * @param name
     * @param fields
     * @param type   values accepted: unsubscribed, active
     * @return String in JSON format
     */
    public static String createUserBodyToSend(String email, String name, String fields, String type) {
        JSONObject jsonObject = new JSONObject().put("email", email).put("name", name).put("type", type);
        if (fields != null) {
            jsonObject.put("fields", fields);
        }
        return jsonObject.toString();
    }

    
    /**
     * Method to update the details of a user
     * 
     * @param identifier User ID
     * @param updateBody String in JSON format with the key-pair(s) to update
     * @return Updated user details in JSON format
     */
    public HttpResponse<String> updateUserDetails(int identifier, String updateBody) {
        HttpResponse<String> response = Unirest.put(API_END_POINT + "subscribers/" + identifier).header("content-type", "application/json").header("x-mailerlite-apikey", API_KEY).body(updateBody)
        .asString();
        return response;
    }
    
    /**
     * Method to update the details of a user
     * @param identifier User ID
     * @param name  Name of User
     * @param fields Fields to update
     * @return
     */
    public HttpResponse<String> updateUserDetails(int identifier, String name, String fields) {
        return updateUserDetails(identifier, createUpdateBodyToSend(name, fields));
    }
    
    
    /**
     * Method to update the details of a user
     * 
     * @param identifier User Email
     * @param updateBody String in JSON format with the key-pair(s) to update
     * @return Updated user details in JSON format
     */
    public HttpResponse<String> updateUserDetails(String identifier, String updateBody) {
        HttpResponse<String> response = Unirest.put(API_END_POINT + "subscribers/" + identifier).header("content-type", "application/json").header("x-mailerlite-apikey", API_KEY).body(updateBody)
        .asString();
        return response;
    }
    
    /**
     * Method to update the details of a user
     * @param identifier User Email
     * @param name  Name of User
     * @param fields Fields to update
     * @return
     */
    public HttpResponse<String> updateUserDetails(String identifier, String name, String fields) {
        return updateUserDetails(identifier, createUpdateBodyToSend(name, fields));
    }
    
    /**
     * Method to construct String in JSON format
     * @param name User's name
     * @param fields Fields to update
     * @return
     */
    public static String createUpdateBodyToSend(String name, String fields) {
        JSONObject fieldParsed = new JSONObject(fields);
        JSONObject jsonObject = new JSONObject().put("name", name);
        if (fields != null) {
            jsonObject.put("fields", fieldParsed);
        }
        return jsonObject.toString();
    }
}