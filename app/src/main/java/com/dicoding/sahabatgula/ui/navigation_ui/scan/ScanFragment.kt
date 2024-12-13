package com.dicoding.sahabatgula.ui.navigation_ui.scan

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sahabatgula.databinding.FragmentScanBinding
import com.dicoding.sahabatgula.ui.navigation_ui.scan.ImageUtils.toJPEGByteArray
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var scanResultAdapter: ScanAdapter
    private val CAMERA_REQUEST_CODE = 101
    private val scanViewModel: ScanViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("ScanFragment", "onViewCreated: Observing LiveData")
        // Observasi LiveData dari ViewModel
        scanViewModel.data.observe(viewLifecycleOwner) { data ->
            Log.d("ScanFragment", "Received data: $data")
            if (data != null && data.isNotEmpty()) {
                scanResultAdapter.submitList(data)
            } else {
                waitingTextVisibility()
            }

        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        // Setup RecyclerView
        setupRecyclerView()
        // Inisialisasi kamera jika izin diberikan
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        val waitingText = binding.textWaiting
        waitingText.visibility = View.VISIBLE // Tampilkan layout

        Handler(Looper.getMainLooper()).postDelayed({
            // Fade out animasi
            ObjectAnimator.ofFloat(waitingText, "alpha", 1f, 0f).apply {
                duration = 500 // Durasi animasi
                start()
                doOnEnd {
                    waitingText.visibility = View.GONE // Sembunyikan setelah animasi selesai
                }
            }
        }, 5000)
    }

    private fun setupRecyclerView() {
        Log.d("ScanFragment", "RecyclerView setup started")
        scanResultAdapter = ScanAdapter()
        binding.rvScanResult.apply {
            adapter = scanResultAdapter
            layoutManager = LinearLayoutManager(context)
        }
        Log.d("ScanFragment", "RecyclerView setup completed")
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.cameraX.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(640, 480))
                .build().also {
                    it.setAnalyzer(cameraExecutor, ImageAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                Toast.makeText(requireContext(), "Gagal memulai kamera", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Izin kamera diperlukan untuk melakukan pemindaian", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
        _binding = null
    }

    inner class ImageAnalyzer : ImageAnalysis.Analyzer {
        private var lastAnalyzedTimestamp = System.currentTimeMillis()

        @ExperimentalGetImage
        override fun analyze(imageProxy: ImageProxy) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastAnalyzedTimestamp >= 5000) {
                val image = imageProxy.image
                if (image != null) {
                    val imageBytes = image.toJPEGByteArray()
                    scanViewModel.sendImageToServer(imageBytes)
                }
                lastAnalyzedTimestamp = currentTime
                imageProxy.close()
            } else {
                imageProxy.close()
            }
        }
    }

    private fun waitingTextVisibility() {
        binding.textWaiting.visibility = View.VISIBLE
        binding.rvScanResult.visibility = View.GONE
    }

}