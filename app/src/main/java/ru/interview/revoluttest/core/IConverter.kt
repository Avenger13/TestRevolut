package ru.interview.revoluttest.core

interface IConverter<in T, out R> {
    fun convert(t: T): R
}