/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

// [CourseDetailFragment] displays the details of the  selected course
class CourseDetailFragment : Fragment() {

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

    // Bind views with the passed in course data
    private fun bind(course: Course) {
        binding.apply {
            courseName.text = course.courseName
            courseCredit.text = course.courseCredit.toString()
            courseGrade.text = course.courseGrade.toString()
            deleteItem.setOnClickListener { showConfirmationDialog() }
//            editCourse.setOnClickListener { editCourse() }
        }
    }

    // Navigate to the Edit course screen.
//    private fun editCourse() {
//        val action = CourseDetailFragmentDirections.actionCourseDetailFragmentToAddCourseFragment(
//            getString(R.string.edit_fragment_title),
//            course.id
//        )
//        this.findNavController().navigate(action)
//    }

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

    // Deletes the current  course and navigates to the list fragment
    private fun deleteCourse() {
        viewModel.deleteCourse(course)
        findNavController().navigateUp()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.courseId

        /*
        * Retrieve the course details using the id.
        * Attach an observer on  the data (instead of polling for changes) and only update
        * the UI when the data actually changes.
         */
        viewModel.retrieveCourse(id).observe(this.viewLifecycleOwner) { selectedCourse ->
            course = selectedCourse
            bind(course)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}