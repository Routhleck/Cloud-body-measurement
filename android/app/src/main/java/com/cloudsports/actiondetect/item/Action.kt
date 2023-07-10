package com.cloudsports.actiondetect.item

class Action(var title: String, var msg: String, var image: String, var localImage: String) {
    override fun toString(): String {
        return "Action(title='$title', msg='$msg', image='$image', localImage='$localImage')"
    }
}