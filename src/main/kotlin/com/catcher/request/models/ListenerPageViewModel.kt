package com.catcher.request.models

import org.http4k.template.ViewModel

data class ListenerPageViewModel(val name: String?) : ViewModel {
    override fun template() = super.template() + ".html"
}
