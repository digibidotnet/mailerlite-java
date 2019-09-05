package net.digibi.mailerlite;

import java.util.Map;

import org.json.JSONObject;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

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
     * @param type allowed values: unsubscribed, active, unconfirmed
     * @return
     */
    public boolean moveSubscriberToGroup(long groupId, String email, String type) {
        return moveSubscriberToGroup(groupId, createUserBodyToSend(email, null, type));
    }

    /**
     * Moves user(identified by email, name) to specified groupId, set fields & type of subscription
     * @param groupId
     * @param email
     * @param fields fields to set
     * @param type allowed values: unsubscribed, active, unconfirmed
     * @return
     */
    public boolean moveSubscriberToGroup(long groupId, String email, Map<String, String> fields, String type) {
        return moveSubscriberToGroup(groupId, createUserBodyToSend(email, fields, type));
    }

    /**
     * Method to construct String in JSON format
     * 
     * @param email
     * @param fields
     * @param type   values accepted: unsubscribed, active
     * @return String in JSON format
     */
    public static String createUserBodyToSend(String email, Map<String, String> fields, String type) {
        JSONObject jsonObject = new JSONObject().put("email", email).put("type", type);
        if (fields != null) {
            jsonObject.put("fields", fields.toString());
        }
        return jsonObject.toString();
    }
    
    public HttpResponse<String> updateUserDetails(int identifier, Map<String, String> fields) {
        JSONObject fieldsParsed = new JSONObject().put("fields", new JSONObject(fields));
        HttpResponse<String> response = Unirest
            .put(API_END_POINT + "subscribers/" + identifier)
            .header("content-type", "application/json")
            .header("x-mailerlite-apikey", API_KEY)
            .body(fieldsParsed.toString())
            .asString();
        return response;
    }
    
    public HttpResponse<String> updateUserDetails(String identifier, Map<String, String> fields) {
        JSONObject fieldsParsed = new JSONObject().put("fields", new JSONObject(fields));
        HttpResponse<String> response = Unirest
            .put(API_END_POINT + "subscribers/" + identifier)
            .header("content-type", "application/json")
            .header("x-mailerlite-apikey", API_KEY)
            .body(fieldsParsed.toString())
            .asString();
        return response;
    }

    public static String createUpdateBodyToSend(String name, Map<String, String> fields) {
        JSONObject jsonObject = new JSONObject().put("name", name);
        if (fields != null) {
            jsonObject.put("fields", (new JSONObject(fields)).toString());
        }
        return jsonObject.toString();
    }
}