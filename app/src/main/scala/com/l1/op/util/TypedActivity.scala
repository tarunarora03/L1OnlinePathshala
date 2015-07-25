package com.l1.op.util

import android.app.Activity
import android.view.View
import android.widget._
import com.l1.op.R

case class TypedResource[T](id: Int)

object TR {
  val usernameEditText = TypedResource[EditText](R.id.username)
  val passwordEditText = TypedResource[EditText](R.id.password)
  val emailEditText = TypedResource[EditText](R.id.email)
  val phoneEditText = TypedResource[EditText](R.id.phoneNumber)
  val fullnameEditText = TypedResource[EditText](R.id.fullname)

  val signInButton = TypedResource[Button](R.id.sign_in_button)
  val signUpButton = TypedResource[Button](R.id.sign_up_button)
  val registerButton = TypedResource[Button](R.id.register_button)
  val submitButton = TypedResource[Button](R.id.submit_button)

  val selectedAnsRadioGrp = TypedResource[RadioGroup](R.id.answersgroup)
  val selectedFieldRadioGrp = TypedResource[RadioGroup](R.id.field)

  val usernameR = TypedResource[TextView](R.id.username_readOnly)
  val scoreResultsR = TypedResource[TextView](R.id.scoreResults)

  //  val header = TypedResource[ImageView](R.id.header)
  val noQuestions = TypedResource[TextView](R.id.noquestions)

}

trait TypedViewHolder {
  def view: View

  def findView[T](tr: TypedResource[T]) = view.findViewById(tr.id).asInstanceOf[T]
}

trait TypedView extends View with TypedViewHolder {
  def view = this
}

trait TypedActivityHolder {
  def activity: Activity

  def findView[T](tr: TypedResource[T]) = activity.findViewById(tr.id).asInstanceOf[T]

  implicit def addOnClickToViews(view: View) = new ViewOnClick(view)
}

trait TypedActivity extends Activity with TypedActivityHolder {
  def activity = this
}

object TypedResource {
  implicit def view2typed(v: View) = new TypedViewHolder {
    def view = v
  }

  implicit def activity2typed(act: Activity) = new TypedActivityHolder {
    def activity = act
  }
}

class ViewOnClick(view: View) {
  def onClick(action: View => Any) = {
    view.setOnClickListener(new View.OnClickListener() {
      def onClick(v: View) {
        action(v)
      }
    })
  }
}