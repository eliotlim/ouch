package net.gostun.ouch.moneypack;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public abstract class AbstractCash extends ImageView {

	public AbstractCash(Context context, Drawable drawable) {
		super(context);
		this.setImageDrawable(drawable);
	}
}
