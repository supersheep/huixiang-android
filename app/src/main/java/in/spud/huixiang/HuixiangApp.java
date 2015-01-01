package in.spud.huixiang;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import in.spud.huixiang.api.HuixiangApi;
import in.spud.huixiang.model.Account;
import in.spud.huixiang.util.Contants;
import in.spud.huixiang.util.FileUtils;

/**
 * Created by spud on 15/1/1.
 */
public class HuixiangApp extends Application {

    private static HuixiangApp mInstance;
    private HuixiangApi mApi;
    private Account mAccount;

    public static HuixiangApp get(){
        return mInstance;
    }

    public HuixiangApi getApi(){
        return mApi;
    }


    public Account getAccount(){
        if (mAccount == null) {
            loadAccountInfo();
        }
        return mAccount;
    }

    public void setAccount(Account account){
        this.mAccount = account;
        if (mAccount != null) {
            FileUtils.saveInternalFile(getApplicationContext(), new Gson().toJson(mAccount), Contants.ACCOUNT_FILENAME);
        } else {
            FileUtils.saveInternalFile(getApplicationContext(), "", Contants.ACCOUNT_FILENAME);
        }
    }

    private void loadAccountInfo(){
        String jsonString = FileUtils.loadInternalFile(getApplicationContext(), Contants.ACCOUNT_FILENAME);
        if(!TextUtils.isEmpty(jsonString)){
            mAccount = new Gson().fromJson(jsonString, Account.class);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        RequestQueue queue = Volley.newRequestQueue(this);
        mApi = new HuixiangApi(queue);

    }
}
