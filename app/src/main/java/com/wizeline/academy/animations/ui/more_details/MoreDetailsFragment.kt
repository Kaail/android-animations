package com.wizeline.academy.animations.ui.more_details

import android.R as AndroidR
import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.wizeline.academy.animations.databinding.MoreDetailsFragmentBinding
import com.wizeline.academy.animations.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreDetailsFragment : Fragment() {

    private var _binding: MoreDetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoreDetailsViewModel by viewModels()
    private val args: MoreDetailsFragmentArgs by navArgs()

    private lateinit var scaleXAnimation: SpringAnimation
    private lateinit var scaleYAnimation: SpringAnimation
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private companion object Params {
        const val INITIAL_SCALE = 1f
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupScaleAnimation() {
        scaleXAnimation = createSpringAnimation(binding.ivImageDetailLarge, SpringAnimation.SCALE_X)

        scaleYAnimation = createSpringAnimation(binding.ivImageDetailLarge, SpringAnimation.SCALE_Y)

        setupPinchToZoom()

        binding.ivImageDetailLarge.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                scaleXAnimation.start()
                scaleYAnimation.start()
            } else {
                scaleXAnimation.cancel()
                scaleYAnimation.cancel()
                scaleGestureDetector.onTouchEvent(event)
            }
            true
        }
    }

    private fun setupPinchToZoom() {
        var scaleFactor = INITIAL_SCALE
        scaleGestureDetector = ScaleGestureDetector(
            requireContext(),
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    scaleFactor *= detector.scaleFactor
                    binding.ivImageDetailLarge.scaleX *= scaleFactor
                    binding.ivImageDetailLarge.scaleY *= scaleFactor
                    return true
                }
            }
        )
    }

    private fun createSpringAnimation(
        view: View, property: DynamicAnimation.ViewProperty
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val spring = SpringForce(INITIAL_SCALE)
        spring.stiffness = SpringForce.STIFFNESS_HIGH
        spring.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        animation.spring = spring
        return animation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MoreDetailsFragmentBinding.inflate(inflater, container, false)
        binding.ivImageDetailLarge.loadImage(args.imageId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.title.observe(viewLifecycleOwner) { binding.tvTitle.text = it }
        viewModel.content.observe(viewLifecycleOwner) { binding.tvFullTextContent.text = it }
        viewModel.fetchData(args.contentIndex)

        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(AndroidR.transition.move)

        setupScaleAnimation()
    }
}