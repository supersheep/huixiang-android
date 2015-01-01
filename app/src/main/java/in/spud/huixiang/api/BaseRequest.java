package in.spud.huixiang.api;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import in.spud.huixiang.HuixiangApp;
import in.spud.huixiang.model.Account;

/**
 * Created by spud on 15/1/1.
 */
public abstract class BaseRequest<T> extends Request<T> {

    Response.Listener mListener;

    public BaseRequest(int method, String url, Response.Listener listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Account account = HuixiangApp.get().getAccount();
        if (account != null && !TextUtils.isEmpty(account.getClient_hash())) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("Cookie", "cu=" + account.getClient_hash());
            return params;
        } else {
            return super.getHeaders();
        }
    }

    protected abstract T parseNetworkResponseDelegate(String string);

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            T result = parseNetworkResponseDelegate(jsonString);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(Object o) {
        if (mListener != null) {
            mListener.onResponse(o);
        }
    }

}
