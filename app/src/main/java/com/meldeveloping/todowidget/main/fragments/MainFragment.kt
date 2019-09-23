package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.model.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.todo_list_item.view.*
import org.koin.android.ext.android.inject

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by inject()

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
        var mainListAdapter = mainViewModel.getAdapterForMainList()
        if (mainListAdapter.itemCount != 0) {
            itemsList.layoutManager = LinearLayoutManager(context)
            itemsList.adapter = mainListAdapter

            mainListAdapter.setClickListener(View.OnClickListener {
//                Log.i("TAG", "--------------${itemsList.getChildViewHolder(it).itemId}-------------------")
                goToEditFragment(itemsList.findViewHolderForAdapterPosition(itemsList.indexOfChild(it))!!.itemId.toInt())
            })
        } else {
            emptyListTextView.visibility = View.VISIBLE
        }
    }

    private fun goToEditFragment(toDoListTitle: Int? = null) {
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.mainContainer, EditFragment.newInstance(toDoListTitle))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}
