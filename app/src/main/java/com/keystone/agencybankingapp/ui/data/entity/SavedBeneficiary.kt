package com.keystone.agencybankingapp.ui.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "savedBeneficiary")
@Parcelize
data class SavedBeneficiary(
    @PrimaryKey
    val id: Int = 0,
    val accountName: String,
    val bankName: String,
    val accountNumber: String
): Parcelable
