package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.bazaarvoice.bvsdkdemoandroid.DemoRouter
import com.bazaarvoice.bvsdkdemoandroid.R

class DemoProgressiveSubmissionAPIFragment : Fragment() {

    private var router: DemoRouter? = null
    private var productId: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_progressive_submission_demo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        router = DemoRouter(context)
        val productIdInput = view.findViewById<EditText>(R.id.input_productid)
        productIdInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                productId = p0.toString()
            }

        })

        val loadDemoBtn = view.findViewById<Button>(R.id.btnProgressiveSubmissionDemoLoad)
        loadDemoBtn.setOnClickListener {
            router?.transitionToProgressiveSubmissionActivity(productId)
        }
    }

    companion object {
       @JvmStatic fun newInstance(): Fragment {
            return DemoProgressiveSubmissionAPIFragment()
        }
    }

}
