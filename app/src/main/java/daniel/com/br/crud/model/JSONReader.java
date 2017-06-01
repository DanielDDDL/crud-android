//package daniel.com.br.crud.model;
//
///**
// * Created by Leonardo Fuso on 30/05/17.
// */
//
//import android.util.Log;
//
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//
//public class JSONReader {
//
//    public static <T> Object readJSON(Class<T[]> batata, String json) throws IOException {
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        List<T> list = Arrays.asList(mapper.readValue(json, batata));
//
//        return list;
//    }
//
//    public static String get_generic(String address, RequestQueue queue){
//        final String url = address;
//
//        // prepare the Request
//        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>()
//                {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        // display response
//                        Log.d("Response", response.toString());
//                    }
//                },
//                new Response.ErrorListener()
//                {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("Error.Response", response);
//                    }
//                }
//        );
//
//        // add it to the RequestQueue
//        queue.add(getRequest);
//        return null;
//    }
//
//}
