
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
package com.pangolin.collegegpacalculator.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pangolin.collegegpacalculator.databinding.CourseListItemBinding
import com.pangolin.collegegpacalculator.model.Course

// [ListAdapater] implementation for the recyclerview
class CourseListAdapter(private val onCourseClicked: (Course) -> Unit) :
    ListAdapter<Course, CourseListAdapter.CourseViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        return CourseViewHolder(CourseListItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            )
        ))
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val current = getItem(position)

        holder.itemView.setOnClickListener {
            onCourseClicked(current)
        }
        holder.bind(current)
    }

    class CourseViewHolder(private var binding:CourseListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(course: Course) {
                binding.apply {
                    courseName.text = course.courseName
                    courseCredit.text = course.courseCredit.toString()
                    courseGrade.text = course.courseGrade.toString()
                }
            }

        }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Course>() {
            override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
                return oldItem.courseName == newItem.courseName
            }
        }
    }

}

