package com.meldeveloping.todowidget.db.room

import androidx.room.TypeConverter
import com.meldeveloping.todowidget.db.ToDoListItem

class ItemConverter {

    companion object {
        private const val SPLIT_OBJECTS_CHAR = ','
        private const val SPLIT_PARAMETERS_CHAR = ' '
        private const val CHECKED_PARAMETER_LIST_POSITION = 0
        private const val TEXT_PARAMETER_LIST_POSITION = 1
    }

    @TypeConverter
    fun fromItemsToString(list: ArrayList<ToDoListItem>): String {
        val builder = StringBuilder()

        for (item in list) {
            builder.append(item.isChecked)
                .append(SPLIT_PARAMETERS_CHAR)
                .append(item.itemText)
                .append(SPLIT_OBJECTS_CHAR)
        }

        return builder.toString().trimEnd(SPLIT_OBJECTS_CHAR)
    }

    @TypeConverter
    fun toItemsFromString(items: String): ArrayList<ToDoListItem> {
        val result = ArrayList<ToDoListItem>()

        for (listObject in items.split(SPLIT_OBJECTS_CHAR)) {
            result.add(
                ToDoListItem(
                    isChecked = getParameterFromString(listObject, CHECKED_PARAMETER_LIST_POSITION).toInt(),
                    itemText = getParameterFromString(listObject, TEXT_PARAMETER_LIST_POSITION)
                )
            )
        }
        return result
    }

    private fun getParameterFromString(
        listObject: String,
        listItemPosition: Int
    ) = listObject.split(SPLIT_PARAMETERS_CHAR)[listItemPosition]
}