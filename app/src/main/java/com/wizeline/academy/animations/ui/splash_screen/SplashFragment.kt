package com.wizeline.academy.animations.ui.splash_screen

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.wizeline.academy.animations.R
import com.wizeline.academy.animations.databinding.SplashFragmentBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private var _binding: SplashFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SplashFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Implement XML and programmatically animators in order to practice both options
        val scaleFromXml =
            (AnimatorInflater.loadAnimator(requireContext(), R.animator.grow_up) as AnimatorSet)
                .apply {
                    setTarget(binding.ivWizelineLogo)
                }

        val fadeInProgrammatically =
            ObjectAnimator
                .ofFloat(binding.ivWizelineLogo, "alpha", 0F, 1F)
                .apply {
                    duration = 2000
                }

        AnimatorSet()
            .apply {
                play(scaleFromXml).with(fadeInProgrammatically)
                start()
            }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            delay(2000)
            goToHomeScreen()
        }
    }

    private fun goToHomeScreen() {
        val directions = SplashFragmentDirections.toHomeFragment()
        findNavController().navigate(directions)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}