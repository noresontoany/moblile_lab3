package Interfaces

import android.view.MenuItem
import android.widget.ImageView

interface OnItemClickListner {
    fun onItemCLik(pos: Int)
    fun onContextMenu(imageView: ImageView, pos: Int)

    fun onMenuItemCLick(item: MenuItem);

}