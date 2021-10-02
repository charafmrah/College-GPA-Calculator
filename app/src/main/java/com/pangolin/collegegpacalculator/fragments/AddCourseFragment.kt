package com.pangolin.collegegpacalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pangolin.collegegpacalculator.CalculatorApplication
import com.pangolin.collegegpacalculator.GRADELETTERS
import com.pangolin.collegegpacalculator.R
import com.pangolin.collegegpacalculator.databinding.FragmentAddCourseBinding
import com.pangolin.collegegpacalculator.databinding.FragmentCourseListBinding
import com.pangolin.collegegpacalculator.model.Course
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModel
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModelFactory
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.view.menu.MenuView
import com.pangolin.collegegpacalculator.CREDITS

class AddCourseFragment : Fragment() {

    private val viewModel: CalculatorViewModel by activityViewModels {
        CalculatorViewModelFactory(
            (activity?.application as CalculatorApplication).database.courseDao()
        )
    }

    private var _binding: FragmentAddCourseBinding? = null
    private val binding get() = _binding!!

    lateinit var course: Course

    private val navigationArgs: CourseDetailFragmentArgs by navArgs()

    private fun bind(course: Course) {
        binding.apply{
            courseName.setText(course.courseName, TextView.BufferType.SPANNABLE)
//            courseCredit.getFocusables(0)
//            courseGrade.editText(course.courseGrade, TextView.BufferType.SPANNABLE)
            saveAction.setOnClickListener { updateCourse() }
        }
    }

    private fun addNewCourse() {
        if (isEntryValid()) {
            viewModel.addNewCourse(
                binding.courseName.text.toString(),
                binding.courseCredit.editText?.text.toString(),
                binding.courseGrade.editText?.text.toString(),
            )
        }
        val action = AddCourseFragmentDirections.actionAddCourseFragmentToCourseListFragment()
        findNavController().navigate(action)
    }

    private fun updateCourse() {
        if(isEntryValid()) {
            viewModel.updateCourse(
                this.navigationArgs.courseId,
                this.binding.courseName.text.toString(),
                this.binding.courseCredit.editText?.text.toString(),
                this.binding.courseGrade.editText?.text.toString()
            )
            val action = AddCourseFragmentDirections.actionAddCourseFragmentToCourseListFragment()
            findNavController().navigate(action)
        }
    }

    private fun isEntryValid(): Boolean {
        return viewModel.isEntryValid(
            binding.courseName.text.toString(),
            binding.courseCredit.editText?.text.toString(),
            binding.courseGrade.editText?.text.toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gradesAdapter = ArrayAdapter(requireContext(), R.layout.add_list_item, CREDITS)
        (binding.courseCredit.editText as? AutoCompleteTextView)?.setAdapter(gradesAdapter)

        val creditAdapter = ArrayAdapter(requireContext(), R.layout.add_list_item, GRADELETTERS)
        (binding.courseGrade.editText as? AutoCompleteTextView)?.setAdapter(creditAdapter)

        val id = navigationArgs.courseId
        if (id > 0) {
            viewModel.retrieveCourse(id).observe(this.viewLifecycleOwner) { selectedCourse ->
                course = selectedCourse
                bind(course)
            }
        } else {
            binding.saveAction.setOnClickListener {
                addNewCourse()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}