package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter
import com.bazaarvoice.bvsdkdemoandroid.R

class DemoProgressiveSubmissionAPIFragment : Fragment() {

    private var router: DemoRouter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_progressive_submission_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = DemoRouter(context)
        val apiSpinner = view.findViewById<Spinner>(R.id.api_spinner)
        val loadDemoBtn = view.findViewById<Button>(R.id.btnProgressiveSubmissionDemoLoad)
        loadDemoBtn.setOnClickListener {
            when (apiSpinner.selectedItem.toString()) {
                "Initiate Submit" ->router?.transitionToProgressiveSubmissionActivity("initiate", null)
                "Progressive Submission Review" -> router?.transitionToProgressiveSubmissionActivity("review", null)
            }
        }
    }

    companion object {
       @JvmStatic fun newInstance(): Fragment {
            return DemoProgressiveSubmissionAPIFragment()
        }
    }
}
