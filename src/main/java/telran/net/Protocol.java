package telran.net;

import org.json.JSONObject;

import static telran.net.TcpConfigurationProperties.REQUEST_DATA_FIELD;
import static telran.net.TcpConfigurationProperties.REQUEST_TYPE_FIELD;

public interface Protocol {
    Response getResponse(Request request);
    default String getResponseWithJSON(String requestJSON){
        JSONObject jSONObject = new JSONObject(requestJSON);
        String requestType = jSONObject.getString(REQUEST_TYPE_FIELD);
        String requestData = jSONObject.getString(REQUEST_DATA_FIELD);
        Request request = new Request(requestType, requestData);
        return getResponse(request).toString();
    };

}
