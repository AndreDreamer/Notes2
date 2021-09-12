package com.example.notes2

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText

class FloatingWidgetService : Service() {
    private var mWindowManager: WindowManager? = null
    private var mFloatingView: View? = null
    var title: EditText? = null
    var text: EditText? = null
    var buttonOK: Button? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()


        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        buttonOK = mFloatingView!!.findViewById(R.id.buttonOKFlow)
        title = mFloatingView!!.findViewById(R.id.editTitleFlow)
        text = mFloatingView!!.findViewById(R.id.editTextFlow)
        val params1 = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        //Add the view to the window.
        val params2 = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params1.gravity =
            Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
        params1.x = 0
        params1.y = 100

        //Specify the view position
        params2.gravity =
            Gravity.TOP or Gravity.LEFT //Initially view will be added to top-left corner
        params2.x = 0
        params2.y = 100

        //Add the view to the window
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(mFloatingView, params1)


        //The root element of the collapsed view layout
        val collapsedView = mFloatingView!!.findViewById<View>(R.id.collapse_view)
        //The root element of the expanded view layout
        val expandedView = mFloatingView!!.findViewById<View>(R.id.expanded_container)
        collapsedView.visibility = View.VISIBLE
        expandedView.visibility = View.GONE
        buttonOK.setOnClickListener(View.OnClickListener {
            createNote()
            collapsedView.visibility = View.VISIBLE
            expandedView.visibility = View.GONE
            mWindowManager!!.updateViewLayout(mFloatingView, params1)
        })
        collapsedView.setOnClickListener {
            collapsedView.visibility = View.GONE
            expandedView.visibility = View.VISIBLE
        }


        //Drag and move floating view using user's touch action.
        collapsedView.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {

                        //remember the initial position.
                        initialX = params1.x
                        initialY = params1.y
                        initialX = params2.x
                        initialY = params2.y

                        //get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val Xdiff = (event.rawX - initialTouchX).toInt()
                        val Ydiff = (event.rawY - initialTouchY).toInt()


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed) {
                                collapsedView.visibility = View.GONE
                                expandedView.visibility = View.VISIBLE
                                mWindowManager!!.updateViewLayout(mFloatingView, params2)
                            }
                        }
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //Calculate the X and Y coordinates of the view.
                        params1.x = initialX + (event.rawX - initialTouchX).toInt()
                        params1.y = initialY + (event.rawY - initialTouchY).toInt()
                        params2.x = initialX + (event.rawX - initialTouchX).toInt()
                        params2.y = initialY + (event.rawY - initialTouchY).toInt()

                        //Update the layout with new X & Y coordinate
                        mWindowManager!!.updateViewLayout(mFloatingView, params1)
                        return true
                    }
                }
                return false
            }
        })
        expandedView.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params1.x
                        initialY = params1.y
                        initialX = params2.x
                        initialY = params2.y

                        //get the touch location
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val Xdiff = (event.rawX - initialTouchX).toInt()
                        val Ydiff = (event.rawY - initialTouchY).toInt()


                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.visibility = View.GONE
                                expandedView.visibility = View.VISIBLE
                                mWindowManager!!.updateViewLayout(mFloatingView, params1)
                            }
                        }
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        //Calculate the X and Y coordinates of the view.
                        params1.x = initialX + (event.rawX - initialTouchX).toInt()
                        params1.y = initialY + (event.rawY - initialTouchY).toInt()
                        params2.x = initialX + (event.rawX - initialTouchX).toInt()
                        params2.y = initialY + (event.rawY - initialTouchY).toInt()


                        //Update the layout with new X & Y coordinate
                        mWindowManager!!.updateViewLayout(mFloatingView, params2)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun createNote() {
        if (!text!!.text.toString().isEmpty()) {
            var header = title!!.text.toString()
            val plot = text!!.text.toString()
            if (title!!.text.toString().isEmpty()) {
                header = if (plot.length > 15) plot.substring(0, 15) + "..." else plot
            }
            val note = Note(header, plot)
            MainActivity.notes.add(note)
            MainActivity.db.putNotes(MainActivity.notes)
        }
        text!!.setText("")
        title!!.setText("")
    }

    private val isViewCollapsed: Boolean
        private get() = mFloatingView == null || mFloatingView!!.findViewById<View>(R.id.collapse_view).visibility == View.VISIBLE

    override fun onDestroy() {
        super.onDestroy()
        if (mFloatingView != null) mWindowManager!!.removeView(mFloatingView)
    }
}