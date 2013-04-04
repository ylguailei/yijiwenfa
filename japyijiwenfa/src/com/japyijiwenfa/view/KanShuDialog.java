package com.japyijiwenfa.view;


import com.japyijiwenfa.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class KanShuDialog extends Dialog{
	private TextView textview_title;
	private TextView textview_content;
	private Button certainButton;
	private Button cancelButton;

	public KanShuDialog(Context context) {
		super(context,0);
		show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.dialog_layout);
		textview_title = (TextView) this.findViewById(R.id.dialog_textview_title);
		textview_content = (TextView) this.findViewById(R.id.dialog_textview_content);
		certainButton = (Button) this.findViewById(R.id.dialog_textview_button1);
		cancelButton = (Button) this.findViewById(R.id.dialog_textview_button2);
		
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		if(textview_title!=null){
			textview_title.setText(title);
		}
	}
	
	/**
	 * 设置内容
	 * @param content
	 */
	public void setContent(String content){
		if(textview_content!=null){
			textview_content.setText(content);
		}
	}
	
	/**
	 * 设置确定按键
	 * @param text
	 * @param onClickListener
	 */
	public void setCertainButton(String text,View.OnClickListener onClickListener){
		if(certainButton!=null){
			certainButton.setText(text);
			certainButton.setOnClickListener(onClickListener);
		}
	}
	
	/**
	 *设置取消按键
	 * @param text
	 * @param onClickListener
	 */
	public void setCancelButton(String text,View.OnClickListener onClickListener){
		if(cancelButton!=null){
			cancelButton.setText(text);
			cancelButton.setOnClickListener(onClickListener);
		}
	}
    
	/**
	 * 得到返回按钮
	 */
	public Button getCancelButton(){	
		return cancelButton;
	}
	
	/**
	 * 设置CertainButton是否可见
	 * @param isVisible true 表示可见 false 表示不可见
	 */
	public void setCertainButtonVisibility(boolean isVisible){
		if(isVisible){
			certainButton.setVisibility(View.VISIBLE);
		}else{
			certainButton.setVisibility(View.GONE);
		}
	}
}

