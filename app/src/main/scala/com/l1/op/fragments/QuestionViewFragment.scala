package com.l1.op.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View.OnClickListener
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget._
import com.l1.op.R
import com.l1.op.util.Messages._

/**
 * Created by Tarun on 4/23/2015.
 */


trait DataPass {
  def onDataPass(score: CurrentScore, isLastQuestion: Boolean, smoothScroll: Boolean)
}

object QuestionViewFragment {

  def newInstance(question: Questions, isLastQuestion: Boolean): QuestionViewFragment = {
    val args: Bundle = new Bundle()
    val fragment: QuestionViewFragment = new QuestionViewFragment(/*question, isLastQuestion*/)
    args.putSerializable("ques", question)
    args.putBoolean("isLastQuestion", isLastQuestion)
    fragment.setArguments(args)
    return fragment
  }
}

class QuestionViewFragment(/*question: Questions, isLastQuestion: Boolean*/) extends Fragment /*with TypedActivity*/ {

  var datapass: DataPass = _

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val args: Bundle = getArguments()
    val question = args.getSerializable("ques").asInstanceOf[Questions]
    val isLastQuestion = args.getBoolean("isLastQuestion")
    val view: View = inflater.inflate(R.layout.question_ans_view, container, false)
    val textR = view.findViewById(R.id.textquestion).asInstanceOf[TextView]
    val dateR = view.findViewById(R.id.dateheader).asInstanceOf[TextView]
    textR.setText(question.questionText)
    dateR.setText(question.date)

    val ansGroup = view.findViewById(R.id.answersgroup).asInstanceOf[RadioGroup]
    question.ansArray.foreach {
      ans =>
        val rbButton = new RadioButton(getActivity)
        rbButton.setText(ans)
        ansGroup.addView(rbButton)
    }

    val button = view.findViewById(R.id.submit_button).asInstanceOf[Button]

    isLastQuestion match {
      case true => button.setText(getResources.getString(R.string.button_text_no_next))
      case false => button.setText(getResources.getString(R.string.button_text_with_next))
    }
    button.setOnClickListener(
      new OnClickListener {
        override def onClick(v: View): Unit = {
          val selectedOpt = ansGroup.getCheckedRadioButtonId
          if (selectedOpt == -1) {
            Toast.makeText(getActivity, getResources.getString(R.string.noAnsSelected), Toast.LENGTH_LONG).show()
          } else {
            val selAns = view.findViewById(selectedOpt).asInstanceOf[RadioButton]
            selAns.getText match {
              case question.correctAns =>
                Log.i(TAGS_QuestionViewFragment, " correct Ans")
                datapass.onDataPass(CurrentScore("1", "1"), isLastQuestion, true)
              case a@_ =>
                Log.i(TAGS_QuestionViewFragment, s"Incorret ans ${a}")
                datapass.onDataPass(CurrentScore("0", "1"), isLastQuestion, true)
            }
          }
        }
      }
    )
    view
  }

  override def onAttach(activity: Activity): Unit = {
    super.onAttach(activity)
    datapass = activity.asInstanceOf[DataPass]
  }
}