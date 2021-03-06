package de.xikolo.controllers.main

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import de.xikolo.R
import de.xikolo.controllers.announcement.AnnouncementActivityAutoBundle
import de.xikolo.extensions.observe
import de.xikolo.models.Announcement
import de.xikolo.utils.LanalyticsUtil
import de.xikolo.viewmodels.main.AnnouncementListViewModel

class NewsListFragment : MainFragment<AnnouncementListViewModel>() {

    companion object {
        val TAG: String = NewsListFragment::class.java.simpleName
    }

    @BindView(R.id.content_view)
    internal lateinit var recyclerView: RecyclerView

    private lateinit var announcementListAdapter: AnnouncementListAdapter

    override val layoutResource = R.layout.fragment_announcement_list

    override fun createViewModel(): AnnouncementListViewModel {
        return AnnouncementListViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        announcementListAdapter = AnnouncementListAdapter({ announcementId -> openAnnouncement(announcementId) })

        val layoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            layoutManager.orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = announcementListAdapter

        viewModel.announcements
            .observe(viewLifecycleOwner) {
                showAnnouncementList(it)
            }
    }

    override fun onStart() {
        super.onStart()
        activityCallback?.onFragmentAttached(R.id.navigation_news)
    }

    private fun showAnnouncementList(announcements: List<Announcement>) {
        if (announcements.isEmpty()) {
            showEmptyMessage(R.string.empty_message_global_announcements_title)
        } else {
            showContent()
            announcementListAdapter.update(announcements)
        }
    }

    private fun openAnnouncement(announcementId: String) {
        val intent = AnnouncementActivityAutoBundle.builder(announcementId, true).build(activity!!)
        startActivity(intent)
        LanalyticsUtil.trackVisitedAnnouncementDetail(announcementId)
    }

}
