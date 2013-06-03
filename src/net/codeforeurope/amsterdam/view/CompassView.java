package net.codeforeurope.amsterdam.view;
import net.codeforeurope.amsterdam.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.widget.ImageView;

public class CompassView extends ImageView {
	Paint paint;
	int direction = 0;

	public CompassView(Context context) {

		super(context);
		// TODO Auto-generated constructor stub

		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);

//		this.setImageResource(R.drawable.compassrose);
	}

}
