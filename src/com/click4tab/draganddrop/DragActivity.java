package com.click4tab.draganddrop;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DragActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag);
		findViewById(R.id.textView1).setOnTouchListener(new MyTouchListener());
		findViewById(R.id.textView2).setOnTouchListener(new MyTouchListener());
		// findViewById(R.id.myimage3).setOnTouchListener(new
		// MyTouchListener());
		// findViewById(R.id.myimage4).setOnTouchListener(new
		// MyTouchListener());
		findViewById(R.id.topleft).setOnDragListener(new MyDragListener());
		findViewById(R.id.topright).setOnDragListener(new MyDragListener());
		// findViewById(R.id.bottomleft).setOnDragListener(new
		// MyDragListener());
		// findViewById(R.id.bottomright).setOnDragListener(new
		// MyDragListener());

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
				ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				LinearLayout container = (LinearLayout) v;
				container.addView(view);
				view.setVisibility(View.VISIBLE);
				Log.e("drag", "drag drop");

				break;
			case DragEvent.ACTION_DRAG_ENDED:
				v.setBackgroundDrawable(normalShape);
				Log.e("drag", "drag END");
				// Log.e("view", ((TextView) v).getText().toString().trim());
				TextView view3 = (TextView) event.getLocalState();
				Log.e("TEXT", view3.getText().toString().trim());

				// view3.getText().toString().trim();

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