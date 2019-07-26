package co.tiagoaguiar.recyclermasterkt.view

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import co.tiagoaguiar.recyclermasterkt.R
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

/**
 *
 * Junho, 21 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class EmailItemUI : AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>) = with(ui) {

        cardView {
            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 32f
                setColor(Color.WHITE)
            }

            relativeLayout {
                lparams(matchParent, wrapContent) {
                    horizontalPadding = dip(16)
                    verticalPadding = dip(8)
                }

                textView {
                    id = R.id.txt_icon
                    textSize = 24f
                    background = oval(Color.RED)
                    gravity = Gravity.CENTER
                    textColor = Color.WHITE
                }.lparams(dip(48), dip(48))

                textView {
                    id = R.id.txt_user
                    textColor = Color.DKGRAY
                    textSize = 18f
                }.lparams(wrapContent, wrapContent) {
                    rightOf(R.id.txt_icon)
                    sameTop(R.id.txt_icon)
                    marginStart = dip(16)
                }

                textView {
                    id = R.id.txt_subject
                    lines = 1
                    textColor = Color.DKGRAY
                    textSize = 16f
                    ellipsize = TextUtils.TruncateAt.END
                }.lparams {
                    below(R.id.txt_user)
                    sameStart(R.id.txt_user)
                    marginEnd = dip(24)
                }

                textView {
                    id = R.id.txt_preview
                    lines = 1
                    textColor = Color.DKGRAY
                    textSize = 16f
                    ellipsize = TextUtils.TruncateAt.END
                }.lparams {
                    below(R.id.txt_subject)
                    sameStart(R.id.txt_user)
                    marginEnd = dip(24)
                }

                textView {
                    id = R.id.txt_date
                    lines = 1
                    textColor = Color.DKGRAY
                    ellipsize = TextUtils.TruncateAt.END
                }.lparams {
                    alignParentTop()
                    alignParentEnd()
                }

                imageView(R.drawable.ic_star_border_black_24dp) {
                    id = R.id.img_star
                }.lparams(dip(24), dip(24)) {
                    alignParentEnd()
                    alignParentBottom()
                }

            }
        }

    }

}

fun View.oval(@ColorInt color: Int): ShapeDrawable {
    val oval = ShapeDrawable(OvalShape())
    with(oval) {
        intrinsicHeight = height
        intrinsicWidth = width
        paint.color = color
    }
    return oval
}
