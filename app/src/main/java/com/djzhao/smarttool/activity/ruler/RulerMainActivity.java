package com.djzhao.smarttool.activity.ruler;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.djzhao.smarttool.R;
import com.djzhao.smarttool.activity.base.BaseActivity;
import com.djzhao.smarttool.view.ruler.RulerView;

import java.text.DecimalFormat;

public class RulerMainActivity extends BaseActivity {

    private RulerView rulerView;
    private TextView textHint;

    @Override
    protected void findViewById() {
        rulerView = $(R.id.ruler_view);
        textHint = $(R.id.ruler_text_hint);
    }


    @Override
    protected void initView() {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            getWindowManager().getDefaultDisplay().getRealSize(point);
        } else {
            getWindowManager().getDefaultDisplay().getSize(point);
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double inchesX = point.x / dm.xdpi;
        double inchesY = point.y / dm.ydpi;
        double cmX = inchesX * 2.54;
        double cmY = inchesY * 2.54;
        rulerView.setSize(toInt(cmX * 10), toInt(cmY * 10))
                .setTextSize(dm.xdpi * 12 / 160)
                .setLineLength((int) (dm.xdpi * 10 / 160))
                .build();
        DecimalFormat decimalFormat = new DecimalFormat("###.00");
        textHint.setText("屏幕信息\n宽：" + decimalFormat.format(cmX) + " CM\n高：" + decimalFormat.format(cmY) + " CM");
    }// http://suo.im/api.php?url=

    /**
     * 四舍五入
     *
     * @param number
     * @return
     */
    private int toInt(double number) {
        return Integer.parseInt(new java.text.DecimalFormat("0").format(number));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.ruler_main_activity);

        findViewById();
        initView();
    }
}
