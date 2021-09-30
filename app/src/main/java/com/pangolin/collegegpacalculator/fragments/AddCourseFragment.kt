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

class AddCourseFragment : Fragment() {



    private var _binding: FragmentAddCourseBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by activityViewModels {
        CalculatorViewModelFactory(
            (activity?.application as CalculatorApplication).database.courseDao()
        )
    }

    lateinit var course: Course

    private val navigationArgs: CourseDetailFragmentArgs by navArgs()

//    private fun bind(course: Course) {
//        binding.apply{
//            courseName.setText(course.courseName, TextView.BufferType.SPANNABLE)
////            courseCredit.setText(course.courseCredit, TextView.BufferType.SPANNABLE)
////            courseGrade.setText(course.courseGrade, TextView.BufferType.SPANNABLE)
//            saveAction.setOnClickListener { updateCourse() }
//        }
//    }

//    private fun updateCourse() {
//        if(isEntryValid()) {
//            /*viewModel.updateCourse(
//                this.navigationArgs.id,
//                this.binding.courseName.text.toString(),
//                this.binding.courseCredit.text.toString(),
//                this.binding.courseGrade.text.toString()
//            )*/
//            val action = AddCourseFragmentDirections.actionAddCourseFragmentToCourseListFragment()
//            findNavController().navigate(action)
//        }
//    }

//    private fun isEntryValid(): Boolean {
//        return viewModel.isEntryValid(
//            binding.courseName.text.toString(),
////            binding.courseCredit.text.toString(),
////            binding.courseGrade.text.toString()
//        )
//    }

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

        val adapter = ArrayAdapter(requireContext(), R.layout.add_list_item, GRADELETTERS)
        (binding.courseCredit.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

}