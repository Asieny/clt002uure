package com.guangxi.culturecloud.views;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.guangxi.culturecloud.R;

import java.util.Calendar;


public class TestDialog extends Dialog
{
    
    private Context context;
    
    private int style;
    
    private NumberPicker np1, np2, np3;
    
    static Calendar c = Calendar.getInstance();
    
    private static String str1 = c.get(Calendar.YEAR) + "";
    
    private static String str2 = (c.get(Calendar.MONTH) + 1) + "";
    
    private static String str3 = c.get(Calendar.DATE) + "";
    
    public TestDialog(Context context)
    {
        super(context);
        this.context = context;
    }
    
    public TestDialog(Context context, int style)
    {
        super(context);
        this.context = context;
        this.style = style;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog);
        
        np1 = (NumberPicker)findViewById(R.id.np1);
        np2 = (NumberPicker)findViewById(R.id.np2);
        np3 = (NumberPicker)findViewById(R.id.np3);
        
        np1.setMaxValue(2299);
        np1.setMinValue(1900);
        np1.setValue(c.get(Calendar.YEAR));
        np1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            
            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2)
            {
                // TODO Auto-generated method stub
                str1 = np1.getValue() + "";
                if (Integer.parseInt(str1) % 4 == 0 && Integer.parseInt(str1) % 100 != 0 || Integer.parseInt(str1) % 400 == 0)
                {
                    if (str2.equals("1") || str2.equals("3") || str2.equals("5") || str2.equals("7") || str2.equals("8") || str2.equals("10") || str2.equals("12"))
                    {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    }
                    else if (str2.equals("4") || str2.equals("6") || str2.equals("9") || str2.equals("11"))
                    {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    }
                    else
                    {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    }
                    
                }
                else
                {
                    if (str2.equals("1") || str2.equals("3") || str2.equals("5") || str2.equals("7") || str2.equals("8") || str2.equals("10") || str2.equals("12"))
                    {
                        np3.setMaxValue(31);
                        np3.setMinValue(1);
                    }
                    else if (str2.equals("4") || str2.equals("6") || str2.equals("9") || str2.equals("11"))
                    {
                        np3.setMaxValue(30);
                        np3.setMinValue(1);
                    }
                    else
                    {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }
                
            }
        });
        
        np2.setMaxValue(12);
        np2.setMinValue(1);
        np2.setValue(c.get(Calendar.MONTH) + 1);
        np2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            
            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2)
            {
                // TODO Auto-generated method stub
                str2 = np2.getValue() + "";
                if (str2.equals("1") || str2.equals("3") || str2.equals("5") || str2.equals("7") || str2.equals("8") || str2.equals("10") || str2.equals("12"))
                {
                    np3.setMaxValue(31);
                    np3.setMinValue(1);
                }
                else if (str2.equals("4") || str2.equals("6") || str2.equals("9") || str2.equals("11"))
                {
                    np3.setMaxValue(30);
                    np3.setMinValue(1);
                }
                else
                {
                    if (Integer.parseInt(str1) % 4 == 0 && Integer.parseInt(str1) % 100 != 0 || Integer.parseInt(str1) % 400 == 0)
                    {
                        np3.setMaxValue(29);
                        np3.setMinValue(1);
                    }
                    else
                    {
                        np3.setMaxValue(28);
                        np3.setMinValue(1);
                    }
                }
            }
        });
        
        np3.setMaxValue(31);
        np3.setMinValue(1);
        np3.setValue(c.get(Calendar.DATE));
        np3.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
        {
            
            @Override
            public void onValueChange(NumberPicker arg0, int arg1, int arg2)
            {
                // TODO Auto-generated method stub
                str3 = np3.getValue() + "";
            }
        });
        
        // 设置返回按钮事件和文本
        if (backButtonText != null)
        {
            Button bckButton = ((Button)findViewById(R.id.dialog_back));
            bckButton.setText(backButtonText);
            
            if (backButtonClickListener != null)
            {
                bckButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        backButtonClickListener.onClick(new TestDialog(getContext()), DialogInterface.BUTTON_NEGATIVE);
                        dismiss();
                    }
                });
            }
        }
        else
        {
            findViewById(R.id.dialog_back).setVisibility(View.GONE);
        }
        
        // 设置确定按钮事件和文本
        if (confirmButtonText != null)
        {
            Button cfmButton = ((Button)findViewById(R.id.dialog_confirm));
            cfmButton.setText(confirmButtonText);
            
            if (confirmButtonClickListener != null)
            {
                cfmButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        confirmButtonClickListener.onClick(new TestDialog(getContext()), DialogInterface.BUTTON_NEGATIVE);
                        dismiss();
                    }
                });
            }
        }
        else
        {
            findViewById(R.id.dialog_confirm).setVisibility(View.GONE);
        }
    }
    
    private String backButtonText; // 对话框返回按钮文本
    
    private String confirmButtonText; // 对话框确定文本
    
    // 对话框按钮监听事件
    private OnClickListener backButtonClickListener, confirmButtonClickListener;
    
    /**
     * 设置back按钮的事件和文本
     * 
     * @param backButtonText
     * @param listener
     * @return
     */
    public void setBackButton(String backButtonText, OnClickListener listener)
    {
        this.backButtonText = backButtonText;
        this.backButtonClickListener = listener;
    }
    
    /**
     * 设置确定按钮事件和文本
     * 
     * @param
     * @param listener
     * @return
     */
    public void setConfirmButton(String confirmButtonText, OnClickListener listener)
    {
        this.confirmButtonText = confirmButtonText;
        this.confirmButtonClickListener = listener;
    }
    
    public static String getDate()
    {
        if (str2.length() == 1)
        {
            str2 = "0" + str2;
        }
        if (str3.length() == 1)
        {
            str3 = "0" + str3;
        }
        return str1 + "-" + str2 + "-" + str3;
    }
    
    @Override
    public void show()
    {
        // TODO Auto-generated method stub
        super.show();
    }
}
