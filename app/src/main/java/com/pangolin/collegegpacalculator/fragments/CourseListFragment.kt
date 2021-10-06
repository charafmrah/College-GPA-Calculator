package com.pangolin.collegegpacalculator.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pangolin.collegegpacalculator.CalculatorApplication
import com.pangolin.collegegpacalculator.R
import com.pangolin.collegegpacalculator.adapters.CourseListAdapter
import com.pangolin.collegegpacalculator.databinding.FragmentCourseListBinding
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModel
import com.pangolin.collegegpacalculator.viewmodels.CalculatorViewModelFactory

class CourseListFragment : Fragment() {

    private var _binding: FragmentCourseListBinding? = null
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
        _binding = FragmentCourseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CourseListAdapter {
            val action =
                CourseListFragmentDirections.actionCourseListFragmentToCourseDetailFragment(it.id)
            this.findNavController().navigate(action)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter

        viewModel.allCourses.observe(this.viewLifecycleOwner) {
            courses -> courses.let {
                adapter.submitList(it)

            }
            binding.gpaResult.text = viewModel.calculateGpa().toString()
        }

        binding.floatingActionButton.setOnClickListener {
            val action = CourseListFragmentDirections.actionCourseListFragmentToAddCourseFragment(
                getString(R.string.add_fragment_title)
            )
            this.findNavController().navigate(action)
        }


    }

}