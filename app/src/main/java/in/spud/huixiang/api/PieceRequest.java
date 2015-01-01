package in.spud.huixiang.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import in.spud.huixiang.model.Piece;

/**
 * Created by spud on 15/1/1.
 */
public class PieceRequest extends BaseRequest {
    public PieceRequest(Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, ApiContants.PIECES_URL, listener, errorListener);
    }

    @Override
    protected Object parseNetworkResponseDelegate(String string) {
        return new Gson().fromJson(string, new TypeToken<ArrayList<Piece>>(){}.getType());
    }

}
