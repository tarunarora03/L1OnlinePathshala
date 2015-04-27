package com.l1.op.adapters

import android.support.v4.app.{Fragment, FragmentManager, FragmentStatePagerAdapter}
import android.util.Log
import com.l1.op.fragments.QuestionViewFragment
import com.l1.op.helper.Questions
import com.l1.op.util.Messages._


/**
 * Created by Tarun on 4/20/2015.
 */

object QuestionViewPagerAdapter {

  def apply(fm: FragmentManager, questionList: Seq[Questions]) = new QuestionViewPagerAdapter(fm, questionList)

}

class QuestionViewPagerAdapter(val fm: FragmentManager, questionList: Seq[Questions]) extends FragmentStatePagerAdapter(fm) {
  override def getCount() = questionList.size

  def isLastQuestion(ps: Int): Boolean = {
    if (getCount() - (ps + 1) == 0)
      true
    else false
  }

  override def getItem(position: Int): Fragment = {
    Log.i(TAGS_QuestionViewPagerAdapter, s"Calling Fragment for position ${position}")
    val fragment = QuestionViewFragment.newInstance(questionList(position), isLastQuestion(position))
    return fragment
  }


}