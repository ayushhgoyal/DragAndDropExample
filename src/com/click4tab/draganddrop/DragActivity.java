package com.click4tab.draganddrop;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DragActivity extends Activity {
	int numberOfViews = 0;
	StringBuilder str;
	String correctAns = "onetwothree";
	LinearLayout upperLayout;
	LinearLayout lowerLayout;
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		overridePendingTransition(R.anim.zoon_enter, R.anim.zoom_exit);

		super.onPause();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.zoon_enter, R.anim.zoom_exit);

		setContentView(R.layout.activity_drag);
		findViewById(R.id.textView1).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.textView2).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.textView3).setOnTouchListener(new MyTouchListener());
		// findViewById(R.id.myimage3).setOnTouchListener(new
		// MyTouchListener());
		// findViewById(R.id.myimage4).setOnTouchListener(new
		// MyTouchListener());
		upperLayout = (LinearLayout) findViewById(R.id.topleft);
		lowerLayout = (LinearLayout) findViewById(R.id.topright);

		upperLayout.setOnDragListener(new MyDragListener());
		lowerLayout.setOnDragListener(new MyDragListener());
		// findViewById(R.id.bottomleft).setOnDragListener(new
		// MyDragListener());
		// findViewById(R.id.bottomright).setOnDragListener(new
		// MyDragListener());

		// ---------

		// snippet to jumble the arraylist.
		ArrayList<String> x = new ArrayList<String>();
		x.add("one");
		x.add("two");
		x.add("three");
		x.add("four");
		Log.e("before JUMBLE", x.toString());
		Collections.shuffle(x);
		Log.e("after JUMBLE", x.toString());

		// ------
		str = new StringBuilder();
		Button restart = (Button) findViewById(R.id.button1);
		restart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

	}

	private final class MyTouchListener implements OnTouchListener {
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
				ClipData data = ClipData.newPlainText("", "");
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
						view);
				view.startDrag(data, shadowBuilder, view, 0);
				view.setVisibility(View.INVISIBLE);
				Log.e("action", "action down on image");
				return true;
			} else {
				return false;
			}
		}
	}

	private boolean dropEventNotHandled(DragEvent dragEvent) {
		return !dragEvent.getResult();
	}

	class MyDragListener implements OnDragListener {
		Drawable enterShape = getResources().getDrawable(
				R.drawable.shape_droptarget);
		Drawable normalShape = getResources().getDrawable(R.drawable.shape);

		@Override
		public boolean onDrag(View v, DragEvent event) {
			// Log.e("DRAGGGG",
			// upperLayout.toString() + " + " + lowerLayout.toString());
			int action = event.getAction();
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				// Do nothing
				Log.e("drag", "drag started");
				break;
			case DragEvent.ACTION_DRAG_ENTERED:
				v.setBackgroundDrawable(enterShape);
				Log.e("drag", "drag entered");

				break;
			case DragEvent.ACTION_DRAG_EXITED:
				v.setBackgroundDrawable(normalShape);
				Log.e("drag", "drag exit");

				break;
			case DragEvent.ACTION_DROP:
				// Dropped, reassign View to ViewGroup
				View view = (View) event.getLocalState();
				Log.e("VIEWW 1",
						v.toString()
								+ " ID "
								+ view.getParent().toString().trim()
										.equals(v.toString().trim()));
				ViewGroup owner = (ViewGroup) view.getParent();

				if (!view.getParent().toString().trim()
						.equals(v.toString().trim())) {
					owner.removeView(view);
					LinearLayout container = (LinearLayout) v;
					container.addView(view);
					view.setVisibility(View.VISIBLE);
					Log.e("drag", "drag drop");
					numberOfViews++;
					TextView view3 = (TextView) event.getLocalState();
					view3.setEnabled(false);
					String s = view3.getText().toString().trim();
					Log.e("TEXT", view3.getText().toString().trim());
					str.append(s);
					if (numberOfViews == 3) {
						if (str.toString().equals(correctAns)) {
							Toast.makeText(getApplicationContext(), "Correct",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Wrong",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else {// dropped in same view
					View view2 = (View) event.getLocalState();
					view2.setVisibility(View.VISIBLE);
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setBackgroundDrawable(normalShape);
				Log.e("drag", "drag END");

				// Log.e("view", ((TextView) v).getText().toString().trim());
				// TextView view3 = (TextView) event.getLocalState();

				// view3.getText().toString().trim();
				Log.e("numberOfView", numberOfViews + "");

				if (dropEventNotHandled(event)) {
					View view2 = (View) event.getLocalState();
					view2.setVisibility(View.VISIBLE);
					Log.e("BANG", "NOT HANDLED");
				}

			default:
				break;
			}
			return true;
		}
	}
}