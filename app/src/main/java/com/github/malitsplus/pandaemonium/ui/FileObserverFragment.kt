package com.github.malitsplus.pandaemonium.ui

import android.content.Context
import android.os.Bundle
import android.os.FileObserver
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.malitsplus.pandaemonium.data.RecursiveFileObserver
import com.github.malitsplus.pandaemonium.databinding.FragmentCellInfoBinding
import com.github.malitsplus.pandaemonium.databinding.FragmentFileObserverBinding

class FileObserverFragment : Fragment() {

    private var _binding: FragmentFileObserverBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var recursiveObserver: RecursiveFileObserver

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFileObserverBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listener = RecursiveFileObserver.EventListener { event, file ->
            when (event) {
                FileObserver.ACCESS -> Log.d("FsRead", file.absolutePath)
                else -> Log.d("FsDefault", file.absolutePath)
            }
        }
        recursiveObserver = RecursiveFileObserver("/system", listener)
        recursiveObserver.startWatching()
    }

    override fun onDestroyView() {
        recursiveObserver.stopWatching()
        super.onDestroyView()
        _binding = null
    }
}