package telran.net;

import org.json.JSONObject;

import static telran.net.TcpConfigurationProperties.REQUEST_DATA_FIELD;
import static telran.net.TcpConfigurationProperties.REQUEST_TYPE_FIELD;

public record Response(ResponseCode responceCode, String responseData) {
    @Override
    public final String toString() {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put(REQUEST_TYPE_FIELD, responceCode);
            jsonObj.put(REQUEST_DATA_FIELD, responseData);
            return jsonObj.toString();
    }

}
