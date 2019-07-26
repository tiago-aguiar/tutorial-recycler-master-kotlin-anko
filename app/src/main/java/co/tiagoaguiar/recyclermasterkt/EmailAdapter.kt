package co.tiagoaguiar.recyclermasterkt

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.NORMAL
import android.graphics.drawable.GradientDrawable
import android.util.SparseBooleanArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.util.isNotEmpty
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.recyclermasterkt.model.Email
import co.tiagoaguiar.recyclermasterkt.view.EmailItemUI
import co.tiagoaguiar.recyclermasterkt.view.oval
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onLongClick

/**
 *
 * Junho, 19 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */
class EmailAdapter(val emails: MutableList<Email>) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    val selectedItems = SparseBooleanArray()
    private var currentSelectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        return EmailViewHolder(
            EmailItemUI().createView(
                AnkoContext.create(parent.context, parent)
            )
        )
    }

    override fun getItemCount(): Int = emails.size

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emails[position])
        holder.itemView.onClick {
            if (selectedItems.isNotEmpty()) {
                onItemClick?.invoke(position)
            }
        }
        holder.itemView.onLongClick {
            onItemLongClick?.invoke(position)
        }

        if (currentSelectedPos == position) currentSelectedPos = -1
    }

    fun toggleSelection(position: Int) {
        currentSelectedPos = position
        if (selectedItems[position, false]) {
            selectedItems.delete(position)
            emails[position].selected = false
        } else {
            selectedItems.put(position, true)
            emails[position].selected = true
        }
        notifyItemChanged(position)
    }


    fun deleteEmails() {
        emails.removeAll(emails.filter { email ->
            email.selected
        })
        notifyDataSetChanged()
        currentSelectedPos = -1
    }

    var onItemClick: ((Int) -> Unit)? = null
    var onItemLongClick: ((Int) -> Unit)? = null

    inner class EmailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val txtIcon = itemView.find<TextView>(R.id.txt_icon)
        private val txtUser = itemView.find<TextView>(R.id.txt_user)
        private val txtSubject = itemView.find<TextView>(R.id.txt_subject)
        private val txtPreview = itemView.find<TextView>(R.id.txt_preview)
        private val txtDate = itemView.find<TextView>(R.id.txt_date)
        private val imgStar = itemView.find<ImageView>(R.id.img_star)


        fun bind(email: Email) {
            with(email) {
                val hash = user.hashCode()
                txtIcon.text = user.first().toString()
                txtIcon.background = txtIcon.oval(Color.rgb(hash, hash / 2, 0))
                txtUser.text = user
                txtSubject.text = subject
                txtPreview.text = preview
                txtDate.text = date

                txtUser.setTypeface(Typeface.DEFAULT, if (unread) BOLD else NORMAL)
                txtSubject.setTypeface(Typeface.DEFAULT, if (unread) BOLD else NORMAL)
                txtDate.setTypeface(Typeface.DEFAULT, if (unread) BOLD else NORMAL)

                imgStar.setImageResource(
                    if (stared) R.drawable.ic_star_black_24dp
                    else R.drawable.ic_star_border_black_24dp
                )


                if (email.selected) {
                    txtIcon.background = txtIcon.oval(Color.rgb(26, 115, 233))
                    itemView.background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 32f
                        setColor(Color.rgb(232, 240, 253))
                    }
                } else {
                    itemView.background = GradientDrawable().apply {
                        shape = GradientDrawable.RECTANGLE
                        cornerRadius = 32f
                        setColor(Color.WHITE)
                    }
                }

                if (selectedItems.isNotEmpty())
                    animate(txtIcon, email)
            }
        }

        private fun animate(view: TextView, email: Email) {
            val oa1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
            val oa2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
            oa1.interpolator = DecelerateInterpolator()
            oa2.interpolator = AccelerateDecelerateInterpolator()
            oa1.duration = 200
            oa2.duration = 200

            oa1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    if (email.selected)
                        view.text = "\u2713"
                    oa2.start()
                }
            })
            oa1.start()
        }

    }
}




























