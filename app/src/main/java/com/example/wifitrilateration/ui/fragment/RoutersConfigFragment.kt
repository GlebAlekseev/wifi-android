package com.example.wifitrilateration.ui.fragment

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.wifitrilateration.di.FromViewModelFactory
import com.example.wifitrilateration.domain.entity.ResultStatus
import com.example.wifitrilateration.domain.entity.RouterConfigurationtViewState
import com.example.wifitrilateration.ui.viewmodel.RoutersConfigViewModel
import com.example.wifitrilateration.utils.appComponent
import com.glebalekseevjk.wifitrilateration.R
import com.glebalekseevjk.wifitrilateration.databinding.FragmentRoutersConfigBinding
import com.glebalekseevjk.yasmrhomework.ui.rv.adapter.ConfigRouterAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject


class RoutersConfigFragment : Fragment() {
    @FromViewModelFactory
    @Inject
    lateinit var routerConfigViewModel: RoutersConfigViewModel

    private var _binding: FragmentRoutersConfigBinding? = null
    private val binding: FragmentRoutersConfigBinding
        get() = _binding ?: throw RuntimeException("FragmentRoutersConfigBinding is null")

    private lateinit var configRouterAdapter: ConfigRouterAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.appComponent.createRouterConfigFragmentSubComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutersConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        initListeners()
        observeViewModel()
    }

    private fun initListeners(){
        binding.addRouterConfigBtn.setOnClickListener {
            findNavController().navigate(R.id.action_routersConfigFragment_to_routersNowFragment)
        }
    }

    private fun observeViewModel() {
        observeSubmitListAdapter()
    }
    private fun observeSubmitListAdapter() {
        lifecycleScope.launch {
            with(routerConfigViewModel) {
                routerConfigViewState.collect { state ->
                        submitListAdapter(state)
                    }
            }
        }
    }
    private fun submitListAdapter(state: RouterConfigurationtViewState) {
        when (state.result.status) {
            ResultStatus.SUCCESS -> {
                val list = state.result.data
                configRouterAdapter.submitList(list.toList())
            }
            ResultStatus.LOADING -> {
            }
            ResultStatus.FAILURE -> {
            }
            ResultStatus.UNAUTHORIZED -> {
                checkAuth()
                Toast.makeText(context, state.result.message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    private fun setupRecyclerView() {
        configRouterAdapter = ConfigRouterAdapter()
        with(binding.routerConfigListRv) {
            adapter = configRouterAdapter
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