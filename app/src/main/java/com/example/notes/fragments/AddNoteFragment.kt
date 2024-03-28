package com.example.notes.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.notes.MainActivity
import com.example.notes.viewmodel.NoteViewModel
import com.example.notesroompractice.R
import com.example.notesroompractice.databinding.FragmentAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private lateinit var addNoteView: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel
        addNoteView = view

        // Initialize date picker
        binding.addNoteDate.setOnClickListener { showDatePicker() }

    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialogTheme,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val formattedDate = String.format(
                    "%02d-%02d-%d",
                    selectedDayOfMonth,
                    selectedMonth + 1,
                    selectedYear
                )
                binding.addNoteDate.text = formattedDate
            },
            year, month, dayOfMonth
        )

        datePickerDialog.show()
    }
    private fun saveNote(view: View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()
        val noteDateString = binding.addNoteDate.text.toString().trim()

        if(noteTitle.isNotEmpty() && noteDateString.isNotEmpty()){
            val noteDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(noteDateString)
            val note = com.example.notes.model.Note(0, noteDesc, noteTitle, noteDate)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context, "Note saved!", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }
        else if (noteTitle.isEmpty()) {
            Toast.makeText(addNoteView.context, "Please enter Note Title", Toast.LENGTH_SHORT).show()
        } else if (noteDateString.isEmpty()) {
            Toast.makeText(addNoteView.context, "Please enter Note Date", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when(menuItem.itemId){
            R.id.saveMenu ->{
                saveNote(addNoteView)
                true
            }else-> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }

    fun goBack(view: View) {
        requireActivity().onBackPressed()
    }


}