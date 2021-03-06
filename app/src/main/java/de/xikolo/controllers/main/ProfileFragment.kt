package de.xikolo.controllers.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import de.xikolo.App
import de.xikolo.R
import de.xikolo.config.GlideApp
import de.xikolo.extensions.observe
import de.xikolo.models.User
import de.xikolo.utils.extensions.displaySize
import de.xikolo.viewmodels.main.ProfileViewModel
import de.xikolo.views.CustomSizeImageView

class ProfileFragment : MainFragment<ProfileViewModel>() {

    companion object {
        val TAG: String = ProfileFragment::class.java.simpleName
    }

    @BindView(R.id.textName)
    lateinit var textName: TextView

    @BindView(R.id.imageHeader)
    lateinit var imageHeader: CustomSizeImageView

    @BindView(R.id.imageProfile)
    lateinit var imageProfile: CustomSizeImageView

    @BindView(R.id.textEnrollCount)
    lateinit var textEnrollCounts: TextView

    @BindView(R.id.textEmail)
    lateinit var textEmail: TextView

    override val layoutResource = R.layout.fragment_profile

    override fun createViewModel(): ProfileViewModel {
        return ProfileViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user
            .observe(viewLifecycleOwner) {
                showUser(it)
                showContent()
            }

        viewModel.enrollments
            .observe(viewLifecycleOwner) {
                updateEnrollmentCount(viewModel.enrollmentCount)
            }
    }

    private fun showUser(user: User) {
        val userTitle = String.format(
            getString(R.string.user_name),
            user.profile.firstName,
            user.profile.lastName
        )

        activityCallback?.onFragmentAttached(R.id.navigation_login, userTitle)

        textName.text = userTitle
        textEmail.text = user.profile.email


        val size = activity.displaySize
        val heightHeader: Int
        val heightProfile: Int
        if (activity?.resources?.configuration?.orientation == Configuration.ORIENTATION_PORTRAIT) {
            heightHeader = (size.y * 0.2).toInt()
            heightProfile = (size.x * 0.2).toInt()
        } else {
            heightHeader = (size.y * 0.35).toInt()
            heightProfile = (size.y * 0.2).toInt()
        }
        imageHeader.setDimensions(size.x, heightHeader)
        imageHeader.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        imageProfile.setDimensions(heightProfile, heightProfile)
        imageProfile.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)

        GlideApp.with(this).load(R.drawable.title).into(imageHeader)

        GlideApp.with(App.instance)
            .load(user.avatarUrl)
            .circleCrop()
            .allPlaceholders(R.drawable.avatar)
            .override(heightProfile)
            .into(imageProfile)

        val layoutParams = imageProfile.layoutParams as RelativeLayout.LayoutParams
        layoutParams.setMargins(0, imageHeader.measuredHeight - imageProfile.measuredHeight / 2, 0, 0)
        imageProfile.layoutParams = layoutParams
    }

    private fun updateEnrollmentCount(count: Long) {
        textEnrollCounts.text = count.toString()
    }

    override fun onLoginStateChange(isLoggedIn: Boolean) {
        if (!isLoggedIn) {
            viewModel.user.removeObservers(viewLifecycleOwner)
            viewModel.enrollments.removeObservers(viewLifecycleOwner)
            fragmentManager?.popBackStack()
        }
    }

}
