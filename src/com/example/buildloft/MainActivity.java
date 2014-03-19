package com.example.buildloft;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll=new LinearLayout(this);
		Button myButton=new Button(this);
		myButton.setText("进入游戏");
		Button myButton2=new Button(this);
		myButton2.setText("进入游戏2");
		myButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.this.startActivity(new Intent(MainActivity.this,PhysicsExample2.class));
				
			}
		});
		myButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				MainActivity.this.startActivity(new Intent(MainActivity.this,PhysicsExample.class));
				
			}
		});
		ll.addView(myButton);
		ll.addView(myButton2);
		setContentView(ll);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
