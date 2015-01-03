package in.spud.huixiang.api;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by spud on 15/1/3.
 */
public class UnfavRequest extends BaseRequest {

    public long getmPieceId() {
        return mPieceId;
    }

    public void setmPieceId(long mPieceId) {
        this.mPieceId = mPieceId;
    }

    private long mPieceId = 0;

    public UnfavRequest(long pieceId, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.POST, ApiContants.UNFAV_URL, listener, errorListener);
        mPieceId = pieceId;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map params = new HashMap();
        params.put("pieceid", String.valueOf(mPieceId));
        return params;
    }

    @Override
    protected Object parseNetworkResponseDelegate(String string) {
        return null;
    }
}
