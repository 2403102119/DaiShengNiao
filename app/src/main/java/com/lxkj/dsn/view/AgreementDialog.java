package com.lxkj.dsn.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.lxkj.dsn.R;
import com.lxkj.dsn.biz.ActivitySwitcher;
import com.lxkj.dsn.ui.fragment.system.WebFra;


public class AgreementDialog extends Dialog {

    private Context context;      // 上下文
    private TextView hintTv,rightTv,leftTv;
    onRightClickListener onRightClickListener;
    private void setOnRightClickListener(onRightClickListener onRightClickListener){
        this.onRightClickListener = onRightClickListener;
    }

    public AgreementDialog(final Context context, final onRightClickListener onRightClickListener) {
        super(context, R.style.Theme_dialog); //dialog的样式
        this.context = context;
        this.onRightClickListener = onRightClickListener;
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
        setContentView(R.layout.layout_dialog_xy);
        WindowManager windowManager = ((Activity)context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth()*4/5; // 设置dialog宽度为屏幕的4/5
        lp.dimAmount = 0.5f;//dimAmount在0.0f和1.0f之间，0.0f完全不暗，即背景是可见的 ，1.0f时候，背景全部变黑暗。
        getWindow().setAttributes(lp);
        //要达到背景全变暗的效果，需设置getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); 否则，背景无效果
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setCanceledOnTouchOutside(false);// 点击Dialog外部消失
        setCancelable(false);
        //遍历控件id,添加点击事件
        rightTv = findViewById(R.id.tv_agree);
        leftTv = findViewById(R.id.tv_disAgree);
        hintTv = findViewById(R.id.tv_privacy_text);


        String str = new String("欢迎使用戴胜鸟图书APP软件，在您成为戴胜鸟的一员，务必仔细阅读，充分理解协议中的条款内容候后再点击同意。\n\n您可阅读《服务协议》和《隐私协议》了解详细信息，如您同意，请点击同意，开始接受我们的服务。");
//
////         SpannableString 的用法、和 SpannableStringBuilder 很相似、下面主要以 SpannableStringBuilder 来介绍
//         SpannableString spannableString = new SpannableString(str);
//         ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0070C0"));
//         spannableString.setSpan(colorSpan, 35, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        hintTv.setText(spannableString);
//
//        // SpannableStringBuilder 用法
        SpannableStringBuilder spannableBuilder = new SpannableStringBuilder(str);
//
//        // 设置字体大小
//        // 相对于默认字体大小的倍数,这里是1.3倍
//         RelativeSizeSpan sizeSpan = new RelativeSizeSpan((float) 1.0);
//        spannableBuilder.setSpan(sizeSpan, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置删除线
        StrikethroughSpan strikeSpan = new StrikethroughSpan();
        // 设置下划线
        UnderlineSpan underlineSpan = new UnderlineSpan();

        // 在设置点击事件、同时设置字体颜色
        ClickableSpan clickableSpanOne = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "用户协议");
                bundle.putString("url","http://8.140.109.101/daishengniao/display/agreement?id=1");
                ActivitySwitcher.startFragment(context, WebFra.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#13C45B"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
                // paint.setStrikeThruText(true);
            }
        };
        ClickableSpan clickableSpanTwo = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "隐私政策");
                bundle.putString("url","http://8.140.109.101/daishengniao/display/agreement?id=2");
                ActivitySwitcher.startFragment(context, WebFra.class, bundle);
            }

            @Override
            public void updateDrawState(TextPaint paint) {
                paint.setColor(Color.parseColor("#13C45B"));
                // 设置下划线 true显示、false不显示
                paint.setUnderlineText(false);
            }
        };
        spannableBuilder.setSpan(clickableSpanOne, 59, 65, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableBuilder.setSpan(clickableSpanTwo, 66, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 不设置点击不生效
        hintTv.setMovementMethod(LinkMovementMethod.getInstance());
        hintTv.setText(spannableBuilder);
        // 去掉点击后文字的背景色
        // mTextView.setHighlightColor(Color.parseColor("#00000000"));
        leftTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                onRightClickListener.onLeftClickListener();
            }
        });
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightClickListener.onRightClickListener();
                dismiss();
            }
        });
    }

    public interface onRightClickListener{
        void onRightClickListener();

        void onLeftClickListener();
    }
}
