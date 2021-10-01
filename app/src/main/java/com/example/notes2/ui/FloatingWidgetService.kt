package com.example.notes2.ui

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.View.OnTouchListener
import android.widget.Button
import android.widget.EditText
import com.example.notes2.MyDB
import com.example.notes2.R
import com.example.notes2.model.Note

class FloatingWidgetService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var viewNote: View
    private lateinit var title: EditText
    private lateinit var text: EditText
    private lateinit var btnOK: Button
    private val xCoordinateOfView: Int = 0
    private val yCoordinateOfView: Int = 100


    @SuppressLint("InflateParams")
    override fun onCreate() {
        super.onCreate()

        //Inflate the floating view layout we created
        viewNote = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        btnOK = viewNote.findViewById(R.id.buttonOKFlow)
        title = viewNote.findViewById(R.id.editTitleFlow)
        text = viewNote.findViewById(R.id.editTextFlow)

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
            Gravity.TOP or Gravity.START //Initially view will be added to top-left corner
        params1.x = xCoordinateOfView
        params1.y = yCoordinateOfView

        //Specify the view position
        params2.gravity =
            Gravity.TOP or Gravity.START //Initially view will be added to top-left corner
        params2.x = xCoordinateOfView
        params2.y = yCoordinateOfView

        //Add the view to the window
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(viewNote, params1)


        //The root element of the collapsed view layout
        val collapsedView = viewNote.findViewById<View>(R.id.collapse_view)
        //The root element of the expanded view layout
        val expandedView = viewNote.findViewById<View>(R.id.expanded_container)
        collapsedView.visibility = View.VISIBLE
        expandedView.visibility = View.GONE
        btnOK.setOnClickListener {
            createNote()
            collapsedView.visibility = View.VISIBLE
            expandedView.visibility = View.GONE
            windowManager.updateViewLayout(viewNote, params1)
        }
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
            @SuppressLint("ClickableViewAccessibility")
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
                        val xDifference = (event.rawX - initialTouchX).toInt()
                        val yDifference = (event.rawY - initialTouchY).toInt()


                        //The check for xDifference <10 && yDifference< 10 because sometime elements moves a little while clicking.
                        //So that is click event.


                        if (xDifference < 10 && yDifference < 10) {
                            if (isViewCollapsed) {
                                collapsedView.visibility = View.GONE
                                expandedView.visibility = View.VISIBLE
                                windowManager.updateViewLayout(viewNote, params2)
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
                        windowManager.updateViewLayout(viewNote, params1)
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
            @SuppressLint("ClickableViewAccessibility")
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
                                windowManager.updateViewLayout(viewNote, params1)
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
                        windowManager.updateViewLayout(viewNote, params2)
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun createNote() {
        if (text.text.toString().isNotEmpty()) {
            var header = title.text.toString()
            val plot = text.text.toString()
            if (title.text.toString().isEmpty()) {
                header = if (plot.length > 15) plot.substring(0, 15) + "..." else plot
            }
            val note = Note(header, plot)
            MyDB.addNote(note)
        }
        text.setText("")
        title.setText("")
    }

    private val isViewCollapsed: Boolean
        get() = viewNote.findViewById<View>(R.id.collapse_view).visibility == View.VISIBLE

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(viewNote)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
}

