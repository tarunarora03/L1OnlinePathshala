package com.l1.op.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View.OnClickListener
import android.view.{LayoutInflater, View, ViewGroup}
import android.widget.{Button, RadioButton, RadioGroup, TextView}
import com.l1.op.R
import com.l1.op.helper.Questions
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
    val fragment: QuestionViewFragment = new QuestionViewFragment(question, isLastQuestion)
    args.putString("question", question.questionText)
    args.putString("date", question.date)
    args.putStringArray("ans_opt", question.ansArray)
    args.putString("corr_ans", question.correctAns)
    fragment.setArguments(args)
    return fragment
  }
}

class QuestionViewFragment(question: Questions, isLastQuestion: Boolean) extends Fragment /*with TypedActivity*/ {

  var datapass: DataPass = _

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    val args: Bundle = getArguments()
    val view: View = inflater.inflate(R.layout.question_ans_view, container, false)
    isLastQuestion match {
      case false =>
        val textR = view.findViewById(R.id.textquestion).asInstanceOf[TextView]
        val dateR = view.findViewById(R.id.dateheader).asInstanceOf[TextView]
        val q: String = args.getString("question")
        textR.setText(q)
        dateR.setText(args.getString("date"))

        val ansGroup = view.findViewById(R.id.answersgroup).asInstanceOf[RadioGroup]
        //args.getStringArray("ans_opt").foreach {
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
        )
        view

      case true =>
        val textNoQuestion = view.findViewById(R.id.noquestions).asInstanceOf[TextView]
        textNoQuestion.setText(getResources.getString(R.string.no_questions))
        view
    }


  }

  override def onAttach(activity: Activity): Unit = {
    super.onAttach(activity)
    datapass = activity.asInstanceOf[DataPass]
  }
}