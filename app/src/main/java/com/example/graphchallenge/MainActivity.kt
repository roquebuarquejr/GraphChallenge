package com.example.graphchallenge

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.graphchallenge.presentation.GraphViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val barChart = findViewById<BarChart>(R.id.barChart)
        val barTopFive = findViewById<BarChart>(R.id.barTopFive)
        val barDownFive = findViewById<BarChart>(R.id.barDownFive)
        val tvTotal = findViewById<TextView>(R.id.tvTotal)
        val tvMax = findViewById<TextView>(R.id.tvMax)
        val tvMin = findViewById<TextView>(R.id.tvMin)
        val tvAvg = findViewById<TextView>(R.id.tvAvg)
        val swipeRefresh = findViewById<SwipeRefreshLayout>(R.id.swipeRefresh)

        val viewModel = GraphViewModel.create(application)

        swipeRefresh.setOnRefreshListener {
            viewModel.fetchItems()
        }
        viewModel.items.observe(this) {
            swipeRefresh.isRefreshing = false
            val entries = it.mapIndexed { index, item ->
                BarEntry(
                    index.toFloat(),
                    item.total
                )
            }
            setUpBarChart(barChart, entries)
        }

        viewModel.topFiveItems.observe(this) {
            val entries = it.mapIndexed { index, item ->
                BarEntry(
                    index.toFloat(),
                    item.total
                )
            }

            setUpBarChart(barTopFive, entries)
        }

        viewModel.downFiveItems.observe(this) {
            val entries = it.mapIndexed { index, item ->
                BarEntry(
                    index.toFloat(),
                    item.total
                )
            }

            setUpBarChart(
                barDownFive,
                entries
            )
        }

        viewModel.total.observe(this) {
            tvTotal.text = it.toString()
        }

        viewModel.max.observe(this) {
            tvMax.text = it.toString()
        }

        viewModel.min.observe(this) {
            tvMin.text = it.toString()
        }

        viewModel.avg.observe(this){
            tvAvg.text = it.toString()
        }

    }

    private fun setUpBarChart(
        barChart: BarChart,
        entries: List<BarEntry>
    ) {

        val dataset = BarDataSet(entries, "Total")
        dataset.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
        dataset.valueTextColor = Color.BLACK
        dataset.valueTextSize = 12f

        val data = BarData(dataset)
        barChart.apply {
            axisLeft.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            axisRight.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            description.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            setData(data)
            animateY(1000)
        }
    }
}