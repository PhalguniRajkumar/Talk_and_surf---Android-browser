package com.rajkumar.talkandsurf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Starting extends Activity implements AnimationListener{
	
	Animation an;
	ImageView im;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.starting);
		  im=(ImageView)findViewById(R.id.im);
	        an=AnimationUtils.loadAnimation(this,R.anim.animation);
	     	im.setAnimation(an);
	        im.startAnimation(an);
	        an.setAnimationListener(this);
		
	}

	@Override
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		Intent i=new Intent(this,MainActivity.class);
		startActivity(i);
		finish();
		
	}

	@Override
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
		
	}

}
