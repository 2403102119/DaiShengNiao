package com.lxkj.dsn.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lxkj.dsn.R;


/*
 * https://blog.csdn.net/whitley_gong/article/details/51956429
 * 自定义控件购物车加减数量
 * */
public class AmountView2 extends LinearLayout implements View.OnClickListener, TextWatcher {
    private static final String TAG = "AmountView";
    private int amount = 1; //购买数量
    private int goods_storage = 10000; //商品库存

    private OnAmountChangeListener mListener;

    private TextView etAmount;
    private TextView btnDecrease;
    private TextView btnIncrease;

    public AmountView2(Context context) {
        this(context, null);
    }

    public AmountView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_amount, this);
        etAmount = (TextView) findViewById(R.id.etAmount);
        btnDecrease = (TextView) findViewById(R.id.btnDecrease);
        btnIncrease = (TextView) findViewById(R.id.btnIncrease);
        btnDecrease.setOnClickListener(this);
        btnIncrease.setOnClickListener(this);
//        etAmount.addTextChangedListener(this);

        etAmount.setFocusable(false);

        etAmount.setFocusableInTouchMode(false);

//        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.AmountView2);
//        int btnWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView2_btnWidth, LayoutParams.WRAP_CONTENT);
//        int tvWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView2_tvWidth, 80);
//        int tvTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView2_tvTextSize, 0);
//        int btnTextSize = obtainStyledAttributes.getDimensionPixelSize(R.styleable.AmountView2_btnTextSize, 0);
//        obtainStyledAttributes.recycle();
//
//        LayoutParams btnParams = new LayoutParams(btnWidth, LayoutParams.MATCH_PARENT);
//        btnDecrease.setLayoutParams(btnParams);
//        btnIncrease.setLayoutParams(btnParams);
//        if (btnTextSize != 0) {
//            btnDecrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
//            btnIncrease.setTextSize(TypedValue.COMPLEX_UNIT_PX, btnTextSize);
//        }

//        LayoutParams textParams = new LayoutParams(tvWidth, LayoutParams.MATCH_PARENT);
//        etAmount.setLayoutParams(textParams);
//        if (tvTextSize != 0) {
//            etAmount.setTextSize(tvTextSize);
//        }


    }

    public void setOnAmountChangeListener(OnAmountChangeListener onAmountChangeListener) {
        this.mListener = onAmountChangeListener;
    }

    public void setGoods_storage(int goods_storage) {
        this.goods_storage = goods_storage;
    }

    public void setGoodsNubber(String number) {
        etAmount.setText(number);
        amount = Integer.parseInt(number);
    }
    public String getGoodsNuvver(){
        return etAmount.getText().toString();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnDecrease) {
            if (amount > 1) {
                amount--;
                etAmount.setText(amount + "");
            }
        } else if (i == R.id.btnIncrease) {
            if (amount < goods_storage) {
                amount++;
                etAmount.setText(amount + "");
            }
        }

        etAmount.clearFocus();

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().isEmpty())
            return;
        amount = Integer.valueOf(s.toString());
        if (amount > goods_storage) {
            etAmount.setText(goods_storage + "");
            return;
        }

        if (mListener != null) {
            mListener.onAmountChange(this, amount);
        }
    }


    public interface OnAmountChangeListener {
        void onAmountChange(View view, int amount);
    }

}
