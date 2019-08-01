package com.bazaarvoice.bvsdkdemoandroid.progressivesubmission.ui.demoprogressivesubmission

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.bazaarvoice.bvandroidsdk.FormField
import com.bazaarvoice.bvsdkdemoandroid.R


class ProgressiveSubmissionFormItemView  @JvmOverloads constructor(context: Context, attributeStyle: AttributeSet? = null, style: Int = 0) :
        LinearLayout(context, attributeStyle, style) {
    lateinit var formSubmissionHandler: FormSubmissionHandler

    init {
        orientation = LinearLayout.VERTICAL
    }

    fun setFormField(formField: FormField){
        val view = View.inflate(context, R.layout.form_item, null)
        val title = view.findViewById<TextView>(R.id.Label)
        val labelChoices = view.findViewById<RadioGroup>(R.id.Label_Choices)
        val inputAnswer = view.findViewById<TextView>(R.id.inputAnswer)
        if(formField.label == null || formField.label.isEmpty()) return
        title.text = formField.label
        if (formField.isAutoPopulate) {
            labelChoices.visibility = View.VISIBLE
            inputAnswer.visibility = View.GONE
            for(valueLabel in formField.valueLabels) {
                val labelBtn = RadioButton(context)
                labelBtn.text = valueLabel.value
                labelChoices.addView(labelBtn)
            }
        } else {
            labelChoices.visibility = View.GONE
            inputAnswer.visibility = View.VISIBLE
        }
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        params.setMargins(32, 32, 32, 16)
        view.layoutParams = params
        addView(view)
    }

}
