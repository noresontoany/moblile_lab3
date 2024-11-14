package Interfaces

import android.view.MenuItem
import android.widget.ImageView

interface OnItemClickListner {
    fun onItemCLik(id: String)
    fun onContextMenu(imageView: ImageView, id: String)

    fun onMenuItemCLick(item: MenuItem);

}