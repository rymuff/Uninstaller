package com.kweisa.uninstaller.data

import com.kweisa.uninstaller.R
import com.kweisa.uninstaller.databinding.LayoutItemBinding
import com.xwray.groupie.databinding.BindableItem

class AppItem(private val app: App) : BindableItem<LayoutItemBinding>() {
    override fun getLayout() = R.layout.layout_item

    override fun bind(viewBinding: LayoutItemBinding, position: Int) {
        viewBinding.app = app
    }

    fun getAppData() = app
}