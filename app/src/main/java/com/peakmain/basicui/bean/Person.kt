package com.peakmain.basicui.bean

class Person {
    var age = 0
    lateinit var name: String

    constructor()
    constructor(name: String, age: Int) {
        this.age = age
        this.name = name
    }

}