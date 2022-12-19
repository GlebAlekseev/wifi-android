package com.example.wifitrilateration.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.wifitrilateration.data.framework.entity.RouterRssi
import com.example.wifitrilateration.di.FromViewModelFactory
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.domain.entity.RouterConfigurationtViewState
import com.example.wifitrilateration.ui.viewmodel.RoutersConfigViewModel
import com.example.wifitrilateration.ui.viewmodel.RoutersNowViewModel
import com.example.wifitrilateration.utils.appComponent
import com.glebalekseevjk.wifitrilateration.R
import com.glebalekseevjk.wifitrilateration.databinding.FragmentRoutersConfigBinding
import com.glebalekseevjk.wifitrilateration.databinding.FragmentRoutersNowBinding
import com.glebalekseevjk.yasmrhomework.ui.rv.adapter.ConfigRouterAdapter
import com.glebalekseevjk.yasmrhomework.ui.rv.adapter.RouterRSSIAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject


class RoutersNowFragment : Fragment() {
    @FromViewModelFactory
    @Inject
    lateinit var routersNowViewModel: RoutersNowViewModel

    private var _binding: FragmentRoutersNowBinding? = null
    private val binding: FragmentRoutersNowBinding
        get() = _binding ?: throw RuntimeException("FragmentRoutersNowBinding is null")

    private lateinit var routerRssiAdapter: RouterRSSIAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createRoutersNowFragmentSubcomponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutersNowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        routerRssiAdapter.editClickListener  =  { _bundle ->
            findNavController().navigate(R.id.action_routersNowFragment_to_mapsFragment, _bundle)
        }
    }



    private fun observeViewModel() {
        observeSubmitListAdapter()
    }
    private fun observeSubmitListAdapter() {
        lifecycleScope.launch {
            with(routersNowViewModel) {
                observeRSSIDevices {
                    submitListAdapter(it.data)
                }
            }
        }
    }
    private fun submitListAdapter(list: List<RouterRssi>) {
        routerRssiAdapter.submitList(list)
    }

    private fun setupRecyclerView() {
        routerRssiAdapter = RouterRSSIAdapter()
        with(binding.routerRssiListRv) {
            adapter = routerRssiAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun checkAuth() {
//        if (!routerConfigViewModel.isAuth) {
//            findNavController().navigate(R.id.action_)
//        }
    }


}