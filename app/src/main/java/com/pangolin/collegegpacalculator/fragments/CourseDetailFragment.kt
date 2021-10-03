package com.pangolin.collegegpacalculator.fragments

import android.content.ClipData
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pangolin.collegegpacalculator.CalculatorApplication
import com.pangolin.collegegpacalculator.R
import com.pangolin.collegegpacalculator.databinding.FragmentCourseDetailBinding
import com.pangolin.collegegpacalculator.model.Course
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModel
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModelFactory

class CourseDetailFragment: Fragment() {

    private val navigationArgs: CourseDetailFragmentArgs by navArgs()
    lateinit var course: Course

    private var _binding: FragmentCourseDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalculatorViewModel by activityViewModels {
        CalculatorViewModelFactory(
            (activity?.application as CalculatorApplication).database.courseDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCourseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun bind(course: Course) {
        binding.apply {
            courseName.text = course.courseName
            courseCredit.text = course.courseCredit.toString()
            courseGrade.text = course.toString()
            deleteItem.setOnClickListener { showConfirmationDialog() }
            editCourse.setOnClickListener { editCourse() }
        }
    }

    private fun editCourse() {
        val action = CourseDetailFragmentDirections.actionCourseDetailFragmentToAddCourseFragment(
            getString(R.string.edit_fragment_title),
            course.id
        )
        this.findNavController().navigate(action)
    }

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteCourse()
            }
            .show()
    }

    private fun deleteCourse() {
        viewModel.deleteCourse(course)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}