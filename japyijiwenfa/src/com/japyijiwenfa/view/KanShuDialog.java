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
	 * ���ñ���
	 * @param title
	 */
	public void setTitle(String title){
		if(textview_title!=null){
			textview_title.setText(title);
		}
	}
	
	/**
	 * ��������
	 * @param content
	 */
	public void setContent(String content){
		if(textview_content!=null){
			textview_content.setText(content);
		}
	}
	
	/**
	 * ����ȷ������
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
	 *����ȡ������
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
	 * �õ����ذ�ť
	 */
	public Button getCancelButton(){	
		return cancelButton;
	}
	
	/**
	 * ����CertainButton�Ƿ�ɼ�
	 * @param isVisible true ��ʾ�ɼ� false ��ʾ���ɼ�
	 */
	public void setCertainButtonVisibility(boolean isVisible){
		if(isVisible){
			certainButton.setVisibility(View.VISIBLE);
		}else{
			certainButton.setVisibility(View.GONE);
		}
	}
}

