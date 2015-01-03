package in.spud.huixiang.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.spud.huixiang.model.Piece;

/**
 * Created by spud on 15/1/2.
 */
public class FavListRequest extends BaseRequest {

    private long page = 0;
    private long per = 10;

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public long getPer() {
        return per;
    }

    public void setPer(long per) {
        this.per = per;
    }

    public FavListRequest(long page, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format("%s?per=10&page=%d", ApiContants.FAV_LIST_URL, page), listener, errorListener);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", String.valueOf(page));
        params.put("per", String.valueOf(per));
        return params;
    }

    @Override
    protected List<Piece> parseNetworkResponseDelegate(String string) {
        return new Gson().fromJson(string, new TypeToken<List<Piece>>(){}.getType());
    }
}
