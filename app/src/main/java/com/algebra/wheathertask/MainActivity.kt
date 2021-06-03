package com.algebra.wheathertask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.algebra.wheathertask.databinding.ActivityMainBinding
import com.algebra.wheathertask.dialog.DialogExit
import com.algebra.wheathertask.internet.InternetConnectionActivity
import com.algebra.wheathertask.internet.isInternetAvailable
import com.algebra.wheathertask.internet.verifyAvailableNetwork
import com.algebra.wheathertask.list.CitiesAdapter
import com.algebra.wheathertask.model.CitiesNames
import com.algebra.wheathertask.model.HistoryOfCity
import com.algebra.wheathertask.model.Weather
import com.algebra.wheathertask.photo.PhotoProvider
import com.algebra.wheathertask.viewModel.HistoryOfCitiesViewModel
import com.algebra.wheathertask.viewModel.WeatherViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchView: SearchView
    private val viewModel: WeatherViewModel by viewModels()
    private val adapter = CitiesAdapter()
    private val viewModelDatabase: HistoryOfCitiesViewModel by viewModels()
    private var mainList = mutableListOf<HistoryOfCity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!(isInternetAvailable() || verifyAvailableNetwork(this)))
            startActivity(Intent(this, InternetConnectionActivity::class.java))

        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launchWhenResumed {
            mainList = viewModelDatabase.getAllHistoryOfCities() as MutableList<HistoryOfCity>
            if (mainList.isNotEmpty()) {
                viewModel.getWeaterProperties(mainList[mainList.size - 1].city)
            }
        }

        setSupportActionBar(binding.toolbar)
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
        bind()
        setUpRecyclerView()
    }

    override fun onBackPressed() {
        val dialog = DialogExit("Are you sure you want to exit?")
        dialog.show(supportFragmentManager, "Logout")
        dialog.listener = object : DialogExit.ListenerForDialog {
            override fun okPress(isPress: Boolean) {
                finishAffinity()
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        adapter.listener = object : CitiesAdapter.Listener {
            override fun onItemClick(city: CitiesNames) {
                viewModel.getWeaterProperties(city.city)
                addCity(city)
            }
        }
    }

    private fun addCity(city: CitiesNames) {
        val historyOfCity = HistoryOfCity(0, city.city, city.country)
        lifecycleScope.launchWhenResumed {
            val allList = viewModelDatabase.getAllHistoryOfCities() as MutableList<HistoryOfCity>
            if (allList.size >= 6) viewModelDatabase.deleteCity(allList[0].id)
            viewModelDatabase.insertCity(0, city.city, city.country)
        }
        mainList.add(historyOfCity)
        if (mainList.size > 6) mainList.removeAt(0)
        if (searchView.query.isEmpty()) viewModelDatabase.historyCities.postValue(mainList)
    }

    @SuppressLint("SetTextI18n")
    private fun bind() {
        viewModel.weatherProperty.observe(this, Observer {
            setValues(it)
        })
        viewModel.errorObser.observe(this, Observer {
            if (it.contains("host")) displayMessage("Check you internet connection!")
            else if (it.contains("HTTP 404 Not Found")) displayMessage("City not found, try again!")
            else displayMessage("Something went wrong!")
        })
        viewModel.citiesProperty.observe(this, Observer {
            adapter.setList(it.data)
        })
        viewModelDatabase.historyCities.observe(this@MainActivity, Observer {
            val newListOfHistoryOfGames = mutableListOf<CitiesNames>()
            it.forEach {
                newListOfHistoryOfGames.add(CitiesNames(it.city, it.country))
            }
            adapter.setList(newListOfHistoryOfGames.asReversed())
        })
    }

    private fun displayMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    @SuppressLint("SetTextI18n")
    private fun setValues(weather: Weather) {
        binding.tvLocation.text = "${weather.name}, ${weather.sys.country}"
        binding.tvMinTemp.text = "Min temp: ${checkLenghtOfTemp(weather.main.temp_min - 273.15)} °C"
        binding.tvMaxTemp.text = "Max temp: ${checkLenghtOfTemp(weather.main.temp_max - 273.15)} °C"
        binding.tvTemp.text = "${checkLenghtOfTemp(weather.main.temp - 273.15)}°C"
        binding.tvDescription.text = weather.weather[0].description.toUpperCase()
        binding.tvFeelsLike.text = "Feels like: ${checkLenghtOfTemp(weather.main.feels_like - 273.15)} °C"
        binding.tvUpdateAt.text = getCurrentDateAndTime()
        val url = PhotoProvider().photos.getValue(weather.weather[0].main)
        binding.tvPressure.text = "Pressure ${weather.main.pressure}"
        binding.tvHumidity.text = "Humidity ${weather.main.humidity}%"
        binding.tvWindSpeed.text = "Wind ${weather.wind.speed}km/h"

        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(binding.imageWeather)
    }

    private fun getCurrentDateAndTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy\n     HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun checkLenghtOfTemp(temp: Double): String {
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.CEILING
        return df.format(temp)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.serachIcon)
        searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Entry city"

        lifecycleScope.launchWhenResumed {
            setSearch()
        }
        return super.onCreateOptionsMenu(menu)
    }

    @SuppressLint("CheckResult")
    private suspend fun setSearch() {
        Observable.create<String> { emiter ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        viewModel.getWeaterProperties(it)
                        Log.d("ListCities", adapter.listOfCities.toString())
                        run loop@{
                            adapter.listOfCities.forEach { citiesNames ->
                                if (citiesNames.city.toLowerCase() == it.toLowerCase()) {
                                    addCity(citiesNames)
                                    return@loop
                                }
                            }
                        }
                    }
                    searchView.setQuery("", false)
                    searchView.isIconified = true
                    adapter.setList(emptyList())
                    setSupportActionBar(binding.toolbar)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { entryText ->
                        if (!emiter.isDisposed)
                            emiter.onNext(newText)
                    }
                    return true
                }
            })
        }.debounce(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when {
                    it.length >= 3 -> viewModel.getSeachingCities("10", it)
                    it.isEmpty() -> {
                        if (mainList.size != 0) viewModelDatabase.historyCities.postValue(mainList)
                        else lifecycleScope.launchWhenResumed {
                            mainList = viewModelDatabase.getAllHistoryOfCities() as MutableList<HistoryOfCity>
                        }
                        searchView.setOnQueryTextFocusChangeListener { v, newViewFocus ->
                            if (!newViewFocus) {
                                mainList.clear()
                                viewModelDatabase.historyCities.postValue(emptyList())
                            } else {
                                lifecycleScope.launchWhenResumed {
                                    mainList = viewModelDatabase.getAllHistoryOfCities() as MutableList<HistoryOfCity>
                                    viewModelDatabase.historyCities.postValue(mainList)
                                }
                            }
                        }
                    }
                    else -> adapter.setList(emptyList())
                }
            }
    }
}

