package com.github.malitsplus.pandaemonium.ui

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.telephony.CellInfoLte
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.malitsplus.pandaemonium.databinding.FragmentCellInfoBinding


class CellInfoFragment : Fragment(), LocationListener {

    private var _binding: FragmentCellInfoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var telephonyManager: TelephonyManager
    private lateinit var locationManager: LocationManager

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCellInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        telephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val cellTemplate =
            "CI:        %d\n" +
            "PCI:       %d\n" +
            "TAC:       %d\n" +
            "EARFCN:    %d\n" +
            "BandWith:  %d"

        val cellInfoList = telephonyManager.allCellInfo
        cellInfoList.forEach {
            if (it is CellInfoLte) {
                if (it.isRegistered) {
                    val cell = it.cellIdentity
                    val cellText = cellTemplate.format(cell.ci, cell.pci, cell.tac, cell.earfcn, cell.bandwidth)
                    binding.textCellInfo.text = cellText
                }
            }
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        latitude = location.latitude
        longitude = location.longitude
        val gpsText =
            "Latitude:  $latitude\n" +
            "Longitude: $longitude"
        _binding?.textGpsInfo?.text = gpsText
    }

    override fun onDestroyView() {
        locationManager.removeUpdates(this)
        super.onDestroyView()
        _binding = null
    }
}