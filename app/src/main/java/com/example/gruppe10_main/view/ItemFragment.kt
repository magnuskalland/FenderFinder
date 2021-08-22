package com.example.gruppe10_main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.example.gruppe10_main.R
import com.example.gruppe10_main.misc.TAG
import com.example.gruppe10_main.viewmodel.DriftyViewModel

/**
 * Fragment class for displaying current item user wants to search. Current item
 * is provided by the shared [DriftyViewModel]. Invoked by [OverviewFragment].
 * Implements [AdapterView.OnItemClickListener].
 */
class ItemFragment: ParentFragment(R.layout.fragment_item), AdapterView.OnItemClickListener {

    private val buttonSaveFragment = ButtonSaveFragment()
    private lateinit var driftyViewModel: DriftyViewModel
    private lateinit var listView : ListView
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var currentChoice: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.v(TAG, "ItemFragment onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        changeFragment(R.id.bottom_container, buttonSaveFragment, false)

        driftyViewModel = ViewModelProvider(requireActivity()).get(DriftyViewModel::class.java)
        listView = view.findViewById(R.id.listView)
        initListView()

    }

    /**
     * Inflates [ListView] with items from string array resources.
     */
    private fun initListView() {
        arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_single_choice,
                resources.getStringArray(R.array.leeway_items))

        listView.adapter = arrayAdapter
        listView.choiceMode = ListView.CHOICE_MODE_SINGLE
        listView.onItemClickListener = this
    }

    /**
     * Implements [AdapterView.OnItemClickListener] interface.
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        currentChoice = parent?.getItemAtPosition(position).toString()
        driftyViewModel.setObjectType(currentChoice)

    }
}