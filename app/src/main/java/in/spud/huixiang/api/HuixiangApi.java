package in.spud.huixiang.api;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


import in.spud.huixiang.HuixiangApp;
import in.spud.huixiang.model.Piece;

/**
 * Created by spud on 15/1/1.
 */
public class HuixiangApi {

    private final RequestQueue mQueue;

    public HuixiangApi(RequestQueue requestQueue){
        mQueue = requestQueue;
    }

    public Request getPieces(Response.Listener listener, Response.ErrorListener errorListener){
        PieceRequest req = new PieceRequest(listener, errorListener);
        return mQueue.add(req);
    }

    public Request getFavList(int page, Response.Listener listener, Response.ErrorListener errorListener){
        FavListRequest req = new FavListRequest(page, listener, errorListener);
        return mQueue.add(req);
    }

    public Request unfav(long pieceId, Response.Listener listener, Response.ErrorListener errorListener){
        UnfavRequest req = new UnfavRequest(pieceId, listener, errorListener);
        return mQueue.add(req);
    }

}
