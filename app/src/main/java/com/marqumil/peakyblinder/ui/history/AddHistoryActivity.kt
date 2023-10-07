package com.marqumil.peakyblinder.ui.history

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.marqumil.peakyblinder.MainActivity
import com.marqumil.peakyblinder.databinding.ActivityAddHistoryBinding
import com.marqumil.peakyblinder.remote.ResultState
import com.marqumil.peakyblinder.ui.utils.ConfirmDialog

class AddHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: DatePickerDialog
    private lateinit var spinnerAdapter: SpinnerAdapter

    private var date: String = ""
    private var time: String = ""
    private var glucose: Int = 0
    private var meals: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        viewModel = HistoryViewModel()

        binding.ivBack.setOnClickListener {
            finish()
        }

        setSpinner()
        setDate()
        setTime()

        binding.apply {
            btnSubmit.setOnClickListener {
                glucose = 0
                if (etGlucose.text?.isEmpty() == true) {
                    glucose = 0
                } else {
                    glucose = etGlucose.text.toString().toInt()
                }

                viewModel.checkInputHistory(date, time, glucose, meals)
                viewModel.cekInputHistory.observe(this@AddHistoryActivity) {
                    when (it) {
                        is ResultState.Success -> {
                            Toast.makeText(this@AddHistoryActivity, "Success", Toast.LENGTH_LONG).show()
                            // make dialog confirmation
                            val dialog = ConfirmDialog()
                            dialog.title = "Success, Your glucose history has been added"
                            dialog.description = "Do you want to add another glucose history?"
                            dialog.positiveText = "OK"
                            dialog.positiveCallback = {
                                dialog.dismiss()
                                val intent = Intent(this@AddHistoryActivity, AddHistoryActivity::class.java)
                                startActivity(intent)
                            }
                            dialog.negativeText = "Cancel"
                            dialog.negativeCallback = {
                                dialog.dismiss()
                                finish()
                                val intent = Intent(this@AddHistoryActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                            dialog.show(this@AddHistoryActivity.supportFragmentManager, "confirm_dialog")
                        }

                        is ResultState.Failure -> {
                            val dialog = ConfirmDialog()
                            dialog.title = "Error"
                            dialog.description = "Your glucose history has not been added"
                            dialog.positiveText = "OK"
                            dialog.positiveCallback = {
                                dialog.dismiss()
                            }
                            dialog.show(this@AddHistoryActivity.supportFragmentManager, "confirm_dialog")
                            Toast.makeText(this@AddHistoryActivity, it.throwable.toString(), Toast.LENGTH_LONG).show()
                        }

                        else -> {
                            Toast.makeText(this@AddHistoryActivity, it.toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

    }

    // fun set spinner
    private fun setSpinner() {
        binding.apply {
            val items = listOf("Before meal", "after meal", "fasting")
            val adapter =
                ArrayAdapter(this@AddHistoryActivity, R.layout.simple_spinner_dropdown_item, items)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    meals = items[position]
                    Log.d("spinner", meals)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    meals = items[0]
                    Log.d("spinner", meals)
                }
            }
        }
    }

    // fun set date
    private fun setDate() {
        binding.apply {
            cvTanggal.setOnClickListener {
                datePickerDialog = DatePickerDialog(this@AddHistoryActivity)
                datePickerDialog.show()

                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    // Handle the user's selection of a date
                    tvHintTanggal.text = "$dayOfMonth/$month/$year"
                    Log.d("date", "$year $month $dayOfMonth")
                    date = "$year/$month/$dayOfMonth"
                }
            }

            tvHintTanggal.setOnClickListener {
                datePickerDialog = DatePickerDialog(this@AddHistoryActivity)
                datePickerDialog.show()

                datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                    // Handle the user's selection of a date
                    tvHintTanggal.text = "$dayOfMonth/$month/$year"
                    Log.d("date", "$year $month $dayOfMonth")
                    date = "$year/$month/$dayOfMonth"
                }
            }
        }
    }


    // set time picker
    private fun setTime() {
        binding.apply {
            cvTime.setOnClickListener {
                tmTimePicker.visibility = View.VISIBLE
                tvHintTime.visibility = View.GONE

                tmTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                    Log.d("time", "$hourOfDay $minute")

                    time = "$hourOfDay:$minute"
                }
            }

            tvHintTime.setOnClickListener {
                tmTimePicker.visibility = View.VISIBLE
                tvHintTime.visibility = View.GONE

                tmTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
                    Log.d("time", "$hourOfDay $minute")
                    time = "$hourOfDay:$minute"
                }
            }
        }
    }

}