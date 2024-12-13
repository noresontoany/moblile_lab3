package Interfaces

import android.view.MenuItem
import android.widget.ImageView

interface OnItemClickListner {
    fun onItemCLik(id: Long)
    fun onContextMenu(imageView: ImageView, id: Long)

    fun onMenuItemCLick(item: MenuItem);

}