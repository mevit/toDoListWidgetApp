package com.meldeveloping.todowidget.main.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.meldeveloping.todowidget.R
import com.meldeveloping.todowidget.adapter.MainListAdapter
import com.meldeveloping.todowidget.model.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()

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
        val mainListAdapter = mainViewModel.getAdapterForMainList()
        if (mainListAdapter.itemCount != 0) {
            itemsList.layoutManager = LinearLayoutManager(context)
            itemsList.adapter = mainListAdapter
            mainListAdapter.setClickListener(View.OnClickListener {
                goToEditFragment(MainListAdapter.itemId)
            })
        } else {
            emptyListTextView.visibility = View.VISIBLE
        }
    }

    private fun goToEditFragment(toDoListId: Int? = null) {
        fragmentManager!!
            .beginTransaction()
            .replace(R.id.mainContainer, EditFragment.newInstance(toDoListId))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }

}
