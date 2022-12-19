package com.example.wifitrilateration.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.wifitrilateration.di.FromViewModelFactory
import com.example.wifitrilateration.domain.entity.ResultStatus.*
import com.example.wifitrilateration.ui.framework.map.MapNavigatorLoader
import com.example.wifitrilateration.ui.framework.map.MapObjectsHolder
import com.example.wifitrilateration.ui.framework.map.MapObjectsLoader
import com.example.wifitrilateration.ui.viewmodel.MapsViewModel
import com.example.wifitrilateration.utils.appComponent
import com.example.wifitrilateration.utils.getResourceString
import com.glebalekseevjk.wifitrilateration.R
import com.glebalekseevjk.wifitrilateration.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import javax.inject.Inject


class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding: FragmentMapsBinding
        get() = _binding ?: throw RuntimeException("FragmentMapsBinding is null")

    @FromViewModelFactory
    @Inject
    lateinit var mapsViewModel: MapsViewModel

    private lateinit var mapObjectsLoader: MapObjectsLoader
    private lateinit var mapNavigatorLoader: MapNavigatorLoader
    private var mapObjectsHolder: MapObjectsHolder? = MapObjectsHolder()

    private val callback = OnMapReadyCallback { googleMap ->
        initMap(googleMap)
        initMapAdditionsView()

        val mapObjects =
            mapsViewModel.getMapObjects(requireContext().getResourceString(R.string.map_geojson))

        mapObjectsLoader = MapObjectsLoader(requireContext(), googleMap, mapObjectsHolder!!)
        mapNavigatorLoader = MapNavigatorLoader(requireContext(), googleMap, mapObjectsHolder!!)
        mapObjectsLoader.loadObjects(mapObjects)
        mapObjectsHolder?.setVisibleLevel(0)


        mapsViewModel.observeMyLocation {
            when(it.status) {
                SUCCESS -> {
                    mapObjectsHolder?.clearNavigators()
                    mapNavigatorLoader.loadNavigators(it.data)
                    println("^^^^^^^^^^^^^^^observeMyLocation SUCCESS")
                }
                LOADING -> {
                    println("^^^^^^^^^^^^^^^observeMyLocation LOADING")
                }
                FAILURE -> {
                    println("^^^^^^^^^^^^^^^observeMyLocation FAILURE")
                }
                DISABLED_LOCATION -> {
                    println("^^^^^^^^^^^^^^^observeMyLocation DISABLED_LOCATION")
                }
                DISABLED_WIFI -> {
                    println("^^^^^^^^^^^^^^^observeMyLocation DISABLED_WIFI")
                }
                else -> {
                    println("^^^^^^^^^^^^^^^observeMyLocation else")
                }
            }
        }

    }

    private fun initMapAdditionsView() {
        binding.floorsLl.visibility = View.VISIBLE
        binding.floor0Btn.setOnClickListener {
            mapObjectsHolder?.setVisibleLevel(0)
        }
        binding.floor1Btn.setOnClickListener {
            mapObjectsHolder?.setVisibleLevel(1)
        }
        binding.floor2Btn.setOnClickListener {
            mapObjectsHolder?.setVisibleLevel(2)
        }
        binding.floor3Btn.setOnClickListener {
            mapObjectsHolder?.setVisibleLevel(3)
        }
    }

    private fun initMap(googleMap: GoogleMap) {
        val startTarget = LatLng(
            requireContext().getResourceString(R.string.map_start_lat).toDouble(),
            requireContext().getResourceString(R.string.map_start_lng).toDouble()
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(startTarget))

        val mapConstraints = LatLngBounds(
            LatLng(
                requireContext().getResourceString(R.string.map_constrains_southwest_lat)
                    .toDouble(),
                requireContext().getResourceString(R.string.map_constrains_southwest_lng)
                    .toDouble(),
            ),
            LatLng(
                requireContext().getResourceString(R.string.map_constrains_northeast_lat)
                    .toDouble(),
                requireContext().getResourceString(R.string.map_constrains_northeast_lng)
                    .toDouble(),
            )
        )
        googleMap.setLatLngBoundsForCameraTarget(mapConstraints)

        googleMap.setMinZoomPreference(
            requireContext().getResourceString(R.string.map_min_zoom).toFloat()
        )

        // https://mapstyle.withgoogle.com/
        val mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
            requireContext(),
            R.raw.map_dark_theme
        )
        googleMap.setMapStyle(mapStyleOptions)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent
            .createMapsFragmentSubcomponent()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mapObjectsHolder = null
    }

    override fun onStop() {
        super.onStop()
        mapsViewModel.unregisterReceivers()
    }
    companion object{
        const val BSSID = "bssid"
    }
}