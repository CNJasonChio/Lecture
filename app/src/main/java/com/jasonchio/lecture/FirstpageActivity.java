package com.jasonchio.lecture;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstpageActivity extends AppCompatActivity {

	EditText testEdit;
	Button button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_firstpage);
		testEdit=(EditText)findViewById(R.id.test);
		button=(Button)findViewById(R.id.send);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone=testEdit.getText().toString();
				Toast.makeText(FirstpageActivity.this,phone,Toast.LENGTH_SHORT).show();
			}
		});
	}
}
