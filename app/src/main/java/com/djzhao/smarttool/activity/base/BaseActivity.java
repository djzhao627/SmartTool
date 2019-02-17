package com.djzhao.smarttool.activity.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.djzhao.smarttool.fragment.base.BaseFragment;
import com.djzhao.smarttool.util.AppManager;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by djzhao on 18/03/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static final String TAG = BaseActivity.class.getSimpleName();
    protected InputMethodManager imm;
    protected Handler mHandler = null;
    private TelephonyManager tManager;
    protected Context mContext;
    protected ProgressDialog progressDialog;
    private ArrayList<BaseFragment> fragments;// back fragment list.
    private BaseFragment fragment;// current fragment.

    public void DisplayToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void DisplayToastLong(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected abstract void findViewById();

    protected String getClientOs() {
        return Build.ID;
    }

    protected String getClientOsVer() {
        return Build.VERSION.RELEASE;
    }

    protected String getCountry() {
        return Locale.getDefault().getCountry();
    }

    protected String getDeviceId() throws Exception {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return this.tManager.getDeviceId();
        } else {
            return null;
        }
    }

    protected String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    protected String getToken() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            return this.tManager.getSimSerialNumber();
        }
        return null;
    }

    protected String getVersionName() throws Exception {
        return getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    }

    @SuppressLint("NewApi")
    protected void hideOrShowSoftInput(boolean paramBoolean,
                                       EditText paramEditText) {
        if (paramBoolean) {
            this.imm.showSoftInput(paramEditText, 0);
            return;
        }
        this.imm.hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    protected abstract void initView();

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        AppManager.getInstance().addActivity(this);
        this.tManager = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE));
        this.imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
        this.mContext = this;
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void openActivity(Class<?> paramClass) {
        Intent localIntent = new Intent(this, paramClass);
        startActivity(localIntent);
    }

    protected void openActivity(Class<?> paramClass, Bundle bundle) {
        Intent localIntent = new Intent(this, paramClass);
        if (bundle != null)
            localIntent.putExtras(bundle);
        startActivity(localIntent);
    }

    protected void openActivity(Class<?> paramClass, String action) {
        Intent intent = new Intent(this, paramClass);
        if (action != null) {
            intent.setAction(action);
        }

        startActivity(intent);
    }

    protected void openActivity(Class<?> paramClass, String action,
                                Bundle bundle) {
        Intent intent = new Intent(this, paramClass);
        if (action != null) {
            intent.setAction(action);
        }
        if (bundle != null)
            intent.putExtras(bundle);

        startActivity(intent);
    }

    protected void openActivity(String paramString) {
        openActivity(paramString, null);
    }

    protected void openActivity(String paramString, Bundle paramBundle) {
        Intent localIntent = new Intent(paramString);
        if (paramBundle != null)
            localIntent.putExtras(paramBundle);
        startActivity(localIntent);
    }

    /**
     * 显示进度对话框
     */
    protected void showProgressDialog(String... title) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            if (title == null || title.length == 0) {
                progressDialog.setMessage("正在加载...");
            } else {
                progressDialog.setMessage(title[0]);
            }
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    protected void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * Find the view by id in this activity
     *
     * @param viewID the view what you want to instantiation
     * @return the view's instantiation
     */
    @SuppressWarnings("unchecked")
    protected <T> T $(int viewID) {
        return (T) findViewById(viewID);
    }

    /**
     * Find the view by id in appointed view
     *
     * @param viewID the view what you want to instantiation
     * @return the view's instantiation
     */
    @SuppressWarnings("unchecked")
    protected <T> T $with(View view, int viewID) {
        return (T) view.findViewById(viewID);
    }


    //======================fragment======================//


}