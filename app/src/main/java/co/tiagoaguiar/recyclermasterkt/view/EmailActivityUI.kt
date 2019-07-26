package co.tiagoaguiar.recyclermasterkt.view

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.view.ActionMode
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.recyclermasterkt.EmailActivity
import co.tiagoaguiar.recyclermasterkt.EmailAdapter
import co.tiagoaguiar.recyclermasterkt.R
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

/**
 *
 * Junho, 19 2019
 * @author suporte@moonjava.com.br (Tiago Aguiar).
 */

class EmailActivityUI(private val emailAdapter: EmailAdapter) : AnkoComponent<EmailActivity> {

    private var actionMode: ActionMode? = null
    private lateinit var rv: RecyclerView

    override fun createView(ui: AnkoContext<EmailActivity>) = ui.apply {

        relativeLayout {
            backgroundColor = Color.BLUE
            lparams(matchParent, matchParent)

            themedAppBarLayout(R.style.ThemeOverlay_AppCompat_ActionBar) {
                id = R.id.app_bar
                toolbar {
                    id = R.id.toolbar
                    backgroundColor = Color.WHITE
                    popupTheme = R.style.ThemeOverlay_AppCompat_Light
                    title = ctx.getString(R.string.app_name)
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent)

            rv = recyclerView {
                backgroundColor = Color.WHITE
                layoutManager = LinearLayoutManager(ctx)
                adapter = emailAdapter
            }.lparams(matchParent, matchParent) {
                below(R.id.app_bar)
            }

            floatingActionButton {
                image = ContextCompat.getDrawable(ctx, R.drawable.ic_add_black_24dp)
                supportBackgroundTintList = ColorStateList.valueOf(Color.WHITE)
                supportImageTintList = ColorStateList.valueOf(Color.RED)
                rippleColor = Color.LTGRAY
                onClick {
                    ui.owner.addEmail()
                    rv.scrollToPosition(0)
                }
            }.lparams {
                alignParentEnd()
                alignParentBottom()
                setMargins(0, 0, dip(16), dip(16))
            }
        }

        val helper =
            ItemTouchHelper(
                ItemTouchHandler(0,
                    // used for drag/move items
                    // ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT
                )
            )

        helper.attachToRecyclerView(rv)
        emailAdapter.onItemClick = {
            enableActionMode(it, ui)
        }
        emailAdapter.onItemLongClick = {
            enableActionMode(it, ui)
        }

    }.view

    private fun enableActionMode(position: Int, ui: AnkoContext<EmailActivity>) {
        if (actionMode == null)
            actionMode = ui.owner.startSupportActionMode(object : ActionMode.Callback {
                override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
                    return true
                }

                override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                    return false
                }

                override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                    if (item?.itemId == R.id.action_delete) {
                        emailAdapter.deleteEmails()
                        mode?.finish()
                        return true
                    }
                    return false
                }

                override fun onDestroyActionMode(mode: ActionMode?) {
                    emailAdapter.selectedItems.clear()
                    emailAdapter.emails
                        .filter { it.selected }
                        .forEach { it.selected = false }

                    emailAdapter.notifyDataSetChanged()
                    actionMode = null
                }
            })

        emailAdapter.toggleSelection(position)
        val size = emailAdapter.selectedItems.size()
        if (size == 0) {
            actionMode?.finish()
        } else {
            actionMode?.title = "$size"
            actionMode?.invalidate()
        }
    }


    inner class ItemTouchHandler(dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val from = viewHolder.adapterPosition
            val to = target.adapterPosition

            Collections.swap(emailAdapter.emails, from, to)
            emailAdapter.notifyItemMoved(from, to)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            emailAdapter.emails.removeAt(viewHolder.adapterPosition)
            emailAdapter.notifyItemRemoved(viewHolder.adapterPosition)
        }
    }

}
