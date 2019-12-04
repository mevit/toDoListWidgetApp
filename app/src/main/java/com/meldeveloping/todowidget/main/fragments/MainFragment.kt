package com.meldeveloping.todowidget.main.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.extension.showLog
import com.meldeveloping.todowidget.main.MainActivity
import com.meldeveloping.todowidget.model.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.FieldPosition

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewButton()
        initRecyclerView()
    }

    private fun initNewButton() {
        goToEditButton.setOnClickListener {
            goToEditFragment()
        }
    }

    private fun initRecyclerView() {
        val mainListAdapter = initMainListAdapter()
        if (mainListAdapter.itemCount != 0) {
            val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.item_layout_animation)
            val layoutManager = LinearLayoutManager(context)
            itemsList.layoutManager = layoutManager
            itemsList.adapter = mainListAdapter
            itemsList.layoutAnimation = animation
            itemsList.smoothScrollToPosition(mainListAdapter.itemCount - 1)
        } else {
            emptyListAnimation()
        }
    }

    private fun goToEditFragment(toDoListId: Int = MainActivity.DEFAULT_TODO_LIST_ID) {
        if (toDoListId == MainActivity.DEFAULT_TODO_LIST_ID) {
            fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.mainContainer, EditFragment.newInstance(toDoListId))
                .addToBackStack(null)
                .commit()
        } else {
            fragmentManager!!
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.mainContainer, EditFragment.newInstance(toDoListId))
                .addToBackStack(null)
                .commit()
        }
    }

    private fun itemLongClick(toDoListId: Int, position: Int) {
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_dialog, null)
        val dltBtn = view.findViewById<TextView>(R.id.deleteButton)
        val pinBtn = view.findViewById<TextView>(R.id.pinButton)
        val builder = AlertDialog.Builder(context!!).setView(view)
        val dialog: AlertDialog = builder.create()

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        dialog.window!!.setLayout(600, 500)

        dltBtn.setOnClickListener {
            mainViewModel.removeItem(position)
            if(itemsList.adapter!!.itemCount == 0)
                emptyListAnimation()
            dialog.dismiss()
        }

        if (mainViewModel.isToDoListPinned(position)) {
            pinBtn.text = getString(R.string.main_fragment_unpin)
        } else {
            pinBtn.text = getString(R.string.main_fragment_pin)
        }

        pinBtn.setOnClickListener {
            if (mainViewModel.isToDoListPinned(position)) {
                mainViewModel.unpinItem(position)
            } else {
                mainViewModel.pinItem(position)
            }
            itemsList.adapter = initMainListAdapter()
            dialog.dismiss()
        }
    }

    private fun initMainListAdapter(): MainListAdapter {
        val mainListAdapter = mainViewModel.getAdapterForMainList()

        mainListAdapter.setClickListener(View.OnClickListener {
            goToEditFragment(MainListAdapter.itemId)
        })
        mainListAdapter.setLongClickListener(View.OnLongClickListener {
            itemLongClick(MainListAdapter.itemId, MainListAdapter.itemPosition)
            return@OnLongClickListener true
        })

        return mainListAdapter
    }

    private fun emptyListAnimation() {
        val animation = AnimationUtils.loadAnimation(context, R.anim.fade_main_fragment)
        emptyMainFragment.startAnimation(animation)
        animation.setAnimationListener( object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationStart(p0: Animation?) {
                emptyMainFragment.visibility = View.VISIBLE
            }
        })
    }

}
