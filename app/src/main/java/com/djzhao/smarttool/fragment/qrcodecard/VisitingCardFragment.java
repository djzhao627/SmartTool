package com.djzhao.smarttool.fragment.qrcodecard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.djzhao.smarttool.R;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class VisitingCardFragment extends Fragment implements View.OnClickListener {

    private EditText nameInput;
    private EditText roleInput;
    private EditText phoneInput;
    private EditText addressInput;
    private EditText emailInput;
    private EditText companyInput;
    private EditText homepageInput;
    private FloatingActionButton createQRCodeBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qrcode_visiting_card_fragment, container, false);
        nameInput = view.findViewById(R.id.qrcode_visiting_card_name_input);
        roleInput = view.findViewById(R.id.qrcode_visiting_card_role_input);
        phoneInput = view.findViewById(R.id.qrcode_visiting_card_phone_input);
        addressInput = view.findViewById(R.id.qrcode_visiting_card_address_input);
        emailInput = view.findViewById(R.id.qrcode_visiting_card_email_input);
        companyInput = view.findViewById(R.id.qrcode_visiting_card_company_input);
        homepageInput = view.findViewById(R.id.qrcode_visiting_card_homepage_input);
        createQRCodeBtn = view.findViewById(R.id.qrcode_visiting_card_create_qrcode_btn);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createQRCodeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_visiting_card_create_qrcode_btn:
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(nameInput.getWindowToken(), 0);
                createQRCode();

        }
    }

    private void createQRCode() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("MECARD:");
        if (TextUtils.isEmpty(nameInput.getText())) {
            Snackbar.make(nameInput, "名称不可为空", Snackbar.LENGTH_LONG).show();
            nameInput.requestFocus();
            return;
        }
        stringBuffer.append("N:" + nameInput.getText().toString().trim());

        if (!TextUtils.isEmpty(roleInput.getText())) {
            stringBuffer.append(";ROLE:" + roleInput.getText().toString().trim());
        }

        if (TextUtils.isEmpty(phoneInput.getText())) {
            Snackbar.make(phoneInput, "手机号码不可为空", Snackbar.LENGTH_LONG).show();
            phoneInput.requestFocus();
            return;
        }
        stringBuffer.append(";TEL:" + phoneInput.getText().toString().trim());

        if (!TextUtils.isEmpty(addressInput.getText())) {
            stringBuffer.append(";ADR:" + addressInput.getText().toString().trim());
        }

        if (!TextUtils.isEmpty(emailInput.getText())) {
            stringBuffer.append(";EMAIL:" + emailInput.getText().toString().trim());
        }

        if (!TextUtils.isEmpty(companyInput.getText())) {
            stringBuffer.append(";ORG:" + companyInput.getText().toString().trim());
        }

        if (!TextUtils.isEmpty(homepageInput.getText())) {
            stringBuffer.append(";URL:" + homepageInput.getText().toString().trim());
        }
        stringBuffer.append(";");

        final Bitmap image = CodeUtils.createImage(stringBuffer.toString(), 500, 500, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);


        Button shareBtn = dialogView.findViewById(R.id.dialog_bottom_btn_sheet_share);
        Button saveBtn = dialogView.findViewById(R.id.dialog_bottom_btn_sheet_save);
        ImageView img_bottom_dialog = dialogView.findViewById(R.id.dialog_bottomimg);
        Glide.with(getContext()).load(image).into(img_bottom_dialog);
        bottomSheetDialog.setContentView(dialogView);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQRCode(image);
            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode(image);
            }
        });
        bottomSheetDialog.show();
    }

    private void shareQRCode(Bitmap image) {
        File file = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri imageUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getContext(), "com.djzhao.smarttool.fileprovider", file);
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.setType("image/*");
        getActivity().startActivity(Intent.createChooser(shareIntent, "分享二维码至"));
    }

    private void saveQRCode(Bitmap image) {
        String date = new Date().toString();
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + date + ".jpg";
        FileOutputStream out = null;
        File file = new File(filePath);
        try {
            out = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(getContext(), "已经保存至：" + filePath, Toast.LENGTH_LONG).show();
    }


}
